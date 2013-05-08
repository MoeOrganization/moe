package org.moe.runtime

/**
 * MoeMethod
 *
 * @param name method name
 * @param subroutine signature (MoeSignature)
 * @param declaration_env the captured creation environment (MoeEnvironment)
 * @param body executable body of method 
 */

class MoeMethod (
    name: String,
    signature: MoeSignature,
    declaration_env: MoeEnvironment,
    body: (MoeEnvironment) => MoeObject
  ) extends MoeCode(signature, declaration_env, body, Some(name)) {

  override def prepareExecutionEnvironment(args: MoeArguments): MoeEnvironment = {
    if (!args.hasInvocant)
      throw new MoeErrors.MoeProblems("no invocant set")
    val env = super.prepareExecutionEnvironment(args)
    env.setCurrentInvocant(args.getInvocant.get)  
    env
  }
}
