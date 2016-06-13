package io.codearte.jfairy.producer.person.locale.pl;

import io.codearte.jfairy.producer.person.NationalIdentificationNumberFactory;
import io.codearte.jfairy.producer.person.NationalIdentificationNumberProperties;

public interface PeselFactory extends NationalIdentificationNumberFactory {

	PeselProvider produceNationalIdentificationNumberProvider(NationalIdentificationNumberProperties.Property... properties);

}
