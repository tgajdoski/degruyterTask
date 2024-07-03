package api

import org.apache.pekko.actor.ActorSystem
import play.api.http.{FileMimeTypes, HttpVerbs}
import play.api.i18n.{Langs, MessagesApi}
import play.api.libs.concurrent.CustomExecutionContext
import play.api.mvc._
import play.api.Logger
import api.author.AuthorResourceHandler
import api.book.BookResourceHandler

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

/**
 * custom action builder; used just for logging requests
 * ActionBuilder can be used by default in bookcontroller and authorcontroller, this is just for implementing logs, learning and playing around
 */
class ResourceActionBuilder @Inject()(playBodyParsers: PlayBodyParsers)
                                     (implicit val executionContext: ExecutionContext)
  extends ActionBuilder[Request, AnyContent] with HttpVerbs {

  override val parser: BodyParser[AnyContent] = playBodyParsers.anyContent

  private val logger = Logger(this.getClass)

  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
    logger.info(s"Received request: ${request.method} ${request.uri}")
    block(request)
  }
}

case class ResourceControllerComponents @Inject()(
    resourceActionBuilder: ResourceActionBuilder,
    bookResourceHandler: BookResourceHandler,
    authorResourceHandler: AuthorResourceHandler,
    actionBuilder: DefaultActionBuilder,
    parsers: PlayBodyParsers,
    messagesApi: MessagesApi,
    langs: Langs,
    fileMimeTypes: FileMimeTypes,
    executionContext: scala.concurrent.ExecutionContext)
    extends ControllerComponents

class ResourceBaseController @Inject()(rcc: ResourceControllerComponents)
    extends BaseController {
  override protected def controllerComponents: ControllerComponents = rcc
  def ResourceAction: ResourceActionBuilder = rcc.resourceActionBuilder
}

class DatabaseExecutionContext @Inject()(actorSystem: ActorSystem)
  extends CustomExecutionContext(actorSystem, "database.dispatcher")