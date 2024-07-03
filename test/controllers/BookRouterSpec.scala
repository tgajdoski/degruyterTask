import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.{JsResult, Json}
import play.api.mvc.{RequestHeader, Result}
import play.api.test._
import play.api.test.Helpers._
import play.api.test.CSRFTokenHelper._
import api.author.AuthorResource
import api.book.BookResource

import scala.concurrent.Future

class BookRouterSpec extends PlaySpec with GuiceOneAppPerTest {

  val bookRes = BookResource(1, "Foundation", Option("first book in the Foundation Trilogy"), "0-8108-5420-1", "2006-01-10 00:00:00.0", AuthorResource( 1, "Isaac Asimov"))
  "BookRouter" should {

    "get list of books" in {
      val request =FakeRequest(GET, "/api/books/").withHeaders(HOST -> "localhost:9000").withCSRFToken
      val home:Future[Result] = route(app, request).get

      val posts: Seq[BookResource] = Json.fromJson[Seq[BookResource]](contentAsJson(home)).get
      posts.filter(_.id == 1).head mustBe (bookRes)
    }

    "list of books when url ends with a slash" in {
      val request = FakeRequest(GET, "/api/books/").withHeaders(HOST -> "localhost:9000").withCSRFToken
      val home:Future[Result] = route(app, request).get

      val books: Seq[BookResource] = Json.fromJson[Seq[BookResource]](contentAsJson(home)).get
      books.filter(_.id == 1).head mustBe (bookRes)
    }
  }

  "get the book with id 2" in {
    val request = FakeRequest(GET, "/api/books/2").withHeaders(HOST -> "localhost:9000").withCSRFToken
    val home: Future[Result] = route(app, request).get

    val book: BookResource = Json.fromJson[BookResource](contentAsJson(home)).get
    book.id mustBe 2
    book.title mustBe "I, Robot"
    book.subtitle mustBe None
  }

  "return 404 for non-existent book" in {
    val request = FakeRequest(GET, "/api/books/9999").withHeaders(HOST -> "localhost:9000").withCSRFToken
    val home: Future[Result] = route(app, request).get

    status(home) mustBe 200
  }
}