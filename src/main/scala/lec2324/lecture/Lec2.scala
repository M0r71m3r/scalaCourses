package lec2324.lecture

import lec2324.magicZone.ImplicitForPrint

import scala.annotation.tailrec

object UnapplyTuple extends App with ImplicitForPrint {
  val (x, y, z) = (1, 2, 3)
  x.print // 1
  y.print // 2
  z.print // 3
}

object StrMargin extends App with ImplicitForPrint {
  val str1 =
    """
      |Aaaa
      |Bbbb
      |"""
  str1.print // коряво
  val str2 =
    """
      |Cccc
      |Dddd
      |""".stripMargin //ok
  str2.print
}

object DefaultDefParam extends App with ImplicitForPrint {
  def sayHello(to: String = "World!"): Unit = s"Hello $to".print
  sayHello() // Hello World!
  sayHello("Scala") // Hello Scala
}

object IfElse extends App with ImplicitForPrint {
  //java:
  // if (1==1) {
  //   return 1-2;
  // } else return 1-3;

  // if (check) something else somethingElse
  val x = 1
  val y = 2
  val z = 3

  def f(f: String = "f"): String = {
    f.print
    f
  }

  def g(g: String = "g"): String = {
    g.print
    g
  }

  def h(h: String = "h"): String = {
    h.print
    h
  }

  if (x == y) f() // sugar => else ()
  if (x == y) {
    f()
  } else if (y == z) {
    g()
  } else {
    h()
  }

  lazy val lazyEval = {
    "Thread sleep".print
    Thread.sleep(6000)
    1
  }
  val sleepless = {
    "sleepless".print
    2
  }
  "1 if eval".print
  if (1 == 1) sleepless else lazyEval
  "2 if eval".print
  if (false) sleepless else lazyEval
  "3 if eval".print
  if (3 == 3) lazyEval else sleepless
  //sleepless
  //1 if
  //2 if
  //Thread sleep
  //3 if

  val res = if (x == y) f() else g() // without else => res: Any, cause sugar else ()
  res.print
  //java:
  // x == y ? v1 : v2

}

object For extends App with ImplicitForPrint {
  //java:
  // for (statement1; statement2; statement3) {
  //     // code
  // }

  for (i <- 1 to 5) i.print
  for (i <- 1 until 5) i.print

  val l = List(1, 2, 3, 4, 5)
  for (i <- l) i.print

  val l2 = l.map(_ * 2)
  for { // or for(i <- l;j <- l2)
    i <- l
    j <- l2
  } s"i $i j $j".print // (1, 1), (1, 2), (1, 3) (1, 4) ... (2, 1) .. (3, 4) .. etc

  for (i <- l) {
    val res = i * i
    res.print
  }

  val l3: List[Int] = for (i <- l) yield i * 2 //eq l.map(_ * 2)

  val userBase = List(
    ("Вася", 28),
    ("Юля", 33),
    ("Катя", 44),
    ("Петя", 23)
  )

  val somethings: List[String] =
    for (user <- userBase if user._2 >= 20 && user._2 < 30)
      yield user._1
  somethings.print //Вася, Петя

  def f(n: Int, sum: Int) =
    for {
      i <- 0 until n
      j <- 0 until n if i + j == sum
    } yield (i, j)

  f(10, 10).print // (1, 9) (2, 8) (3, 7) (4, 6) (5, 5) (6, 4) (7, 3) (8, 2) (9, 1)
}

object While extends App with ImplicitForPrint {

  //java:
  // var i = 0;
  // while (i < 3) {
  //     sout(i);
  //     i += 1;
  // }
  var i = 0
  while (i < 3) {
    i.print
    i += 1
  }

  var i2 = 0
  do {
    i2.print
    i2 += 1
  } while (i2 < 0)

}

object Rec extends App with ImplicitForPrint {
  // 5! = 1 * 2 * 3 * 4 * 5 = 120

  //@tailrec => recursive call not in tail position => compile error
  def factorial1(n: BigInt): BigInt = {
    if (n == 0) 1
    else n * factorial1(n - 1)
  }
  factorial1(5).print
//  factorial1(10000) //10000 => java.lang.StackOverflowError

//  def factorial2(n: BigInt): BigInt = {
//    @tailrec // only on tail recursive function
//    def facHelp(acc: BigInt, n: BigInt): BigInt = {
//      if (n == 0) acc
//      else facHelp(n * acc, n - 1)
//    }
//    facHelp(1, n)
//  }

  @tailrec // only on tail recursive function
  def facHelp(acc: BigInt, n: BigInt): BigInt = {
    if (n == 0) acc
    else facHelp(n * acc, n - 1)
  }
  facHelp(1, 10000).print
}

object TryMe extends App with ImplicitForPrint {
  //java:
  // try {
  //   //some code
  // } catch (Exception e) {
  //     e.printStackTrace();
  // } catch (IllegalAccessError e) {
  //     e.printStackTrace();
  // } catch (Exception | IllegalAccessError e) { //or
  //     e.printStackTrace();
  // } finally {
  //   //some code
  // }

  def div(x: Int, y: Int): Int = x / y // div by 0 => throw exception

  try {
    val res = div(1, 0)
    res.print
  } catch {
    case ae: ArithmeticException =>
      "Oh no, div by zero!".print
      ae.getMessage.print
      ae.printStackTrace()
    case e: Exception =>
      e.getMessage.print
      e.printStackTrace()
    case t: Throwable =>
      "Throwable".print
      t.print
  } finally {
    "finally block".print
  }

}

object BreakMe extends App with ImplicitForPrint {

  import scala.util.control.Breaks.break
  import scala.util.control.Breaks.breakable

  val search = "peter piper picked a peck of pickled peppers"
  var numPs = 0
  for (i <- 0 until search.length) {
    breakable {
      if (search.charAt(i) != 'p') {
        break()
      } else {
        numPs += 1
      }
    }
  }
  s"Found $numPs p's in the string.".print

  val countPs = search.count(_ == 'p')
  s"Found $countPs p's in the string.".print
}
