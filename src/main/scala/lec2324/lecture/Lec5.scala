package lec2324.lecture

import lec2324.lecture.ScalaEnumByEnum.Colors.Color
import lec2324.lecture.ScalaEnumByEnumWithOverride.ColorsV2.ColorV2
import lec2324.lecture.ScalaEnumByEnumWithParams.ColorsV3
import lec2324.lecture.ScalaEnumByEnumWithParams.ColorsV3.ColorV3
import lec2324.magicZone.ImplicitForPrint

import java.util
import scala.collection.mutable

object ScalaEnumByEnum extends App with ImplicitForPrint {

  object Colors extends Enumeration {
    type Color = Value

    val Red, Green, Blue = Value
  }

  object ColorOps {
    def isRed(c: Color): Boolean = c == Colors.Red
  }

  val r = Colors.Red
  ColorOps.isRed(r).print
  val g = Colors.Green
  ColorOps.isRed(g).print

  Colors.values.foreach(_.print)

  val r1 = Colors.withName("Red")
  ColorOps.isRed(r1).print

  //val b = Colors.withName("Black") //No value found for 'Black'

  r.id.print
  g.id.print

}

object ScalaEnumByEnumWithOverride extends App with ImplicitForPrint {
  object ColorsV2 extends Enumeration {
    type ColorV2 = Value

    val Red   = Value(1, "Red color")
    val Green = Value("Green color")
    val Blue  = Value(3)
  }

  object ColorOps {
    def isRed(c: ColorV2): Boolean = c == ColorsV2.Red
  }

  val r = ColorsV2.Red
  ColorOps.isRed(r).print
  val g = ColorsV2.Green
  ColorOps.isRed(g).print

  ColorsV2.values.foreach(_.print) // Red, Green, Blue

  val r1 = ColorsV2.withName("Red color")
  ColorOps.isRed(r1).print

  //val b = Colors.withName("Black") //No value found for 'Black'

  r.id.print
  g.id.print
  ColorsV2.Blue.id.print
}

object ScalaEnumByEnumWithParams extends App with ImplicitForPrint {

  object ColorsV3 extends Enumeration {
    protected case class RGBColor(i: Int, r: Int, g: Int, b: Int) extends super.Val(i) {
      def printRgbValue(): Unit = s"${this.getClass.getSimpleName} rgb is $r $g $b".print
    }

    type ColorV3 = RGBColor

    def valueToRgb(v: Value) = v.asInstanceOf[RGBColor]

    val Red   = RGBColor(1, 255, 0, 0)
    val Green = RGBColor(2, 0, 255, 0)
    val Blue  = RGBColor(3, 0, 0, 255)
  }

  val r = ColorsV3.Red
  r.printRgbValue()

  ColorsV3.values.toList
    .map(ColorsV3.valueToRgb)
    .foreach(_.printRgbValue())

  def matchColor(c: ColorV3): Boolean = {
    c match {
      case ColorsV3.Red => true
    }
  }

  matchColor(r).print

//  val green = ColorsV3.Green
//  mathColor(green).print // scala.MatchError

}


object ScalaEnumBySealed extends App with ImplicitForPrint {

  sealed trait ColorV4 {
    val r: Int
    val g: Int
    val b: Int
  }

  case class Red(r: Int, g: Int, b: Int)   extends ColorV4
  case class Green(r: Int, g: Int, b: Int) extends ColorV4
  case class Blue(r: Int, g: Int, b: Int)  extends ColorV4

  def matchColor(t: ColorV4): Boolean = {
    t match {
      case Red(r, g, b) => true
      // compile check
    }
  }

  matchColor(Red(2, 2, 3))
}

object ScalaCollections extends App with ImplicitForPrint {

  val l  = List(1, 2, 3)
  val l1 = 1 :: 2 :: 3 :: Nil
  val l2 = List.empty[Int]

  val f1         = (i: Int) => i + 2
  def f2(i: Int) = i * 2

  val lf = List(f1, f2(_))

  l1.isEmpty.print
  l1.nonEmpty.print
  l1.size.print

  "------------------".print

  l1.foreach(_.print)

  "------------------".print

  val res = lf.map(f => f(0))
  res.print

  "------------------".print

  val lfV2: List[List[Int => Int]] = List(List(f1), List(f2(_)))
  val lfV3: List[Int => Int]       = lfV2.flatten

  val lfV4: List[Int] = lf.flatMap(f => l1.map(x => f(x))) // eq lf.flatMap(l1.map)
  // lf.flatMap(f => l1.map(x => f(x)))
  // where l1 = List( 1, 2, 3 )
  //       lf = List( f1, f2 )
  //       f1 = i + 2
  //       f2 = i * 2
  //
  // (f1, f2) flatMap ( (1, 2, 3) map ( x => f(x) ) )
  // List(f1(1), f1(2), f1(3)) => List(3, 4, 5)
  // List(f2(1), f2(2), f2(3)) => List(2, 4, 6)
  // List( List(3, 4, 5),  List(2, 4, 6) ) => List(3, 4, 5, 2, 4, 6)
  lfV4.print

  // [[]] flatten []
  // Option(any) => any method return Option(any2)
  // val o = Option(Id(id = 1, name = Option(2))
  // o.flatMap(_.name) => Option(name) // with map => Option(Option(name))

  "------------------".print

  l1.head.print // if empty NonSuchElementException
  l1.headOption.print
  l1.last.print
  l1.lastOption.print
  l1.tail.print // if empty UnsupportedOperationException

  l1.find(x => x == 1).print // l1.find(_ == 1)

  "------------------".print

  l1.filter(x => x != 2).print

  "------------------".print

  l1.exists(x => x == 1 || x == 2).print
  l1.contains(1).print
  "------------------".print
  l1.forall(x => x > 0).print
  l1.count(x => x > 0).print
  "------------------".print
  l1.mkString(", ").print

  l1.sum.print
  l1.product.print
  "------------------".print

  l1.max.print // if empty UnsupportedOperationException("empty.max")
  l1.maxOption.print
  l1.min.print // if empty UnsupportedOperationException("empty.min")
  l1.minOption.print

  "------------------".print

  l1.take(2).print
  l1.takeWhile(_ != 2).print
  l1.drop(2).print
  l1.dropWhile(_ != 2).print

  "------------------".print

  // (true, false)
  val (even, odd) = l1.partition(x => x % 2 == 0)
  even.print
  odd.print

  "------------------".print

  case class Colored(id: Long, color: ColorV3)

  val c1 = Colored(1, ColorsV3.Red)
  val c2 = Colored(2, ColorsV3.Green)
  val c3 = Colored(3, ColorsV3.Red)
  val c4 = Colored(4, ColorsV3.Blue)

  val lColored = List(c1, c2, c3, c4)

  val grouppedBy: Map[ColorV3, List[Colored]] = lColored.groupBy(_.color)
  grouppedBy.print

  lColored.maxBy(_.id).print // if empty UnsupportedOperationException("empty.maxBy")
  lColored.maxByOption(_.id).print
  lColored.minBy(_.id).print // if empty UnsupportedOperationException("empty.minBy")
  lColored.minByOption(_.id).print

  "------------------".print
  val res1 = l1.product
  //  val res2 = lf.fold(0)((acc, x) => acc * x)
  res1.print

  val r1 = lf.foldLeft(0) { case (acc, f) =>
    f(acc)
  }
  r1.print
  val r2 = lf.foldRight(0) { case (f, acc) =>
    f(acc)
  }
  r2.print

  val l1Set: Set[Int]  = l1.toSet
  val l1Ar: Array[Int] = l1.toArray

  "------------------".print

  var simpleListV1 = List(1, 2, 3)
  val simpleListV2 = List(3, 4, 5)

  val intersect = simpleListV1.intersect(simpleListV2)
  intersect.print
  val diff = simpleListV1.diff(simpleListV2)
  diff.print
  val diff2 = simpleListV2.diff(simpleListV1)
  diff2.print
  val concatL = simpleListV1 ++ simpleListV2
  concatL.print

}

object ScalaMutableImmutableCollections extends App with ImplicitForPrint {
  val immutableList = scala.collection.immutable.List(1, 2, 3, 4)

  val mutableList = scala.collection.mutable.ListBuffer(1, 2, 3)
  mutableList.print
  "------------------".print
  mutableList.addOne(1).print
  mutableList.prepend(0).print
  "------------------".print
  mutableList.print

  //mutableList = mutableList //Reassignment to val

  var mutableListV2 = scala.collection.mutable.ListBuffer(1, 2, 3)
  mutableListV2 = mutableList
}

object ScalaJavaCollections extends App with ImplicitForPrint {
  import scala.jdk.CollectionConverters._
  val l = List(1)

  val java1: util.List[Int] = l.asJava
  val java2: util.Collection[Int] = l.asJavaCollection

  // val java1V2 = java1.stream().map(x => x + 1).toList

  val scalaJavaCollection: mutable.Seq[Int] = java1.asScala
  val scalaJavaCollectionV2: Iterable[Int] = java2.asScala
}