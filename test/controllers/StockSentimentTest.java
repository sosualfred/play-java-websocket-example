package controllers;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StockSentimentTest {

    @Test
    public void scoresAndLabels() {
        assertThat(StockSentiment.score("<p>Strong quarter, analysts say <b>buy</b></p>")).isEqualTo(1);
        assertThat(StockSentiment.score("<p>Shares plunge after earnings miss</p>")).isEqualTo(-1);
        assertThat(StockSentiment.score("<p>Apple event today</p>")).isEqualTo(0);

        assertThat(StockSentiment.summarize(List.of(1, 1, -1, 0)).get("label").asText()).isEqualTo("pos");
        assertThat(StockSentiment.summarize(List.of(-1, -1, 1)).get("label").asText()).isEqualTo("neg");
        assertThat(StockSentiment.summarize(List.of()).get("label").asText()).isEqualTo("neutral");
    }
}
