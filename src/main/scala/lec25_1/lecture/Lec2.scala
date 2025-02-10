package lec25_1.lecture

import scala.util.control.Breaks.{break, breakable}

object Lec2IfElse extends App {
  // java
  //  if (1 == 1) {
  //    return 1;
  //  } else  {
  //    return 2;
  //  }

  val x = if (1 == 1) {
    1
  } else if (2 == 2) {
    2
  } else 3

  val withOutElse /* : Any */ = if (2 == 2) 1 // else ()

  // var x = bool ? 1 : 2
  val x1 = if (true) 1 else 2 // тернарка
}

object Lec2Lazy extends App {
  lazy val lazyEval = {
    println("Thread sleep")
    Thread.sleep(3000)
    1
  }
  def sleepless = {
    println("sleepless")
    2
  }
  println("1 if eval")
  if (1 == 1) sleepless else lazyEval
  println("2 if eval")
  if (2 != 2) sleepless
  println("3 if eval")
  if (3 != 3) lazyEval else sleepless
}

object Lec2For extends App {
  // java
  // for (int i = 0; i< 10; i++)
  val x  = 1 until 10 // for (int i = 0; i< 10; i++)
  val x1 = 1 to 10    // for (int i = 0;  i<= 10; i++)
  val x2 = for (i <- 1 until 10) yield i * 2;
  //  println(x2)
  //  for (i <- 1 to 10; j <- 1 to 10 if j > 5 ) println(s"i=$i j = $j")
  for {
    i <- 1 to 10
    if i > 5
    j <- 1 to 10
  } {
    val x = i * 2
    println(s"i=$i j = $j x = $x")
  }
  //  for (i <- 1 until 10) println(i)

  x.foreach(println)
  val x2Alt = x.map(v => v * 2)
}

object Lec2While extends App {
  var x = 0
  while (x < 3) {
    println(x)
    x += 1
  }

  var x1 = 0
  do {
    println(x1)
    x1 += 1
  } while (x1 < 0)
}

object Lec2Recursion extends App {
  // 5! = 1 * 2* 3 * 4 *5 = 120

  def factorial1(n: BigInt): BigInt = {
    if (n <= 0) 1
    else n * factorial1(n - 1)
  }

  import scala.annotation.tailrec
  def factorial2(n: BigInt): BigInt = {

    @tailrec
    def calcFactorial(acc: BigInt, n: BigInt): BigInt = {
      if (n == 0) acc
      else calcFactorial(n * acc, n - 1)
    }

    calcFactorial(1, n)
  }

  println(factorial2(10_000).toString().length)
}

object Lec2Try extends App {
  // java
  /*
  try {

  } catch (MathException e){

  } catch (Exception e) {
  }
   */
  try {
    val x = 1 / 0
    println(x)
  } catch {
    case e: Exception =>
      println(e)
    case e: Throwable =>
      println(e)
  } finally {
    println("finally")
  }
}

object Lec2Break extends App {
  val search = "OVer............"
  var num    = 0
  //  continue java
  for (i <- 0 until search.length) {
    breakable {
      if (search.charAt(i) != '.') {
        break
      } else {
        num += 1
      }
    }
  }
  val x1 = search.count(c => c == '.')
  println(s" Found $x1 ..")
}
