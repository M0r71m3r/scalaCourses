package lec2425.lecture

import scala.util.{Failure, Success, Try}

object IfElse extends App {
  // java:
  // if (1==1) {
  //  sout(1)
  // } else {
  //  sout(2)
  // };

  //scala: if (cond) 1 else 2

  val x = 1
  val y = 2
  val z = 3

  def f(str: String = "f"): String = {
    println(str)
    str
  }
  def g(str: String = "g"): String = {
    println(str)
    str
  }
  def h(str: String = "h"): String = {
    println(str)
    str
  }

  if (x == y) f()
  if (x == y) {
    f()
  }
  if (x == y) {
    f()
  } else if (y == z) {
    g()
  } else {
    g()
  }

  val res: Any = if (x == y) f() // cause if without else => else ()

  // java: bool ? chto-to : chto-to
  val res1 = if (true) 1 else 2

}

object LazyIf extends App {
  lazy val lazyE = {
    println("ThreadSleep")
    Thread.sleep(1000)
    1
  }
  val sleepless = {
    println("sleepless")
    2
  }
  println("1 if eval")
  if (1 == 1) sleepless else lazyE
  println("2 if eval")
  if (1 != 1) sleepless else lazyE
  println("3 if eval")
  if (3 == 3) lazyE else sleepless
}

object For extends App {
  // java:
  // for (c; c; c) {
  //   sout(c)
  // }

  for (i <- 1 to 5) println(i)
  for (i <- 1 until 5) println(i)

  val l = List(1, 2, 3, 4, 5)
  for (i <- l) println(i)

  val l1 = l.map(_ * 2) // 2, 4, 6, 8, 10

  for {
    i <- l
    j <- l1
  } println(s"$i $j")

  for (i <- l) {
    val res = i * i
    println(res)
  }

  // eq l1
  val l1V2: List[Int] = for (i <- l) yield i*2 // without yield => Unit
  l1V2.foreach(println)

  val userBase  = List(("Вася", 28), ("Юля", 33), ("Катя", 44), ("Петя", 23))

  val filtered  = for (user <- userBase if user._2 >= 20 && user._2 < 30) yield user._1
  // eq
  val filteredF = userBase.filter(_._2 >= 20).filter(_._2 < 30)

  def f(n: Int, sum: Int): Seq[(Int, Int)] =
    for {
      i <- 0 until n
      j <- 0 until n if i + j == sum
    } yield (i, j)
  println(f(10, 10))

}

object While extends App {
  // java:
  // var i = 0;
  // while (i < 3) {
  //  sout(i);
  //  i += 1;
  // }
  var i = 0
  while (i < 3) {
    println(i)
    i += 1
  }

  do {
    println(i)
    i += 1
  } while (i < 0)
}

object Rec extends App {
  def factorial(n: BigInt): BigInt = {
    if (n==0) 1
    else n * factorial(n - 1)
  }
  println(factorial(BigInt(10000))) // ce
}

object TailRec extends App {
  def factorial(n: BigInt): BigInt = {
    import scala.annotation.tailrec
    @tailrec // if def != tailrec => ce
    def factorialTail(acc: BigInt, n: BigInt): BigInt = {
      if (n == 0) acc
      else factorialTail(n * acc, n - 1)
    }
    factorialTail(1, n)
  }
  println(factorial(BigInt(10000)))
}

object TryMe extends App {
  // java:
  //        try {
  //          //some code
  //        } catch (Exception e) {
  //            e.printStackTrace();
  //        } catch (IllegalAccessError e) {
  //            e.printStackTrace();
  //        } catch (Exception | IllegalAccessError e) { //or
  //            e.printStackTrace();
  //        } finally {
  //          //some code
  //        }

  try {
    val res = 1 / 0
    println(res)
  } catch {
    case t: Throwable =>
      println("Throwable!")
      t.printStackTrace()
    case e: Exception =>
      println(e.getMessage)
      e.printStackTrace()
  } finally {
    println("finally block")
  }
}

object BreakMe extends App {

  import scala.util.control.Breaks.break
  import scala.util.control.Breaks.breakable

  val search = "peter piper picked a peck of pickled peppers"

  // bad code
  var numPs = 0
  for (i <- 0 until search.length) {
    breakable {
      if (search.charAt(i) != 'p') {
        break
      } else {
        numPs += 1
      }
    }
  }
  println(s"Found $numPs p's in the string.")

  // bad code
  var numPs1 = 0
  def x123: Int = {
    for (i <- 0 until search.length) {
        if (search.charAt(i) != 'p') {
          // do nothing
        } else {
          numPs1 += 1
        }
    }
    numPs1
  }
  println(s"Found $x123 p's in the string.")

  // good code
  val numPs2 = search.count(_ == 'p')
  println(s"Found $numPs2 p's in the string.")

  // val x: String = ??? // can be null, cause java str

  // Option[T] => Some(1) or None
  // val x: Option[String] = ??? // can be None or Some(str), cause Option
}
