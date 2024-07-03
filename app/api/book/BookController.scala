package api.book

import play.api.Logger
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc._
import api.{ResourceBaseController, ResourceControllerComponents}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}


case class BookFormInput(title: String, subtitle: Option[String], ISBN: String, published: String, authorId:String)
object BookFormInput {
  def unapply(bookFormInput: BookFormInput): Option[(String, Option[String], String, String, String)] =
    Some((bookFormInput.title, bookFormInput.subtitle, bookFormInput.ISBN, bookFormInput.published, bookFormInput.authorId))
}

/**
  * Takes HTTP requests and produces JSON.
  */
class BookController @Inject()(rcc: ResourceControllerComponents)(
  implicit ec: ExecutionContext)
  extends ResourceBaseController(rcc) {

  private val logger = Logger(getClass)

  private val form: Form[BookFormInput] = {
    import play.api.data.Forms._

    Form(
      mapping(
        "title" -> nonEmptyText,
        "subtitle" -> optional(text),
        "ISBN" -> nonEmptyText,
        "published" -> nonEmptyText,
        "authorId" -> nonEmptyText
      )(BookFormInput.apply)(BookFormInput.unapply)
    )
  }

  def index: Action[AnyContent] = ResourceAction.async { implicit request =>
    logger.trace("index: ")
    rcc.bookResourceHandler.find.map { books =>
      Ok(Json.toJson(books))
    }
  }

  def process: Action[AnyContent] = ResourceAction.async { implicit request =>
    logger.trace("process: ")
    processJsonBook()
  }

  def show(id: String): Action[AnyContent] = ResourceAction.async {
    implicit request =>
      logger.trace(s"show: id = $id")
      rcc.bookResourceHandler.lookup(id).map { book =>
        Ok(Json.toJson(book))
      }
  }

  private def processJsonBook[A]()(
    implicit request: Request[A]): Future[Result] = {
      def failure(badForm: Form[BookFormInput]): Future[Result] = {
        Future.successful(BadRequest(Json.obj("error" -> "Bad form data")))
      }
      def success(input: BookFormInput): Future[Result] = {
        rcc.bookResourceHandler.create(input).map { book =>
          Created(Json.toJson(book))
        }
      }
    form.bindFromRequest().fold(failure, success)
  }
}
