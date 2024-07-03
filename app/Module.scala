import javax.inject._
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import play.api.{Configuration, Environment}
import api.book._
import api.author._
/**
  * Sets up custom components for Play.
  *
  * https://www.playframework.com/documentation/latest/ScalaDependencyInjection
  */
class Module(environment: Environment, configuration: Configuration)
    extends AbstractModule
    with ScalaModule {

  override def configure() = {
    bind[BookRepository].to[BookRepositoryImpl].in[Singleton]()
    bind[AuthorRepository].to[AuthorRepositoryImpl].in[Singleton]()

  }
}
