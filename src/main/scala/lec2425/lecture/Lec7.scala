package lec2425.lecture

import lec2425.lecture.ScalaEnumByEnum.Colors.Color

import java.{lang, util}
import scala.jdk.CollectionConverters.{IterableHasAsJava, IterableHasAsScala}

object ScalaEnumByEnum extends App {
  object Colors extends Enumeration {
    type Color = Value
    val RED, GREEN, BLUE = Value
  }

  def isRed(c: Color) = c match {
    case lec2425.lecture.ScalaEnumByEnum.Colors.RED =>
      true
    case _ =>
      false
//    case lec2425.lecture.ScalaEnumByEnum.Colors.BLUE => ???
  }

  //println(isRed(Colors.RED)) //true
  //println(isRed(Colors.GREEN)) //false

//  Colors.values.foreach(println) // see values method

  val g: Color = Colors.withName("GREEN")
  println(g)
  // java.util.NoSuchElementException: No value found for 'BLACK'
  //val b = Colors.withName("BLACK")

  println(g.id)
}

object ScalaEnumByEnumWithOverride extends App {
  object ColorsV2 extends Enumeration {
    type Color = Value
    val RED   = Value(1, "Red color")
    val GREEN = Value(3)
    val BLUE  = Value("Blue color")
  }
  val red   = ColorsV2.withName("Red color")
  val green = ColorsV2.withName("GREEN")
  val blue  = ColorsV2.withName("Blue color")
  List(red, green, blue).foreach { c =>
    println(s"$c, ${c.id}")
  //Red color, 1
  //GREEN, 3
  //Blue color, 4
  }
}

object ScalaEnumByEnumWithParams extends App {
  object ColorsV3 extends Enumeration {
    protected case class RGBColor(i: Int, r: Int = 0, g: Int = 0, b: Int = 0) extends super.Val(i) {
      def printRGBValue(): Unit = println(s"${this.getClass.getSimpleName} $i rbg is $r;$g;$b")
    }

    type Color = RGBColor
    val RED    = RGBColor(1, r = 255)
    val BLACK  = RGBColor(2)
    val GREEN  = RGBColor(3, g = 255)
    val BLUE   = RGBColor(4, b = 255)
    val ORANGE = RED.copy(i = 5, g = 122, b = 122)

    def valueToRGB: Value => RGBColor = _.asInstanceOf[RGBColor]
  }
  // copy without i => java.lang.AssertionError: assertion failed: Duplicate id: 1
//  val orange: ColorsV3.Color = ColorsV3.RED.copy(g = 122, b = 122)
//  orange.printRGBValue()

//  val o1 = ColorsV3.ORANGE
//  o1.printRGBValue()

  // must have `toList` in methods seq
  ColorsV3.values.toList.map(ColorsV3.valueToRGB).foreach { c =>
    c.printRGBValue()
  }

}

object ScalaEnumBySealed extends App {
  sealed trait ColorV4 {
    val r: Int
    val g: Int
    val b: Int
    def printRGBValue(): Unit = println(s"${this.getClass.getSimpleName} rbg is $r;$g;$b")
  }
  case class Red() extends ColorV4 {
    override val r: Int = 255
    override val g: Int = 0
    override val b: Int = 0
  }
  case class Green() extends ColorV4 {
    override val r: Int = 0
    override val g: Int = 255
    override val b: Int = 0
  }

  def isRed(c: ColorV4): Boolean = c match {
    case Red() => ???
    // case Green() => ???
    // compile check
  }
}

object ScalaList extends App {

  /**
   * List(1, 2, 3)
   */
  val l1: List[Int] = List(1, 2, 3)
  val l2: List[Int] = 1 :: 2 :: 3 :: Nil
  val l3            = List.empty[Int]
  val l4            = Nil

  val f1              = (i: Int) => i + 2
  def f2(i: Int): Int = i * 2

  /**
   * (i: Int) => i + 2, i * 2
   */
  val lf: List[Int => Int] = List(f1, f2)

  l1.isEmpty
  l1.nonEmpty
  l1.size

  l1.foreach(println)

  // javaList.stream().map(x -> x * x).toList
  val r = lf.map(f => f(0))
  println(r)

  val l5: List[List[Int]] = List(List(1), List(2), List(3))
  val l6: List[Int]       = l5.flatten

  //List(List(3, 4, 5), List(2, 4, 6))
  val l7 = lf.map(l1.map)
  // [f1, f2] map ( f => (1,2,3) map (i => f(i)) )
  println(l7)

  l1.head // NoSuchElementException
  l1.headOption
  l1.last // NoSuchElementException
  l1.lastOption

  l1.tail // UnsupportedOperationException
  l1.init // UnsupportedOperationException

  l1.drop(1)
  l1.dropRight(1)
  l1.dropWhile(_ < 2)

  val f1_1: Option[Int] = l1.find(_ == 2)

  val l1_1: List[Int] = l1.filter(_ > 2)

  l1.exists(_ > 1)
  l1.contains(1)
  l1.forall(_ % 2 == 0)
  l1.count(_  % 2 == 0)

  val str: String = l1.mkString("; ")

  l1.sum
  l1.product

  l1.max // UnsupportedOperationException
  l1.maxOption
  l1.min // UnsupportedOperationException
  l1.minOption

  l1.take(2)
  l1.takeRight(3)
  l1.takeWhile(_ != 3)

  val (even, odd) = l1.partition(_ % 2 == 0)

  case class Colored(id: Long, color: Color)

  val c1 = Colored(1L, ScalaEnumByEnum.Colors.RED)
  val c2 = Colored(2L, ScalaEnumByEnum.Colors.GREEN)
  val c3 = Colored(3L, ScalaEnumByEnum.Colors.RED)
  val c4 = Colored(4L, ScalaEnumByEnum.Colors.BLUE)

  val lColored                         = List(c1, c2, c3, c4)
  val group: Map[Color, List[Colored]] = lColored.groupBy(_.color)
  println(group)

  lColored.maxBy(_.id) // UnsupportedOperationException
  lColored.maxByOption(_.id)
  lColored.minBy(_.id) // UnsupportedOperationException
  lColored.minByOption(_.id)
  println("-------------------------------")
  val r1 = lf.foldLeft(0) { case (acc, f) =>
    f(acc)
  }
  println(r1)
  val r2 = lf.foldRight(0) { case (f, acc) =>
    f(acc)
  }
  println(r2)

  val r3 = l1.fold(1)((acc, e) => acc * e) // eq product
  println(r3)
  val r4 = l1.foldLeft("1") { case (acc, e) =>
    acc * e
  }
  println(r4)

  val l1Set  = l1.toSet
  val l1Arry = l1.toArray

  val jColletion: lang.Iterable[Int]    = l1.asJava
  val jColletion1: util.Collection[Int] = l1.asJavaCollection
  val jTScala: Iterable[Int]            = jColletion.asScala

  l1 match {
    case Nil                      => println("empty")
    case head :: Nil              => println("uno element")
    case head :: tail if head > 1 => println("has tail and head > 1")
    case _ :: tail                => println("has tail")
    case _                        => println("not list") // runtime check on Iterable
  }
}

object ScalaListSpec extends App {
  var simpleListV1 = List(1, 2, 3)
  val simpleListV2 = List(3, 4, 5)

  val intersect = simpleListV1.intersect(simpleListV2)
  println(intersect) // List(3)
  val diff2 = simpleListV1.diff(simpleListV2)
  println(diff2) // List(1, 2)
  val concat = simpleListV1.concat(simpleListV2)
  println(concat) // List(1, 2, 3, 3, 4, 5)

  val x: List[String] = simpleListV1.collect { case 1 =>
    "1"
//    case _ => "2"
  }
  println(x)
  // PartialFunction[Int, String] => не все инты в этой функции покрыты
  val x1: Option[String] = simpleListV1.collectFirst { case 1 =>
    "1"
  //    case _ => "2"
  }
  println(x1)

  val l: List[Option[Int]] = ???
  val x_1: List[Int]       = l.collect { case Some(i) => i }
  val x_2: List[Int]       = l.flatMap(_.toList)

}
