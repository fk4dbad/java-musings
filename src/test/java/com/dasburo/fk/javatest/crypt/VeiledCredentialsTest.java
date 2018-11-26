package com.dasburo.fk.javatest.crypt;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VeiledCredentialsTest {

  private static final Logger LOG = LoggerFactory.getLogger(VeiledCredentialsTest.class);

  @Test
  public void testVeil() {
    Map<String, String> credentials = new HashMap<>();
    credentials.put("username", "xxx");
    credentials.put("password", "yyy");
    credentials.put("subscription", "zzz");
    LOG.info(new VeiledCredentials(credentials).toString());
  }

}
