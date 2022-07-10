package com.csproject.hrm.common.properties;

import com.sun.istack.logging.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;

public class PropertiesDescription {

  private static final Map<String, String> mapConfigProperties = new HashMap();

  static {
    try {
      Properties properties = new Properties();
      properties.load(
          new InputStreamReader(
              Objects.requireNonNull(
                  PropertiesDescription.class
                      .getClassLoader()
                      .getResourceAsStream("description.properties")),
              StandardCharsets.UTF_8));
      Enumeration<?> enumeration = properties.propertyNames();
      while (enumeration.hasMoreElements()) {
        String key = (String) enumeration.nextElement();
        String value = properties.getProperty(key);
        mapConfigProperties.put(key, value);
      }
    } catch (IOException e) {
      Logger.getLogger(PropertiesDescription.class).log(Level.SEVERE, "static", e);
    }
  }

  public static String getValueByKey(String key) {
    String value = mapConfigProperties.get(key);
    return value;
  }
}