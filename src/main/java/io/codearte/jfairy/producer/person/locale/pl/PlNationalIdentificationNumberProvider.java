package io.codearte.jfairy.producer.person.locale.pl;

import com.google.inject.assistedinject.Assisted;
import io.codearte.jfairy.producer.BaseProducer;
import io.codearte.jfairy.producer.DateProducer;
import io.codearte.jfairy.producer.person.NationalIdentificationNumber;
import io.codearte.jfairy.producer.person.NationalIdentificationNumberProperties;
import io.codearte.jfairy.producer.person.NationalIdentificationNumberProvider;
import io.codearte.jfairy.producer.person.Person;
import org.joda.time.DateTime;

import javax.inject.Inject;

import static java.lang.Integer.valueOf;
import static java.lang.String.format;

/**
 * Spanish National Identification Number (known as PESEL or Polish Powszechny Elektroniczny System Ewidencji Ludności)
 *
 * Universal Electronic System for Registration of the Population)
 * More info: http://en.wikipedia.org/wiki/PESEL
 */
public class PlNationalIdentificationNumberProvider implements NationalIdentificationNumberProvider {

	private static final int NATIONAL_IDENTIFICATION_NUMBER_LENGTH = 11;
	private static final int VALIDITY_IN_YEARS = 10;

	private static final int[] PERIOD_WEIGHTS = {80, 0, 20, 40, 60};
	private static final int PERIOD_FACTOR = 100;
	private static final int BEGIN_YEAR = 1800;

	private static final int[] WEIGHTS = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
	private static final int MAX_SERIAL_NUMBER = 999;
	private static final int TEN = 10;

	private static final int[] SEX_FIELDS = {0, 2, 4, 6, 8};

	private final BaseProducer baseProducer;
	private final DateProducer dateProducer;
	private DateTime issueDate;
	private Person.Sex sex;

	@Inject
	public PlNationalIdentificationNumberProvider(DateProducer dateProducer, BaseProducer baseProducer,
												  @Assisted
	                     NationalIdentificationNumberProperties.Property... properties) {
		this.dateProducer = dateProducer;
		this.baseProducer = baseProducer;

		with(properties);

	}

	public void with(NationalIdentificationNumberProperties.Property[] properties) {
		for (NationalIdentificationNumberProperties.Property property : properties) {
			property.apply(this);
		}
	}

	@Override
	public NationalIdentificationNumber get() {

		if (issueDate == null) {
			issueDate = dateProducer.randomDateInThePast(VALIDITY_IN_YEARS);
		}
		if (sex == null) {
			sex = baseProducer.randomBoolean() ? Person.Sex.MALE : Person.Sex.FEMALE;
		}

		return new NationalIdentificationNumber(generate());
	}

	private String generate() {
		int year = issueDate.getYearOfCentury();
		int month = calculateMonth(issueDate.getMonthOfYear(), issueDate.getYear());
		int day = issueDate.getDayOfMonth();
		int serialNumber = baseProducer.randomInt(MAX_SERIAL_NUMBER);
		int sexCode = calculateSexCode(sex);

		String nationalIdentificationNumber = format("%02d%02d%02d%03d%d", year, month, day, serialNumber, sexCode);

		return nationalIdentificationNumber + calculateChecksum(nationalIdentificationNumber);
	}

	public void setIssueDate(DateTime issueDate) {
		this.issueDate = issueDate;
	}

	public void setSex(Person.Sex sex) {
		this.sex = sex;
	}

	/**
	 * @param nationalIdentificationNumber National Identification Number (PESEL)
	 * @return nationalIdentificationNumber validity
	 */
	public static boolean isValid(String nationalIdentificationNumber) {
		int size = nationalIdentificationNumber.length();
		if (size != NATIONAL_IDENTIFICATION_NUMBER_LENGTH) {
			return false;
		}

		int checksum = valueOf(nationalIdentificationNumber.substring(size - 1));
		int checkDigit = calculateChecksum(nationalIdentificationNumber);

		return checkDigit == checksum;

	}

	private int calculateMonth(int month, int year) {
		return month + PERIOD_WEIGHTS[(year - BEGIN_YEAR) / PERIOD_FACTOR];
	}

	// This should be tested
	private int calculateSexCode(Person.Sex sex) {
		return SEX_FIELDS[baseProducer.randomInt(SEX_FIELDS.length - 1)] + (sex == Person.Sex.MALE ? 1 : 0);
	}

	private static int calculateChecksum(String nationalIdentificationNumber) {
		int sum = 0;
		int i = 0;
		for (int weight : WEIGHTS) {
			int digit = Character.digit(nationalIdentificationNumber.charAt(i++), 10);
			sum += digit * weight;
		}

		int checksum = (sum % TEN);

		if (0 == checksum) {
			return 0;
		}

		return TEN - checksum;
	}
}
