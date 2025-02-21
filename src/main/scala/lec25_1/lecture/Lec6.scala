package lec25_1.lecture

import scala.util.{Failure, Success, Try}

object Lec6ForCompr extends App {
  case class ClassWithOption(id: Long, urza: Option[Urza])

  case class Urza(name: Option[String])

  val cwo  = Option(ClassWithOption(1, Some(Urza(Some("urza")))))
  val name = cwo.flatMap(_.urza).flatMap(_.name)

  // Во что раскладывается for Comp
  val nameCorrect = cwo.flatMap { cwo =>
    cwo.urza.flatMap { urzo =>
      urzo.name.map { n =>
        n.capitalize
      }
    }
  }
  val name1 = for {
    cwo1 <- cwo
    urza <- cwo1.urza
    name <- urza.name
  } yield name

  val name2 = for {
    ClassWithOption(_, n) <- cwo
    Urza(u)               <- n
    name                  <- u if name.nonEmpty
  } yield name

  println(s"name: $name")
  println(s"name1: $name1")
  println(s"name2: $name2")
}

object Lec6ForComprCustom extends App {
  trait ForCompSupport[A] {
    def flatMap[B](f: A => ForCompSupport[B]): ForCompSupport[B] =
      if (isEmpty) ForCompSupportEmpty[B]() else f(this.get)

    def map[B](f: A => B): ForCompSupport[B] =
      if (isEmpty) ForCompSupportEmpty[B]() else ForCompSupportTest(f(this.get))

    def withFilter(p: A => Boolean): ForCompSupport[A] =
      if (isEmpty || p(this.get)) this else ForCompSupportEmpty[A]()

    def isEmpty: Boolean = this == ForCompSupportEmpty[A]()

    def get: A
  }

  object ForCompSupport {
    def apply[A](i: A): ForCompSupport[A] = if (i == null) ForCompSupportEmpty() else ForCompSupportTest(i)
  }

  case class ForCompSupportEmpty[A]() extends ForCompSupport[A] {
    override def get: A = throw new Exception("ForCompSupportEmptty !!!")
  }

  case class ForCompSupportTest[A](i: A) extends ForCompSupport[A] {
    override def get: A = i
  }

  val fcst   = ForCompSupport(2).map(_ * 2)
  val fcsStr = ForCompSupport("any")

  val x = for {
    x <- fcst
    if x > 2
    x1 <- fcsStr
  } yield (x, x1)
}

object Lec6Try extends App {
  val maybeThrowException = Try {
    1 / 0
  }

  val maybeThrowExceptionV2 = Try {
    1 / 1
  }

  maybeThrowException match {
    case Failure(exception) => println("exception")
    case Success(value)     => println(value)
  }

  val isSuccess = maybeThrowException.isSuccess
  val isFailure = maybeThrowException.isFailure
  //  val getMaybe = maybeThrowException.get
  val getOrElse = maybeThrowException.getOrElse(3d)
  val mapTry    = maybeThrowException.map(_ * 2)
  val filterTry = mapTry.filter(_ < 2)
  maybeThrowException.foreach(println)

  val recoverFail = maybeThrowException.recover {
    case _: ArithmeticException =>
      println("Arichmetic")
      4d

    case x: RuntimeException =>
      println("fail")
      3d
  }
  println(s"recoverFail: ${recoverFail}")

  val newX = maybeThrowException.flatMap(_ => maybeThrowExceptionV2)

  val newX1 = for {
    n  <- maybeThrowException
    n1 <- maybeThrowExceptionV2
  } yield n1

  //  CoreUtils.h2hConfig.flatMap { conf =>
  //    Try(conf.getBoolean("isH2hImportEnabled")).flatMap { enabled =>
  //      Try(conf.getConfig("quartz")).flatMap { quartz =>
  //        Try(quartz.getConfig("schedules")).flatMap { schedule =>
  //          Try(schedule.getConfig("weekdays")).flatMap { weekdays =>
  //            Try(weekdays.getString("expression")).flatMap { weekdaysExpression =>
  //              Try(schedule.getConfig("weekend")).flatMap { weekend =>
  //                Try(weekend.getString("expression")).map { weekendExpression =>
  //                  (enabled, weekdaysExpression, weekendExpression)
  //                }
  //              }
  //            }
  //          }
  //        }
  //      }
  //    }
  //  }

  val x = Try {
    1 / 0
  }.toOption

}

object Lec6Either extends App {
  // Either => Left(A) or Right(B)
  def getInforFromUrL(url: String): Either[String, Int] = {
    if (url.isEmpty) Left("Empty url!")
    else Right(url.length)
  }

  val res1 = getInforFromUrL("AAA")
  println(res1)
  val res2 = getInforFromUrL("")
  println(res2)

  getInforFromUrL("BBB") match {
    case Left(valueLeft)   => println(valueLeft)
    case Right(valueRight) => println(valueRight)
  }

  getInforFromUrL("CCC").map(_.toLong)

  getInforFromUrL("DDD").left
  getInforFromUrL("DDD").right

  getInforFromUrL("EEE").flatMap { x =>
    getInforFromUrL("ZZZ").map { y => x + y }
  }

  for {
    eee <- getInforFromUrL("EEE")
    zzz <- getInforFromUrL("ZZZ")
  } yield eee + zzz

  val x = getInforFromUrL("GGG").toOption

  case class PersonalInfo(id: Long, contractNumber: String, phone: String)
  val infos = List(
    PersonalInfo(1, "43", "99"),
    PersonalInfo(2, "", "1"),
    PersonalInfo(3, "99", ""),
    PersonalInfo(4, "11", "43"),
    PersonalInfo(5, "24", "22"),
  )

  def insertToDb(pi: PersonalInfo): Unit = println(s"Insert to DB : ${pi}")
  val result = infos.map {
    case pi: PersonalInfo if pi.contractNumber.isEmpty =>
      Left(s"${pi} => contractNumber is empty")

    case pi: PersonalInfo if pi.phone.isEmpty =>
      Left(s"$pi => phone is empty")

    case pi =>
      insertToDb(pi)
      Right(pi.id)
  }

  println("----------------------------")
  val resultStr = result.mkString(",\n")
  println(resultStr)

}
