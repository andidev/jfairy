package io.codearte.jfairy.producer.person.locale.sv

import io.codearte.jfairy.Fairy
import spock.lang.Specification

class SvNationalIdentityCardNumberProviderSpec extends Specification {

	def Fairy fairy;
	def String nationalIdentityCardNumber

	def setup() {
		fairy = Fairy.create(Locale.forLanguageTag("sv"))
		nationalIdentityCardNumber = fairy.person().nationalIdentityCardNumber
	}

	def "should generate number with 8 characters"() {
		expect:
			nationalIdentityCardNumber.length() == 8
	}
}
