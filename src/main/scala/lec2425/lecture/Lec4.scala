package lec2425.lecture

object ScalaTraits extends App {
  class Animal1

//  trait Animal
//  val animal = new Animal{} // for test
  trait Animal {
    val name: String
    def makeSound(): Unit
    def printInfo(): Unit = {
      print(s"${this.getClass.getSimpleName}, name: $name, sound is: ")
      makeSound()
    }
  }

  case class Cat(name: String) extends Animal {
    override def makeSound(): Unit = println("mew!")
  }

  trait HasShell {
    def infoAboutShell(): Unit = println("I have a shell!")
  }
  // just for test more one `with`
  trait HasShell2 {}
  // extends class1 || trait1 with trait2 with traitN
  // only traits can be `with`
  case class Turtle(name: String) extends Animal with HasShell with HasShell2 {
    override def makeSound(): Unit = println("shh!")
  }
  val cat1 = Cat("Sylvanas")
  cat1.printInfo()
  println("---------------")
  val turtle = Turtle("Turtwig")
  turtle.printInfo()
  turtle.infoAboutShell() // cause with HasShell
}

object ScalaSelfTypes extends App {
  trait HasGills {
    val hasGills = "has gills"
  }
  trait HasGills1 {
    val r: Int // for auto override param in `with`
  }
  trait CanSwimUnderWater {
    this: HasGills => // self type
    val x = 1
    def printCanSwimUnderWater(): Unit =
      println(s"Can swim underwater cause $hasGills")
  }
  case class Fish(name: String, r: Int) extends CanSwimUnderWater with HasGills with HasGills1

  // self type in classes:
  class TestSelfType1(i: Int) {
    this: HasGills => // just work
    def a(): Unit = println("a")
  }
  case class Test2(r: Int) extends TestSelfType1(r) with HasGills
  val t2 = Test2(1)
  t2.a()
  // next lec mb answer:
//  case class TestSelfType(i: Int) {
//    this: HasGills => // wtf
//    def a(): Unit = println("a")
//  }
//  class TestSelfType2(i: Int) extends TestSelfType(i) with HasGills {
//    def z(): Unit = println("z")
//  }
//  val tts = new TestSelfType2(1)
//  tts.a()

}

object ScalaAbstractClasses extends App {
  abstract class Figure(name: String) {
    def area: Int //= 0
    override def toString: String = s"Name: $name, area: $area"
  }
  case class Squre(name: String, side: Int) extends Figure(name) {
    override def area: Int = side * side
  }
  //val f = new Figure("a") // ce, like interface or abstract class(???) in java
  val s = Squre("d6", 2)
  println(s.name)
  println(s.side)
  println(s.area)
}

object Match extends App {
  import scala.util.Random
  val nextInt = Random.nextInt(10)

  nextInt match {
    case 0 => println("zero")
    case 1 => println("one")
    case 2 => println("two")
//    case i => println(i)
    case _ => println("unknown")
  }

  // can return value
  val x = nextInt match {
    case 0 => "zero"
    case 1 => "one"
    case _ => "unknown"
  }
  println(x)

  def giveMeStr: String = nextInt match {
    case 0 => "zero"
    case 1 => "one"
    case _ => "unknown"
  }
  println(giveMeStr)
}

object CaseClassMatch extends App {
  trait Action
  case class Create(name: String)           extends Action
  case class Read(id: Long, maxSize: Int)   extends Action
  case class Update(id: Long, name: String) extends Action
  case class Delete(id: Long)               extends Action

  def checkMaxSize(ms: Int): Boolean = ms > 100 && ms < 150

  def processAction(ac: Action): Unit =
    ac match {
      case Create(name) =>
        println(s"Create $name")
      case Delete(id) =>
        println(s"Delete $id")
      // can be if maxSize > 100 && maxSize < 150, && can be ||
      case Read(id, maxSize) if checkMaxSize(maxSize) =>
        println(s"read with maxSize > 100")
      case Read(id, _) =>
        println(s"just read")
      case Update(id, name) =>
        println("update")
      case any =>
        println("any: " + any)
    }

  processAction(new Action {})
  processAction(Create("aaa"))
  processAction(Read(1, 101))
  processAction(Read(1, 99))

  def processActionV2(ac: Action): Unit =
    ac match {
      case c: Create =>
        println(s"Create ${c.name}")
      case d: Delete =>
        println(s"Delete ${d.id}")
      // can be if maxSize > 100 && maxSize < 150, && can be ||
      case r: Read if checkMaxSize(r.maxSize) =>
        println(s"read with maxSize > 100")
      case r: Read =>
        println(s"just read")
      case _: Update =>
        println("update")
      case any =>
        println("any: " + any)
    }
  processActionV2(new Action {})
  processActionV2(Create("aaa"))
  processActionV2(Read(1, 101))
  processActionV2(Read(1, 99))
}

sealed trait ActionV2
case class Create(name: String)           extends ActionV2
case class Read(id: Long, maxSize: Int)   extends ActionV2
case class Update(id: Long, name: String) extends ActionV2
case class Delete(id: Long)               extends ActionV2

object SealedCaseClassMatch extends App {
  def processActionV3(ac: ActionV2) =
    ac match {
      case Create(name)      => println(1) // just for test
      case Read(id, maxSize) => println(2) // just for test
      case Update(id, name)  => println(3) // just for test
      //case _ => // drop
//    case Delete(id) = println(4) warning on compile
    }
  processActionV3(Create("1123"))
}
