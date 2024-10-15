package lec2324.lecture

import lec2324.lecture.OperationsV2.OperationV2
import lec2324.magicZone.ImplicitForPrint

object Operations extends Enumeration {
  type Operation = Value
  val Plus, Minus = Value
}

object OperationsV2 extends Enumeration {
  type OperationV2 = Value
  val Product, Divide = Value
}

object EnumTypeErasure extends App with ImplicitForPrint {

//  def toString(o: Operation): String = o.toString
  def toString(o: OperationV2): String = o.toString

}

object ScalaSet extends App with ImplicitForPrint {

  /** Set(1, 2, 3) */
  val s1 = Set(1, 2, 3)
  s1.print
  val s2 = Set(4, 1, 2, 2, 3, 3)
  s2.print

  val s3 = Set.empty[Int]
//  val s4 = Set()[Int] // bad

  // case or equals+hashCode for correct set work
  case class SimpleInt(i: Int) {
    override def toString: String = s"SimpleInt($i)"
  }

  val si1 = SimpleInt(1)
  val si2 = SimpleInt(2)
  val si3 = SimpleInt(2)
  val si4 = SimpleInt(3)

  val s1WithClass: Set[SimpleInt] = Set(si1, si2, si3, si4)
  s1WithClass.print

  val s4 = s1 - 2 // s1.excl(2)
  s4.print
  val s5 = s1 - 5
//  if (s1.size == s5.size) throw new Exception("EEE")
  s5.print

  "--------------------".print

  val s6 = s1 -- s4
  s6.print

  val s7 = s1 + 5
  s7.print

  val s8 = s1 ++ s2
  s8.print

  val l1 = 1 :: 2 :: 2 :: Nil
  val s9 = l1.toSet
  s9.print

}

object ScalaMap extends App with ImplicitForPrint {

  val m1 = Map(1 -> "AAA", 2 -> "BBB", 3 -> "CCC")
  m1.print // Map(1 -> "AAA", 2 -> "BBB", 3 -> "CCC")
  val m2 = Map(1 -> "AAA", 2 -> "BBB", 2 -> "VVV", 3 -> "CCC")
  m2.print // Map(1 -> "AAA", 2 -> "VVV", 3 -> "CCC")

  (m1 == m2).print // false

//  val m3 = m1.map(kv => (kv._2,kv._1))

  m1.values.print
  m1.keySet.print

//  val l1 = 1 :: Nil
//  val l2 = l1 + "1"

  val m3 = m1.map { x =>
    val newK = x._1 + 1
    val newV = x._2 ++ "!!!" // eq s"${x._2}!!!"
    newK -> newV // eq (newK, newV)
  }
  "--------------------".print
  m3.print

  val m4 = m1.map { case (k, v) =>
    val newK = k + 1
    val newV = v ++ "!!!"
    (newK, newV) // eq newK -> newV
  }
  m4.print

  "--------------------".print

  val key = 1

  val v: Option[String] = m1.get(key) // Option(any) => Some(any) || None
  v.print
//  val v1 = m1(5) // key not found
//  v1.print
  val v1: Option[String] = m1.get(5) // Option(any) => Some(any) || None
  v1.print

  val v2 = m1.getOrElse(5, "-1")
  v2.print
  "--------------------".print
  val aM = Map(1 -> "One")
  aM.print // Map(1 -> "One")
  val aM1 = aM + (2 -> "Two")
  aM1.print // Map(1 -> "One", 2 -> "Two")

  "--------------------".print

  val aM3 = Map(1 -> "One", 2 -> "Two", 3 -> "Thr", 4 -> "Four")
  aM3.print // Map(1 -> "One", 2 -> "Two", 3 -> "Thr", 4 -> "Four")
  val aM3_Mins = aM3 - 3
  aM3_Mins.print // Map(1 -> "One", 2 -> "Two", 4 -> "Four")
  val aM3_MinsV2 = aM3 - 3 - 2 //  - 5 valid
  aM3_MinsV2.print // Map(1 -> "One", 4 -> "Four")
  val aM3_MinsV3 = aM3 -- List(3, 2)
  aM3_MinsV3.print // Map(1 -> "One", 4 -> "Four")

  "--------------------".print

  val up = m1.updated(1, "ONE")
  up.print // Map(1 -> "ONE", 2 -> "BBB", 3 -> "CCC")
  val up2 = m1 + (1 -> "ONE")
  up2.print // Map(1 -> "ONE", 2 -> "BBB", 3 -> "CCC")

}

object ScalaCollectionTricks extends App with ImplicitForPrint {

  val lWithOpt: List[Option[Int]] = List(Option(1), None, Option(2))
  lWithOpt.print
  val lWithOptFlatten: List[Int] = lWithOpt.flatten
  lWithOptFlatten.print
  // or
  val x = lWithOpt.collect { case Some(x) => x }
  x.print
  "--------------------".print
//  val optList = Option(List(1, 2))
//  val optListFlatten = optList.flatten

  val optListList: Option[List[List[Int]]] = Option(List(List(1)))
  val optListListMpa: Option[List[Int]]    = optListList.map(_.flatten)

  val lWithNull = List(null, 1, 2, 3)

  val lWithNullHeadOption = lWithNull.headOption
  lWithNullHeadOption.print // Some(null)

  val lWithNullOpt = lWithNull.headOption.flatMap(x => Option(x))
  lWithNullOpt.print // None

  "--------------------".print

  def evenOrOdd(it: Iterable[Int]): Iterable[Boolean] = it.map(x => x % 2 == 0)

  val example          = Set(2, 4, 6, 10)
  val exampleEvenOrOdd = evenOrOdd(example)
  exampleEvenOrOdd.size.print // 1, cause Set in Iterable

//  val m1 = Map(1 -> "AAA", 2 -> "BBB", 3 -> "CCC")
//  val m1EvenOrOdd = evenOrOdd(m1)
//  m1EvenOrOdd.size.print

}
