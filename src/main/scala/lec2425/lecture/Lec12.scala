package lec2425.lecture

object ScalaGenerics extends App {
  // java class GenericExample<E>() {}

  def simpleGenericDef[T](x: T): Unit                    = println(x)
  def simpleGenericDef1[T, A, B, C, In, Out](x: T): Unit = println(x)

  case class SimpleGenericClass[T1](a: T1) {
    println(s"info: T1 = ${a.getClass.getSimpleName}")
    def print(): Unit = {
      println("start print")
      simpleGenericDef(a) // or simpleGenericDef[T1](a)
      println("end print")
    }
  }

  val s1 = SimpleGenericClass(1)
  s1.print()
  println("--------------")
  val s2 = SimpleGenericClass("1")
  s2.print()
}

object ScalaVariance extends App {
  class C1[A]  // инвариантый класс
  class C2[+A] // ковариантый класс
  class C3[-A] // контравариантый класс
}

trait Item {
  def number: String
}
trait HasPrice extends Item {
  def price: Int
}
trait Book extends HasPrice {
  def author: String
}

object Examples {
  val i1 = new Item {
    override def number: String = "???"
  }
  val h1 = new HasPrice {
    override def price: Int = 1

    override def number: String = "???"
  }
  val b1 = new Book {
    override def author: String = "???"

    override def price: Int = 1

    override def number: String = "???"
  }
}

object ScalaInvariance extends App {
  // pipe T => T, Any => T, T => Any
  trait Pipe[T, A] {
    def process(t: T): T
    def process1(t: String): T
    def process2(t: T): String
    // other examples
    def process3(t: A): T = ???
    def process4(t: T): A = ???
  }
  val t1 = new Pipe[HasPrice, String] {
    override def process(t: HasPrice): HasPrice = ???
    override def process1(t: String): HasPrice  = ???
    override def process2(t: HasPrice): String  = ???
  }
  // t1.process(Examples.i1) // ce need HasPrice
  t1.process(Examples.h1)
  t1.process(Examples.b1)
}

object ScalaCovariance extends App {
  // prod Any => T
  trait Prod[+T] {
    def prod(a: String): T
    // ce: Covariant type T occurs in contravariant position in type T of value a
    //def prod1(a: T): String
  }
  val p = new Prod[Item] {
    override def prod(a: String): Item = ???
  }
  val p1 = new Prod[HasPrice] {
    override def prod(a: String): HasPrice = ???
  }
  val p2 = new Prod[Book] {
    override def prod(a: String): Book = ???
  }
  def make(p: Prod[HasPrice]): Unit = println(p)
  // make(p) // ce need HasPrice
  make(p1)
  make(p2)
}

object ScalaContravariance extends App {
  // serializer T => Any
  trait Serializer[-T] {
    def serialize(t: T): String
    //ce :Contravariant type T occurs in covariant position in type T of value serialize1
    //def serialize1(t: String): T
  }
  val s1 = new Serializer[Item] {
    override def serialize(t: Item): String = t.number
  }
  val s2 = new Serializer[HasPrice] {
    override def serialize(t: HasPrice): String = t.price.toString
  }

  val hasPriceSerializerViaItem: Serializer[Item] = s1 //: Serializer[Item]
  println(hasPriceSerializerViaItem.serialize(Examples.i1))
  println(hasPriceSerializerViaItem.serialize(Examples.h1))
  println(hasPriceSerializerViaItem.serialize(Examples.b1))

  val hasPriceSerializer: Serializer[HasPrice] = s2 //: Serializer[HasPrice]
  //hasPriceSerializer.serialize(Examples.i1) // ce
  println(hasPriceSerializer.serialize(Examples.h1))
  println(hasPriceSerializer.serialize(Examples.b1))

  val bookSerializerViaHasPrice: Serializer[Book] = s2 //: Serializer[HasPrice]
  // bookSerializerViaHasPrice.serialize(Examples.i1) // ce
  // bookSerializerViaHasPrice.serialize(Examples.h1) // ce
  println(bookSerializerViaHasPrice.serialize(Examples.b1))
}

// A extends B extends C
// T => A, B:
// pipe T => T, Any => T, T => Any
// +T => A, B:
// prod Any => T
// -T => B, C:
// serializer T => Any

object Example2 extends App {
  // List[+A]
  val l: List[Item] = List(Examples.h1, Examples.b1)
}

trait Animal
trait Cat           extends Animal
trait HomeCat       extends Cat
trait MyHomeCat     extends HomeCat
trait MyHomeHomeCat extends MyHomeCat
trait Lion

object ScalaUpperTypeBounds extends App {
  // Верхнее ограничение типа T <: A указывает на то что тип T относится к подтипу типа A
  // Т.е является наследником
  // T <: A
  case class HomeCatContainer[I <: HomeCat](i: I) {
    def item: I = i
  }

  val a     = new Animal {}
  val c1    = new Cat {}
  val hc2   = new HomeCat {}
  val mhc3  = new MyHomeCat {}
  val mmhc4 = new MyHomeHomeCat {}

//  val broken: HomeCatContainer[Cat] = new HomeCatContainer(c1)
//  val broken = new HomeCatContainer[HomeCat](a)
  val hcc  = new HomeCatContainer[HomeCat](hc2)
  val hcc1 = new HomeCatContainer[HomeCat](mhc3)
  val hcc2 = new HomeCatContainer[HomeCat](mmhc4)
  // Type Int does not conform to upper bound HomeCat
  //  val hcc3  = new HomeCatContainer[Int](1)
  val hcc4 = new HomeCatContainer[MyHomeCat](mhc3)
  val hcc5 = new HomeCatContainer[MyHomeCat](mmhc4)
  val hcc6 = new HomeCatContainer[MyHomeHomeCat](mmhc4)
}

object ScalaLowerTypeBounds extends App {
  // Термин T >: A выражает, то что параметр типа T является родителем типа A
  // T >: A

  trait List[+A] {
    def prepend1[B >: A](elem: B): NonEmptyList[B] = NonEmptyList(elem, this)
  }
  case class NonEmptyList[+A](head: A, tail: List[A]) extends List[A]
  object Nil                                          extends List[Nothing]
  val a     = new Animal {}
  val c1    = new Cat {}
  val hc2   = new HomeCat {}
  val mhc3  = new MyHomeCat {}
  val mmhc4 = new MyHomeHomeCat {}

  val l1: NonEmptyList[Cat]    = Nil.prepend1(c1)
  val l11: NonEmptyList[Cat]   = l1.prepend1(c1)
  val l2: NonEmptyList[Animal] = l1.prepend1(a)
  val l3: NonEmptyList[Animal] = l2.prepend1(hc2)

  // B является родителем типа MyHomeCat
  def prepend12[B >: MyHomeCat](elem: B): B = elem

//  prepend12[MyHomeHomeCat](a)
  prepend12(c1)
  prepend12(hc2)

  case class Box[B >: HomeCat](b: B)

  val b0: Box[Animal]  = Box[Animal](a)
  val b1: Box[Cat]     = Box(c1)
  val b2: Box[HomeCat] = Box(hc2)
  val b3: Box[HomeCat] = Box(mhc3)
  // type MyHomeCat does not conform to lower bound HomeCat of type parameter B
  // val b4               = Box[MyHomeCat](mhc3)

  def aaaa[B](b: B with MyHomeCat) = ???

  // aaaa(c1) // ce
  // aaaa(hc2) // ce
  aaaa(mhc3)
  aaaa(mmhc4)
}
