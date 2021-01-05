package controllers.jdbcapp

import play.api.data.Form
import play.api.data.Forms._

object Person {

  case class Attr(name: String, mail: String, tel: String)

  val attrForm = Form(
    mapping(
      "name" -> text,
      "mail" -> text,
      "tel" -> text,
    )(Attr.apply)(Attr.unapply)
  )
}
