package models.slickapp

import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText, text, email}

case class SlickPerson(id: Int, name: String, mail: String, fax: String)
case class SlickPersonForm(name: String, mail: String, fax: String)


object SlickPerson {
  val slickPersonForm: Form[SlickPersonForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "mail" -> email,
      "fax" -> text
        .verifying(
          error = "1文字よりも大きい文字を入力してください",
          constraint = _.length > 1)
        .verifying(
          error = "半角数字のみ",
          constraint = _.matches("""[0-9]+""")
        )
    )(SlickPersonForm.apply)(SlickPersonForm.unapply)
  }

  // 検索用
  val slickPersonFind: Form[SlickPersonFind] = Form {
    mapping(
      "find" -> text
    )(SlickPersonFind.apply)(SlickPersonFind.unapply)
  }

  case class SlickPersonFind(find: String)
}
