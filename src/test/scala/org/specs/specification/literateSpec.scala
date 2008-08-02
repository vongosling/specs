package org.specs.specification

object literateSpec extends Fixtures { <text> 
  A literate specification is a text embedded in xml tags.
  A literate specification can execute code by enclosing it in accolades: { (1 + 1).shh }
 
  However, <ex>an assertion can be included in a literate specification as part of an example</ex>, like this:
{""" 
     { "1 must be 1" in {1 must_== 1} }    """}{exampleOk}
 
  or <ex>with an "ex" xml tag</ex>, like that:
{""" 
     <ex>1 must be 1</ex> { 1 must_== 1  }   """}{taggedExample}
 
  <ex>It is possible to mark an example as not implemented yet</ex>:
{""" 
     <ex> this example is not yet implemented </ex> { notImplemented }   """} {notImplementedExample}
    
  In that case, the example will be added but marked as skipped
</text> isSus
}
trait Fixtures extends LiterateSpecification {
   object example1 extends LiterateSpecification  {
     <t>{"1 must be 1" in {1 must_== 1}}</t> isSus  }
   object example2 extends LiterateSpecification  {
     <t><ex>1 must be 1</ex> { 1 must_== 1  } </t> isSus  }
   object example3 extends LiterateSpecification  {
     <t><ex> this example is not yet implemented </ex> { notImplemented }</t> isSus  }
   def exampleOk = checkSuccess(example1)
   def taggedExample = checkSuccess(example2)
   def notImplementedExample = checkSkipped(example3)
   def checkSuccess(s: Specification) = check(s.systems.flatMap(_.examples).flatMap(_.failures).size must_== 0)
   def checkSkipped(s: Specification) = check(s.systems.flatMap(_.examples).flatMap(_.skipped).size must_== 1)
}
class LiterateSpecTest extends org.specs.runner.JUnit4(literateSpec)
