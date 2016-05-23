package hmda.validation.engine.ts.validity

import hmda.model.fi.ts.TransmittalSheet
import hmda.validation.api.ValidationApi
import hmda.validation.engine.ts.TsCommonEngine
import hmda.validation.rules.ts.validity._

trait TsValidityEngine extends TsCommonEngine with ValidationApi {

  def checkValidity(ts: TransmittalSheet): TsValidation = {
    val checks: List[TsValidation] = List(
      V105,
      V111,
      V112,
      V120,
      V125,
      V135,
      V140,
      V155
    ).map(check(_, ts))

    validateAll(checks, ts)
  }
}
