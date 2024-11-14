package lec2425.lecture

import java.util.Date
import scala.beans.BeanProperty

object ScalaHigherOrderFunction extends App {

  def hof1(i: Int, f: Int => Int): Int = f(i)

  def quad(i: Int): Int = i * i

  val res = hof1(2, quad)
//  println(res)

  // def type doesnt req
  def hof2_1(i: Int): Int => Int = (a: Int) => i * a
  // def type req
  def hof2(i: Int): Int => Int = a => {
    println(s"f: $i * $a")
    i * a
  }
  // def type req
  def hof2_2(i: Int): Int => Int = {
    println(s"f: ${i} * a")
    _ * i
  }
  def res2: Int => Int = hof2(2)
  val res3: Int => Int = hof2(2)
//  println(res2(3))
//  println(res3(3))
//  println(res3)

  val l = List(1, 2, 3, 4, 5)
  println(l.map(res2))
  println("----------")
  println(l.map(res3))
  println("----------")
  println(l.map(quad))         // auto convert quad to Int => Int
  println(l.map(i => quad(i))) // java: i -> quad(i) || Object::hashCode
}

object ScalaHigherOrderFunctionProd extends App {
  object ModifyListInt {
    // def add2(ints: List[Int])    = ints.map(i => i + 2)
    def add2(ints: List[Int]): List[Int] =
      ints.map(_ + 2)
    def prodBy2(ints: List[Int]): List[Int] =
      ints.map(_ * 2)
    def quad(ints: List[Int]): List[Int] =
      ints.map(i => i * i)
  }
  object ModifyListIntV2 {
    private def applyFToList(ints: List[Int], f: Int => Int) = {
      ints.map(f)
    }
    def add2(ints: List[Int]): List[Int]    = applyFToList(ints, _ + 2)
    def prodBy2(ints: List[Int]): List[Int] = applyFToList(ints, _ * 2)
    def quad(ints: List[Int]): List[Int]    = applyFToList(ints, i => i * i)
  }
  val l: List[Int] = List(1, 2, 3, 4, 5)
  println(ModifyListInt.add2(l))
  println("--------------")
  println(ModifyListIntV2.add2(l))
}

object ScalaCurry extends App {

  def sum(x: Int)(y: Int): Int = x + y

  def inc: Int => Int   = sum(1)
  def inc_1: Int => Int = sum(1)(_)
  def inc_2: Int => Int = sum(_)(1)

  val res = inc(2)
  println(res)
  println("-------------")
  val res1 = inc_1(2)
  println(res1)

  def sum1(x: Int, y: Int): Int = x + y

  def inc_3: Int => Int = sum1(1, _)
  def inc_4: Int => Int = sum1(_, 1)
  println("-------------")
  val res2 = inc_2(2)
  println(res2)

  def sum3(x: Int, y: Int, z: Int): Int = x + y + z
  def sum4(x: Int)(y: Int)(z: Int): Int = x + y + z

  def inc_5: (Int, Int) => Int = sum3(_, 1, _)
  def inc_6: (Int, Int) => Int = sum4(_)(1)(_)

  def sumF(f: Int => Int)(x: Int, y: Int) = f(x) + f(y)
  // f = identity(x) + identity(y)
  // f = x + y
  def justSum = sumF(identity)(_, _)

  def justSumV2 = sumF(i => i * 2)(_, _)
  println(justSumV2(2, 3))

  case class Person(
      @BeanProperty lastName: String,
      @BeanProperty firstName: String,
      @BeanProperty middleName: String
  )
  def transformNameByPhoneExists(
      name: String,
      phoneExists: Boolean,
      age: Int,
      bd: Date,
      dateCreate: Date
  ) = if (name != null && name.nonEmpty && phoneExists) name.head.toString.capitalize else ""

  private def getFio(
      person: Person,
      phoneExists: Boolean,
      age: Int,
      bd: Date,
      dateCreate: Date
  ): Option[String] = {
    val curryTransformNameByPhoneExists =
      transformNameByPhoneExists(_, phoneExists, age, bd, dateCreate)

    val lastName   =
      Option(person.getLastName).map(s => s.head.toString.capitalize).getOrElse("") // bug
    val firstName  = curryTransformNameByPhoneExists(person.getFirstName)
    val middleName = curryTransformNameByPhoneExists(person.getMiddleName)
    // Уланов П О
    // Уланов Павел Олегович
    Option(s"$lastName $firstName $middleName")
  }

  private def getFio1(
      person: Person,
      phoneExists: Boolean,
      age: Int,
      bd: Date,
      dateCreate: Date
  ): Option[String] = {
    val lastName = Option(person.getLastName).map(s => s.head.toString.capitalize).getOrElse("")
    val firstName =
      transformNameByPhoneExists(person.getFirstName, phoneExists, age, dateCreate, bd) // bug
    val middleName =
      transformNameByPhoneExists(person.getMiddleName, phoneExists, age, bd, dateCreate)
    Option(s"$lastName $firstName $middleName")
  }
}