package controllers.anormapp

import play.api.data.Form
import play.api.data.Forms._
// IntelliJでは未使用になるが、anormのimportは必要
import anorm._

object AnormForm {
  case class Attr(name: String, mail: String, tel: String)

  val attrForm = Form(
    mapping(
      "name" -> text,
      "mail" -> text,
      "tel" -> text,
    )(Attr.apply)(Attr.unapply)
  )

  case class AnormData(id: Int, name: String, mail: String, tel: String)
  val anormForm = Form(
    mapping(
      "id" -> number,
      "name" -> text,
      "mail" -> text,
      "tel" -> text
    )(AnormData.apply)(AnormData.unapply)
  )

  val anormParser = {
    SqlParser.int("people.id") ~
      SqlParser.str("people.name") ~
      SqlParser.str("people.mail") ~
      SqlParser.str("people.tel")
  } map {
    case id ~ name ~ mail ~ tel =>
      AnormData(id, name, mail, tel)
  }

}
