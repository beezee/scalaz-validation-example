import scalaz._
import scalaz.Scalaz._

final class ValidationErrorOps(kv: (String, String)) {
  import ModelValidation._

  def validationError: ValidationError =
    Map(kv._1 -> NonEmptyList(kv._2))
}

trait ToValidationErrorOps {
  implicit def toValidationErrorOpsFromStringTuple(t: (String, String)):
  ValidationErrorOps = new ValidationErrorOps(t)
}

object User extends ToValidationErrorOps {
  type ValidationError = Map[String, NonEmptyList[String]]
  type ModelValidation[A] = Validation[ValidationError, A]

  def nonEmpty(key: String, value: String): ModelValidation[String] =
    value.some.filter(_.nonEmpty)
      .toSuccess((key, "cannot be empty").validationError)

  def validEmail(email: String): ModelValidation[String] =
    for {
      present <- nonEmpty("email", email)
      valid <- email.contains("@").fold(email.some, None)
        .toSuccess(("email", "must be a valid email")
        .validationError)
    } yield valid

  def validated(name: String, email: String, password: String):
  ModelValidation[User] =
    (nonEmpty("name", name) |@|
     validEmail(email) |@|
     nonEmpty("password", password))(User.apply)
}

case class User(name: String, email: String, password: String)
