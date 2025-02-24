package lec25_1.lecture

// 1. extends Enumeration
// 2. sealed trait

object ScalaEnumByEnum extends App {
  object Colors extends Enumeration {
    type Color = Value
    val Red, Green, Blue = Value

    def isRed(c: Colors.Color): Boolean = c == Colors.Red
  }
  println(Colors.Red)
  println(Colors.Red.id)
  println(Colors.Blue.id)

  println(Colors.isRed(Colors.Red))
  println(Colors.isRed(Colors.Green))

  Colors.values.foreach(println)

  val c = Colors.withName("Red")
  println(c)
//  val b = Colors.withName("Black") // No value found for 'Black'
//  println(b)
}

object ScalaEnumByEnumWithOverride extends App {
  object Colors extends Enumeration {
    type Color = Value
    val Red                             = Value(10, "Red color")
    val Green                           = Value(1)
    val Blue                            = Value("Blue color")
    def isRed(c: Colors.Color): Boolean = c == Colors.Red
  }
//  Colors.values.foreach { v =>
//    println(v.id -> v)
//  }
  val c = Colors.withName("Red color")
  println(c)

  c match {
    case x /*: Colors.Color*/ if Colors.isRed(x) =>
      println(s"Colors.Red: $x")
    case _ =>
      println("Not red, haha")
  }
}

object ScalaEnumByEnumWithParams extends App {
  object ColorWP extends Enumeration {
    protected case class RGBColor(i: Int, r: Int, g: Int, b: Int) extends super.Val(i) {
      def printRGV(): Unit = println(s"${this.getClass.getSimpleName} rgb is $r $g $b")
    }

    type Color = RGBColor
    val Red   = RGBColor(1, 255, 0, 0)
    val Green = RGBColor(2, 0, 255, 0)
    val Blue  = RGBColor(3, 0, 0, 255)

    def valueToRBG(x: Value): ColorWP.Color = x.asInstanceOf[RGBColor]
  }

//  println(ColorWP.Blue.i)
//  ColorWP.Blue.printRGV()

  // toList => обязательно, т.к values = Enumeration#ValueSet,
  // в котором может лежать только Value
  ColorWP.values.toList.map(ColorWP.valueToRBG).foreach { rgb =>
    println(rgb.id)
    rgb.printRGV()
  }
}

object ScalaEnumBySealed extends App {
  sealed trait ColorST {
    val r: Int                = 0
    val g: Int                = 0
    val b: Int                = 0
    def printRGBValue(): Unit = println(s"${this.getClass.getSimpleName} rgb is $r $g $b")
  }

  case class Red() extends ColorST {
    override val r: Int = 255
  }

  case class Green() extends ColorST {
    override val g: Int = 255
  }

  case class Blue() extends ColorST {
    override val b: Int = 255
  }

  def matchColor(t: ColorST): Unit = t match {
    case r: Red   => r.printRGBValue()
    case g: Green => g.printRGBValue()
    case b: Blue  => b.printRGBValue()
  }

  matchColor(Red())
  matchColor(Green())
  matchColor(Blue())
}

object ScalaList extends App {
  val l1: List[Int] = List(1, 2, 3, 4)
  val l2: List[Int] = List.empty[Int]
  val l3: List[Int] = Nil
  val l4: List[Int] = 1 :: 2 :: 3 :: Nil

  l1.isEmpty  // eq Nil
  l1.nonEmpty // !isEmpty
  l1.size
  l1.length

//  l1(1) // so bad
//  println(l1(1))

//  l1.foreach(println)
//  for (i <- l1) println(i) // eq foreach

//  l1.map(_ + 1).foreach(println)

  val l5: List[Int] = List(l1, l4).flatten
  println(l5)

  l1.head // NoSuchElementException
  l1.headOption
  l1.last //NoSuchElementException
  l1.lastOption

  l1.tail // List(2,3,4)
  l1.init // List(1,2,3)

  val o: Option[Int] = l1.find(_ > 2) // Some(...) or None
  l1.filter(_ < 3)
  l1.exists(_ > 2)
  l1.contains(2) // [1,2,3].contains("2") => valid, but false
  l1.forall(_ > 2)
  l1.count(_ % 2 == 0)

  println(l1.mkString(","))
  println(l1.mkString("start: ", ",", " :end"))

  l1.sum     // Numeric
  l1.product // Numeric

  l1.max // UnsupportedOperationException
  l1.maxOption
  l1.min // UnsupportedOperationException
  l1.minOption

  l1.take(1)
  l1.takeWhile(_ != 3)
  l1.drop(1)
  l1.dropWhile(_ != 3)
  l1.dropRight(1)

  val (even, odd) = l1.partition(_ % 2 == 0)
  println(even)
  println(odd)

  case class Colored(id: Long, color: String)
  val c1 = Colored(1L, "Red")
  val c2 = Colored(2L, "Green")
  val c3 = Colored(3L, "Blue")
  val c4 = Colored(4L, "Red")

  val lColored = List(c1, c2, c3, c4)

  val m: Map[String, List[Colored]] = lColored.groupBy(_.color)
//  println(m)

  lColored.maxBy(_.id) // UnsupportedOperationException
  lColored.maxByOption(_.id)
  lColored.minBy(_.id)
  lColored.minByOption(_.id) // UnsupportedOperationException

//  l1.grouped(2).foreach(println)

  val x1 = l1.fold(0) { case (acc, e) => acc + e }
  val x2 = l1.foldLeft("0") { case (acc, e) => acc + e }
//  println(x2)

  println("---------------")
  val f1                   = (i: Int) => i + 2
  val f2                   = (i: Int) => i * 2
  val lf: List[Int => Int] = List(f1, f2)
  val x3                   = lf.foldLeft(0) { case (acc, f) => f(acc) }
  println(x3) // 4
  val x4 = lf.foldRight(0) { case (f, acc) => f(acc) }
  println(x4) // 2

  l1.toSet
  l1.toArray
  l1.toVector

  val x = List(Option(1), Option(2), None).collect {
    case Some(a) if a != 0 => a
  }
  l1.collectFirst {
    case a if a == 1 => a
  }
}

object ScalaListSpec extends App {
  case class Colored1(id: Long, color: String)
  val l1: List[Int]      = List(1, 2, 3, 4)
  val l2: List[Colored1] = List.empty[Colored1]

  l1 match {
    case List(a, b, c, d) =>
      println(a, b, c, d)
    case Nil =>
      println("empty")
    case l if l.isEmpty =>
      println("empty")
    case head :: Nil => // List(1)
      println(head)
    case head :: tail => // List(1,2,3)
      println(head)
      println(tail)
    case full @ head :: scnd :: tail =>
  }

  l2 match {
    case full @ Colored1(id, color) :: tail =>
    case Nil                                => println("empty")
  }

  val s: Iterable[Int] = Vector(1, 2, 3, 4) //bad!
  s match {
    case a :: b :: c :: d :: Nil => println(a) // babah
  }

  val l3 = List(1, 2, 3, 4)
  val l4 = List(4)

  val intersect = l3.intersect(l4)
  val diff1     = l3.diff(l4)
  val diff2     = l4.diff(l3)
  val concat    = l3.concat(l4)
  val concat2   = l3.++(l4)

}
