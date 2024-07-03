package api.book

import anorm._
import anorm.SqlParser._
import play.api.db._
import api.DatabaseExecutionContext

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future
//import scala.util.Try

/**
 * acts like DAP Data Access Object in layered architecture
 */
final case class BookData(id: Option[BookId], title: String, subtitle: Option[String], ISBN: String, published: String, authorId: String)

class BookId private (val underlying: Int) extends AnyVal {
  override def toString: String = underlying.toString
}

object BookId {
  def apply(raw: String): BookId = {
    require(raw != null)
    new BookId(Integer.parseInt(raw))
//    val id = Try(raw.toInt).getOrElse(0)
//    new BookId(id)
  }
}

trait BookRepository {
  def create(data: BookData): Future[BookId]

  def list(): Future[Iterable[BookData]]

  def get(id: BookId): Future[Option[BookData]]
}

@Singleton
class BookRepositoryImpl @Inject()(db: Database)(implicit ec: DatabaseExecutionContext)
  extends BookRepository {


  val simple: RowParser[BookData] = {
    long("id") ~
      str("title") ~
      str("subtitle").? ~
      str("ISBN") ~
      date("published") ~
      long("author_id") map {
      case id ~ title ~ subtitle ~ isbn ~ published ~ authorId =>
        BookData(Some(BookId(id.toString)), title, subtitle, isbn, published.toString, authorId.toString)
    }
  }

  override def list(): Future[Iterable[BookData]] = Future {
    db.withConnection { implicit connection =>
      SQL"SELECT * FROM book".as(simple.*)
    }
  }

  override def get(id: BookId): Future[Option[BookData]] = Future {
    db.withConnection { implicit connection =>
      SQL"SELECT * FROM book WHERE id = ${id.underlying}".as(simple.singleOpt)
    }
  }

  override def create(data: BookData): Future[BookId] = Future {
    db.withConnection { implicit connection =>
      val id: Long = SQL"SELECT MAX(id) as max_id FROM book".as(scalar[Long].single) + 1
      SQL"""
      INSERT INTO book (id, title, subtitle, ISBN, published, author_id)
      VALUES ($id, ${data.title}, ${data.subtitle}, ${data.ISBN}, ${data.published}, ${data.authorId})
    """.executeInsert()
      BookId(id.toString)
    }
  }
}