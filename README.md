#Scalaz Validation Example

Just some dumb fake code to play with scalaz validations

To use:

*  Clone repo
*  sbt console

Try the following at console:

```scala
import User._

User.validated("name", "email@domain", "password")
User.validated("", "email@domain", "password")
User.validated("name", "email.domain", "password")
User.validated("name", "email.domain", "")
```

You get the point
