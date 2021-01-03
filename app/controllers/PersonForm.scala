package controllers

import play.api.data.Form
import play.api.data.Forms._

object PersonForm {

  case class Data(name: String, mail: String, tel: String)

  val form = Form(
    mapping(
      "name" -> text,
      "mail" -> text,
      "tel" -> text,
    )(Data.apply)(Data.unapply)
  )
}
