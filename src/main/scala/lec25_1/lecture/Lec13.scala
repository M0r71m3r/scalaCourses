package lec25_1.lecture

import java.util.concurrent.Executors
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Random, Success, Try}

object JustFuture extends App {

  val pool        = Executors.newFixedThreadPool(4)
  implicit val ec = ExecutionContext.fromExecutor(pool)

//  val f2: Future[Int] = Future(1+1)
  val f3: Future[Int] = Future {
    println("start")
    Thread.sleep(5000)
    1 + 1
  }
  val res = Await.result(f3, Duration.Inf)
  pool.shutdown()
  println(res)
}

object CreateFuture extends App {
  val pool        = Executors.newFixedThreadPool(4)
  implicit val ec = ExecutionContext.fromExecutor(pool)

//  val f: Future[String]  = Future.successful("qe")
//  val f1: Future[String] = Future.failed[String](new Exception("oops"))

  val f = Future {
    42 / 0
  }
  f.onComplete {
    case Failure(exception) =>
      exception.printStackTrace()
    case Success(value) =>
      println(s"value = ${value}")
  }
  Try {
    Await.result(f, Duration.Inf)
  }
  pool.shutdown()
}

object WithoutFutureCoffee extends App {
  // Определим осмысленные синонимы:
  type CoffeeBeans  = String // Кофейные зерна
  type GroundCoffee = String // Молотый кофе

  case class Water(temperature: Int) // no com

  type Milk        = String // no com
  type FrothedMilk = String // Взбитое молоко
  type Espresso    = String // no com
  type Cappuccino  = String // no com

  // Методы-заглушки для отдельных шагов алгоритма:
  def grind(beans: CoffeeBeans): GroundCoffee = {
    val str = s"ground coffee of $beans"
    println(str)
    str
  }
  def heatWater(water: Water): Water = {
    println("heatWater")
    water.copy(temperature = 85)
  }
  def frothMilk(milk: Milk): FrothedMilk = {
    println("frothMilk")
    s"frothed $milk"
  }
  def brew(coffee: GroundCoffee, heatedWater: Water): Espresso = {
    println("espresso")
    "espresso"
  }
  def combine(espresso: Espresso, frothedMilk: FrothedMilk): Cappuccino = {
    println("cappuccino")
    "cappuccino"
  }

  // Исключения, на случай если что-то пойдёт не так
  // (они понадобятся нам позже):
  case class GrindingException(msg: String) extends Exception(msg)

  case class FrothingException(msg: String) extends Exception(msg)

  case class WaterBoilingException(msg: String) extends Exception(msg)

  case class BrewingException(msg: String) extends Exception(msg)

  // последовательно выполним алгоритм:
  def prepareCappuccino(): Try[Cappuccino] = for {
    ground   <- Try(grind("arabica beans"))
    water    <- Try(heatWater(Water(25)))
    espresso <- Try(brew(ground, water))
    foam     <- Try(frothMilk("milk"))
  } yield combine(espresso, foam)
  prepareCappuccino()
}

object WithFutureCoffeeViaFor extends App {
  val pool        = Executors.newFixedThreadPool(4)
  implicit val ec = ExecutionContext.fromExecutor(pool)

  // Определим осмысленные синонимы:
  type CoffeeBeans  = String // Кофейные зерна
  type GroundCoffee = String // Молотый кофе

  case class Water(temperature: Int) // no com

  type Milk        = String // no com
  type FrothedMilk = String // Взбитое молоко
  type Espresso    = String // no com
  type Cappuccino  = String // no com

  // Исключения, на случай если что-то пойдёт не так
  // (они понадобятся нам позже):
  case class GrindingException(msg: String) extends Exception(msg)

  case class FrothingException(msg: String) extends Exception(msg)

  case class WaterBoilingException(msg: String) extends Exception(msg)

  case class BrewingException(msg: String) extends Exception(msg)

  def grind(beans: CoffeeBeans): Future[GroundCoffee] = Future {
    println("start grinding...")
    Thread.sleep(Random.nextInt(2000))
    if (beans == "baked beans") throw GrindingException("are you joking?")
    println("finished grinding...")
    s"ground coffee of $beans"
  }

  def heatWater(water: Water): Future[Water] = Future {
    println("heating the water now")
    Thread.sleep(Random.nextInt(2000))
    println("hot, it's hot!")
    water.copy(temperature = 85)
  }

  def frothMilk(milk: Milk): Future[FrothedMilk] = Future {
    println("milk frothing system up")
    Thread.sleep(Random.nextInt(2000))
    println("milk frothing system down")
    s"frothed $milk"
  }

  def brew(coffee: GroundCoffee, heatedWater: Water): Future[Espresso] = Future {
    println("happy brewing :)")
    Thread.sleep(Random.nextInt(2000))
    println("it's brewed!")
    "espresso"
  }

  def combine(espresso: Espresso, foam: FrothedMilk): Cappuccino = {
    println("cappuccino")
    "cappuccino"
  }

  def prepareCappuccinoSequentially(): Future[Cappuccino] = {
    for {
      ground   <- grind("arabica beans")
      water    <- heatWater(Water(20))
      foam     <- frothMilk("milk")
      espresso <- brew(ground, water)
    } yield combine(espresso, foam)
  }
  Await.result(prepareCappuccinoSequentially(), Duration.Inf)
  pool.shutdown()
}

object WithFutureCoffeeViaForAsync extends App {
  val pool        = Executors.newFixedThreadPool(4)
  implicit val ec = ExecutionContext.fromExecutor(pool)

  // Определим осмысленные синонимы:
  type CoffeeBeans  = String // Кофейные зерна
  type GroundCoffee = String // Молотый кофе

  case class Water(temperature: Int) // no com

  type Milk        = String // no com
  type FrothedMilk = String // Взбитое молоко
  type Espresso    = String // no com
  type Cappuccino  = String // no com

  // Исключения, на случай если что-то пойдёт не так
  // (они понадобятся нам позже):
  case class GrindingException(msg: String) extends Exception(msg)

  case class FrothingException(msg: String) extends Exception(msg)

  case class WaterBoilingException(msg: String) extends Exception(msg)

  case class BrewingException(msg: String) extends Exception(msg)

  def grind(beans: CoffeeBeans): Future[GroundCoffee] = Future {
    println("start grinding...")
    Thread.sleep(Random.nextInt(2000))
    if (beans == "baked beans") throw GrindingException("are you joking?")
    println("finished grinding...")
    s"ground coffee of $beans"
  }

  def heatWater(water: Water): Future[Water] = Future {
    println("heating the water now")
    Thread.sleep(Random.nextInt(2000))
    println("hot, it's hot!")
    water.copy(temperature = 85)
  }

  def frothMilk(milk: Milk): Future[FrothedMilk] = Future {
    println("milk frothing system up")
    Thread.sleep(Random.nextInt(2000))
    println("milk frothing system down")
    s"frothed $milk"
  }

  def brew(coffee: GroundCoffee, heatedWater: Water): Future[Espresso] = Future {
    println("happy brewing :)")
    Thread.sleep(Random.nextInt(2000))
    println("it's brewed!")
    "espresso"
  }

  def combine(espresso: Espresso, foam: FrothedMilk): Cappuccino = {
    println("cappuccino")
    "cappuccino"
  }

  def prepareCappuccinoSequentially(): Future[Cappuccino] = {
    val groundCoffee = grind("arabica beans")
    val heatWater1   = heatWater(Water(20))
    val frothMilk1   = frothMilk("milk")
    for {
      ground   <- groundCoffee
      water    <- heatWater1
      foam     <- frothMilk1
      espresso <- brew(ground, water)
    } yield combine(espresso, foam)
  }
  Await.result(prepareCappuccinoSequentially(), Duration.Inf)
  pool.shutdown()
}

object MapMe extends App {
  val pool        = Executors.newFixedThreadPool(4)
  implicit val ec = ExecutionContext.fromExecutor(pool)

  val f: Future[Future[Int]] = Future {
    println("123")
    123
  }.map { i =>
    Future {
      Thread.sleep(5000)
      println("123451")
      i * 2
    }
  }
  // await на внешней фьюче не дает await на внутренней фьюче
  // используйте flatmap => Future[Future[Int]] => Future[Int]
  Await.result(f, Duration.Inf)
  println(3)
  pool.shutdown()
}

object FutureSequence extends App {
  val pool        = Executors.newFixedThreadPool(4)
  implicit val ec = ExecutionContext.fromExecutor(pool)

  val ids: List[Future[Int]] = (1 to 100).toList.map { i =>
    Future {
      Thread.sleep(Random.nextInt(1000))
      i
    }
  }
  val x: Future[List[Int]] = Future.sequence(ids)

  val res = Await.result(x, Duration.Inf)
  res.foreach(println)

  pool.shutdown()
}

object FutureTraverseV1 extends App {
  val pool        = Executors.newFixedThreadPool(20)
  implicit val ec = ExecutionContext.fromExecutor(pool)

  val ids = (1 to 100).toList

  val x: Future[List[Int]] = Future.traverse(ids) { i =>
    Future {
      val sleep = Random.nextInt(2000)
      println(i + s" sleep = $sleep " + Thread.currentThread().getName)
      i
    }
  }
  val res = Await.result(x, Duration.Inf)
  res.foreach(println)

  pool.shutdown()
}

object FutureTraverseV2 extends App {
  val pool        = Executors.newFixedThreadPool(20)
  implicit val ec = ExecutionContext.fromExecutor(pool)

  val ids = (1 to 2000).toList

  val x: Future[Iterator[List[Int]]] = Future.traverse(ids.grouped(200)) { li =>
    Future {
      li.map { i =>
        val sleep = Random.nextInt(2000)
        Thread.sleep(sleep)
        println(i + s" sleep = $sleep " + Thread.currentThread().getName)
        i
      }
    }
  }
  val res = Await.result(x, Duration.Inf)
  println(res.toList.flatten)

  pool.shutdown()
}

object OptFlatmapFut extends App {
  val pool        = Executors.newFixedThreadPool(20)
  implicit val ec = ExecutionContext.fromExecutor(pool)

//  val x = Option(1).flatMap { i =>
//    Future(i)
//  }

  val x11 = for {
    x <- Future(Option(1))
    y <- Future(x)
    x1 <- Future(Option(1))
  } yield y


  pool.shutdown()
}


