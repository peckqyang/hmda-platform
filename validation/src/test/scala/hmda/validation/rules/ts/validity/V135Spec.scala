package hmda.validation.rules.lar.validity

import hmda.model.fi.ts.TransmittalSheet
import hmda.validation.rules.EditCheck
import hmda.validation.rules.ts.TsEditCheckSpec
import hmda.validation.rules.ts.validity.{ V135, ValidityUtils }

class V135Spec extends TsEditCheckSpec with ValidityUtils {

  property("passes with properly formed number") {
    forAll(tsGen) { ts =>
      ts.mustPass
    }
  }

  property("fail with other seperators") {
    forAll(tsGen, badPhoneNumberGen) { (ts: TransmittalSheet, x: String) =>
      val badContact = ts.contact.copy(fax = x)
      val badTs = ts.copy(contact = badContact)
      badTs.mustFail
    }
  }

  override def check: EditCheck[TransmittalSheet] = V135

}