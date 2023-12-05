package magicZone

/** MAGIC DETECTED */
trait ImplicitForPrint {
  implicit class PrintMe[T <: Any](any: T) {
    /** just println(any) */
    implicit def print: Unit = println(any)
  }
}