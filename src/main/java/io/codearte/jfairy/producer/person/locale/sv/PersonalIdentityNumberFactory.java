package io.codearte.jfairy.producer.person.locale.sv;

import io.codearte.jfairy.producer.person.NationalIdentificationNumberFactory;
import io.codearte.jfairy.producer.person.NationalIdentificationNumberProperties;

public interface PersonalIdentityNumberFactory extends NationalIdentificationNumberFactory {

	PersonalIdentityNumberProvider produceNationalIdentificationNumberProvider(NationalIdentificationNumberProperties.Property... properties);

}
