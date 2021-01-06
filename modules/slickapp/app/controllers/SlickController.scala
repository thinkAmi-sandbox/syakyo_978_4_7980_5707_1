package controllers.slickapp

import models.slickapp.{SlickPerson, SlickPersonForm, SlickPersonRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n._
import play.api.libs.json.Json
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Singleton
import javax.inject._

@Singleton
class SlickController @Inject() (
                                  repository: SlickPersonRepository, // リポジトリのインスタンス
                                  cc: MessagesControllerComponents
                                )(implicit ec: ExecutionContext)
extends MessagesAbstractController(cc) {
  // listが非同期のため、Action.asyncを使う
  def index() = Action.async {implicit request =>
    repository.list().map {people =>
      Ok(views.html.index(
        "Slick People Data.",
        people,
      ))
    }
  }

  def add() = Action {implicit request =>
    Ok(views.html.add_slick(
      "フォームを記入してください",
      SlickPerson.slickPersonForm,
    ))
  }

  def create() = Action.async {implicit request =>
    // foldでフォーム送信の結果を返す
    SlickPerson.slickPersonForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.add_slick("error.", errorForm)))
      },
      person => {
        repository.create(person.name, person.mail, person.fax).map {_ =>
          Redirect(controllers.slickapp.routes.SlickController.index())
        }
      }
    )
  }

  def show(id: Int) = Action.async {implicit request =>
    repository.get(id).map {person =>
      Ok(views.html.show_slick(
        "People Data.",
        person,
      ))

    }
  }

  def edit(id: Int) = Action.async {implicit request =>
    repository.get(id).map {person =>
      val formData: Form[SlickPersonForm] = SlickPerson.slickPersonForm
        .fill(SlickPersonForm(person.name, person.mail, person.fax))

      Ok(views.html.edit_slick(
        "Edit Person.", formData, id,
      ))
    }
  }

  def update(id: Int) = Action.async {implicit request =>
    SlickPerson.slickPersonForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.edit_slick("error.", errorForm, id)))
      },
      person => {
        repository.update(id, person.name, person.mail, person.fax).map {_ =>
          Redirect(controllers.slickapp.routes.SlickController.index())
        }
      }
    )
  }

  def delete(id: Int) = Action.async {implicit request =>
    repository.get(id).map {person =>
      Ok(views.html.delete_slick(
        "Delete Person", person, id
      ))
    }
  }

  def remove(id: Int) = Action.async {implicit request =>
    repository.delete(id).map {_ =>
      Redirect(controllers.slickapp.routes.SlickController.index())
    }
  }

  def find() = Action {implicit request =>
    Ok(views.html.find(
      "Find Data",
      SlickPerson.slickPersonFind, Seq[SlickPerson]()
    ))
  }

  def search() = Action.async {implicit request =>
    SlickPerson.slickPersonFind.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.find("error", errorForm, Seq[SlickPerson]())))
      },
      found => {
        repository.find(found.find).map {result =>
          Ok(views.html.find(
            s"Found: ${found.find}",
            SlickPerson.slickPersonFind,
            result
          ))
        }
      }
    )
  }

  def like() = Action {implicit request =>
    Ok(views.html.like(
      "Like Data",
      SlickPerson.slickPersonFind, Seq[SlickPerson]()
    ))
  }

  def searchLike() = Action.async { implicit request =>
    SlickPerson.slickPersonFind.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.like("error", errorForm, Seq[SlickPerson]())))
      },
      found => {
        repository.like(found.find).map {result =>
          Ok(views.html.like(
            s"Found: ${found.find}",
            SlickPerson.slickPersonFind,
            result
          ))
        }
      }
    )
  }

  def likeMultiple() = Action {implicit request =>
    Ok(views.html.like_multiple(
      "Like Data",
      SlickPerson.slickPersonFind, Seq[SlickPerson]()
    ))
  }

  def searchLikeMultiple() = Action.async {implicit request =>
    SlickPerson.slickPersonFind.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.like_multiple("error", errorForm, Seq[SlickPerson]())))
      },
      found => {
        repository.likeMultiple(found.find).map {result =>
          Ok(views.html.like_multiple(
            s"Found: ${found.find}",
            SlickPerson.slickPersonFind,
            result
          ))
        }
      }
    )
  }
}
