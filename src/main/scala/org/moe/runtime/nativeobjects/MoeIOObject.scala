package org.moe.runtime.nativeobjects

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

import scala.io.Source
import scala.util.{Try, Success, Failure}

/** 
 * FIXME 
 * - convert this to java.nio.* instead
 * - implement proper exception handling
 */

class MoeIOObject(
    v: java.io.File, klass : Option[MoeClass] = None
  ) extends MoeNativeObject[java.io.File](v, klass) {

  private lazy val reader = new java.io.BufferedReader(new java.io.FileReader(getNativeValue))
  private lazy val writer = new java.io.FileWriter(getNativeValue)

  private def file = getNativeValue

  // runtime methods

  def isReadable   (r: MoeRuntime): MoeBoolObject = r.NativeObjects.getBool(file.canRead())
  def isWriteable  (r: MoeRuntime): MoeBoolObject = r.NativeObjects.getBool(file.canWrite())
  def isExecutable (r: MoeRuntime): MoeBoolObject = r.NativeObjects.getBool(file.canExecute())

  def create (r: MoeRuntime): MoeBoolObject = r.NativeObjects.getBool(file.createNewFile())
  def delete (r: MoeRuntime): MoeBoolObject = r.NativeObjects.getBool(file.delete())
  def exists (r: MoeRuntime): MoeBoolObject = r.NativeObjects.getBool(file.exists())

  def print (r: MoeRuntime, data: MoeArrayObject): MoeUndefObject = {
    writer.write(data.unboxToArrayBuffer.get.map(_.unboxToString.get).mkString)
    writer.flush()
    r.NativeObjects.getUndef
  }

  def say (r: MoeRuntime, data: MoeArrayObject): MoeUndefObject = {
    writer.write(data.unboxToArrayBuffer.get.map(_.unboxToString.get + "\n").mkString)
    writer.flush()
    r.NativeObjects.getUndef
  }

  def readline (r: MoeRuntime): MoeStrObject = {
    val line = reader.readLine()
    r.NativeObjects.getStr(if (line == null) "" else line)
  }

  def readlines (r: MoeRuntime): MoeArrayObject = r.NativeObjects.getArray(
    Source.fromFile(file).getLines.toList.map(r.NativeObjects.getStr(_)):_*
  )

  def slurp (r: MoeRuntime): MoeStrObject = r.NativeObjects.getStr(
    Source.fromFile(file).mkString
  )

  // MoeNativeObject overrides

  override def copy = new MoeIOObject(
    new java.io.File(getNativeValue.toString), 
    getAssociatedClass
  )

  // MoeObject overrides

  override def toString: String = getNativeValue.toString

  // unboxing

}
