package lec25_1.lecture

object HW extends App {
  /*  {
    val oString: Option[String] = Option("Hello World")
    val searchStr: String       = "Hello"
    def findSubString(string: Option[String]): Unit = {
      if (string.getOrElse("").contains(searchStr)) println("Слово найдено")
      else println("Слово не найдено")
    }
    def findSubString2(string: Option[String]): Unit = {
      if (string.exists(_.contains(searchStr))) println("Слово найдено")
      else println("Слово не найдено")
    }
    findSubString(oString)
    findSubString2(oString)
  }*/

  /*
  {
    def function1: Int => Int = (a: Int) => a + a
    def function2: Int => Int = (a: Int) => a * a
    def function3: Int => Int = (a: Int) => a / 2

    def listFun(f: Int => Int, f2: Int => Int, f3: Int => Int, i: Int): List[Int] = {
      val l = List(f(i), f2(i), f3(i))
      l
      // just List(f(i), f2(i), f3(i))
    }
    println(listFun(function1, function2, function3, 4))

    val lf = List(function1, function2, function3/*, function2, function3*/)
    val l  = lf.map(f => f(4))
    println(l)
  }
   */

  /*  {
    def mapToMaps(map1: Map[String, Int]): (Map[String, Int], Map[String, Int]) = {
      val map2 = map1.filter { case (_, value) =>
        value > 0
      }
      val map3 = map1.filter { case (_, value) =>
        value < 0
      }
      (map2, map3)
    }
    println(mapToMaps(Map("A" -> -1, "B" -> 1, "C" -> 3, "D" -> -4)))

    def mapToMaps2(map1: Map[String, Int]): (Map[String, Int], Map[String, Int]) = {
      map1.partition(_._2 > 0)
    }
    println(mapToMaps2(Map("A" -> -1, "B" -> 1, "C" -> 3, "D" -> -4)))
  }*/

  /*  {
    def task12(n: Int): Boolean = {
      for (i <- 1 to n) {
        if (i * i == n) return true
      }
      false
    }
    val n = 10
    def task12_1(n: Int) = (1 to n).exists(i => i * i == n)
    println(task12(n))
    println(task12_1(n))
  }*/
}

object Implicits {
  // 1. Параметры // val
  // 2. Функции(преобразования) // def
  // 3. Классы // class

  // Должно быть ключевое слово implicit
  // Тип соответствовать объявленному
  // имплисит должен находиться в соответствующей зоне видимости
  // ОНО КОМПАЙЛ ТАЙМ (т.е вычисляется и подставляется на моменте компиляции)

  // ctrl alt shift + - показать все имплиситы
  // ctrl alt shift - - выключить показ всех имплиситов
}

case class Example(value: String)
object ImplicitValForImport {
  implicit val e2: Example = Example("123")
}

object ImplicitVal extends App {
  implicit val x: Int = 1

  def f(s: String)(implicit e: Example): Unit =
    println(s"$s ${e.value}")

  val e = Example("123")
//  import ImplicitValForImport._ => ce ambiguous implicit values
  implicit val e1: Example = Example("123")
//  implicit val e2: Example = Example("123") => ce ambiguous implicit values

  f("321")(e)
  f("321") /*(e)*/

  //  def f1(implicit e: Example)(s: String) = ??? => ce

  //  def f1(s: String)(implicit e: Example, i: Int) = ???
  //  f1("123")

//  def f2(implicit e: Example, i: Int) = "???"
//  println(f2)

  case class ExampleV2(s: String)(implicit e: Example)
}

object ImplicitDef extends App {
  case class A(i: Int)
  case class B(i: Int)

  implicit def a2B(a: A): B = B(a.i)

  val b: B = A(4)
  println(b)

  case class C(i: Int)
  implicit def b2C(b: B): C = C(b.i)
  implicit def a2C(a: A): C = b2C(a2B(a)) // or implicit b2C

  val c: C = A(4)
}

object ImplicitDefConversion extends App {
  val jd: java.lang.Double = null
  println(jd) // null
  val sd: Double = jd
  println(sd) // 0.0
  val optSd: Option[Double] = Option(jd)
  println(optSd) // Some(0.0)
  val optSd1: Option[ /*java.lang.*/ Double] = Option(jd).map(x => x)
  println(optSd1) // None
}

object ImplicitDefForImport extends App {
  implicit def getIntFromStr(s: String): Int = s.length
}

object ImplicitDefImport extends App {
  import ImplicitDefForImport._
  "afsasfa" / 2
}

object ImplicitClass extends App {
  implicit class IntOps(i: Int) {
    def opt: Option[Int] = Option(i)
  }
  val i               = 1
  val io: Option[Int] = i.opt
}

object StrSyntax {
  implicit class StrOps(s: String) {
    def orEmpty: String = Option(s).getOrElse("")
  }
}

object ImplicitClassStrSyntax extends App {
  import StrSyntax._
  val str          = Option("asf").getOrElse("")
  val str1         = "asf".orEmpty
  val str2: String = null.asInstanceOf[String].orEmpty
  println(str2)

  import scala.beans.BeanProperty
  case class Person(@BeanProperty name: String, @BeanProperty age: Int)
  val p: Person = Person(null, 1)

  println(Option(p.getName).getOrElse(""))
  println(p.getName.orEmpty)
}

object ImplicitHell extends App {
  implicit val seq: Seq[String] = Seq("a a", "b b", "c c")
  val x                         = 1.split(" ").mkString("what(", ",", ")")
  println(x)
  println(1 + "124")
}
