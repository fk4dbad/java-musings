package com.dasburo.fk.javatest.crypt;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VeiledCredentials {
  private static final ObjectMapper OM = new ObjectMapper();

  private final String veiledString;

  public VeiledCredentials(String input) {
    veiledString = Objects.requireNonNull(input);
    unveil(veiledString); // test unveil
  }

  public VeiledCredentials(Map<String, String> credentials) {
    veiledString = veil(credentials);
  }

  protected static String veil(Map<String, String> credentials) {
    byte[] bytes;
    try {
      bytes = OM.writeValueAsBytes(credentials);
    } catch (JsonProcessingException ex) {
      throw new IllegalArgumentException("veiling of credentials failed: " + ex.getMessage());
    }
    return Base64.getEncoder()
        .encodeToString(bytes);
  }

  protected static Map<String, String> unveil(String veiled) {
    try {
      return OM.readValue(Base64.getDecoder()
          .decode(veiled), Map.class);
    } catch (IOException ex) {
      throw new IllegalArgumentException("unveiling of credentials failed: " + ex.getMessage());
    }
  }

  public String get(String part) {
    return unveil(veiledString).get(part);
  }

  @Override
  public String toString() {
    return veiledString;
  }
}
