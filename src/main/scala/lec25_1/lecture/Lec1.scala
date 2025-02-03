package lec25_1.lecture

object HelloWorld {
  def main(args: Array[String]): Unit = {}
}

object HelloWorldWithApp extends App {
  val x: Int       = 1
  val x1 /*: Int*/ = 1
  var x2           = 2
  x2 = 3
  //x1 = 4 // ce
//  println(x2)

  val x4 = {
    println("init x4")
    println("x4")
    5
  }
  println(x4)
}

/**
 * asadasd
 */
object TypesAndAllALL extends App {
  val x: Int        = 1
  val x1: Long      = 1L
  val x2: Double    = 1d
  val x3: Char      = '1'
  lazy val x4: Byte = ???
  // val x5: Nothing = ???
  val x6: String = "1234"
  /*
  asdas
  asdas
   */
  val x7: Boolean = false
  // Unit == void (java)
  val x8: Unit = println(x4)
}

object StringSmthng extends App {
  val x: Int   = 1
  val x1: Long = 1L
  val x2       = "str"
  val x3       = s"$x + $x1 = ${x + x1}"
  // println(x3)
  val x4 =
    s"""
       |${x1}
       |${x2}
       |""".stripMargin
  val x5 =
    s"""
      |${x1}
      |${x2}
      |""" /*.stripMargin*/
  println(x4)
  println("--------------")
  println(x5)
}

object TypeDef extends App {
  // x++, x-- - нету
  val x    = 1 + 1
  val x1   = 1 - 1
  val x2   = 1 * 1
  val x4   = 1 / 1
  val x4_1 = 1 % 1
  val x5   = "1" + "1"
  // not use equals, use ==
  val x6 = "1" == "1"
  val x7 = "1" < "1" // > >= <= !=
}

object TupleSmthng extends App {
  val x: (Int, String, Boolean) = (1, "2", false)
  val (i, l, b)                 = x
  val (i1, _, b1)               = x
  val t2: (Int, String)         = 1 -> "1"

  println(i)
  println(i1)

  val x1: String = x._2
  val x2: Int    = x._1

  type PersonId = Long

  def get(pId: PersonId) = {
    pId + 1
  }

  type ContactType  = String
  type ContactValue = String
  val m: Map[Long, Map[String, String]]                 = ???
  val m1: Map[PersonId, Map[ContactType, ContactValue]] = ???
}

object JavaScalaTypes extends App {
//  import java.lang.Long
  import java.lang.{Long => JLong}
  val x: Long   = 1L
  val x1: JLong = null
  println(x1)
  val x2: Long = Long2long(x1)
  println(x2)
}

object DefFun extends App {
  // java: void name(String s) {}
  def print(s: String): Unit = {
    println(s) //; println("1")
  }
  def print1(s: String): Unit = println(s)

  val f: String => Unit  = (s: String) => println(s)
  val f1: String => Unit = s => println(s)

  def printAndGetTuple(i: Int, s: String): (Int, String) = {
    println(i)
    println(s)
    i -> s
  }

  val (x1, y1) = printAndGetTuple(1, "2")
  println(x1, y1)

  val f2: String => (Int, String) = printAndGetTuple(1, _)
}

object DefaultDefParam extends App {
  val isFio = false
  val isEmpty = true
  def f(i: Int = 0, s: String = "", isEmpty: Boolean = false) = s"$i $s $isEmpty"
  val x                                                 = f()
  val x1                                                = f(4)
  val x2                                                = f(s = "1")
  val x3                                                = f(isEmpty = isEmpty)
  val x4                                                = f(1, "1", isEmpty = true)
  val x5                                                = f(s = "1", i = 1, isEmpty = true)
}

object DefParams extends App {
  // def f(i:Int, s:String)
  def f(i:Int)(s:String): String = {
    s"$i $s"
  }
  val x = f(1)("2")
}
