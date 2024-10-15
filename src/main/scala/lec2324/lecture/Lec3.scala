package lec2324.lecture

import lec2324.magicZone.ImplicitForPrint

object ScalaClasses extends App with ImplicitForPrint {
  //java:
  //class Person {
  //    String name;
  //    int age;
  //    void displayInfo(){
  //        System.out.printf("Name: %s \tAge: %d\n", name, age);
  //    }
  //}

  class A
  val a1 = new A

  class Point(val x: Int, val y: Int) { //or x:int eq private val x: Int
    def distance(other: Point): Double = {
      import scala.math.sqrt

      val deltaX     = other.x - this.x
      val deltaXQuad = deltaX * deltaX

      val deltaY     = other.y - this.y
      val deltaYQuad = deltaY * deltaY

      sqrt(deltaXQuad + deltaYQuad)
    }
    override def toString: String = s"($x, $y)"

    private val placeholder     = "Create point with"
    private def printInfo: Unit = s"$placeholder x = $x y = $y".print
    printInfo
  }

  val p1 = new Point(2, 2)
  val p2 = new Point(4, 2)

  s"distance between $p1 and  $p2 eq ${p1.distance(p2)}".print

  //p1.x = 3 // Reassignment to val

  //p1.placeholder //is inaccessible from this place
  //p1.printInfo //is inaccessible from this place

}
object ScalaClassesWithVar extends App with ImplicitForPrint {
  class PointWithChangeableXY(var x: Int, var y: Int)

  val pointWithChangeableXY = new PointWithChangeableXY(1, 2)
  s"1. x = ${pointWithChangeableXY.x} y = ${pointWithChangeableXY.y}".print
  pointWithChangeableXY.x = 3 // ok
  s"2. x = ${pointWithChangeableXY.x} y = ${pointWithChangeableXY.y}".print
  val pointWithChangeableXY1 = new PointWithChangeableXY(y = 1, x = 2) //!!!
  s"3. x = ${pointWithChangeableXY1.x} y = ${pointWithChangeableXY1.y}".print
}

object ScalaClassesWithDefaultParams extends App with ImplicitForPrint {
  class PointWithDefaultXY(val x: Int = 0, val y: Int = 0)
  val pointWithDefaultXY1 = new PointWithDefaultXY()
  s"1. x = ${pointWithDefaultXY1.x} y = ${pointWithDefaultXY1.y}".print
  val pointWithDefaultX = new PointWithDefaultXY(1)
  s"2. x = ${pointWithDefaultX.x} y = ${pointWithDefaultX.y}".print
  val pointWithDefaultY = new PointWithDefaultXY(y = 2) //!!!
  s"3. x = ${pointWithDefaultY.x} y = ${pointWithDefaultY.y}".print
}
object ScalaClassesWithAuxiliaryConstructors extends App with ImplicitForPrint {

  class PointWithAuxiliaryConstructors(val x: Int = 0, val y: Int = 0) {
    def this() = this(0, 0)
    def this(x: Int) = {
      this(x, 5) // create new PointWithAuxiliaryConstructors(x,5)
      println(x)
    }

//    def this(y:Int) = this(0, y)
//    def this(x:Int) = this(x, 0)

//    def this(xOrY: Int, isX: Boolean) = if (isX) this(xOrY, 0) else this(0, xOrY)
//    def this(x:Int, y:Int = 0) = this(x, 0)
    def this(xOrY: Int, isX: Boolean) =
      this(
        if (isX) xOrY else 0,
        if (isX) 0 else xOrY
      )

  }

  val pointWithAuxiliaryConstructor1 = new PointWithAuxiliaryConstructors(4, 2)
  val pointWithAuxiliaryConstructor2 = new PointWithAuxiliaryConstructors()
  val pointWithAuxiliaryConstructor3 = new PointWithAuxiliaryConstructors(2, isX = true)
  val pointWithAuxiliaryConstructor4 = new PointWithAuxiliaryConstructors(2, isX = false)
  val pointWithAuxiliaryConstructor5 = new PointWithAuxiliaryConstructors(2)

}

object ScalaClassesWithAuxiliaryConstructorsV2 {
  class X(x: Int) {
    def validate(x: Int): Boolean = x > 0
    validate(x)
  }
  class Y(y: Int)
  class PointWithAuxiliaryConstructorsV2(val x: X, val y: Y) {
    def this() = this(new X(0), new Y(0))

    def this(x: X) = this(x, new Y(0))

    def this(y: Y) = this(new X(0), y)
  }

  val pointWithAuxiliaryConstructorsV2_1 = new PointWithAuxiliaryConstructorsV2()
  val pointWithAuxiliaryConstructorsV2_2 = new PointWithAuxiliaryConstructorsV2(new X(2))
  val pointWithAuxiliaryConstructorsV2_3 = new PointWithAuxiliaryConstructorsV2(new Y(2))
  val pointWithAuxiliaryConstructorsV2_4 = new PointWithAuxiliaryConstructorsV2(new X(4), new Y(2))
}

object ScalaClassesExtends extends App with ImplicitForPrint {

  class Animal(name: String) {
    def printName: Unit = name.print
  }

  class Dog(name: String, age: Int) extends Animal(name) {
    def printInfo: Unit = s"name = $name, age = $age".print
  }
  val d1 = new Dog("Rex", 15)
  d1.printName
  d1.printInfo

}

object ScalaCaseClasses extends App with ImplicitForPrint {
  case class Point(x: Int, y: Int) {
    def alala(): Unit = "alala".print

  }
  val p0       = Point.apply(1, 2)
  val (a1, a2) = Point.unapply(p0).get

  val p1 = Point(1, 2)
  p1.alala()
//  p1.x = 3 //Reassignment to val
//  p1.x = 3 //var x: Int
  p1.x.print

  val p2 = p1.copy(x = 2)
  p2.print

}

object ScalaObjects extends App with ImplicitForPrint {
  LogAny.log("asdasd")

//  import LogAny.log
//  import LogAnyV2.{ log => log2 }
//  log("asdas")
  LogAnyV2.log("dsdds")

  //scala in java: LogAny$ logAnyScalaObject = LogAny$.MODULE$;
}

object LogAny {
  println("INIT LogAny")
  def log(any: Any): Unit = println(s"INFO: $any")
}

object LogAnyV2 {
  println("INIT LogAny")
  def log(any: Any): Unit = println(s"INFO: $any")
}

object ScalaCompanionObjects extends App with ImplicitForPrint {

  case class Point(x: Int, y: Int) //java: +- record

  object Point {
    def distance(from: Point, to: Point): Double = {
      import scala.math.sqrt

      val deltaX     = to.x - from.x
      val deltaXQuad = deltaX * deltaX

      val deltaY     = to.y - from.y
      val deltaYQuad = deltaY * deltaY

      sqrt(deltaXQuad + deltaYQuad)
    }
    def createFromX(x: Int): Point = Point(x, 0)
    def createFromY(y: Int): Point = Point(0, y)
    def createWithZeroXY: Point    = Point(0, 0)
  }

  val p1 = Point(2, 2)
  val p2 = Point(2, 4)

  val p3 = Point.apply(1, 2) //todo check lazy
  Point.distance(p1, p2).print

  val p4 = Point.createWithZeroXY
  val p5 = Point.createFromX(1)
  val p6 = Point.createFromY(2)
}

object ScalaTraits extends App with ImplicitForPrint {

//  trait Animal
//  val animal = new Animal {} // for tests

  trait Animal {
    val name: String

    def makeSound: Unit

    def printInfo: Unit = {
      print(s"${this.getClass.getSimpleName}, name: $name, sound is: ")
      this.makeSound
    }
  }

  trait HasShell {
    def infoAboutShell: Unit = println("I have a shell!")
  }

  case class Cat(name: String) extends Animal {
    override def makeSound: Unit = println("mew!")
  }

  case class Turtle(name: String) extends Animal with HasShell {
    override def makeSound: Unit = println("shhh!")
  }

  val cat1 = Cat("Sylvanas")
  cat1.printInfo // Cat, name: Sylvanas, sound is: mew!
  println("-------------")
  val turtle1 = Turtle("Turtwig")
  turtle1.printInfo      // Turtle, name: Turtwig, sound is: shhh!
  turtle1.infoAboutShell //I have a shell!

  // class1 extends class2 || tarit1 with trait2 with traitN
}

object ScalaSelfTypes extends App with ImplicitForPrint {
  trait Animal
  trait HasGills { // имеет жабры
    val strHasGills: String = "has gills"
  }

  trait CanSwimUnderwater { // может плавать под водой
    this: HasGills => // !!!
    def printCanSwimUnderwater: Unit = println(s"Can swim underwater cause $strHasGills")
  }

//  case class Fish0(name: String) extends Animal with CanSwimUnderwater // Illegal inheritance, self-type Fish does not conform to HasGills

  case class Fish(name: String) extends Animal with HasGills with CanSwimUnderwater {
    def makeSound: Unit = println("blblblbl!")
  }

  val fish1 = Fish("Nemo")
  fish1.printCanSwimUnderwater // Can swim underwater cause has gills

}

object ScalaAbstractClasses extends App with ImplicitForPrint {
  abstract class Figure(name: String) {
    def area: Int
    override def toString = s"Name: $name, area: $area"
  }

//  val f = new Figure("") // Class 'Figure' is abstract; cannot be instantiated

  case class Square(name: String, side: Int) extends Figure(name) {
    override def area: Int = side * side
  }

  case class Rectangle(name: String, a: Int, b: Int) extends Figure(name) {
    override def area: Int = 2 * (a + b)
  }

  val square1    = Square("square1", 2)
  val rectangle1 = Rectangle("rectangle1", 4, 2)
  square1.area.print
  rectangle1.area.print

}
