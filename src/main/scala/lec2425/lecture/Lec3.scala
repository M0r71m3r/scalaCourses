package lec2425.lecture

import scala.beans.BeanProperty

object ScalaClasses extends App {
  //java:
  //class Person {
  //  String name;
  //  int age;
  //
  //  void displayInfo() {
  //    sout("chtoto")
  //  }
  //}

  //scala example:
//  class A
//  val a1 = new A

  class Point(private val x: Int, val y: Int) { // or just x:Int
    println(s"$x, $y")

    def distance(other: Point): Double = {
      import scala.math.sqrt

      val deltaX     = other.x - this.x
      val deltaXQuad = deltaX * deltaX

      val deltaY     = other.y - this.y
      val deltaYQuad = deltaY * deltaY

      sqrt(deltaXQuad + deltaYQuad)
    }

    override def toString: String = s"($x, $y)"
  }

  val p1 = new Point(1, 1)
//  println(p1.toString)
  val p2 = new Point(2, 4)
//  println(p2.x)
  println(p2.y)
//  println(p1.distance(p2))
}

object ScalaClassesWithVar extends App {
  class Point(var x: Int, var y: Int)
  val p1 = new Point(1, 2)
  println(p1.x)
  p1.x = 3
  println(p1.x)
}

object ScalaClassesWithDefaultParam extends App {
  class Point(var x: Int = 0, var y: Int = 0) {
    var color: String             = _
    def scaleTemp(): Unit         = color = "Red"
    override def toString: String = s"$x, $y"
  }
//  val p1 = new Point()
//  println(p1)
//  val p2 = new Point(x = 3)
//  println(p2)
//  val p3 = new Point(y = 4)
//  println(p3)
//  val p4 = new Point(3, 4)
//  println(p4)
  val p5 = new Point(3, 4)
  println(p5.color)
  p5.scaleTemp()
  println(p5.color)
}

object ScalaClassesWithAuxiliaryConstructors extends App {
  // 1. В реализации this обязательно сначала вызывается любой другой из реализованных конструкторов,
  //    будь то главный или любой другой из вспомогательных
  // 2. Все вспомогательные конструкторы должны иметь разную сигнатуру
  class PointWithAuxiliaryConstructor(val x: Int, val y: Int) {
    def this() = this(0, 0)
//    def this() = this(0, 0) // scnd throw ce, cause 2 eq sign
    def this(xOrY: Int, isX: Boolean) = this(if (isX) xOrY else 0, if (isX) 0 else xOrY)
  }
  val p0 = new PointWithAuxiliaryConstructor
}

object ScalaClassesWithAuxiliaryConstructorsV2 extends App {
  class X(x: Int)
  class Y(y: Int)
  class PointWithAuxiliaryConstructor(val x: X, val y: Y) {
    def this(x: X) = this(x, new Y(0))
    def this(y: Y) = this(new X(0), y)
  }

  // server call with params to return distance
//  def toPoint(l: Double, p: Double) = ???
//  toPoint(55, 37) // Москва
//  toPoint(37, 55) // Иран
}

object ScalaClassesExtends extends App {
  class Animal(
      @BeanProperty var name: String,
      @BeanProperty val color: String
  ) {
    def printName(): Unit = {
      println(name)
    }
    def walk(): Unit =
      println("Animal walk")
  }
  val a1 = new Animal("1", "10")
  a1.setName("2")
  a1.getName
  a1.getColor // set not exists, cause val

  class Dog(name: String, age: Int) extends Animal(name, "black") {
    def printInfo(): Unit = {
      println(s"$name, $age")
    }
    override def walk(): Unit = {
      println("Dog walk: wuf!")
    }
  }
  class Cat(name: String, age: Int) extends Animal(name, "white") {
    def printInfo(): Unit = {
      println(s"$name, $age")
    }
    override def walk(): Unit = {
      println("Cat walk: mew!")
    }
  }
  val d1 = new Dog("Rex", 3)
//  d1.printName
//  d1.printInfo

  def printWalk(a: Animal): Unit = a.walk()

  val c1 = new Cat("Urza", 3)
  printWalk(d1)
  printWalk(c1)
}

object ScalaCaseClasses extends App {
  case class Point(x: Int, var y: Int)
  //  object Point {
  //    def apply(x: Int, y: Int): Point = new Point(1, 2)
  //  }
  class Point1(x: Int, var y: Int)

//   extends via Point ce, case-to-case сделать незя
  case class p(i: Int, i1: Int) extends Point1(i, i1)

  val p1 = Point(1, 2)
  //p1.x = 3 => ce cause Reassignment to val
//  p1.y = 3 // cause var y
//  println(p1)
//  val p2 = p1.copy(x = 2)
//  println(p2)

  val x  = Point.unapply(null)
  val x1 = Point.unapply(p1)
  println(x)
  println(x1)
}

object ScalaObjects extends App {
  System.out.println("1")
  LogAny.log(1)
//  LogAny.x
//  LogAny.y // is inaccessible from this place
  LogAny1.log(2)
}

object LogAny {
  println("->>>>>>>>>>>>>>>>>123")
  val x                          = 1
  private val y                  = 1
  def log(a: Any): Unit          = println(s"INFO $a")
  private def log1(a: Any): Unit = println(s"INFO $a")
}
object LogAny1 {
  println("->>>>>>>>>>>>>>>>>LogAny1")
  def log(a: Any): Unit = println(s"INFO $a")
}

object ScalaCompanionObjects extends App {
  // class, case class, trait, abstract class
  case class Point(x: Int, y: Int) {
    def toWest(): Unit = println("111")
  }
  // static methods in java class
  object Point {
    def createFromX(x: Int): Point = Point(x, 0)
    def createFromY(y: Int): Point = Point(0, y)
    def zeroPoint: Point           = Point(0, 0)

    def distance(p1: Point, p2: Point): Double = {
      import scala.math.sqrt
      val deltaX     = p1.x - p2.x
      val deltaXQuad = deltaX * deltaX

      val deltaY     = p1.y - p2.y
      val deltaYQuad = deltaY * deltaY

      sqrt(deltaXQuad + deltaYQuad)
    }
  }
  val p1 = Point(1, 2)
  val p2 = Point(2, 4)
  Point.distance(p1, p2)
  val p3    = Point.createFromX(1)
  val p4    = Point.createFromY(2)
  val pZero = Point.zeroPoint
  //  p2.copy()
  //  p2.toString()
  //  p2 == p1
  //  p1.hashCode()
}


object MoreCompanionObjects extends App {
  class Point()
  case class Point1() extends Point
  case class Point2() extends Point

  object Point {
    def toP(p:Point): Unit = {
      println(p)
    }
  }
  Point.toP(Point1())
  Point.toP(Point2())
}
