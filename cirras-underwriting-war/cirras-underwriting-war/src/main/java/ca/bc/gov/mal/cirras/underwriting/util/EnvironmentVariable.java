package ca.bc.gov.mal.cirras.underwriting.util;

public class EnvironmentVariable {

  private EnvironmentVariable() {
    // Hide public constructor
  }

  public static String getVariable(String variable) {
    return System.getenv(variable);
  }
}