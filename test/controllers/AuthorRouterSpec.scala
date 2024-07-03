package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.test.CSRFTokenHelper._
import play.api.test.Helpers._
import play.api.test._
import api.author.AuthorResource
import api.book.BookResource

import scala.concurrent.Future

class AuthorRouterSpec extends PlaySpec with GuiceOneAppPerTest {

  val autRes = AuthorResource(1, "Isaac Asimov")
  "AuthRouter" should {

    "get list of authors" in {
      val request =FakeRequest(GET, "/api/authors").withHeaders(HOST -> "localhost:9000").withCSRFToken
      val home:Future[Result] = route(app, request).get

      val auths: Seq[AuthorResource] = Json.fromJson[Seq[AuthorResource]](contentAsJson(home)).get
      auths.filter(_.id == 1).head mustBe (autRes)
    }

    "list of authors when url ends with a slash" in {
      val request = FakeRequest(GET, "/api/authors/").withHeaders(HOST -> "localhost:9000").withCSRFToken
      val home:Future[Result] = route(app, request).get

      val auths: Seq[AuthorResource] = Json.fromJson[Seq[AuthorResource]](contentAsJson(home)).get
      auths.filter(_.id == 1).head mustBe (autRes)
    }
  }

  "get the authors with id 2" in {
    val request = FakeRequest(GET, "/api/authors/2").withHeaders(HOST -> "localhost:9000").withCSRFToken
    val home: Future[Result] = route(app, request).get

    val auth: AuthorResource = Json.fromJson[AuthorResource](contentAsJson(home)).get
    auth.id mustBe 2
    auth.name mustBe "Ursula K. Le Guin"

  }

  "return 404 for non-existent authors" in {
    val request = FakeRequest(GET, "/api/authors/9999").withHeaders(HOST -> "localhost:9000").withCSRFToken
    val home: Future[Result] = route(app, request).get

    status(home) mustBe 200
  }
}