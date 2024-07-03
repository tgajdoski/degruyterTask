package controllers

import javax.inject._
import play.api.mvc._
import api.book.BookResourceHandler
import scala.concurrent.ExecutionContext

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents, bookResourceHandler: BookResourceHandler)(implicit ec: ExecutionContext) extends BaseController {

  def index() = Action.async { implicit request: Request[AnyContent] =>
    bookResourceHandler.find.map { books =>
      Ok(views.html.index(books))
    }
  }
}