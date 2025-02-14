package lec25_1.lecture
import lec25_1.lecture.Lec4Sealed.Deleate2

object Lec4Trait extends App {
  trait Animal {
    val name: String
    def getName: String
    val surname            = ""
    def getSurname: String = surname
  }

  case class Cat(middleName: String) extends Animal {

    override def getSurname: String = "I "

    override val name: String = middleName

    override def getName: String = middleName
  }

  trait HasShell {
    def info: Unit = println("I have a shell")
  }

  case class Turtle(name: String) extends Animal with HasShell {
    override def getName: String = name
  }

  val turtle = Turtle("nane")
  turtle.info

  class A
  class B
  //  class C extends A with B ERROR !

  //  case class Test() extends Cat("") ERROR

  case class Test() extends A

  val animal = new Animal {
    override val name: String = ???

    override def getName: String = ???
  }
}
object Lec4SelfType extends App {
  trait Animal
  trait HasGills {
    val strHasGills = "Has gills"
  }
  trait CanSwim {
    this: HasGills =>
    def print: Unit = println(s"Can swim ${strHasGills}")
  }

  case class Fish(name: String) extends Animal with HasGills with CanSwim

  val gifish = Fish("fish")
  gifish.print
}
object Lec4Abstract extends App {
  abstract class Figure(name: String) {
    def area: Int

    override def toString: String = s"Name: $name, area: ${area}"
  }

  case class Square(name: String, side: Int) extends Figure(name) {
    override def area: Int = side * side
  }

  case class Rectangle(name: String, a: Int, b: Int) extends Figure(name) {
    override def area: Int = a * b
  }

  val square    = Square("square1", 2)
  val rectangle = Rectangle("square1", 2, 3)

  println(square.area)
  println(rectangle.area)

  val figure = new Figure("") {
    override def area: Int = ???
  }

  abstract case class A()
  class B extends A
}
object Lec4Match extends App {
  //  val nexInt = Random.nextInt(3)
  //  println(nexInt)
  //  nexInt match {
  //    case 0 =>
  //      println("zero")
  //      println("zero1")
  //
  //    case 1 => println("one")
  //    case _ => println("Any")
  //  }

  trait Action
  case class Create(name: String, age: Int)    extends Action
  case class Read(id: Long, maxSize: Int)      extends Action
  case class Update(id: Long, newName: String) extends Action
  case class Deleate(id: Long)                 extends Action

  val action: Action = Update(1, "Name1")
  action match {
    case c @ Create(name, _) if name.isEmpty => println("isEmpty")
    case Create(name, age)                   => println(s"Create name: ${name} age: ${age}")
    case d: Deleate                          => println(s"${d.id}")
    case Read(id, maxSize)                   => println(s"REad id: ${id} maxSize: ${maxSize}")
    case Update(id, newName)                 => println(s"Update id: ${id} ")
    case _                                   => println("any")
  }

}
object Lec4Sealed extends App {
  sealed trait Action
  case class Create(name: String, age: Int)    extends Action
  case class Read(id: Long, maxSize: Int)      extends Action
  case class Update(id: Long, newName: String) extends Action
  case class Deleate(id: Long)                 extends Action

  val action: Action = Update(1L, "Name1")
  action match {
    case Create(name, age)   => println(s"Create name: ${name} age: ${age}")
    case Read(id, maxSize)   => println(s"REad id: ${id} maxSize: ${maxSize}")
    case Update(id, newName) => println(s"Update id: ${id} ")
    case Deleate(id)         => println(s"Delete id: ${id} ")
  }

  sealed trait Action2
  class Create2(name: String, age: Int)    extends Action2
  class Read2(id: Long, maxSize: Int)      extends Action2
  class Update2(id: Long, newName: String) extends Action2
  class Deleate2(id: Long)                 extends Action2

  val action2: Action2 = new Delete2V2(3L)
  val item = action2 match {
    case create: Create2 =>
      "Create"
      3d

    case read: Read2        => 3L
    case update: Update2    => 3L
    case deleate: Delete2V2 => 3L
    case deleate: Deleate2  => 3L
  }

}

class Delete2V2(id: Long) extends Deleate2(id)

// !!! just for fun:
//object Lec4Ternar extends App {
//  implicit class Тернар[T](b: Boolean) {
//    class E(value: T) {
//      def неЦарь(right: T): T = if (b) value else right
//    }
//    def царь(left: T): E = new E(left)
//  }
//
//  val test = (3 == 3) царь {
//    println("3")
//    3
//  } неЦарь {
//    println("4")
//    4
//  }
//}
