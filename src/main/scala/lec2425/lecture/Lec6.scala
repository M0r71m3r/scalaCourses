package lec2425.lecture

import scala.util.{Failure, Success, Try}

object OptionHW extends App {
  // good
  def getMeAge(o: Option[Int]): String =
    o match {
      case Some(value) if value < 0   => "???"
      case Some(value) if value < 18  => "juv"
      case Some(value) if value < 36  => "sub adult"
      case Some(value) if value <= 99 => "adult"
      case Some(value) if value > 99  => "rly?"
      case None                       => "unknown"
    }

  // so so
  def getMeAgeV2(o: Option[Int]): String = {
    val noneStr = "None"
    val age     = o.map(_.toString).getOrElse(noneStr)
    if (age == noneStr) "unknown"
    else if (age.toInt < 0) "???"
    else if (age.toInt < 18) "juv"
    else if (age.toInt < 36) "sub adult"
    else if (age.toInt <= 99) "adult"
    else "rly?"
  }
}

object OptionForComp extends App {
  val o1 = Option(1)
  val o2 = Option.empty[Int]
  val o3 = Option(1)

  val sumOpt = o1.filter(_ > 0).flatMap { i1 =>
    o2.filter(_ > 0).flatMap { i2 =>
      o3.map { i3 =>
        i1 + i2 + i3
      }
    }
  }
  println(sumOpt)

  val sumOpt2 = for {
    i1 <- o1 // flatMap
    if i1 > 0 // withFilter
    i2 <- o2 // flatMap
    if i2 > 0 // withFilter
    i3 <- o3 // map
  } yield i1 + i2 + i3
  println(sumOpt2)
}

object MyClassForComp extends App {
  object ForCompSupport {
    def apply[A](a: A): ForCompSupport[A] =
      if (a == null) ForCompEmpty[A]() else ForCompNotEmpty(a)
  }
  trait ForCompSupport[A] {
    def isEmpty: Boolean = this == ForCompEmpty[A]()
    def get: A
    def map[B](f: A => B): ForCompSupport[B] =
      if (isEmpty) ForCompEmpty[B]() else ForCompNotEmpty(f(this.get))
    def flatMap[B](f: A => ForCompSupport[B]): ForCompSupport[B] =
      if (isEmpty) ForCompEmpty[B]() else f(this.get)
    def withFilter(p: A => Boolean): ForCompSupport[A] =
      if (isEmpty || p(this.get)) this else ForCompEmpty[A]()
  }
  case class ForCompEmpty[A]() extends ForCompSupport[A] {
    override def get: A = throw new Exception("ForCompEmpty")
  }
  case class ForCompNotEmpty[A](a: A) extends ForCompSupport[A] {
    override def get: A = a
  }

  val x1_1: ForCompSupport[String] = ForCompSupport[String](null)
  val x1_2: ForCompSupport[Int]    = ForCompSupport(1)
  println(x1_1)
  println(x1_2)

  val fst  = ForCompNotEmpty(2).map(i => i * i)
  val fst2 = ForCompNotEmpty("1")
  val x = for {
    x <- fst // flatMap
    if x > 2 // withFilter
    x2 <- fst2 // map
  } yield x + x2
  println(x)
}

object ScalaTry extends App {
  // Try(any) => Success(any) || Failure(exception)
  val t  = Try(1)
  val b1 = t.isSuccess
  val b2 = t.isFailure

  def mayBeThrow(): Try[Int] = Try {
//    1
    throw new Exception("boom")
  }

  val x = mayBeThrow()
  x match {
    case Failure(exception) =>
      println(exception.getMessage)
    case Success(value) =>
      println(value)
  }
  //val x0 = x.get           // bad, before check _.isSuccess
  val x1 = x.getOrElse(11) // isFailure => default from getOrElse
  // Success[A] map => Success[B]
  // Failure[A] map => Failure[B]
  val x2: Try[String] = x.map { i =>
//    println(i)
    s"$i"
  }

  val x3: Try[Try[Int]] = x.map(_ => mayBeThrow())
  val x4: Try[Int]      = x.flatMap(_ => mayBeThrow())
  val x5: Try[Int]      = x.map(_ => mayBeThrow()).flatten

  val x6 = x.filter(_ > 1)
  x.foreach(_ => println("foreach"))

  // isSuccess => Int else recover with Success
  val x7: Try[Any] = mayBeThrow().recover { case _ =>
    "fail"
  }
  println(x7)

  val x8 = for {
    x  <- mayBeThrow()
    x2 <- mayBeThrow()
  } yield x + x2
  println(x8)
}

object ScalaEither extends App {
  def getInfoFromUrl(url: String): Either[String, Int] = {
    if (url.isEmpty) Left("Empty url!!!")
    else Right(42)
  }
  val res = getInfoFromUrl("")
  println(res)
  val res1 = getInfoFromUrl("111")
  println(res1)

  val b1 = res.isLeft
  val b2 = res.isRight

  res match {
    case Left(value) =>
      println(s" =>>> Left($value)")
    case Right(value) =>
      println(s" =>>> Right($value)")
  }

  val r = res.right
  val l = res.left

  val x1: Either[String, Either[String, Int]] = getInfoFromUrl("1").map { i =>
    getInfoFromUrl("2").map { i2 =>
      i + i2
    }
  }

  val x2: Either[String, Int] = getInfoFromUrl("1").flatMap { i =>
    getInfoFromUrl("2").map { i2 =>
      i + i2
    }
  }
  println(x2)
  println("1111111111111111111111111111111111111111111111111111")
  println(getInfoFromUrl("2").toOption) // Some(42)
  println(getInfoFromUrl("").toOption) // None
}

object ScalaEitherForProd extends App {
  case class PersonalInfo(id: Long, contractNumber:String, inn: Long)

  val infos = List(
    PersonalInfo(1, "111", 2),
    PersonalInfo(2, "112", 2),
    PersonalInfo(3, "113", 2),
    PersonalInfo(4, "114", 3)
  )

  def insertToDB(pi: PersonalInfo): Unit = println(pi)

  val res: List[Either[String, Long]] = infos.map {
    case pi if pi.contractNumber.endsWith("2") =>
      Left(s"$pi => contractNumber endsWith 2 ")
    case pi if pi.inn == 3 =>
      Left(s"$pi =>inn == 3 ")
    case pi =>
      insertToDB(pi)
      Right(pi.id)
  }

  def toDBFail: String => Unit = _ => ()

  res.foreach {
    case Left(value) =>
      toDBFail(value)
    case Right(value) =>
      println(s"Inserted: $value")
  }
}