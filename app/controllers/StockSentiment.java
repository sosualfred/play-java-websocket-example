package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import play.libs.Json;
import play.libs.concurrent.ClassLoaderExecutionContext;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

@Singleton
public class StockSentiment extends Controller {

    private static final Logger log = LoggerFactory.getLogger(StockSentiment.class);

    /** Thrown when Mastodon answers with a non-200 status. */
    private static final class UpstreamException extends RuntimeException {
        final int status;

        UpstreamException(int status) {
            super("Mastodon returned HTTP " + status);
            this.status = status;
        }
    }

    // ponytail: tiny finance word list instead of a sentiment API (free ones are dead or rate-limited);
    // swap in a real NLP model/API if accuracy matters.
    private static final Set<String> POSITIVE = Set.of(
            "buy", "buying", "bull", "bullish", "gain", "gains", "rally", "rallies", "beat", "beats",
            "strong", "growth", "surge", "surges", "soar", "soars", "record", "upgrade", "upgraded",
            "outperform", "profit", "profits", "win", "wins", "good", "great", "rise", "rises", "rising",
            "breakout", "boost", "boosts", "jump", "jumps", "positive");
    private static final Set<String> NEGATIVE = Set.of(
            "sell", "selling", "bear", "bearish", "loss", "losses", "drop", "drops", "fall", "falls",
            "miss", "misses", "weak", "decline", "declines", "plunge", "plunges", "crash", "downgrade",
            "downgraded", "underperform", "lawsuit", "bad", "dump", "fear", "fears", "cut", "cuts",
            "layoffs", "slump", "slumps", "sink", "sinks", "negative", "warning");

    private final String postsUrl;
    private final String token;
    private final WSClient wsClient;
    private final ClassLoaderExecutionContext ec;

    @Inject
    public StockSentiment(WSClient wsClient, Config configuration, ClassLoaderExecutionContext ec) {
        this.wsClient = wsClient;
        this.ec = ec;
        this.postsUrl = configuration.getString("tweet.url");
        this.token = configuration.getString("mastodon.token");
    }

    public CompletionStage<Result> get(String symbol) {
        WSRequest request = wsClient.url(postsUrl + symbol).addQueryParameter("limit", "40");
        if (!token.isEmpty()) request = request.addHeader("Authorization", "Bearer " + token);
        return request.get()
                .thenApplyAsync(response -> {
                    if (response.getStatus() != Http.Status.OK) {
                        throw new UpstreamException(response.getStatus());
                    }
                    List<Integer> scores = stream(response.asJson().spliterator(), false)
                            .map(post -> score(post.path("content").asText()))
                            .collect(toList());
                    return ok(summarize(scores));
                }, ec.current())
                .exceptionally(t -> errorResponse(symbol, t));
    }

    /** +1 / 0 / -1 for one post's HTML content. */
    static int score(String html) {
        int score = 0;
        for (String word : html.replaceAll("<[^>]*>", " ").toLowerCase().split("[^a-z]+")) {
            if (POSITIVE.contains(word)) score++;
            else if (NEGATIVE.contains(word)) score--;
        }
        return Integer.signum(score);
    }

    static JsonNode summarize(List<Integer> scores) {
        double n = Math.max(scores.size(), 1);
        double pos = scores.stream().filter(s -> s > 0).count() / n;
        double neg = scores.stream().filter(s -> s < 0).count() / n;
        String label = pos > neg ? "pos" : neg > pos ? "neg" : "neutral";

        return Json.newObject()
                .put("label", label)
                .set("probability", Json.newObject()
                        .put("neg", neg)
                        .put("neutral", 1 - pos - neg)
                        .put("pos", pos));
    }

    private Result errorResponse(String symbol, Throwable t) {
        // Exceptions thrown inside a CompletionStage arrive wrapped in a CompletionException
        Throwable cause = (t instanceof CompletionException && t.getCause() != null) ? t.getCause() : t;

        if (cause instanceof UpstreamException) {
            int upstream = ((UpstreamException) cause).status;
            log.warn("Mastodon returned HTTP {} for symbol {}", upstream, symbol);
            return status(Http.Status.BAD_GATEWAY, Json.newObject()
                    .put("error", "Could not fetch the posts")
                    .put("upstreamStatus", upstream));
        }

        log.error("Failed to fetch or score posts for symbol {}", symbol, cause);
        return internalServerError(Json.newObject().put("error", "Could not fetch the posts"));
    }
}
