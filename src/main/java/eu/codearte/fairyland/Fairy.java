/*
 * Copyright (c) 2013 Codearte
 */
package eu.codearte.fairyland;

import eu.codearte.fairyland.producer.*;
import eu.codearte.fairyland.producer.text.Text;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;

import static eu.codearte.fairyland.FairyFactory.*;

/**
 * Entry class
 */
public class Fairy {

  private static final String DATA_FILE_PREFIX = "fairyland_";

  private DataMaster dataMaster;

  private Fairy(Locale locale, String filePrefix) {

    try {
      Enumeration<URL> resources =
          getClass().getClassLoader().getResources(filePrefix + locale.getLanguage() + ".yml");
      dataMaster = new DataMaster();
      Yaml yaml = new Yaml();
      while (resources.hasMoreElements()) {
        dataMaster.appendData(yaml.loadAs(resources.nextElement().openStream(), DataMaster.class));
      }
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public static Fairy create() {
    return create(Locale.ENGLISH);
  }

  public static Fairy create(Locale locale) {
    return create(locale, DATA_FILE_PREFIX);
  }

  /**
   * Use this factory method to create your own dataset overriding bundled one
   *
   * @param locale         will be used to assess langCode for data file
   * @param dataFilePrefix prefix of the data file - final pattern will be dataFilePrefix_{langCode}.yml
   * @return
   */
  public static Fairy create(Locale locale, String dataFilePrefix) {
    return new Fairy(locale, dataFilePrefix);
  }

  public <T extends FairyProducer> T produce(Class<T> producer) {
    if (producer == null) {

    }
    try {
      Constructor<T> constructor = producer.getConstructor(RandomDataGenerator.class, RandomGenerator.class);
      RandomGenerator randomGenerator = createRandomGenerator();
      RandomDataGenerator randomDataGenerator = createRandomDataGenerator(dataMaster, randomGenerator);
      return constructor.newInstance(randomDataGenerator, randomGenerator);
    } catch (ReflectiveOperationException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public Text text() {
    return createText(dataMaster);
  }

  public Person person() {
    return createPerson(dataMaster);
  }

  public Company company() {
    return createCompany(dataMaster);
  }
}
