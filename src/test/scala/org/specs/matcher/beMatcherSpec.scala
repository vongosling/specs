package org.specs.matcher
import org.specs.specification._
  
class beMatcherSpec extends org.specs.Specification {  outer =>
  implicit def toAnyResultMatcher[T](result: Result[T]) = new AnyResultMatcher(result)
  class AnyResultMatcher[T](result: Result[T]) {
    def equalTo(o: T) = result.matchWith(beEqualTo(o))
  }
  def equalTo[T](o: T) = beEqualTo(o)
  implicit def toStringResultMatcher(result: Result[String]) = new StringResultMatcher(result)
  class StringResultMatcher(result: Result[String]) {
    def matching(s: String) = result.matchWith(beMatching(s))
  }
  def matching(s: String) = beMatching(s)

  class ListResultMatcher[T](result: Result[List[T]]) {
    def size(i: Int) = result.matchWith(haveSize(i))
  }
  implicit def toListResultMatcher[T](result: Result[List[T]]) = new ListResultMatcher(result)

  def have[T] = new HaveVerbMatcher[T]
  def be[T] = new BeVerbMatcher[T]
  def not[T] = new NotMatcher[T]
  
  "A matcher starting with 'be' can be used with 'be' as a separated word" in {
    "hello" must be matching("h.*") 
  }
  "A matcher starting with 'be' can be used with 'be' as a separated word" in {
    "hello" must be equalTo("hello") 
  }
  "A matcher starting with 'notBe' can be used with 'not be' as a separated word" in {
    "hello" must not be equalTo("world") 
  }
  "A matcher starting with 'notBe' can be used with 'not be' as separated words" in {
    "hello" must not be(matching("z.*"))
  }
  "A collection matcher starting with 'have' can be used with have as a separated word" in {
    List("hello") must have size(1)
  }
  "A collection matcher starting with 'notHave' can be used with 'not have' as a separated words" in {
    List("hello") must not have(size(2))
  }
  "A collection matcher starting with 'have' can be used with have as a separated word" in {
    List(1) must have size(1)
  }
  "A collection matcher starting with 'notHave' can be used with 'not have' as a separated words" in {
    List(1) must not have(size(2))
  }
}