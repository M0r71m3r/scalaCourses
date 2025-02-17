package lec25_1.lecture

object HWRetro3 extends App {
  object Book {
    def apply(auth: String, title: String, year: Int): Book =
      new Book(auth, title, year)
//      var bk:Book = new Book
//      bk.Author = auth
//      bk.Title = title
//      bk.PublishedYear = Year
//      bk
  }
  class Book(auth: String, title: String, year: Int) {
//    var Author: String     = ""
//    var Title: String      = ""
//    var PublishedYear: Int = 0
    override def toString: String =
      s"Author: $auth\nTitle:$title\nPublished in the year: $year"
  }

  val b = Book("1", "2", 3)
  println(b)
}

object HWRetro4 extends App {
  trait Shape {
    val area: Double
    def getArea: Double // = 0
  }
}

object ScalaOption extends App {
  //java: Optional<E>
  // Optional.ofNullable(...) => good
  // Optional.of(...) => bad

  // Option(any) => Some(any) or None
  val o1 = Option(1)         // Some(1)
  val o2 = Option.empty[Int] // None
  val o3 = None              // None
  val o4 = Some(1)           // Some(1)
  val o5 = Option(null)      // None
  // val o6 = Some(null)

  val b1: Boolean = o1.isEmpty
  val b2: Boolean = o1.isDefined
  val b3: Boolean = o1.nonEmpty

  val b4: Boolean = o1.contains(1)

  val o_1: Option[Int] = o1.filter(_ > 0)    // Some(...) or None
  val o_2: Option[Int] = o1.filterNot(_ > 0) // Some(...) or None

  //option match {   case Some(x) => p(x)   case None    => false }
  val b5: Boolean = o1.exists(_ > 0) // true if p(x) == true
  //option match {   case Some(x) => p(x)   case None    => true }
  val b6 = o1.forall(_ > 0) // true if option.isEmpty or p(x) == true

  val i  = o1.get                                      // None => NoSuchElementException
  val i1 = o1.getOrElse(2)                             // if None => 2
  val i2 = o1.getOrElse(throw new Exception("babah!")) // if None => new Exception
}

object ScalaOptionProd extends App {
  def loadById(id: Long): String               = null
  def loadByContractNumber(cn: String): String = "123"
  val x: String =
    Option(loadById(1))
      .orElse(Option(loadByContractNumber("321")))
      .orElse(Option(loadByContractNumber("213")))
      .orElse(Option(loadByContractNumber("312")))
      .getOrElse("not found")
  println(x)
}

object OptionMatch extends App {
  // val x = Option(1)
  // x match {...}
  val y = Option(1) match {
    case None =>
      println("None!")
      3

    case Some(value) if value > 2 =>
      println("> 2")
      1

    case Some(v) =>
      println(s"$v")
      2
  }
}

object ScalaOptionSomeSome extends App {
  val o: Option[Option[Int]] = Option(Option(1))
  // [[x]] flatten => [x]
  // [[[x]]] flatten => [[x]]
  // [[[x]]] flatten flatten => [x]
  val x: Option[Int] = o.flatten
  println(x)                    // Some(1)
  println(Option(None).flatten) // None
}

object ScalaOptionMap extends App {
  val o = Option(1)
  o.map(_ + 1)
  o.map(i => i + 1)
  o.map((i: Int) => i + 1)

  val o1 = o.map(_ + 1).map(_ + 1).map(_ + 1)
  println(o1) // Some(4)
  println("--------------")
  val o2 = o
    .map { i =>
      println(i)
      i + 1
    }
    .map { i =>
      println(i)
      i + 2
    }
    .filter { _ > 6 }
    .map { i =>
      println(i)
      i * 2
    }
  println(o2)
  println("----------------")
  println(o)
}

object ScalaOptionMapSomeSome extends App {
  val o = Option(Option(1))
  o.map(o => o.map(_ + 1)).flatten
  o.flatMap(o => o.map(_ + 1))
}

object ScalaOptionSomeNull extends App {
  case class Person(id: Long, name: String)
  case class Person1(id: Long, name: Option[String])
  val p = Person(1, null)
  val x = Option(p).map(_.name).map(_.length) // Some(null)
  // identity() => x => x
  println(x)
  val x1 = Option(p).flatMap(p => Option(p.name)) // None
  println(x1)
}

object OptionForeach extends App {
  val o = Option(1)
  o.foreach(println)
  o.foreach(println(_))
  o.foreach(x => println(x))
}

object OptionMapDef extends App {
  val o                = Option(1)
  def inc(i: Int)      = i + 1
  val inc1: Int => Int = _ + 1
  println(o.map(inc1)) // Some(2)
  println(o.map(inc))  // Some(2)
}

object OptionWhen extends App {
  val o = Option.when(1 == 2)("123")
  println(o) // None
  val o1 = Option.when(1 != 2)("123")
  println(s"o1 = ${o1}") // Some(123)
}

object OptionIterableOnceMethods extends App {
  val o1 = Option(1)
  val i  = Option.option2Iterable(o1)

  val i1: Int             = o1.head             // 1
  val o2: Option[Int]     = o1.headOption       // Option(1)
  val i2: Int             = o1.last             // 1
  val o3: Option[Int]     = o1.lastOption       // Option(1)
  val l: Iterable[Int]    = o1.tail             // List()
  val o4: Iterable[Int]   = o1.take(1)          // ??? todo check in hw
  val o4_1: Iterable[Int] = o1.takeWhile(_ > 1) // ??? todo check in hw
  val o5: Option[Int]     = o1.find(_ > 1)
  val o6: Option[Int] = o1.collect {
    case i if i != 0 => 10 / i
    case i           => i * 2
  }
  val i3: Int           = o1.count(_ > 12)
  val i4: Int           = o1.sum
  val i5: Int           = o1.product
  val l1: Iterable[Int] = o1.drop(1)          // ??? todo check in hw
  val l2: Iterable[Int] = o1.dropRight(1)     // ??? todo check in hw
  val l3: Iterable[Int] = o1.dropWhile(_ > 1) // ??? todo check in hw
  val o7: Int           = o1.fold(1)(_ + 1)   // if None => 1 else Some(1+1) => 2
  val o8: String        = o1.foldLeft("1") { case (acc, e) => acc + e + 1 }
  val o9: String        = o1.foldRight("1") { case (e, acc) => acc + e + 1 }
//    val o8_1: String      = o1./:("1") { case (acc, e) => acc + e + 1 } //foldLeft
//    val o9_1: String      = o1.:\("1") { case (e, acc) => acc + e + 1 } //foldRight

  def workWithIterable(i: Iterable[Int]): Option[Int] = i.headOption
  workWithIterable(o1)
  workWithIterable(Iterable.empty[Int])
}
