package api.book

import play.api.libs.json._
import api.author.{AuthorId, AuthorRepository, AuthorResource}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}


/**
  * DTO for displaying book information.
  */
case class BookResource(id: Int, title: String, subtitle: Option[String], ISBN: String, published: String, author: AuthorResource)

object BookResource {
  /**
    * Mapping to read/write a BookResource out as a JSON value.
    */
    implicit val format: Format[BookResource] = Json.format
}


/**
  * Acts like a book service in layered architecture
  * Controls access to the backend data, returning BookResource
  */
class BookResourceHandler @Inject()(bookRepository: BookRepository, authorRepository: AuthorRepository)(implicit ec: ExecutionContext) {

  def create(bookInput: BookFormInput): Future[BookResource] = {
    val data = BookData(None, bookInput.title, bookInput.subtitle, bookInput.ISBN, bookInput.published, bookInput.authorId)
    authorRepository.get(AuthorId(bookInput.authorId)).flatMap {
      case Some(_) =>
        bookRepository.create(data).flatMap { id =>
          createBookResource(data.copy(id = Some(id)))
        }
      case None =>
        Future.failed(new Exception("Author does not exist"))
    }
  }

  def lookup(id: String): Future[Option[BookResource]] = {
    val bookFuture = bookRepository.get(BookId(id))
    bookFuture.flatMap {
      case Some(bookData) => createBookResource(bookData).map(Some(_))
      case None => Future.successful(None)
    }
  }

  def find: Future[Iterable[BookResource]] = {
    bookRepository.list().flatMap { bookDataList =>
      Future.sequence(bookDataList.map(bookData => createBookResource(bookData)))
    }
  }

  private def createBookResource(p: BookData): Future[BookResource] = {
    val authorFuture = authorRepository.get(AuthorId(p.authorId)).map(AuthorResource.fromAuthorData)
    authorFuture.map { author =>
      BookResource(p.id.map(_.underlying).getOrElse(0), p.title, p.subtitle, p.ISBN, p.published, author)
    }
  }

}
