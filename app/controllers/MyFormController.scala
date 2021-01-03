package controllers

import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

import javax.inject.Inject

// エラー「trait Singleton is abstract; cannot be instantiated」になるので、Singletonのimportを追加
// https://stackoverflow.com/questions/35457242/guice-and-play2-singleton-from-trait
import javax.inject.Singleton

// MessagesControllerComponentsとMessagesAbstractControllerで、メッセージ機能付きのコントローラとなるため
// フォームを利用する場合はこちらに差し替えておく
@Singleton
class MyFormController @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc){
  import MyForm._

  def index() = Action {
    implicit request =>
      Ok(views.html.form_helper(
        "コントローラーからのメッセージ",
        myForm,  // MyFormで用意したインスタンス変数
      ))
  }

  def form() = Action {
    implicit request =>
      val form = myForm.bindFromRequest()
      val data = form.get
      Ok(views.html.form_helper(
        // オブジェクトのプロパティを参照するため、 ${} を使う
        // https://docs.scala-lang.org/ja/overviews/core/string-interpolation.html
        s"name: ${data.name}, pass: ${data.password}, radio: ${data.radio}",
        form,
      ))
  }
}
