package lec25_1.lecture

object ScalaClasses extends App {
  //java:
  // class Person(
  //     String name;
  //     int age;
  //     Person(String s) { this.name = s }
  //     void printInfo() {
  //         sout("...")
  //     }
  //}

  class A
  val a1 = new A()

  class Point(private val x: Int, val y: Int) {
    def distance(other: Point): Double = {
      import scala.math.sqrt

      val deltaX     = other.x - this.x
      val deltaXQuad = deltaX * deltaX

      val deltaY     = other.y - this.y
      val deltaYQuad = deltaY * deltaY

      sqrt(deltaXQuad + deltaYQuad)
    }

    private def quadX: Int = x * x
    private def quadY: Int = y * y

    override def toString: String = s"Point($x, $y)"
  }
  val p  = new Point(1, 2)
  val p1 = new Point(3, 21)
  println(p.distance(p1))
  println(p)
  println(p1)

  // p.x = 2 // ce
  p.y
}

object ScalaClassesWithVar extends App {
  class Point(var x: Int, var y: Int)
  val mP = new Point(1, 2)
  mP.x = 10
  println(mP.x)
}

object ScalaClassesWithDefaultParam extends App {
//  class Point(val x: Int = 0, val y: Int = 0) {
  class Point(val x: Int = 0)(val y: Int = 0) {
    override def toString = s"Point($x, $y)"
  }

  val zero = new Point()()
  println(zero)
  val notZero = new Point(1)()
  println(notZero)
  val notZero1 = new Point()(y = 1)
  println(notZero1)
  val notZero2 = new Point(x = 1)(y = 2)
  println(notZero2)
}

object ScalaClassesWithAuxiliaryConstructors extends App {
  class Point(val x: Int, val y: Int) {
    def this() = this(0, 0)
//    def this() = this(0, 0)
    def this(x: Int) = this(x, 1)
//    def this(y: Int) = this(1, y)
    def this(xOrY: Int, isX: Boolean) = this(if (isX) xOrY else 0, if (!isX) 0 else xOrY)
//    def this(isX: Boolean, xOrY: Int) = if (isX) this(xOrY, 0) else this(0, xOrY) // ce

    override def toString = s"Point($x, $y)"
  }
  val p1 = new Point()
  println(p1)
}

object ScalaClassesWithAuxiliaryConstructorsV2 extends App {
  class X(i: Int)
  class Y(i: Int)
  class Point(val x: X, val y: Y) {
    def this() = this(new X(0), new Y(0))
    def this(x: X) = this(x, new Y(0))
    def this(y: Y) = this(new X(0), y)
  }

  val p  = new Point(new X(1), new Y(2))
  val p1 = new Point()
  val p2 = new Point(new X(1))
  val p3 = new Point(new Y(2))
}

object ScalaClassesExtends extends App {
  class Animal(name: String) {
    def printName() = println(name)
  }

  class Dog(name: String, age: Int) extends Animal(name) {
    def printInfo = println(s"Dog($name, $age)")
  }
  class Cat(name: String, age: Int) extends Animal(name) {
    def mew = println(s"Meeeeeeew!")
  }
  def printNameByAnimal(a: Animal): Unit = a.printName()

  val a = new Animal("Slon")
  val d = new Dog("Rex", 7)
  val c = new Cat("Barsssik", 1)

  printNameByAnimal(a)
  printNameByAnimal(d)
  printNameByAnimal(c)
}

object ScalaCaseClasses extends App {
  case class Point(var x: Int, y: Int) {
    def print(): String  = "print"
    private def print1() = "print"
  }
  val p = Point(1, 2)
  // eq:
  val p1 = Point.apply(1, 2)
  p.x = 1

  p.toString
  val p2 = p.copy(x = 5)
  println(p2)
  val eq = p1 == p1
  println(eq)
  p1.hashCode()

  val x: Option[(Int, Int)] = Point.unapply(p1)
  val Point(x1, y1)         = p1

  case class A(i: Int, p: Point)
  val a                       = A(1, p1)
  val z @ A(_, Point(x2, y2)) = a
  val x2_1                    = a.p.x
  println(a)
  println(z)
  println(x2)
  println(y2)
}

object ScalaObjects extends App {
  LogAny.log(1)
  // or
  //import LogAny1.log
  //LogAny1.log(2)
}

object LogAny {
  println("Init LogAny")
  def log(a: Any): Unit = println(a)
}
object LogAny1 {
  println("Init LogAny1")
  def log(a: Any): Unit = println(a)
}

object ScalaCompanionObjects extends App {
  // class, case class, trait, abstract class
  case class Point(x: Int, y: Int)

  object Point {
    def createZero(): Point        = Point(0, 0)
    def createFromX(x: Int): Point = Point(x, 0)
    def createFromY(y: Int): Point = Point(0, y)
    def distance(from: Point, to: Point): Double = {
      import scala.math.sqrt

      val deltaX     = to.x - from.x
      val deltaXQuad = deltaX * deltaX

      val deltaY     = to.y - from.y
      val deltaYQuad = deltaY * deltaY

      sqrt(deltaXQuad + deltaYQuad)
    }
  }

  val p1 = Point(1, 2)
  println(p1)
  val p2 = Point(3, 4)
  println(p2)
  println(Point.distance(p1, p2))
  println(Point.createFromX(1))
  println(Point.createFromY(2))
  println(Point.createZero())

}

object CompObjDefClass extends App {
  class A(val i: Int, val s: String) {
    override def toString = s"A($i, $s)"
  }

  object A {
    def apply(i: Int, s: String) = new A(i, s)
  }

  val a = A(1, "2")
  println(a)
}
