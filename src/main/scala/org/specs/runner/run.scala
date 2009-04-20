/**
 * Copyright (c) 2007-2009 Eric Torreborre <etorreborre@yahoo.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software. Neither the name of specs nor the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written permission.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS INTHE SOFTWARE.
 */
import org.specs.util._
import org.specs._
import org.specs.runner._
import org.specs.util.Classes._

object run extends ClassRunner
class ClassRunner extends OutputReporter {
  val timer = new SimpleTimer
  lazy val specs = getSpecifications
  
  override def main(arguments: Array[String]) = {
    if (arguments.length == 0)
      println("The first argument should be at least the specification class name")
    else {
      args = arguments
      specs.foreach(_.main(args))
    }
  }
  override protected def displayUsage = {
    println("usage java <classpath> run [className|-k classNames]")
  }
  override protected def displayOptions = {
    super.displayOptions
    println("    [-k|--classes]")
    println("    [-p|--packages]")
  }
  override protected def displayOptionsDescription = {
    super.displayOptionsDescription
    println("-k, --classes                   comma-separated list of specification class names instead")
    println("-p, --packages                  comma-separated list of specification package names to append to class names")
  }

  def getSpecifications: List[Specification] = {
    val packageNames = argValue(args, List("-p", "--packages")).getOrElse("")
    val specificationPackageNames = packageNames.split(",").toList
    val classNames = argValue(args, List("-k", "--classes")).getOrElse(args(0))
    val specificationNames = classNames.split(",").toList
    val specificationClasses = for { 
      packageName <- specificationPackageNames
      className <- specificationNames
    } yield createSpecification(packageName, className)
    specificationClasses.flatMap(x => x)
  }
  def createSpecification(packageName: String, className: String) = {
    createObject[Specification](fullClassName(packageName, className), args.contains("-v"), args.contains("-v"))
  }
  private def fullClassName(packageName: String, className: String) = {
    if (packageName.trim.isEmpty) 
      className.trim
    else 
      packageName.trim+"."+className.trim
  }

}

