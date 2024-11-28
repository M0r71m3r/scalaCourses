package lec2425.lecture

import java.util.concurrent.Executors
import scala.concurrent.{Await, ExecutionContext, ExecutionContextExecutor, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Random, Success, Try}

object JustFuture extends App {

  val pool                                  = Executors.newFixedThreadPool(4)
  implicit val ec: ExecutionContextExecutor = ExecutionContext.fromExecutor(pool)
  val f                                     = Future { 42 / 0 }

  f.onComplete {
    case Failure(e) =>
      println("------------")
      println(e.getMessage)
      e.printStackTrace()
      println("------------")
    case Success(value) =>
      println(value * 2)
  }
  try { Await.result(f, Duration.Inf) }
  finally {
    pool.shutdown()
  }
}

object CreateFuture extends App {
  val pool                                  = Executors.newFixedThreadPool(4)
  implicit val ec: ExecutionContextExecutor = ExecutionContext.fromExecutor(pool)

  val f  = Future { 42 / 0 }
  val f1 = Future.successful("1")
  val f2 = Future.failed(new Exception("2"))

  Await.result(f, Duration.Inf)
  pool.shutdown()
}

object WithoutFutureCoffee extends App {
  def grind(beans: String): Try[String]                      = Try { "grind!!!" }
  def heatWater(water: String): Try[String]                  = Try { "hot!!!" }
  def forthMilk(milk: String): Try[String]                   = Try { "forthMilk!!!" }
  def doEspresso(coffee: String, water: String): Try[String] = Try { "espresso done" }
  def combine(espresso: String, milk: String): Try[String]   = Try { "cappuccino done" }

  val x = for {
    g  <- grind("arabica")
    w  <- heatWater("0")
    m  <- forthMilk("milk")
    es <- doEspresso(g, w)
    c  <- combine(es, m)
  } yield c

}

object Methods {
  def eval(s: String): String = {
    val r = Random.nextInt(2000)
    Thread.sleep(Random.nextInt(2000))
    val cm = System.currentTimeMillis() / 1000
    println(s"$s $r $cm")
    s
  }
  def grind(beans: String)(implicit executor: ExecutionContext): Future[String] = Future {
    eval(beans)
  }
  def heatWater(water: String)(implicit executor: ExecutionContext): Future[String] = Future {
    eval(water)
  }
  def forthMilk(milk: String)(implicit executor: ExecutionContext): Future[String] = Future {
    eval(milk)
  }
  def doEspresso(coffee: String, water: String)(implicit executor: ExecutionContext): Future[String] = Future {
    eval(coffee + " " + water)
  }
  def combine(espresso: String, milk: String)(implicit executor: ExecutionContext): Future[String] = Future {
    eval(espresso + " " + milk)
  }
}

object WithFutureCoffeeViaFor extends App {
  val pool                                  = Executors.newFixedThreadPool(4)
  implicit val ec: ExecutionContextExecutor = ExecutionContext.fromExecutor(pool)

  val f = for {
    g  <- Methods.grind("arabica") // Future стартанула
    w  <- Methods.heatWater("0")
    m  <- Methods.forthMilk("milk")
    es <- Methods.doEspresso(g, w)
    c  <- Methods.combine(es, m)
  } yield c
  Await.result(f, Duration.Inf)
  pool.shutdown()
}

object WithFutureCoffeeViaForAsync extends App {
  val pool                                  = Executors.newFixedThreadPool(4)
  implicit val ec: ExecutionContextExecutor = ExecutionContext.fromExecutor(pool)

  val gf = Methods.grind("arabica") // Future стартанула
  val wf = Methods.heatWater("0")
  val mf = Methods.forthMilk("milk")

  val f = for {
    g  <- gf
    w  <- wf
    m  <- mf
    es <- Methods.doEspresso(g, w)
    c  <- Methods.combine(es, m)
  } yield c

//  val f2 = gf.flatMap { g =>
//    wf.flatMap { w =>
//      mf.flatMap { m =>
//        Methods.doEspresso(g, w).flatMap { es =>
//          Methods.combine(es, m)
//        }
//      }
//    }
//  }

  Await.result(f, Duration.Inf)
  pool.shutdown()
}

object MapMe extends App {
  val pool                                  = Executors.newFixedThreadPool(4)
  implicit val ec: ExecutionContextExecutor = ExecutionContext.fromExecutor(pool)

  val f = Future {
    println(1)
    42
  }.map { _ =>
    Future {
      Thread.sleep(5000)
      println("2")
      "1"
    }
  }
  // await на внешней future не дает await на внутренней future
  // используйте flatMap
  Await.result(f, Duration.Inf)
  println("3")
  pool.shutdown()
}

object FutureSequence extends App {
  val pool                                  = Executors.newFixedThreadPool(4)
  implicit val ec: ExecutionContextExecutor = ExecutionContext.fromExecutor(pool)

  val ids: List[Future[Int]] = (1 to 100).toList.map(Future(_))
  val x: Future[List[Int]]   = Future.sequence(ids)

  val res = Await.result(x, Duration.Inf)
  println(res)
  pool.shutdown()
}

object FutureTraverseV1 extends App {
  val pool                                  = Executors.newFixedThreadPool(20)
  implicit val ec: ExecutionContextExecutor = ExecutionContext.fromExecutor(pool)

  val ids = (1 to 100).toList

  val x: Future[List[Int]] = Future.traverse(ids) { id =>
    Future {
      val sleep = Random.nextInt(2000)
      println(id + s" sleep = $sleep " + Thread.currentThread().getName)
      id * 2
    }
  }
  val res = Await.result(x, Duration.Inf)
  println(res)
  pool.shutdown()
}

object FutureTraverseV2 extends App {

  val pool                                  = Executors.newFixedThreadPool(20)
  implicit val ec: ExecutionContextExecutor = ExecutionContext.fromExecutor(pool)

  val ids = (1 to 100).toList

  val x = Future.traverse(ids.grouped(20)) { ids =>
    Future {
      ids.map { id =>
        val sleep = Random.nextInt(2000)
        println(id + s" sleep = $sleep " + Thread.currentThread().getName)
        id * 2
      }
    }
  }
  val res = Await.result(x, Duration.Inf)
  println(res.toList.flatten)
  pool.shutdown()

}
