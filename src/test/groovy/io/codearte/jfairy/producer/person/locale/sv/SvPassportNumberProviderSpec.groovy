package io.codearte.jfairy.producer.person.locale.sv

import io.codearte.jfairy.Fairy
import spock.lang.Specification

class SvPassportNumberProviderSpec extends Specification {

	def Fairy fairy;
	def String passportNumber

	def setup() {
		fairy = Fairy.create(Locale.forLanguageTag("sv"))
		passportNumber = fairy.person().passportNumber
	}

	def "should generate number with 8 characters"() {
		expect:
			passportNumber.length() == 8
	}
}
