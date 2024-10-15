package lec2324.lecture

//https://www.scala-exercises.org/ - scala STD + tutor
// ООП значение = объект
// ФП  функция = значение
// функция = значение = объект => функция = объект

object HelloWorld {
  def main(args: Array[String]): Unit = {
    //java: int x = 1; or var x = 1;
//    val x = {
//      println("Chto-to")
//      1
//    }
    println("Hello!")
  }
}

object HelloWorldWithApp extends App {
  //mutable
  var h: Int = 1
  h = 2
  println(h)
}

object TypesAndAllALL extends App {
  val int: Int     = 1
  val long: Long   = 1L
  val d: Double    = 1d
  val eq: Boolean  = int == int
  val str: String  = "A" + "B"
  val str1: String = s"int = $int, sum=${int + int}"
  //  val nothing = throw new Exception()

  //java imports and types
  import java.lang.{ Long => JLong }
  type jLong = java.lang.Long
  val jLong: JLong  = 1L
  val jLong1: jLong = 1L
  val jLong2: JLong = long2Long(jLong1)
  val sLong: Long   = Long2long(jLong1)

  //type - just alias
  //type Point                 = (Int, Int)
  type ContactTypeWithValues = Map[String, List[String]]
  type PersonIdWithContacts  = Map[Long, ContactTypeWithValues]
  //without - val map: Map[Long, Map[String, List[String]]] = ???
  val map: PersonIdWithContacts = Map(1L -> Map("Mobile" -> List("11111111111")))

  // flatten = [[1], [2], [3]] => [1,2,3]
  val values = map.values.flatMap(_.values).flatten

  //tuples
  val t2_1 = 1 -> 1 // 1 -> 1 => tuple [Int Int]
  val t2_2 = (1, 1)
  val t3   = (1L, "F", true)
  //tuple in tuple
  val t3_2: ((Long, String), Boolean) = 1L -> "F" -> true

  println(t3._1)
  println(t3._2)
  println(t3_2._1._2) //bad

  val p = Map((1, 2)) // or Map(1 -> 2)

  //lazy, example - scnd lecture
  lazy val l = 1

  // def name (a:Int): int = {}
  def sum(x: Int, y: Int): Int  = x + y
  def sum2(x: Int)(y: Int): Int = x + y

  //type = function
  val aSum: (Int, Int) => Int  = (x: Int, y: Int) => x + y
  val intToStr: Int => String  = (x: Int) => x.toString
  val intToStr2: Int => String = _.toString

  //pure or not?
  def div(x: Int, y: Int): Int = x / y

  val v  = sum2(1)(_) // curry
  val v1 = v(2)       //3
  println(v1)

  //comment

  /*
  comment
  comment
   */

  /**
   * List of [[String]]
   */
  val ls   = (1 :: 2 :: Nil).map(intToStr)
  val lNil = List.empty[String]
}

object Operators extends App {
  val x = 1.+(2)
  val y = 1 + 2
}

object CollectionSugarExamples extends App {
  val l1: List[Int] = Nil
  val l2: List[Int] = Nil
  val l3            = l1 ++ l2
  val x             = l3.foldLeft(0)(_ + _) //or l3.sum
}
