package hmda.validation.engine.lar.validity

import hmda.model.fi.lar.LoanApplicationRegister
import hmda.validation.api.ValidationApi
import hmda.validation.engine.lar.LarCommonEngine
import hmda.validation.rules.EditCheck
import hmda.validation.rules.lar.validity._

trait LarValidityEngine extends LarCommonEngine with ValidationApi {

  private def doCheck(check: EditCheck[LoanApplicationRegister], lar: LoanApplicationRegister): LarValidation = {
    convertResult(lar, check(lar), check.name)
  }

  def validate(lar: LoanApplicationRegister): LarValidation = {
    val checks = List(
      V220,
      V225,
      V255,
      V262,
      V400
    ).map(doCheck(_, lar))

    validateAll(checks, lar)
  }
}