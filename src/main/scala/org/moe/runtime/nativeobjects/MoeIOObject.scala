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
    v: java.io.File, t : Option[MoeType] = None
  ) extends MoeNativeObject[java.io.File](v, t) {

  private lazy val reader = new java.io.LineNumberReader(new java.io.BufferedReader(new java.io.FileReader(getNativeValue)))
  private lazy val writer = new java.io.FileWriter(getNativeValue)

  private def file = getNativeValue
  private var isAtEOF = false

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
    writer.write(data.unboxToArrayBuffer.get.map(_.unboxToString.get).mkString + "\n")
    writer.flush()
    r.NativeObjects.getUndef
  }

  def readline (r: MoeRuntime): MoeStrObject = {
    val line = reader.readLine()
    r.NativeObjects.getStr(
      if (line == null) {
        isAtEOF = true
        ""
      }
      else
        line
    )
  }

  def readlines (r: MoeRuntime): MoeArrayObject = r.NativeObjects.getArray(
    {
      Source.fromFile(file).getLines.toList.map(r.NativeObjects.getStr(_)):_*
    }
  )

  def slurp (r: MoeRuntime): MoeStrObject = r.NativeObjects.getStr(
    {
      val slurped = Source.fromFile(file).mkString
      isAtEOF = true
      slurped
    }
  )

  def close (r: MoeRuntime): MoeUndefObject = {
    reader.close()
    writer.close()
    r.NativeObjects.getUndef
  }

  def currentLineNumber (r: MoeRuntime): MoeIntObject = r.NativeObjects.getInt( reader.getLineNumber )

  def atEndOfFile (r: MoeRuntime): MoeBoolObject = r.NativeObjects.getBool( isAtEOF )

  // MoeNativeObject overrides

  override def copy = new MoeIOObject(
    new java.io.File(getNativeValue.toString), 
    getAssociatedType
  )

  // MoeObject overrides

  override def toString: String = getNativeValue.toString

  // unboxing

}
