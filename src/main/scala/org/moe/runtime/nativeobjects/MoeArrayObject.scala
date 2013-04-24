package org.moe.runtime.nativeobjects

import org.moe.runtime._

import scala.collection.mutable.ArrayBuffer
import scala.util.{Try, Success, Failure}

class MoeArrayObject(
    v: ArrayBuffer[MoeObject],
    klass : Option[MoeClass] = None
  ) extends MoeNativeObject[ArrayBuffer[MoeObject]](v, klass) {

  def this(list: List[MoeObject]) = this(ArrayBuffer(list : _*))

  private def array = getNativeValue

  // Runtime methods
  
  def at_pos (r: MoeRuntime, i: MoeIntObject): MoeObject = {
    if(i.unboxToInt.get >= array.length) r.NativeObjects.getUndef
    else array(i.unboxToInt.get)
  }

  def bind_pos (r: MoeRuntime, i: MoeIntObject, v: MoeObject): MoeObject = {
    val idx = i.unboxToInt.get
    if (idx < array.length)
      array(idx) = v
    else {
      setNativeValue(array.padTo(idx, r.NativeObjects.getUndef))
      array.insert(idx, v)
    }
    v
  }

  def length (r: MoeRuntime): MoeIntObject = r.NativeObjects.getInt(array.length)

  def clear (r: MoeRuntime): MoeObject = {
    array.clear()
    r.NativeObjects.getUndef
  }

  def head (r: MoeRuntime): MoeObject = array.head
  def tail (r: MoeRuntime): MoeObject = r.NativeObjects.getArray( array.tail: _* )

  def shift (r: MoeRuntime): MoeObject =
    if(array.length == 0) r.NativeObjects.getUndef
    else array.remove(0)

  def pop (r: MoeRuntime): MoeObject =
    if(array.length == 0) r.NativeObjects.getUndef
    else array.remove(array.length - 1)

  def unshift (r: MoeRuntime, values: MoeArrayObject): MoeIntObject = {
    array.insertAll(0, values.unboxToArrayBuffer.get)
    r.NativeObjects.getInt(array.length)
  }

  def push (r: MoeRuntime, values: MoeArrayObject): MoeIntObject = {
    array ++= values.unboxToArrayBuffer.get
    r.NativeObjects.getInt(array.length)
  }

  def slice(r: MoeRuntime, indicies: MoeArrayObject): MoeArrayObject = r.NativeObjects.getArray(
    indicies.unboxToArrayBuffer.get.map(i => at_pos(r, i.asInstanceOf[MoeIntObject])) : _*
  )

  def reverse(r: MoeRuntime): MoeArrayObject = r.NativeObjects.getArray(array.reverse:_*)

  def join(r: MoeRuntime): MoeStrObject = r.NativeObjects.getStr(array.map(_.unboxToString.get).mkString(""))
  def join(r: MoeRuntime, sep: MoeStrObject): MoeStrObject = r.NativeObjects.getStr(
    array.map(_.unboxToString.get).mkString(sep.unboxToString.get)
  )

  def map (r: MoeRuntime, f: MoeCode): MoeArrayObject = r.NativeObjects.getArray(
    array.map(i => f.execute(new MoeArguments(List(i)))) : _*
  )

  def grep (r: MoeRuntime, f: MoeCode): MoeArrayObject = r.NativeObjects.getArray(
    array.filter(i => f.execute(new MoeArguments(List(i))).isTrue) : _*
  )

  def each (r: MoeRuntime, f: MoeCode): MoeUndefObject = {
    array.foreach({ i => f.execute(new MoeArguments(List(i))); ()})
    r.NativeObjects.getUndef
  }

  def reduce (r: MoeRuntime, f: MoeCode, init: Option[MoeObject]): MoeObject = init match {
    case Some(init_val) => array.foldLeft(init_val)({ (a, b) => f.execute(new MoeArguments(List(a, b))) })
    case None           => array.reduceLeft        ({ (a, b) => f.execute(new MoeArguments(List(a, b))) })
  }

  def first (r: MoeRuntime, f: MoeCode): MoeObject = 
    array.dropWhile(i => f.execute(new MoeArguments(List(i))).isFalse).head

  def max (r: MoeRuntime): MoeIntObject = r.NativeObjects.getInt(
      array.map(i => i.unboxToInt.get).max
  )

  def maxstr (r: MoeRuntime): MoeStrObject = r.NativeObjects.getStr(
    array.map(s => s.unboxToString.get).max
  )

  def min (r: MoeRuntime): MoeIntObject = r.NativeObjects.getInt(
      array.map(i => i.unboxToInt.get).min
  )

  def minstr (r: MoeRuntime): MoeStrObject = r.NativeObjects.getStr(
    array.map(s => s.unboxToString.get).min
  )

  def shuffle (r: MoeRuntime): MoeArrayObject = r.NativeObjects.getArray(
    scala.util.Random.shuffle(array) : _*
  )

  def sum (r: MoeRuntime): MoeIntObject = r.NativeObjects.getInt(
    array.map(i => i.unboxToInt.get).sum
  )

  def flatten (r: MoeRuntime): MoeArrayObject = {
    val acc = new ArrayBuffer[MoeObject]
    array.foreach(
      {
        case a: MoeArrayObject => acc ++= a.flatten(r).getNativeValue
        case x: MoeObject      => acc += x
      }
    )
    r.NativeObjects.getArray(acc: _*)
  }

  def repeat (r: MoeRuntime, count: MoeIntObject): MoeArrayObject = {
    val result = new ArrayBuffer[MoeObject]
    for (i <- 1 to count.unboxToInt.get)
      result ++= array
    r.NativeObjects.getArray(result: _*)
  }

  // equality
  def equal_to (r: MoeRuntime, that: MoeArrayObject): MoeBoolObject = 
    r.NativeObjects.getBool(
      length(r).equal_to(that.length(r))
        &&
      ((unboxToArrayBuffer.get, that.unboxToArrayBuffer.get).zipped.forall( (a, b) => a.equal_to(b) ))
    )

  def not_equal_to (r: MoeRuntime, that: MoeArrayObject): MoeBoolObject =
    r.NativeObjects.getBool(
      length(r).not_equal_to(that.length(r))
        ||
      ((unboxToArrayBuffer.get, that.unboxToArrayBuffer.get).zipped.exists( (a, b) => a.not_equal_to(b) ))
    )

  // MoeNativeObject overrides

  override def copy = new MoeArrayObject(ArrayBuffer(getNativeValue:_*), getAssociatedClass)

  // MoeObject overrides
  
  override def isFalse: Boolean = getNativeValue.size == 0
  override def toString: String =
    '[' + getNativeValue.map(_.toString).mkString(", ") + ']'
  
  override def isScalar = false
  override def isArray  = true
  
  // unboxing
  
  override def unboxToArrayBuffer: Try[ArrayBuffer[MoeObject]] = Success(getNativeValue)
}
