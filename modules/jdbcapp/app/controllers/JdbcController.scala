package controllers.jdbcapp

import play.api.db._
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

import java.sql.SQLException
import javax.inject.Inject
import controllers.jdbcapp.Person._

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
      attrForm,
    ))
  }

  def create() = Action {implicit request =>
    val formData = attrForm.bindFromRequest
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
          attrForm,
        ))
    }

    Redirect(controllers.jdbcapp.routes.JdbcController.index)
  }

  def edit(id: Int) = Action {implicit request =>
    var formData = attrForm.bindFromRequest
    try {
      db.withConnection {conn =>
        val stmt = conn.createStatement
        val rs = stmt.executeQuery(s"select * from people where id = ${id}")
        rs.next

        val name = rs.getString("name")
        val mail = rs.getString("mail")
        val tel = rs.getString("tel")
        val data = Attr(name, mail, tel)
        formData = attrForm.fill(data)
      }
    } catch {
      case e: SQLException =>
        Redirect(controllers.jdbcapp.routes.JdbcController.index)
    }

    Ok(views.html.edit(
      "フォームを編集してください",
      formData,
      id,
    ))
  }

  def update(id: Int) = Action {implicit request =>
    val formData = attrForm.bindFromRequest
    val data = formData.get

    try {
      db.withConnection {conn =>
        val ps = conn.prepareStatement("update people set name = ?, mail = ?, tel = ? where id = ?")
        ps.setString(1, data.name)
        ps.setString(2, data.mail)
        ps.setString(3, data.tel)
        ps.setInt(4, id)

        ps.executeUpdate
      }
    } catch {
      case e: SQLException =>
        Ok(views.html.add(
          "フォームに入力してください",
          attrForm,
        ))
    }

    Redirect(controllers.jdbcapp.routes.JdbcController.index)
  }

  def delete(id: Int) = Action {implicit request =>
    var formData: Attr = null
    try {
      db.withConnection {conn =>
        val stmt = conn.createStatement
        val rs = stmt.executeQuery(s"select * from people where id = ${id}")
        rs.next

        val name = rs.getString("name")
        val mail = rs.getString("mail")
        val tel = rs.getString("tel")
        formData = Attr(name, mail, tel)
      }
    } catch {
      case e: SQLException =>
        Redirect(controllers.jdbcapp.routes.JdbcController.index)
    }

    Ok(views.html.delete(
      "このレコードを削除します",
      formData,
      id,
    ))
  }

  def remove(id: Int) = Action {implicit request =>
    try {
      db.withConnection {conn =>
        val ps = conn.prepareStatement("delete  from people where id = ?")
        ps.setInt(1, id)

        ps.executeUpdate
      }
    } catch {
      case e: SQLException =>
        Redirect(controllers.jdbcapp.routes.JdbcController.index())
    }

    Redirect(controllers.jdbcapp.routes.JdbcController.index())
  }
}
