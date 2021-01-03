package controllers

import akka.util.ByteString

import javax.inject._
import play.api._
import play.api.http.HttpEntity
import play.api.mvc._

import java.util.Calendar

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def index2() = Action {
    Ok("Welcome to Play Framework !")
  }

  // ひとまず値を返しておきたい時に使える
  def todo() = TODO

  def indexResult() = Action {
    Result(
      // HTTP200の、ヘッダはそのままなので空
      header = ResponseHeader(200, Map.empty),

      body = HttpEntity.Strict(
        ByteString("This is sample Text."),
        // Content-Type
        Some("text/plain")
      )
    )
  }

  def okAsHtml() = Action {
    Ok("<h1>Hello!</h1><p>This is sample message.</p>")
      // asで Content-Type を指定 (asを使うと変更可能)
      .as("text/html")
  }

  def okAsXml() = Action {
    Ok("<root><title>Hello!</title><message>This is sample message.</message></root>")
      // asで Content-Type を指定 (asを使うと変更可能)
      .as("application/xml")
  }

  def okAsJson() = Action {
    Ok("{title: 'Hello!', message: 'This is sample message.'}")
      // asで Content-Type を指定 (asを使うと変更可能)
      .as("application/json")
  }

  def okWithHeaders() = Action {
    Ok("<title>Hello!</title><h1>Hello!</h1><p>サンプルのメッセージ</p>")
      .withHeaders(
        ACCEPT_CHARSET -> "utf-8",
        ACCEPT_LANGUAGE -> "ja-JP"
        // 書籍では文字化けしたので、Content-Typeの設定で、charsetも指定する
      ).as("text/html; charset=UTF-8")
  }

  def okWithParams(id:Int) = Action {
    Ok("<title>Hello!</title><h1>Hello!</h1><p>ID = " + id + "です。</p>")
      .as("text/html; charset=UTF-8")
  }

  def okWithMultipleParams(id:Int, name:String) = Action {
    Ok("<title>Hello!</title><h1>Hello!</h1><p>ID = " + id + ", Name = " + name + "です。</p>")
      .as("text/html; charset=UTF-8")
  }

  def okWithQuery(id:Int, name:Option[String]) = Action {
    Ok("<title>Hello!</title><h1>Hello!</h1><p>ID = " + id + ", Name = " + name.getOrElse("no-name") + "です。</p>")
      .as("text/html; charset=UTF-8")
  }

  def saveCookie(name:Option[String]) = Action {request =>
    val param:String = name.getOrElse("")
    var message = "<p>nameはありません</p>"

    if (param != "") {
      message = "<p>nameが送られました</p>"
    }

    // cookieにセットされるのは、Option型
    val cookie = request.cookies.get("name")
    // cookie.getOrElse(Cookie("name", "no-cookie."))にて、Cookieがなかったら新しく作り出して取り出す
    message += "<p>cookie: " + cookie.getOrElse(Cookie("name", "no-cookie.")).value + "</p>"

    val res = Ok("<title>Hello!</title><h1>Hello!</h1>" + message)
      .as("text/html; charset=UTF-8")

    if (param != "") {
      res.withCookies(Cookie("name", param)).bakeCookies()
    } else {
      res
    }
  }

  def saveSession(name:Option[String]) = Action {request =>
    val param:String = name.getOrElse("")
    var message = "<p>nameはありません</p>"

    if (param != "") {
      message = "<p>nameが送られました</p>"
    }

    // cookieにセットされるのは、Option型
    val session = request.session.get("name")
    message += "<p>session: " + session.getOrElse("no-session") + "</p>"

    val res = Ok("<title>Hello!</title><h1>Hello!</h1>" + message)
      .as("text/html; charset=UTF-8")

    if (param != "") {
      res.withSession(request.session + ("name" -> param))
    } else {
      res
    }
  }

  def saveSessions(name:Option[String], value:Option[String]) = Action {request =>
    val sessionName:String = name.getOrElse("")
    val sessionValue:String = value.getOrElse("")
    val sessions = request.session.data
    // 今後は加工文字列リテラルを使っていく
    // https://docs.scala-lang.org/ja/overviews/core/string-interpolation.html
    val message = s"<pre>$sessions</p>"

    val res = Ok(s"<title>Hello!</title><h1>Hello!</h1>$message")
      .as("text/html; charset=UTF-8")

    if (sessionName != "") {
      res.withSession(request.session + (sessionName -> sessionValue))
    } else {
      res
    }
  }

  def showIndex() = Action {
    Ok(views.html.index())
  }

  def showIndexWithTemplateParam() = Action {
    Ok(views.html.param("山田太郎"))
  }

  def showIndexWithMultiTemplateParam() = Action {
    Ok(views.html.multi_param(123, "hanako", "flower"))
  }

  def showIndexWithObject() = Action {
    Ok(views.html.param_object(Calendar.getInstance))
  }

  def showStyleSheet() = Action {
    Ok(views.html.style_sheet())
  }

  def showExtendPage() = Action {
    Ok(views.html.extend("コントローラから渡した値です。"))
  }

  def showBlockPage(p:Option[Int]) = Action {
    val items: List[List[String]] = List(
      List("シナノゴールド", "黄"),
      List("フジ", "赤"),
      List("シナノドルチェ", "赤"),
    )

    Ok(views.html.block(
      "blockのメッセージです",
      p.getOrElse(0),
      items,
      List("name", "color"),
    ))
  }

  def formGet() = Action {
    Ok(views.html.form("フォームです"))
  }

  def formPost() = Action { request =>
    // 送信されたフォームをMapのOptionインスタンスにする
    val form: Option[Map[String, Seq[String]]] = request.body.asFormUrlEncoded

    // OptionからMapを取り出す
    // キーがString、値がSeq[String]
    val param: Map[String, Seq[String]] = form.getOrElse(Map())

    // IDEにより、以下に変更
    // val name: String = param.get("name").get(0)
    val name: String = param("name").head
    val password: String = param("pass").head

    Ok(views.html.form(s"name: $name, password: $password"))
  }
}
