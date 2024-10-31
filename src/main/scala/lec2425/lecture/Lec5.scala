package lec2425.lecture

object HWRetro1 extends App {
  //good
  def checkout(x: Int): String =
    if (x > 0) "+"
    else if (x == 0) "0"
    else "-"
  checkout(1)
  //good
  def checkout1(x: Int): String =
    if (x % 2 == 0) "Чётное" else "Нечётное"
  checkout1(1)
}

object HWRetro2 extends App {
  import scala.annotation.tailrec

  // bad
  def fibo_tail(n: Int): Int = {
    @tailrec
    def fibo_tail(n: Int, p: Int, c: Int): Int = {
      if (n == 0) {
        //code
        //code
        return -1;
        //code
        //code
      } // undefined
      if (n == 1) return p;
      fibo_tail(n - 1, c, p + c)
    }
    fibo_tail(n, 0, 1)
  }

  //good
  def fibo_tail1(n: Int): Int = {
    @tailrec
    def fibo_tail(n: Int, p: Int, c: Int): Int = {
      if (n == 0) -1
      else if (n == 1) p
      else fibo_tail(n - 1, c, p + c)
    }
    fibo_tail(n, 0, 1)
  }

  println(fibo_tail(5))
  println(fibo_tail1(5))
}

object ScalaOption extends App {
  // java: Optional<E>

  // Option(any) => Some(any) || None

  /**
   * Option(1)
   */
  val o1: Option[Int] = Option(1)

  /**
   * None
   */
  val o2: Option[Int]     = None
  val o3: Option[Int]     = Some(1)
  val o4: Option[Nothing] = Option.empty
  val o4_1: Option[Int]   = Option.empty //[Int]
  val o4_2: Option[Int]   = Option.empty[Int]

  // Some(1) map (_ + 1) map (_ + 1) map (_ + 1) map (_ + 1) => Some(5)
  // None /* map (_ + 1) map (_ + 1) map (_ + 1) map (_ + 1) */ => None

  val b1 = o1.isEmpty   // true if None
  val b2 = o1.isDefined // eq !isEmpty
  val b3 = o1.nonEmpty  // eq isDefined

  // eq o1.filter(_ > 0)
  val f1: Option[Int] = o1.filter(i => i > 0) // Some(a) => if f in filter return true

  val f2_1: Option[Int] = o1.filter { i =>
//    println("bbbbbbbbbbbbbbb!")
    !(i > 0)
  } // Some(a) => if f in filterNot return false
  val f2_2: Option[Int] = o2.filterNot { i =>
//    println("aaaaaaaaaaaaaaa!")
    i > 0
  } // Some(a) => if f in filterNot return false
//  println(f2_1)
//  println(f2_2)
  val c1: Boolean = o1.contains(1) // Some(a) => true if a == 1
  // eq o1.exists(_ > 1)
  // !isEmpty && p(this.get)
  val e: Boolean = o1.exists(i => i > 1) // true if !isEmpty and f in exists return true
  // isEmpty || p(this.get)
  val forall: Boolean = o1.forall(i => i > 1) // true if isEmpty or f in forall return true

  val o1Value = o1.get
  // val o2Value = o2.get // java.util.NoSuchElementException: None.get
  val o1Value_1 = o1.getOrElse {
    println(1111)
    0
  }
  lazy val o2Value_1 = o2.getOrElse {
    println(2222)
    -1
  }
  println(o1Value_1)
  println(o2Value_1)
}

object ScalaOptionProd extends App {
  def loadById(id: Long): String = {
    null
  }
  def loadByAnotherId(anotherId: Long): String = {
    "aaa"
  }

  def load(id: Long)(anotherId: Long): Option[String] =
    Option(loadById(id)).orElse(Option(loadByAnotherId(anotherId)))

  val x = load(1)(2)
  println(x)
}

object ScalaOptionSomeSome extends App {
  def loadById(id: Long): Option[String] = {
    Option("111")
  }

  val x: Option[Option[String]] = Option(loadById(1L))
  // [[]] flatten => []
  // [[[]]] flatten flatten => []
  val xFlatten = x.flatten
  println(x)
  println(xFlatten)
  println("-----------")
  val optionNone: Option[Option[String]] = Option(Option.empty[String])
  println(optionNone)
  println(optionNone.flatten)
}

object ScalaOptionMap extends App {
  val o1 = Option(1)
  val o2 = Option.empty[Int]

  val x1 = o1.map(_ + 1).map(_ + 1).map(_ + 1).map(_ + 1).map { i =>
    println(s"i = $i")
    i + 1
  }
  val x2 = o2.map(_ + 1).map(_ + 1).map(_ + 1).map(_ + 1).map { i =>
    println(s"j = $i") // not print cause None
    i + 1
  }
  println(x1)
  println(x2) // None
}

object ScalaOptionMapSomeSome extends App {
  val o1 = Option(1)
  val x1 = o1.map { i =>
    println(s"i = $i")
    Option(i + 1)
  }.flatten
  val x2 = o1.flatMap { i =>
    println(s"i = $i")
    Option(i + 1)
  }

  // [] map ([[]]) .flatten => []
  // [] flatMap ([[]]) => []
  val a: Option[Int] => Int = _.getOrElse(0)
  println(x1)
  println(a(x1))

  // doesn`t work, fix me pls
//  val a1: Int => Option[Int]  = _ => Option(1)
//  val x_hack = Option(1).flatten(a1)
//  println(x_hack)
}

object ScalaOptionSomeNull extends App {
  case class Person(id: Long, name: Option[String])
  val p = Person(1L, null)

  val x = Option(p).map(_.name)
  println(x) // Some(null)

  // for avoid Some(null)
  val x1 = Option(p).flatMap(p => Option(p.name))
  println(x1) // None
  println(Some(null)) // Some(null)
  println(Option(null)) // None
}

object OptionForeach extends App {
  val o1 = Option(1)
  val x: Unit = o1.foreach { i =>
    println(i)
    println(i)
    println(i)
  }
}

object OptionMatch extends App {
  val o1 = Option(1)
  o1 match {
    case Some(value) =>
      println(value)
    case None =>
      println("none!")
  }
}

object OptionWhen extends App {
  // use where: if (con) Some(1) else None
  val o1 = Option.when(1 > 0)(1)
  val o2 = Option.when(1 < 0)(1)
  println(o1)
  println(o2)
}

object OptionIterableOnceMethods extends App {
  val o1 = Option(1)
  // todo in lec: list|seq|set|map
  Option.option2Iterable(o1).head
  o1.headOption
  o1.last
  o1.lastOption
  o1.tail // List()
  o1.take(1)
  o1.find(_ > 1)
  o1.collect(_ > 1)
  o1.count(_ > 1)
  o1.sum
  o1.drop(1)
  o1.dropRight(1)
  o1.dropWhile(_ >1)
  o1.fold(1)(_ + 1)
  // o1./:()()
  o1.foldLeft("1")((x,y) => s"x $y ")
  // o1.:\()()
  o1.foldRight("1")((x,y) => s"x $y ")


  def workWithIterable[A](l: A with Iterable[Int]): Int = l.head
  def workWithIterable1(l: Iterable[Int]): Int = l.head

  workWithIterable(o1)
  workWithIterable1(o1)
}