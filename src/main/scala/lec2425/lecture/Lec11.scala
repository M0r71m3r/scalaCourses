package lec2425.lecture

import scala.beans.BeanProperty

object Implicits {
  // implicit val - параметр
  // implicit def - функция / преобразование
  // implicit class - класс

  // implicit val x = 1
  // def a(y: Int)(implicit a:Int)
  // import tor declare in scope (object, def, class)

  // ctrl alt shift + => вкл отображение implicit
  // ctrl alt shift - => выкл отображение implicit
}

object ImplicitVal extends App {
  case class Example(value: String)
  def f(prefix: String)(implicit e: Example): Unit = {
    println(s"$prefix $e")
  }

  //f("1") // ce, cause implicit

  implicit val e1: Example = Example("Example 1")
//  f("1")
//  f("1")(e1)
//  f("2")(Example("Example 2"))

  implicit val e2: Example = Example("Example 1")
//  f("ha-ha") // ambiguous implicit values cause e2
  f("ha-ha")(e2)

  def f1(implicit e: Example)         = println(e) // => def f1()(implicit e: Example)
  def f2(implicit e: Example, x: Int) = println(e + " " + x)

//   def f3(implicit e: Example)(x: Int) // ce
//   def f3(x: Int)(implicit e: Example)(y: Int) // ce
//   def f3(y: Int, implicit e: Example, x: Int) // ce
//   def f3(z: Int)(y: Int, implicit e: Example, x: Int) // ce

  case class ExampleV2(value: String)(implicit x: Int)
  implicit val u = 1
  val e3         = ExampleV2("123")
  val e4         = ExampleV2("123")(2)
}

object ImplicitDef extends App {
  case class A(i: Int)
  case class B(i: Int)

  implicit def a2B: A => B = a => B(a.i)
  val a: A                 = A(1)

  println(a)
  val b: B = a
  println(b)
  case class C(i: Int)
  implicit def b2C(b: B): C = C(b.i)

  val c: C = b2C(a)
}

object ImplicitDefHell extends App {
  val jd: java.lang.Double = null
  println(jd) // null
  val sd: Double = jd
  println(sd) // 0
  val optSd: Option[Double] = Option(jd)
  println(optSd) // Some(0)
  val optSd1: Option[Double] = Option(jd).map(Double2double)
  println(optSd1) // None
}

object ImplicitDefForImport {
  implicit def strToInt(s: String): Int = 42
}

object ImplicitDefImport extends App {
  import ImplicitDefForImport._
  "1412" / 3 // cause strToInt from import
}

object ImplicitClassOps extends App {
  implicit class IntOps(i: Int) {
    def opt = Option(i)
  }
}

object ImplicitClass extends App {
  import ImplicitClassOps._
  val x: Int = 1
  println(x)
  val optX: Option[Int] = x.opt
  println(optX)
}

object ImplicitClassStrSyntax {
  implicit class StrOps(s: String) {
    def orEmpty: String = Option(s).getOrElse("")
  }
  case class Person(@BeanProperty name: String, @BeanProperty age: Int)

  val person1: Person = Person("Hha", 18)
  val person2: Person = Person(null, 18)
  println(person1.getName.orEmpty)
  println(person2.getName.orEmpty)
}

object ImplicitHell extends App {
  implicit val seq: Seq[String] = Seq("a a", "b b", "c c")
  println(1.split(" ").mkString("ImplicitHell(", ", ", ")")) // seq(1)....
}