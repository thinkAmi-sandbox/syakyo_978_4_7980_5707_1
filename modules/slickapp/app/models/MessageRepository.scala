package models.slickapp

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import java.sql.Timestamp
import java.util.Date
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MessageRepository @Inject()
(dbConfigProvider: DatabaseConfigProvider)
(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  // SlickPeopleテーブル用。MessageTableから利用する
  private class PeopleTable(tag: Tag) extends Table[SlickPerson](tag, "people") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def mail = column[String]("mail")

    def fax = column[String]("fax")

    def * = (id, name, mail, fax) <>
      ((SlickPerson.apply _).tupled, SlickPerson.unapply)
  }

  private val people = TableQuery[PeopleTable]

  // message用
  private class MessageTable(tag: Tag) extends Table[Message](tag, "messages") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def personId = column[Int]("personId")
    def message = column[String]("message")
    def created_at = column[Timestamp]("created_at")

    def * = (id, personId, message, created_at) <>
      ((Message.apply _).tupled, Message.unapply)
  }

  private val messages = TableQuery[MessageTable]

  // Messageのみの取得
  def listMsg: Future[Seq[Message]] = {
    db.run(
      messages.sortBy(_.created_at.desc).result
    )
  }

  // Person付Messageの取得
  def listMsgWithPerson(): Future[Seq[MessageWithPerson]] = {

    val query = messages
      .sortBy(_.id.desc)  // sortByと書いてあるが、テーブルのレコードではないため、並べ替えられない
      .join(people).on(_.personId === _.id)

    db.run(query.result).map { obj =>
      // _が(MesageTable, PersonTable)なので、_._1がMessageになる
      obj.groupBy(_._1.id).map { case (_, tuples) =>
        val (message, person) = tuples.head
        MessageWithPerson(message, person)
      }.toSeq
        .sortBy(_.message.created_at.getTime()) // Scalaのソート機能
        .reverse  // そのままではTimestampの古い順なので、新しい順にするためreverse
    }
  }

  def getMsg(id: Int): Future[Message] = db.run {
    messages.filter(_.id === id).result.head
  }

  def createMsg(personId: Int, message: String): Future[Int] =
    db.run(
      messages += Message(0, personId, message,
        new Timestamp(new Date().getTime)) // ダミーとして使われるので、適当な値を入れてみる
    )
}
