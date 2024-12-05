package lec2425.lecture

object HWLec7 extends App {

  // in hw:
  // WeekDay.values.filter(i => i.id == (d1.id + 1)).head
  object TestE extends Enumeration {
    type TestE = Value
    val Red, Green, Blue, Black = Value

    def nextVal(te: TestE): TestE =
      if (this.maxId - 1 == te.id) this(0)
      else this(te.id + 1)

    def nextValV2(te: TestE): TestE =
      this.values.find(_.id == te.id + 1).getOrElse(this(0))

    // for business
    def nextValV3(te: TestE): TestE =
      this.values.find(_.id == te.id + 1).orNull
  }

  val r = TestE.Black
  // bad, cause can be more than last index in enum
  val x = TestE(r.id + 1)

  //println(DayOfTheWeek.maxId)
  //Почему?????????????????
  // The one higher than the highest integer amongst
  // those used to identify values in this enumeration.
  println(TestE.maxId)
  println(TestE.Red.id)
  println(TestE.Green.id)
  println(TestE.Blue.id)
  println(TestE.Black.id)
}

object HWLec9 extends App {
  def function1: Int => Int = (a: Int) => a + a

  def function2: Int => Int = (a: Int) => a * a

  def function3: Int => Int = (a: Int) => a / 2

  val lf = List(function1, function2, function3)

  def listFun(f: Int => Int, f2: Int => Int, f3: Int => Int, i: Int): List[Int] = {
    val l = List(f(i), f2(i), f3(i))
    l
  }

  def listFun(lf: List[Int => Int], i: Int): List[Int] =
    lf.map(f => f(i))
}

object HWLec10 extends App {
  def task12(n: Int): Boolean = {
    for (i <- 1 to n) {
      if (i * i == n) return true
    }
    false
  }
  // Double => Int => Double
  // 4.3 => 4 => 4.0
  def task121(n: Int): Boolean = {
    val r = 1 to n
    r.exists(i => i * i == n)
  }

  val l = List(2, 4, 3, 9, 25, -1).collect {
    case x if x > 0 && task121(x) => x
  }
  println(l)
  val l1 = List(2, 4, 3, 9, 25, -1).collect {
    case x if x > 0 && task12(x) => x
  }
  println(l1)
}

object IntImplicits {
  implicit class Multipl5(i: Int) {
    def multipl5: Int = i * 5
  }

  implicit class Multipl10(i: Int) {
    def multipl10: Int = i * 10
  }

  implicit class IsEven(i: Int) {
    def isEven: Boolean = i % 2 == 0
  }

  implicit class IntOps(i: Int) {
    def multipl5: Int   = i * 5
    def multipl10: Int  = i * 10
    def isEven: Boolean = i % 2 == 0
  }
}

object HWLec11 extends App {
  //6.isEven // Cannot resolve symbol isEven
  IntImplicits.IsEven(6).isEven
}

object HWLec12 extends App {
  def task4[T, U](t: List[T], f: T => U): List[U] = {
    // t map f
    t.map(f)
    t.map { f }
    t.map { f(_) }
    t.map {
      f(_)
    }
  }
  val l = 1 :: Nil
}

object HWLec13_1 extends App {
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.Future

  val listofFutures = List.empty[Future[String]]

  def inList(str: List[Future[String]]): Future[List[String]] = {
    Future.sequence(str).map(l => l.map(_.toUpperCase()))
  }
  val someOperationOnFuture: Future[List[String]] =
    inList(listofFutures) //.map(_.map(_.toUpperCase()))

}
object HWLec13_2 extends App {
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.{Await, Future}
  import scala.concurrent.duration.Duration

  //Напишите функцию, которая принимает список Future[Int]
  //и возвращает Future[Int], представляющий сумму всех значений в этих Future
  def task2(sl: List[Future[Int]]): Future[Int] = {
    println("def thread: " + Thread.currentThread().getName)
    val x: Future[List[Int]] = Future.sequence(sl)
    val res                  = Await.result(x, Duration.Inf)
    Future { res.sum }
  }
  def task2_1(sl: List[Future[Int]]): Future[Int] = {
    println("def thread: " + Thread.currentThread().getName)
    Future.sequence(sl).map(_.sum)
  }
}

