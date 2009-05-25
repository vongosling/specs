package org.specs.specification
import org.specs.util.Classes._
import org.specs.util.{ Configuration }
/**
 * This trait executes an example by cloning the enclosing specification first.
 * 
 * This way, the example is executed in a total isolation so as not to share local variables between examples
 * and avoid side-effects.
 * 
 * Warning: this works by considering that the "examples" method is stable on a BaseSpecification and
 * will always return the same examples in the same order
 */
trait SpecificationExecutor extends ExampleLifeCycle { this: BaseSpecification =>
  /** this variable can be changed to allow sharing variables between examples */
  var oneSpecInstancePerExample = Configuration.config.oneSpecInstancePerExample

  /** cache the specification examples to avoid querying again and again the specification */
  private lazy val specsExamples = this.examples
  /** @return a clone of the specification */
  private[specification] def cloneSpecification = createObject[BaseSpecification](getClass.getName, true)
  /** execute an example by cloning the specification and executing the cloned example */
  override def executeExample(example: Example): this.type = {
    if (oneSpecInstancePerExample) {
      if (specsExamples.contains(example)) {
        val i  = specsExamples.indexOf(example)
        cloneSpecification.foreach { s =>
          val cloned = s.examples(examples.indexOf(example))
          cloned.execution.execute
          ex.copyResults(cloned)
        }
      }
    }
    else { 
      example.execution.execute
    }
    this
  }
}
