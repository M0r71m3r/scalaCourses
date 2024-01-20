package lectures

import magicZone.ImplicitForPrint

object ScalaGenerics extends App with ImplicitForPrint {
  //in java <T>
  def simpleDef[T](x: T): Unit = x.print
  case class SimpleClass[T1](x: T1) {
    s"info T = ${x.getClass.getSimpleName}".print
    def printX = {
      "start print".print
      simpleDef(x) // simpleDef[T1](x)
      "end print".print
    }
  }

  val scg: SimpleClass[Int] = SimpleClass(1)
  scg.printX
  val scg1: SimpleClass[String] = SimpleClass("1")
  scg1.printX
}
object ScalaVariance extends App with ImplicitForPrint {
  class C1[A]  // инвариантный класс
  class C2[+A] // ковариантный класс
  class C3[-A] // контравариантный класс
}

trait Item { def number: String = "111" }
trait HasPrice extends Item     { def price = 1            }
trait Book     extends HasPrice { def author: String = "g" }

object ScalaInvariance extends App with ImplicitForPrint {
  trait Pipelline[T] {
    def prossec(t: T): T
  }

  def oneOf(
      p1: Pipelline[HasPrice],
      p2: Pipelline[HasPrice],
      b: HasPrice
  ): HasPrice = {
    val b1 = p1.prossec(b)
    val b2 = p2.prossec(b)
    if (b1.price < b2.price) b2 else b1
  }

  // Book < HasPrice < Item
  oneOf(
    new Pipelline[HasPrice] {
      override def prossec(t: HasPrice): HasPrice = ???
    },
    new Pipelline[HasPrice] {
      override def prossec(t: HasPrice): HasPrice = ???
    },
    new HasPrice {}
  )
//  val x1 = oneOf(
//    new Pipelline[Book] {
//      override def prossec(t: Book): Book = ???
//    },
//    new Pipelline[Book] {
//      override def prossec(t: Book): Book = ???
//    },
//    new HasPrice {}
//  )

}
object ScalaCovariance extends App with ImplicitForPrint {

  trait Producer[+T] {
    def make: T
  }

  def makeTwo(p: Producer[HasPrice]): Int = p.make.price + p.make.price

  val bookProducer = new Producer[Book] {
    override def make: Book = new Book {}
  }

  makeTwo(bookProducer).print

}
object ScalaContravariance extends App with ImplicitForPrint {
  trait Serilizer[-A] {
    def serilize(a: A): String
  }

  val hasPriceSerizer = new Serilizer[HasPrice] {
    override def serilize(a: HasPrice): String = s"{ price: ${a.price} }"
  }

  val bookSerialer: Serilizer[Book] = hasPriceSerizer

  bookSerialer.serilize(new Book {}).print
}
object ScalaUpperTypeBounds extends App with ImplicitForPrint {

  trait Animal {
    def name: String = this.getClass.getSimpleName
  }
  trait Pet extends Animal

  case class Cat()  extends Pet
  case class Dog()  extends Pet
  case class Lion() extends Animal

  case class PetContainer[P <: Pet](p: P) {
    def pet: P = p
  }
  val dogC: PetContainer[Dog] = PetContainer(Dog())
  val catC: PetContainer[Cat] = PetContainer(Cat())
//  val lionC: PetContainer[Lion] = PetContainer(Lion()) // ce
}
object ScalaLowerTypeBounds extends App with ImplicitForPrint {

  trait List[+A] {
    def prepended[B >: A](elem: B): NonEmptyList[B] = NonEmptyList(elem, this)
  }

  case class NonEmptyList[+A](head: A, tail: List[A]) extends List[A]
  object Nil                                          extends List[Nothing]

  trait Bird
  case class AfroBird() extends Bird
  case class EuroBird() extends Bird

  val afroBrids: List[AfroBird]  = Nil.prepended(AfroBird())
  val birdsFromJapan: List[Bird] = Nil
  val someBird: Bird             = EuroBird()

  val birds: List[AfroBird]         = afroBrids
  val someBirds: NonEmptyList[Bird] = afroBrids.prepended(someBird)
  val moreBirds: NonEmptyList[Bird] = birds.prepended(EuroBird())
  val allBirds: NonEmptyList[Bird]  = afroBrids.prepended(EuroBird())
  val error: NonEmptyList[Object]   = moreBirds.prepended(birdsFromJapan)
}

case class Inn(inn: String)
case class Snils(snils: String)
case class Person(id: Long, cn: String, name: String, var age: Int, var inn: Inn, var snils: Snils) {

  def setAge(newAge: Int)       = this.age = newAge
  def setInn(newInn: Inn)       = this.inn = newInn
  def setSnils(newSnils: Snils) = this.snils = newSnils

}

object ScalaGenericMatcher extends App with ImplicitForPrint {
  val p1 = Person(1, "main", "Urza", 13, null, Snils("2"))
  val p2 = Person(2, "scnd", "Urza", 12, Inn("1"), null)

  def merge[R](
      f: R => Unit,
      v1: R,
      v2: R
  ): Unit = {
    val mb1 = Option(v1)
    val mb2 = Option(v2)
    val merged = (mb1, mb2) match {
      case (Some(s1), _)    => s1
      case (None, Some(s2)) => s2
      case (None, None)     => v1
    }
    f(merged)
  }

  merge(p1.setAge, p1.age, p2.age)
  merge(p1.setSnils, p1.snils, p2.snils)
  merge(p1.setInn, p1.inn, p2.inn)

  p1.print

}
object ScalaPieceOfTypeClasses extends App with ImplicitForPrint {
  trait NeedToValidate[T] {
    def validate(v: T): T
  }

  object ValidateTypeClassSyntax {
    implicit class NeedToValidateOps[T](value: T) {
      def validated(implicit ntv: NeedToValidate[T]): T = ntv.validate(value)
    }

    implicit val validateInn = new NeedToValidate[Inn] {
      override def validate(v: Inn): Inn = {
        if (v.inn.length != 1) throw new Exception("bad inn") else v
      }
    }
    implicit val validateSnils = new NeedToValidate[Snils] {
      override def validate(v: Snils): Snils = {
        if (v.snils.length != 2) throw new Exception("bad snils") else v
      }
    }
  }

  case class PersonDocumentUpdated(inn: Inn, snils: Snils)

  //mb loaded from db
  val p3 = Person(2, "scnd", "Urza", 12, Inn("1"), null)
  //mb consumed from kafka
  val e = PersonDocumentUpdated(Inn("3"), Snils("14"))

  def processPersonDocumentUpdated(event: PersonDocumentUpdated): Unit = {
    import lectures.ScalaPieceOfTypeClasses.ValidateTypeClassSyntax._
    p3.setInn(event.inn.validated)
    p3.setSnils(event.snils.validated)
    p3.print
  }

  processPersonDocumentUpdated(e)

}

object Functions extends App with ImplicitForPrint {
  def addOne(i: Int): Int  = i + 1
  def prodTwo(i: Int): Int = i * 2

  // x = 2
  // addOne compose prodTwo 2
  // addOne(prodTwo(2))
  def compose = (addOne _).compose(prodTwo)
  compose(1)

  // x = 2
  // addOne 2 andThen prodTwo
  // prodTwo(addOne(2))
  def anThen = (addOne _).andThen(prodTwo)
  anThen(1)
}
