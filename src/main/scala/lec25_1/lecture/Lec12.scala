package lec25_1.lecture

import scala.beans.BeanProperty

object Lec12Generic extends App {
  trait Animal {
    def voice(): Unit
  }

  // Animal -> Cat
  // Animal -> Dog
  case class Cat() extends Animal {
    override def voice(): Unit = println("I am Mya")
  }

  case class Dog() extends Animal {
    override def voice(): Unit = println("I am Gav")
  }

  class Box[T](value: T) {
    def get: T = value
  }

  val cat    = Cat()
  val dog    = Dog()
  val boxCat = new Box[Cat](cat)
  boxCat.get.voice()
  val boxDog = new Box[Dog](dog)
  boxDog.get.voice()

  //  val boxAnimal: Box[Animal] = boxCat // CE

}

object Lec12ImplOption extends App {
  implicit class AnyToOption[T](value: T) {
    def asOption: Option[T] = Option(value)
  }

  val strNull: String = null
  val str: String     = "abc"
  println(strNull.asOption)
  println(str.asOption)
}

object Lec12Cov {
  trait Animal {
    def voice(): Unit
  }

  // Animal -> Cat
  // Animal -> Dog
  case class Cat() extends Animal {
    override def voice(): Unit = println("I am Mya")
  }

  case class Dog() extends Animal {
    override def voice(): Unit = println("I am Gav")
  }

  val cat = Cat()
  val dog = Dog()

  class BoxV2[+T](value: T) {
    def get: T = value
    //    def put(v: T): T = v
  }

  // Animal -> Cat; Box[Animal] -> Box[Cat]
  val boxCatV2                 = new BoxV2[Cat](cat)
  val boxDogV2                 = new BoxV2[Dog](dog)
  val boxAnimal: BoxV2[Animal] = boxCatV2

}

object Lec12Contr extends App {
  trait Animal {
    def voice(): Unit
  }

  // Animal -> Cat
  // Animal -> Dog
  case class Cat() extends Animal {
    override def voice(): Unit = println("I am Mya")
  }

  case class Dog() extends Animal {
    override def voice(): Unit = println("I am Gav")
  }

  val cat = Cat()
  val dog = Dog()

  // Animal -> Cat; Box[Cat] -> Box[Animal]
  class BoxV3[-T](value: T) {
    def serialize(value: T): String = value.toString
  }

  val boxCatV3              = new BoxV3[Cat](cat)
  val boxDogV3              = new BoxV3[Dog](dog)
  val boxDog2V3: BoxV3[Dog] = new BoxV3[Animal](cat)
}

object Lec12Upper extends App {
  // T <: A Upper Type Bounds
  // T >: A Lower Type Bounds
  sealed trait Box[+A] {

    def map[B](f: A => B): Box[B] = {
      if (isEmpty) BoxNone else BoxSome(f(this.get))
    }

    def flatMap[B](f: A => Box[B]): Box[B] = {
      if (isEmpty) BoxNone else f(this.get)
    }

    def contains[B >: A](value: B): Boolean = {
      nonEmpty && this.get == value
    }

    def withFilter(p: A => Boolean): Box[A] = {
      if (nonEmpty && p(this.get)) this else BoxNone
    }

    def get: A

    def nonEmpty: Boolean = !isEmpty

    def isEmpty: Boolean = this eq BoxNone
  }

  case class BoxSome[+A](value: A) extends Box[A] {

    override def get: A = value
  }

  case object BoxNone extends Box[Nothing] {

    override def get: Nothing = throw new RuntimeException("NONE!!!")
  }

  object Box {
    def apply[T](value: T): Box[T] = {
      if (value == null) BoxNone else BoxSome(value)
    }
  }

  for {
    c <- Box.apply[String](null) if c.nonEmpty
    d <- Box(c)
  } yield d

  trait Base
  class A   extends Base
  class B() extends A

  val a   = new A()
  val box = Box(a)
  box.contains(10)

  val lb = scala.collection.mutable.ListBuffer("3", "4", "5")
  //  val lb2: scala.collection.mutable.ListBuffer[Any] = lb
  //  lb2(0) = 3

  val l: List[Any] = List(3, 4, 5)
  println(true :: l)
  println(true :: l)
}

object Lec12TypedClass extends App {
  case class Snils(snils: String)
  case class Inn(inn: String)

  case class Person(id: Long, name: String, @BeanProperty var age: Int, @BeanProperty var inn: Inn, @BeanProperty var snils: Snils)

  trait NeedToValidate[T] {
    def validate(v: T): T
  }

  object ValidateTypeClassSyntax {
    implicit class NeedToValidOps[T](value: T) {
      def validated(implicit needToValidate: NeedToValidate[T]): T = needToValidate.validate(value)
    }

    implicit val validateInn: NeedToValidate[Inn] = (v: Inn) => {
      if (v.inn.length != 1) throw new Exception("Bad inn")
      else v
    }

    implicit val validateSnils: NeedToValidate[Snils] = { (v: Snils) =>
      if (v.snils.length != 2) throw new Exception("Bad Snills")
      else v
    }
  }

  case class PersonDocumentInfoUpdate(inn: Inn, snils: Snils)

  val p  = Person(2, "scmd", 14, null, null)
  val e1 = PersonDocumentInfoUpdate(Inn("1"), Snils("2"))
  def proccessPersonDocumentInfoUpdated(event: PersonDocumentInfoUpdate): Unit = {
    import ValidateTypeClassSyntax._
    p.setInn(event.inn.validated)
    p.setSnils(event.snils.validated)
    println(p)
  }

  proccessPersonDocumentInfoUpdated(e1)
}

object ApplicativeSyntax {
  trait Applicative[I, O, M[_]] {
    def ap(m1: M[I => O])(m2: M[I]): M[O]
  }

  implicit class HasApplication[I, O, M[_]](m1: M[I => O]) {
    def application(m2: M[I])(implicit applicative: Applicative[I, O, M]): M[O] =
      applicative.ap(m1)(m2)
  }

  implicit def optionAp[I, O]: Applicative[I, O, Option] = new Applicative[I, O, Option] {
    override def ap(m1: Option[I => O])(m2: Option[I]): Option[O] =
      for {
        f <- m1
        i <- m2
      } yield f(i)
  }
}

object ApplicativeExample extends App {
  import ApplicativeSyntax._

  implicit def listAp[I, O]: Applicative[I, O, List] = new Applicative[I, O, List] {
    override def ap(m1: List[I => O])(m2: List[I]): List[O] =
      for {
        f <- m1
        i <- m2
      } yield f(i)
  }

  {
    val f: Int => Int            = _ * 2
    val vOpt: Option[Int]        = Option(2)
    val fOpt: Option[Int => Int] = Option(f)

    println {
      val res = fOpt.application(vOpt)
      res
    }
  }

  {
    val f: Int => Int           = _ * 2
    val f1: Int => Int          = _ * 3
    val vList: List[Int]        = List(1, 2, 3)
    val fList: List[Int => Int] = List(f, f1)

    println {
      val res = fList.application(vList)
      res
    }
  }

}
