package lec25_1.lecture

object Lec8Set extends App {
  val s1 = Set(1, 2, 3)
  val s2 = Set(1, 2, 3, 3)
  //  println(s"s1: $s1")
  //  println(s"s2: $s2")
  //  println(s1 == s2)

  class SimpleInt(i: Int) { // class not impl eq and hashCode
    override def toString() = s"SimpleInt(${i})"
  }

  val si1 = Set(new SimpleInt(1), new SimpleInt(2), new SimpleInt(3))
  val si2 = Set(new SimpleInt(1), new SimpleInt(2), new SimpleInt(2), new SimpleInt(3), new SimpleInt(3))
  println(si1)
  println(si2)
  println(si1 == si2)
  // equals and hashCode

  //  val s4 = s1 - 3
  //  println(s4)
  //  val s5 = s1 + 5
  //  println(s5)
  //  val s6 = s1 -- s2
  //  println(s6)
}

object Lec8Map extends App {
  val map1 = Map(1 -> "AAA", 2 -> "BBB", 3 -> "CCC")
  val map2 = Map(1 -> "AAA", 2 -> "BBB", 3 -> "CCC", 2 -> "VVV")
  println(map1)
  println(map2)
  map1.keySet.foreach(println)
  map1.values.foreach(println)

  map1.map { x =>
    val nK = x._1 + 1
    val nV = x._2 ++ "!!!"
    nK -> nV
  }

  map1.map { case (k, v) =>
    val nK = k + 1
    val nV = v ++ "!!!"
    nK -> nV
  }

  val key = 1
  val v   = map1.get(key)
  val v2  = map1(key)
  scala.collection.immutable.Map
  val a  = Map(1 -> "AAAA") // Map((1, "AAA"))
  val a2 = a + (2 -> "BBB")
  println(a)
  println(a2)

  val a3 = a - 2
  val a4 = a - 1 - 2
  val a5 = a -- List(1, 2)

  val u7 = a.updated(3, "CCC")
  val u8 = a + (2 -> "EEE")
  println(u7)
  println(u8)

}

object Lec8CollectionTicket extends App {
  val lWithOpt = List(Option(1), None, Option(2))
  println(lWithOpt.flatten) // List(List(1), List(), List(2)).flatten -> List(1, 2)
  val oWithL = Option(List(List(1)))
  println(oWithL.map(_.flatten)) // Option(List(1))

  val lWithNulls = List(null, 1, 2, 3)
  //  println(lWithNulls.headOption.map(_.toString))
  println(lWithNulls.headOption.flatMap(x => Option(x)).map(_.toString))

  val llWithNulls  = lWithNulls ++ lWithNulls
  val llWithNulls1 = lWithNulls :+ 3
  val llWithNulls2 = 3 +: lWithNulls
  println(s"llWithNulls: $llWithNulls")
  println(s"llWithNulls1: $llWithNulls1")
  println(s"llWithNulls2: $llWithNulls2")

  val list         = List(4, 5, 6)
  val llWithNulls3 = list ::: list
  val llWithNulls4 = list ++ list // set ++ set, map ++ map

  def evenOrOdd(it: Iterable[Int]): Iterable[Boolean] = it.map(_ % 2 == 0)

  val exampe        = Set(2, 4, 6, 8, 10)
  val exampleResult = evenOrOdd(exampe).size
  println(exampleResult)
}

object Lec8Array extends App {
  val arrayByte = Array[Byte](40, 64, 41)
  arrayByte.map(_.toChar)
  java.util.Map
  val str = new String(arrayByte)
  println(str)
  println(arrayByte.map(_.toChar).mkString(""))
}
