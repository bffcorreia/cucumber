package io.bffcorreia.cucumber;

import java.io.Serializable;

public class ValidationError implements Serializable {

  private final int key;
  private final String[] substitutions;

  public ValidationError(int key, String... substitutions) {
    this.key = key;
    this.substitutions = substitutions;
  }

  public int getKey() {
    return key;
  }

  public String[] getSubstitutions() {
    return substitutions;
  }
}
