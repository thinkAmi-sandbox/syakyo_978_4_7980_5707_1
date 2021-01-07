package models.slickapp

// TimestampはIDEだとEnter + Altで出てこなかったので、自分でimport
import java.sql.Timestamp

import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText, number}

object Message {
  val messageForm: Form[MessageForm] = Form {
    mapping(
      "personId" -> number,
      "message" -> nonEmptyText,
    )(MessageForm.apply)(MessageForm.unapply)
  }
}

case class Message
(
  id: Int,
  personId: Int,
  message: String,
  created_at: Timestamp,
)

case class MessageForm(personId: Int, message: String)

case class MessageWithPerson(message: Message, person: SlickPerson)
