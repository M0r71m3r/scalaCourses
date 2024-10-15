package lec2324.lecture

import lec2324.magicZone.ImplicitForPrint


object Match extends App with ImplicitForPrint {
  import scala.util.Random

  val nextInt = Random.nextInt(10)

  val sstr = nextInt match {
    case 0 => "zero"
    case 1 => "one"
    case 2 => "two"
    case _ => "unknown"
  }
  sstr.print
}

object CaseClassMatch extends App with ImplicitForPrint {

  trait Action

  case class Create(name: String, age: Int)           extends Action
  case class Read(id: Long, maxSize: Int)             extends Action
  case class Update(id: Long, newName: String)        extends Action
  case class Delete(id: Long, ignoreWarning: Boolean) extends Action

  def processAction(ac: Action): Unit = {
    ac match {
      case Create(name, age) if name.isEmpty =>
        "Error: empty name".print
      case Create(_, age) if age < 18 =>
        "Error: age < 18".print
      case Create(name, age) =>
        s"create with name: $name".print
      case Delete(id, ignoreWarning) =>
        s"delete by id: $id with ignore warning: $ignoreWarning".print
      case Read(id, maxSize) =>
        s"read by id: $id with max size: $maxSize".print
      case Update(id, newName) =>
        s"update by id: $id with new name: $newName".print
      case _ =>
        "unkown action".print
    }
  }

  processAction(Delete(1L, ignoreWarning = true))

  def processActionV2(ac: Action): Unit = {
    ac match {
      case c: Create =>
        s"create with name: ${c.name}".print
      case d: Delete =>
        s"delete by id: ${d.id} with ignore warning: ${d.ignoreWarning}".print
      case r: Read =>
        s"read by id: ${r.id} with max size: ${r.maxSize}".print
      case u: Update =>
        s"update by id: ${u.id} with new name: ${u.newName}".print
      case _ =>
        "unkown action".print
    }
  }

  processActionV2(Create("1", 21))

}

object SealedCaseClassMatch extends App with ImplicitForPrint {

  sealed trait Action // extends only in this file
  // check private trait
  case class Create(name: String, age: Int)           extends Action
  case class Read(id: Long, maxSize: Int)             extends Action
  case class Update(id: Long, newName: String)        extends Action
  case class Delete(id: Long, ignoreWarning: Boolean) extends Action

  def processAction(ac: Action): String = {
    ac match {
      case Create(name, age)         => "???"
      case Read(id, maxSize)         => "???"
      case Update(id, newName)       => "???"
      case Delete(id, ignoreWarning) => "???"
      //not exists "case _ =>" cause sealed trait
    }
  }

  processAction(Create("1", 1))

}

object ScalaOption extends App with ImplicitForPrint {
  // Option(any) => Some(any) or None

  val o1: Option[Int] = Option(1)         // Some(1)
  val o2: Option[Int] = None              // None
  val o3: Option[Int] = Option.empty[Int] // None
  val o4              = Option(null)      // None

  o1.isEmpty.print   // true if None
  o1.isDefined.print // !isEmpty
  o1.nonEmpty.print  // isDefined

  o1.contains(1).print // check doc

  o1.filter(x => x > 0).print // Some(any) if f in filter return true
  o1.filterNot(_ < 0).print   // Some(any) if f in filter return false
  o1.exists(_ > 1).print      // true if f in f exists return true
  o1.forall(_ == 1).print     // eq exists

  val o1Value = o1.get
  o1Value.print
  val o1ValueSafe = o1.getOrElse(-1)
  o1ValueSafe.print

  //val noneGet = o2.get // !!! java.util.NoSuchElementException: None.get

  def loadById(): String        = ""
  def loadByAnotherId(): String = ""

  val loadByOpt: Option[String] = Option(loadById())
    .orElse(Option(loadByAnotherId()))

  loadByOpt match {
    case Some(value) if value.isEmpty =>
      s"!$value!".print
    case Some(value) =>
      value.print
    case None =>
      "loadByOpt None".print
  }

  val optionoption = Option(Option("ha!"))
  optionoption.print
  val optionoptionFlatten = optionoption.flatten
  // [["ha!"]] . flatten => ["ha!"]
  // [[["ha!"]]] . flatten => [["ha!"]] . flatten => ["ha!"]
  optionoptionFlatten.print

  val optionNone = Option(Option.empty[String])
  optionNone.print
  val optionNoneFlatten = optionNone.flatten
  optionNoneFlatten.print

  "-----------------".print
  val o1Mapped = o1.map(_ * 10).map(_.toString).print
  "-----------------".print
  val o2Mapped = o3
    .map { x =>
      println("o3.map")
      x * 10
    }
    .map { x =>
      println("o3.map2")
      x.toString
    }

  val o1Foreach = o1.foreach(_.print)

  case class ClassWithOpt(id: Long, name: Option[String])

  def loadCWO: Option[ClassWithOpt] = Option(ClassWithOpt(1, Option("Urza")))

  val cwoNameOpt1: Option[String] = loadCWO.map(_.name).flatten // eq loadCWO.flatMap(_.name)
  val cwoNameOpt2: Option[String] = loadCWO.flatMap(_.name)

  val cwoNameOpt = for {
    c    <- loadCWO
    name <- c.name
  } yield name

  val cwoNameOptV1: Option[(String, Option[String])] = for {
    ClassWithOpt(_, nOpt) <- loadCWO
    name                  <- nOpt
  } yield name -> nOpt
}

object ScalaTry extends App with ImplicitForPrint {

  // Try(any) => Success(any) or Failure(exception)

  import scala.util.Try
  import scala.util.Failure
  import scala.util.Success

  case class Id(id: Int)

  def mayBeThrowException: Try[Id] = Try {
    throw new Exception("exception")
    //Id(1)
  }

  mayBeThrowException match {
    case Failure(exception) =>
      exception.getMessage.print
    case Success(value) =>
      value.print
  }

  val isSuccess = mayBeThrowException.isSuccess
  val isFailure = mayBeThrowException.isFailure

//  val withGet       = mayBeThrowException.get
  val withGetOrElse = mayBeThrowException.getOrElse(Id(-1))

  val withMap: Try[Int] = mayBeThrowException.map(_.id)

  def mayBeThrowExceptionV2: Try[Id] = Try {
    //    throw new Exception("exception")
    Id(1)
  }

  val x: Try[Id] = mayBeThrowException.flatMap(_ => mayBeThrowExceptionV2)

  val filteredFail = mayBeThrowException.filter(_.id != 0)
  filteredFail.print // Failure(java.lang.Exception: exception)
  val filteredSuc = mayBeThrowExceptionV2.filter(_.id != 0)
  filteredSuc.print //Success(Id(1))
  val filteredFailByFilter = mayBeThrowExceptionV2.filter(_.id == 0)
  filteredFailByFilter.print //Failure(java.util.NoSuchElementException: Predicate does not hold for Id(1))

  mayBeThrowException.foreach(_.print)

  val recoverFail = mayBeThrowException
    .map(_.id.toString)
    .recover { case _ =>
      "fail!"
    }
    .foreach(_.print) //Success(fail!)

  val recoverSuc = mayBeThrowExceptionV2.map(_.id.toString).recover { case _ =>
    "fail!"
  }
  recoverSuc.print //Success(1)

  def mayBeThrowExceptionV3(id: Id): Try[Id] = Try {
    Id(id.id + 1)
  }

  val res1 = mayBeThrowExceptionV2.flatMap(mayBeThrowExceptionV3)
  res1.print // Success(Id(2))

  val res2 = for {
    id1 <- mayBeThrowExceptionV2
    id2 <- mayBeThrowExceptionV3(id1)
  } yield id2
  res2.print // Success(Id(2))

}

object ScalaEither extends App with ImplicitForPrint {
  def getInfoFromUrl(url: String): Either[String, Int] =
    if (url.isEmpty) Left("Empty url!")
    else Right(42)

  val res1 = getInfoFromUrl("AAA")
  res1.print // Right(42)
  val res2 = getInfoFromUrl("")
  res2.print // Left(Empty url!)

  getInfoFromUrl("BBB") match {
    case Left(er) => er.print
    case Right(value) => value.print
  }

  val res3 = getInfoFromUrl("BBB").map(x => x.toLong).getOrElse(-1L)
  res3.print
  "---------------".print
  val res4 = getInfoFromUrl("").map(_.toLong)
  res4.print

  val res5 = getInfoFromUrl("AAA").right.map(i => i + 1) // eq getInfoFromUrl("AAA").map(_ + 1)
  val res6 = getInfoFromUrl("BBB").left.map(s => s"!!! $s !!!") //s: String
  val res7 = getInfoFromUrl("").left.map(s => s"!!! $s !!!") //s: String


  val res8 = getInfoFromUrl("").left.getOrElse("")

  val res9 = getInfoFromUrl("AAA").flatMap { x =>
    getInfoFromUrl("BBB").map { y =>
      x + y
    }
  }
  val res10 = for {
    x <- getInfoFromUrl("AAA")
    y <- getInfoFromUrl("BBB")
  } yield x + y

  val toOptSome = getInfoFromUrl("sss").toOption
  toOptSome.print
  val toOptNone = getInfoFromUrl("").toOption
  toOptNone.print

  case class PersonalInfo(id: Long, contractNumber: String, phone: String)

  val infos = List(
    PersonalInfo(1, "42", "99"),
    PersonalInfo(2, "", "1"),
    PersonalInfo(3, "24", ""),
    PersonalInfo(4, "11", "22")
  )
  def insertToDB(pi: PersonalInfo): Unit = s"Insert to DB: $pi".print

  val res: List[Either[String, Long]] = infos.map {
    case pi: PersonalInfo if pi.contractNumber.isEmpty =>
      Left(s"$pi => contractNumber isEmpty")
    case pi: PersonalInfo if pi.phone.isEmpty =>
      Left(s"$pi => phone isEmpty")
    case pi =>
      insertToDB(pi)
      Right(pi.id)
  }

  res.foreach(_.print)

}
