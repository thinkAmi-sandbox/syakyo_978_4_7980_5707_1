package controllers.jdbcapp

// フォーム用クラス
object MyForm {
  import play.api.data._
  import play.api.data.Forms._

  // データクラス
  case class Data(name: String, password: String, radio: String)

  // フォームクラスのインスタンス変数
  val myForm = Form(
    mapping(
      "name" -> text,
      // p115 では "pass" になっているが、passwordでないと動かないので注意
      // NoSuchElementException: None.get
      "password" -> text,
      "radio" -> text,
    )(Data.apply)(Data.unapply)
  )
}
