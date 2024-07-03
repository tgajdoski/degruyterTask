import javax.inject.Inject
import play.api.OptionalDevContext
import play.api.http._
import play.api.mvc._
import play.api.routing.Router
import play.core.WebCommands
import play.api.Logger

/**
  * Handles all requests.
  * just for logging purposes and learning - can be removed completely
  * https://www.playframework.com/documentation/latest/ScalaHttpRequestHandlers#extending-the-default-request-handler
  */
class RequestHandler @Inject()(webCommands: WebCommands,
                               optDevContext: OptionalDevContext,
                               router: Router,
                               errorHandler: HttpErrorHandler,
                               configuration: HttpConfiguration,
                               filters: HttpFilters)
    extends DefaultHttpRequestHandler(webCommands,
                                      optDevContext,
                                      () => router,
                                      errorHandler,
                                      configuration,
                                      filters) {

  private val logger = Logger(this.getClass)

  override def handlerForRequest(
      request: RequestHeader): (RequestHeader, Handler) = {
    logger.info(s"Received request: ${request.method} ${request.uri}")
    super.handlerForRequest(request)
  }
}