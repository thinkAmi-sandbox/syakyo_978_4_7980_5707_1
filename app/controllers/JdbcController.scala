package controllers

import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}
import play.api.db._

import java.sql.SQLException
import javax.inject.Inject

import PersonForm._


class JdbcController @Inject()(db: Database, cc: MessagesControllerComponents)
  extends MessagesAbstractController(cc){

  def index() = Action {
    implicit request =>
      var msg = "database record: <br><ul>"
      try {
        db.withConnection { conn =>
          val stmt = conn.createStatement
          val rs = stmt.executeQuery("SELECT * from people")

          while (rs.next) {
            msg += s"<li>${rs.getInt("id")}: ${rs.getString("name")}</li>"
          }
          msg += "</ul>"
        }
      } catch {
        case e:SQLException =>
          msg = "<li>no record...</li>"
      }
      Ok(views.html.jdbc_exec(msg))
  }

  def add() = Action {implicit request =>
    Ok(views.html.add(
      "フォームを記入してください",
      form,
    ))
  }

  def create() = Action {implicit request =>
    val formData = form.bindFromRequest
    val data = formData.get

    try {
      db.withConnection { conn =>
        val ps = conn.prepareStatement(
          "insert into people values (default, ?, ?, ?)"
        )

        ps.setString(1, data.name)
        ps.setString(2, data.mail)
        ps.setString(3, data.tel)
        ps.executeUpdate
      }
    } catch {
      case e: SQLException =>
        Ok(views.html.add(
          "フォームに入力してください",
          form
        ))
    }

    Redirect(routes.JdbcController.index)
  }
}
