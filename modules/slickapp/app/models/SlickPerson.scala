package models.slickapp

import play.api.data.Form
import play.api.data.Forms.{mapping, text}

case class SlickPerson(id: Int, name: String, mail: String, fax: String)
case class SlickPersonForm(name: String, mail: String, fax: String)


object SlickPerson {
  val slickPersonForm: Form[SlickPersonForm] = Form {
    mapping(
      "name" -> text,
      "mail" -> text,
      "fax" -> text
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
