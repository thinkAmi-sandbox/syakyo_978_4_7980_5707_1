package models.slickapp

import javax.inject.{Inject, Singleton}
import slick.jdbc.JdbcProfile
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SlickPersonRepository @Inject()
  (dbConfigProvider: DatabaseConfigProvider)  // DBアクセスのための設定情報
  (implicit ec: ExecutionContext)  //  非同期に処理を実行する機能
{

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class SlickPeopleTable(tag: Tag) extends Table[SlickPerson](tag, "people") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc) // ゼロでなくてオー
    def name = column[String]("name")
    def mail = column[String]("mail")
    def fax = column[String]("fax")

    // 双方向マッピング：タプルとSlickPersonインスタンスをマッピング
    def * = (id, name, mail, fax) <>
      ((SlickPerson.apply _).tupled, SlickPerson.unapply)
  }

  private val slickPeople = TableQuery[SlickPeopleTable]

  // DBを操作するメソッド群
  // db.runで、アクションを非同期で実行し、Future(非同期)として返す
  def list(): Future[Seq[SlickPerson]] = db.run {
    // .asc/.descでソートする
    slickPeople.sortBy(_.name.asc).result
  }

  def create(name: String, mail: String, fax: String): Future[Int] =
    db.run(
      // IDには0を指定しているが、実際には内部で自動採番される
      slickPeople += SlickPerson(0, name, mail, fax)
    )

  def get(id: Int): Future[SlickPerson] = db.run {
    slickPeople.filter(_.id === id).result.head
  }

  def update(id: Int, name: String, mail: String, fax: String): Future[Int] = {
    db.run(
      slickPeople.insertOrUpdate(SlickPerson(id, name, mail, fax))
    )
  }

  def delete(id: Int): Future[Int] = {
    db.run(
      slickPeople.filter(_.id === id).delete
    )
  }

  def find(name: String): Future[Seq[SlickPerson]] = db.run {
    slickPeople.filter(_.name === name).result
  }

  def like(name: String): Future[Seq[SlickPerson]] = db.run {
    slickPeople.filter(_.name like s"%${name}%").result
  }

  def likeMultiple(value: String): Future[Seq[SlickPerson]] = db.run {
    slickPeople.filter(people =>
      // 各条件を()で囲んでおくことで、それぞれの条件が見やすくなる
      (people.name like s"%${value}%") || (people.mail like s"%${value}%")).result
  }
}
