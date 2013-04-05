package org.moe.runtime

abstract class MoeContext

case class MoeIntContext()  extends MoeContext
case class MoeNumContext()  extends MoeContext
case class MoeStrContext()  extends MoeContext
case class MoeBoolContext() extends MoeContext