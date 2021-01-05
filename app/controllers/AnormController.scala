package controllers

import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import controllers.PersonForm.{AnormData, Data, anormForm, anormParser, form}
import play.api.db
import play.api.db.Database
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

import java.sql.SQLException
import javax.inject.Inject

// 忘れずにimportを追加
import anorm._

class AnormController @Inject()(db: Database, cc: MessagesControllerComponents)
  extends MessagesAbstractController(cc){
  def index() = Action {implicit request =>
    db.withConnection { implicit  conn =>
      // 末尾の "*" は、ゼロ個以上の値という意味
      val result: List[String] = SQL("Select * from people").as(SqlParser.str("name").*)

      Ok(views.html.anorm_index("People Data.", result))
    }
  }

  def multi_values() = Action {implicit request =>
    db.withConnection { implicit conn =>
      // 末尾の "*" は、ゼロ個以上の値という意味
      val result: List[Any] = SQL("Select * from people")
        // "~" を使って、2つのSqlParserを連結
        .as(SqlParser.str("name").~(SqlParser.str("mail")).*)

      Ok(views.html.anorm_multi_values("People Data.", result))
    }
  }

  def index_parser() = Action {implicit request =>
    db.withConnection { implicit conn =>
      val result: List[AnormData] = SQL("Select * from people").as(anormParser.*)

      Ok(views.html.anorm_index_parser("People Data.", result))
    }
  }

  def showSingle(id: Int) = Action {implicit request =>
    db.withConnection { implicit conn =>
      val result: AnormData = SQL("Select * from people where id = {id}")
        .on("id" -> id)
        // .single()で1件だけ取り出す
        .as(anormParser.single)

      Ok(views.html.anorm_single("People Data.", result))
    }
  }

  def add() = Action {implicit request =>
    // フォームにidを指定できないことから、anormFormではなくformを指定する
    Ok(views.html.add_anorm(
      "フォームを記入してください",
      form,
    ))
  }

  def create() = Action {implicit request =>
    val formData = form.bindFromRequest
    val data = formData.get

    // Anormでやる場合は、明示的にimplicitを指定する
    db.withConnection {implicit conn =>
      SQL("insert into people values (default, {name}, {mail}, {tel})")
        .on(
          "name" -> data.name,
          "mail" -> data.mail,
          "tel" -> data.tel,
        ).executeInsert()
    }

    Redirect(routes.AnormController.index())
  }

  def edit(id: Int) = Action {implicit request =>
    var formData = anormForm.bindFromRequest
    db.withConnection {implicit conn =>
      val person: AnormData = SQL("select * from people where id={id}")
        .on("id" -> id)
        .as(anormParser.single)
      formData = anormForm.fill(person)
    }

    Ok(views.html.edit_anorm(
      "フォームを編集してください",
      formData,
      id,
    ))
  }

  def update(id: Int) = Action {implicit request =>
    val formData = anormForm.bindFromRequest
    val data = formData.get

    db.withConnection {implicit conn =>
      val result = SQL("update people set name = {name}, mail = {mail}, tel = {tel} where id = {id}")
        .on(
          "name" -> data.name,
          "mail" -> data.mail,
          "tel" -> data.tel,
          "id" -> id,
        )
        .executeUpdate()
    }

    Redirect(routes.AnormController.index())
  }

  def delete(id: Int) = Action { implicit request =>
    db.withConnection { implicit conn =>
      val data: AnormData = SQL("select * from people where id = {id}")
        .on("id" -> id)
        .as(anormParser.single)

      Ok(views.html.delete_anorm(
        "このレコードを削除します",
        data,
        id,
      ))
    }
  }

  def remove(id: Int) = Action {implicit request =>
    db.withConnection {implicit conn =>
      val result = SQL("delete from people where id = {id}")
        .on("id" -> id)
        .executeUpdate()
    }
    Redirect(routes.AnormController.index())
  }
}
