
package views.html

import _root_.play.twirl.api.TwirlFeatureImports._
import _root_.play.twirl.api.TwirlHelperImports._
import _root_.play.twirl.api.Html
import _root_.play.twirl.api.JavaScript
import _root_.play.twirl.api.Txt
import _root_.play.twirl.api.Xml
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import java.lang._
import java.util._
import play.core.j.PlayMagicForJava._
import play.mvc._
import play.api.data.Field
import play.data._
import play.core.j.PlayFormsMagicForJava._
import scala.jdk.CollectionConverters._
import views.html.helper.CSPNonce

object index extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template2[play.mvc.Http.Request,org.webjars.play.WebJarsUtil,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(implicit request: play.mvc.Http.Request, webJarsUtil: org.webjars.play.WebJarsUtil):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*2.1*/("""<!DOCTYPE html>

<html>
<head>
    <title>Reactive Stock News Dashboard</title>
    """),_display_(/*7.6*/webJarsUtil/*7.17*/.locate("bootstrap", "css/bootstrap.min.css").css()),format.raw/*7.68*/("""
    """),format.raw/*8.5*/("""<link rel="stylesheet" media="screen" href=""""),_display_(/*8.50*/routes/*8.56*/.Assets.at("stylesheets/main.min.css")),format.raw/*8.94*/("""">
    <link rel="shortcut icon" type="image/png" href=""""),_display_(/*9.55*/routes/*9.61*/.Assets.at("images/favicon.png")),format.raw/*9.93*/("""">
    """),_display_(/*10.6*/webJarsUtil/*10.17*/.locate("jquery", "jquery.min.js").script(scala.collection.immutable.Map("nonce" -> CSPNonce()))),format.raw/*10.113*/("""
    """),_display_(/*11.6*/webJarsUtil/*11.17*/.locate("flot", "jquery.flot.js").script(scala.collection.immutable.Map("nonce" -> CSPNonce()))),format.raw/*11.112*/("""
    """),format.raw/*12.5*/("""<script """),_display_(/*12.14*/{CSPNonce.attr}),format.raw/*12.29*/(""" """),format.raw/*12.30*/("""type='text/javascript' src='"""),_display_(/*12.59*/routes/*12.65*/.Assets.at("javascripts/index.js")),format.raw/*12.99*/("""'></script>
</head>
<body data-ws-url=""""),_display_(/*14.21*/routes/*14.27*/.HomeController.ws.webSocketURL(request)),format.raw/*14.67*/("""">
    <div class="navbar navbar-inverse navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container">
                <a class="brand" href="#">Reactive Stocks</a>
                <form id="addsymbolform" class="navbar-form pull-right">
                    <input id="addsymboltext" type="text" class="span2" placeholder="SYMBOL">
                    <button type="submit" class="btn">Add Stock</button>
                </form>
            </div>
        </div>
    </div>

    <div id="stocks" class="container">

    </div>
</body>
</html>
"""))
      }
    }
  }

  def render(request:play.mvc.Http.Request,webJarsUtil:org.webjars.play.WebJarsUtil): play.twirl.api.HtmlFormat.Appendable = apply(request,webJarsUtil)

  def f:((play.mvc.Http.Request,org.webjars.play.WebJarsUtil) => play.twirl.api.HtmlFormat.Appendable) = (request,webJarsUtil) => apply(request,webJarsUtil)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  SOURCE: app/views/index.scala.html
                  HASH: faf58b398e86b0459aa2dd738b96160d7a054bcf
                  MATRIX: 985->1|1163->86|1273->171|1292->182|1363->233|1394->238|1465->283|1479->289|1537->327|1620->384|1634->390|1686->422|1720->430|1740->441|1858->537|1890->543|1910->554|2027->649|2059->654|2095->663|2131->678|2160->679|2216->708|2231->714|2286->748|2353->788|2368->794|2429->834
                  LINES: 28->1|33->2|38->7|38->7|38->7|39->8|39->8|39->8|39->8|40->9|40->9|40->9|41->10|41->10|41->10|42->11|42->11|42->11|43->12|43->12|43->12|43->12|43->12|43->12|43->12|45->14|45->14|45->14
                  -- GENERATED --
              */
          