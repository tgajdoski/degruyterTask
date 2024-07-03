package api.author

import anorm._
import anorm.SqlParser._
import play.api.db._
import api.DatabaseExecutionContext

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

final case class AuthorData(id: Option[AuthorId], name: String)

class AuthorId private (val underlying: Int) extends AnyVal {
  override def toString: String = underlying.toString
}

object AuthorId {
  def apply(raw: String): AuthorId = {
    require(raw != null)
    new AuthorId(Integer.parseInt(raw))
  }
}

trait AuthorRepository {
  def create(data: AuthorData): Future[AuthorId]

  def list(): Future[Iterable[AuthorData]]

  def get(id: AuthorId): Future[Option[AuthorData]]
}

@Singleton
class AuthorRepositoryImpl @Inject()(db: Database)(implicit ec: DatabaseExecutionContext)
  extends AuthorRepository {

  val simple: RowParser[AuthorData] = {
    long("id") ~
      str("name") map {
      case id ~ name  =>
        AuthorData(Some(AuthorId(id.toString)), name)
    }
  }

  override def list(): Future[Iterable[AuthorData]] = Future {
    db.withConnection { implicit connection =>
      SQL"SELECT * FROM author".as(simple.*)
    }
  }

  override def get(id: AuthorId): Future[Option[AuthorData]] = Future {
    db.withConnection { implicit connection =>
      SQL"SELECT * FROM author WHERE id = ${id.underlying}".as(simple.singleOpt)
    }
  }

  override def create(data: AuthorData): Future[AuthorId] = Future {
    db.withConnection { implicit connection =>
      val id: Long = SQL"SELECT MAX(id) as max_id FROM author".as(scalar[Long].single) + 1
      SQL"""
      INSERT INTO author (id, name)
      VALUES ($id, ${data.name})
    """.executeInsert()
      AuthorId(id.toString)
    }
  }
}