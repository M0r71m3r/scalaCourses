package lec2324.lecture

import lec2324.magicZone.ImplicitForPrint

import java.util.concurrent.Executors
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.util.Failure
import scala.util.Random
import scala.util.Success
import scala.util.Try

object JustFuture extends App with ImplicitForPrint {
  //success
  //failure

  val f1 = Future.successful("123")
  val f2 = Future.failed(new Exception("Oops!"))

  val pool                      = Executors.newFixedThreadPool(2)
  implicit val executionContext = ExecutionContext.fromExecutor(pool)

  val f3 = Future {
    42
  }

  Await.result(f3, Duration.Inf).print
  pool.shutdown()
}
object FutureOnComplete extends App with ImplicitForPrint {

  val pool                      = Executors.newFixedThreadPool(2)
  implicit val executionContext = ExecutionContext.fromExecutor(pool)

  val f3 = Future {
    42 / 0
  }
  f3.onComplete {
    case Failure(exception) =>
      exception.getMessage.print
    case Success(value) =>
      (value * value).print
  }

  Await.result(f3, Duration.Inf).print
  pool.shutdown()

}
object FutureCoffee extends App with ImplicitForPrint {

  //Помолем кофейные зёрна
  //Вскипятим воду
  //Сварим эспрессо, смешав молотые зёрна с кипятком
  //Взобьём молоко
  //Добавим молоко в эспрессо и капучино готов
  val pool                      = Executors.newFixedThreadPool(2)
  implicit val executionContext = ExecutionContext.fromExecutor(pool)

  // Синонимы
  type CoffeeBeans  = String
  type GroundCoffee = String

  case class Water(tmp: Int)

  type Milk        = String
  type FrothedMilk = String
  type Espresso    = String
  type Cappuccino  = String

  def grind(beans: CoffeeBeans): GroundCoffee                           = s"ground coffee of $beans"
  def heatWater(w: Water): Water                                        = w.copy(tmp = 85)
  def frothMilk(milk: Milk): FrothedMilk                                = s"frothed $milk"
  def brew(coffee: GroundCoffee, water: Water): Espresso                = "espresso"
  def combine(espresso: Espresso, frothedMilk: FrothedMilk): Cappuccino = "cappuccino"

  case class GrindingException(msg: String)     extends Exception(msg)
  case class FrothingException(msg: String)     extends Exception(msg)
  case class WaterBoilingException(msg: String) extends Exception(msg)
  case class BrewingException(msg: String)      extends Exception(msg)

  def prepareCapuccino(): Try[Cappuccino] = for {
    ground <- Try(grind("arabica beans"))
    water  <- Try(heatWater(Water(25)))
    es     <- Try(brew(ground, water))
    milk   <- Try(frothMilk("just milk"))
  } yield combine(es, milk)

  def grindV2(beans: CoffeeBeans): Future[GroundCoffee] = Future {
    "start grindV2".print
    Thread.sleep(Random.nextInt(2000))
    "end grindV2".print
    s"ground coffee of $beans"
  }

  def heatWaterV2(w: Water): Future[Water] = Future {
    "start heatWaterV2".print
    Thread.sleep(Random.nextInt(2000))
    "end heatWaterV2".print
    w.copy(tmp = 85)
  }

  def frothMilkV2(milk: Milk): Future[FrothedMilk] = Future {
    println("frothMilkV2 milk frothing system up")
    Thread.sleep(Random.nextInt(2000))
    println("frothMilkV2 milk frothing system down")
    s"frothed $milk"
  }

  def brewV2(coffee: GroundCoffee, heatedWater: Water): Future[Espresso] = Future {
    println("brewV2 happy brewing :)")
    Thread.sleep(Random.nextInt(2000))
    println("brewV2 it's brewed!")
    "espresso"
  }

  def tmpOkay(water: Water): Future[Boolean] = Future {
    (80 to 90).contains(water.tmp)
  }

  val itsOkayV1 = heatWaterV2(Water(25))
    .flatMap(w => tmpOkay(w))

  val itsOkay: Future[Boolean] = for {
    heat  <- heatWaterV2(Water(25))
    itsOk <- tmpOkay(heat)
  } yield itsOk

  def prepareCapuccinoSeq() = {
    for {
      ground <- grindV2("arabica beans")
      water  <- heatWaterV2(Water(25))
      es     <- brewV2(ground, water)
      milk   <- frothMilkV2("just milk")
    } yield combine(es, milk)
  }
  def prepareCapuccinoAsync() = {
    val groundF = grindV2("arabica beans")
    val waterF  = heatWaterV2(Water(25))
    val milkF   = frothMilkV2("just milk")
    for {
      ground <- groundF
      water  <- waterF
      es     <- brewV2(ground, water)
      milk   <- milkF
    } yield combine(es, milk)
  }

//  Await.result(prepareCapuccinoSeq, Duration.Inf)
  Await.result(prepareCapuccinoAsync, Duration.Inf)

  pool.shutdown()
}
object FutureSequence extends App with ImplicitForPrint {
  val pool                      = Executors.newFixedThreadPool(2)
  implicit val executionContext = ExecutionContext.fromExecutor(pool)

  val ids: List[Future[Int]] = (1 to 100).toList.map(id => Future.successful(id))
  val idsRes = ids.map { iF =>
    val x = iF.map { _ + 2 }
    Await.result(x, Duration.Inf)
  }

  val idsF: Future[List[Int]] = Future.sequence(ids).map { l => l.map(_ + 2) }
  val idsFRes                 = Await.result(idsF, Duration.Inf)

  pool.shutdown()
}
object FutureTraverseV1 extends App with ImplicitForPrint {
  val pool = Executors.newFixedThreadPool(512)
  // with 10 => 24 sec
  // with 20 => 15 sec
  // with 50 => 10 sec
  // with 256 => 7 sec
  // with 512 => 7 sec
  implicit val executionContext = ExecutionContext.fromExecutor(pool)

  val ids: List[Int] = (1 to 100).toList

  val x2 = Future.traverse(ids) { id =>
    Future {
      val sleep = 2000 //Random.nextInt(10000)
      s"$id, sleep = $sleep, ${Thread.currentThread().getName}".print
      Thread.sleep(sleep)
      id * 2
    }
  }
  Await.result(x2, Duration.Inf)
  pool.shutdown()
}

object FutureTraverseV2 extends App with ImplicitForPrint {
  val pool                      = Executors.newFixedThreadPool(5)
  implicit val executionContext = ExecutionContext.fromExecutor(pool)

  val ids: List[Int] = (1 to 100).toList

  val x2 = Future.traverse(ids.grouped(20)) { ids =>
    Future {
      ids.map { id =>
        val sleep = 2000 //Random.nextInt(10000)
        s"$id, sleep = $sleep, ${Thread.currentThread().getName}".print
        Thread.sleep(sleep)
        id * 2
      }
    }
  }
  Await.result(x2, Duration.Inf)
  pool.shutdown()
}
