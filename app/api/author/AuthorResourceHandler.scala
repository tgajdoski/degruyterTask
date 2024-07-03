package api.author
import play.api.libs.json._

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}


/**
  * DTO for displaying author information.
  */
case class AuthorResource(id: Int, name: String)

object AuthorResource {
  /**
    * Mapping to read/write a AuthorResource out as a JSON value.
    */
  implicit val format: Format[AuthorResource] = Json.format

  def fromAuthorData(authorData: Option[AuthorData]): AuthorResource = {
    authorData match {
      case Some(data) => AuthorResource(data.id.get.underlying, data.name)
      case None => AuthorResource(0, "Unknown") // save book without author
    }
  }
}


/**
  * Acts like a author service in layered architecture
  * Controls access to the backend data, returning [[AuthorResource]]
  */
class AuthorResourceHandler @Inject()(authorRepository: AuthorRepository)(implicit ec: ExecutionContext) {


  def create(authInput: AuthorFormInput): Future[AuthorResource] = {
    val data = AuthorData(None, authInput.name)
    authorRepository.create(data).map { id =>
      createAuthorResource(data.copy(id = Some(id)))
    }
  }

  def lookup(id: String): Future[Option[AuthorResource]] = {
    val authFuture = authorRepository.get(AuthorId(id))
    authFuture.map { maybeAuthData =>
      maybeAuthData.map { authData =>
        createAuthorResource(authData)
      }
    }
  }

  def find: Future[Iterable[AuthorResource]] = {
    authorRepository.list().map { authDataList =>
      authDataList.map(authData => createAuthorResource(authData))
    }
  }

  private def createAuthorResource(a: AuthorData): AuthorResource = {
    AuthorResource.fromAuthorData(Some(a))
  }

}
