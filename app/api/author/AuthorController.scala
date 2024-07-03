package api.author

import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._
import api.{ResourceBaseController, ResourceControllerComponents}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}


case class AuthorFormInput(name: String)
object AuthorFormInput {
  def unapply(authorFormInput: AuthorFormInput): Option[String] =
    Some(authorFormInput.name)
}

  class AuthorController @Inject()(rcc: ResourceControllerComponents)(
    implicit ec: ExecutionContext)
    extends ResourceBaseController(rcc) {

    private val logger = Logger(getClass)

    private val form: Form[AuthorFormInput] = {
      Form(
        mapping(
          "name" -> nonEmptyText,
        )(AuthorFormInput.apply)(AuthorFormInput.unapply)
      )
    }

    def index: Action[AnyContent] = ResourceAction.async { implicit request =>
      logger.trace("index: ")
      rcc.authorResourceHandler.find.map { author =>
        Ok(Json.toJson(author))
      }
    }

    def process: Action[AnyContent] = ResourceAction.async { implicit request =>
      logger.trace("process: ")
      processJsonAuthor()
    }

    def show(id: String): Action[AnyContent] = ResourceAction.async {
      implicit request =>
        logger.trace(s"show: id = $id")
        rcc.authorResourceHandler.lookup(id).map { author =>
          Ok(Json.toJson(author))
        }
    }

    private def processJsonAuthor[A]()(
      implicit request: Request[A]): Future[Result] = {
        def failure(badForm: Form[AuthorFormInput]) = {
          Future.successful(BadRequest(Json.obj("error" -> "Bad form data")))
        }

        def success(input: AuthorFormInput) = {
          rcc.authorResourceHandler.create(input).map { author =>
            Created(Json.toJson(author))
          }
        }
      form.bindFromRequest().fold(failure, success)
    }
}
