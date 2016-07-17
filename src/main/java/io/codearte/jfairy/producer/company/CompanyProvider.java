package io.codearte.jfairy.producer.company;

import com.google.inject.Provider;
import com.google.inject.assistedinject.Assisted;
import io.codearte.jfairy.data.DataMaster;
import io.codearte.jfairy.producer.BaseProducer;
import io.codearte.jfairy.producer.VATIdentificationNumberProvider;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;

public class CompanyProvider implements Provider<Company> {

	private static final String DOMAIN = "domains";
	private static final String COMPANY_NAME = "companyNames";
	private static final String COMPANY_SUFFIX = "companySuffixes";
	private static final String COMPANY_EMAIL = "companyEmails";

	private String name;
	private String domain;
	private String email;
	private String vatIdentificationNumber;

	private BaseProducer baseProducer;
	private DataMaster dataMaster;

	private final VATIdentificationNumberProvider vatIdentificationNumberProvider;

	@Inject
	public CompanyProvider(BaseProducer baseProducer,
						   DataMaster dataMaster,
						   VATIdentificationNumberProvider vatIdentificationNumberProvider,
						   @Assisted CompanyProperties.CompanyProperty... companyProperties) {
		this.baseProducer = baseProducer;
		this.dataMaster = dataMaster;
		this.vatIdentificationNumberProvider = vatIdentificationNumberProvider;

		for (CompanyProperties.CompanyProperty companyProperty : companyProperties) {
			companyProperty.apply(this);
		}
	}


	@Override
	public Company get() {

		if (name == null) {
			name = dataMaster.getRandomValue(COMPANY_NAME);
			if (baseProducer.trueOrFalse()) {
				name += " " + dataMaster.getRandomValue(COMPANY_SUFFIX);
			}
		}
		if (domain == null) {
			domain = StringUtils.strip(StringUtils.deleteWhitespace(name.toLowerCase()), ".")
					+ "." + dataMaster.getRandomValue(DOMAIN);
		}

		if (email == null) {
			email = dataMaster.getRandomValue(COMPANY_EMAIL);
		}

		if (vatIdentificationNumber == null) {
			vatIdentificationNumber = vatIdentificationNumberProvider.get();
		}

		return new Company(name, domain, email, vatIdentificationNumber);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public void setVatIdentificationNumber(String vatIdentificationNumber) {
		this.vatIdentificationNumber = vatIdentificationNumber;
	}
}
