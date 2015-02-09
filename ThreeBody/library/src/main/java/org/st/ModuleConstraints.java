package org.st;

import java.util.LinkedList;
import java.util.List;

/**
 * Description of media constraints for {@code ModuleStream} and
 * {@code PeerConnection}.
 */
public class ModuleConstraints {
  /** Simple String key/value pair. */
  public static class KeyValuePair {
    private final String key;
    private final String value;

    public KeyValuePair(String key, String value) {
      this.key = key;
      this.value = value;
    }

    public String getKey() {
      return key;
    }

    public String getValue() {
      return value;
    }

    public String toString() {
      return key + ": " + value;
    }
  }


  public final List<KeyValuePair> mandatory;
  public final List<KeyValuePair> optional;

  public ModuleConstraints() {
    mandatory = new LinkedList<KeyValuePair>();
    optional = new LinkedList<KeyValuePair>();
  }

  private static String stringifyKeyValuePairList(List<KeyValuePair> list) {
    StringBuilder builder = new StringBuilder("[");
    for (KeyValuePair pair : list) {
      if (builder.length() > 1) {
        builder.append(", ");
      }
      builder.append(pair.toString());
    }
    return builder.append("]").toString();
  }

  public String toString() {
    return "mandatory: " + stringifyKeyValuePairList(mandatory) +
        ", optional: " + stringifyKeyValuePairList(optional);
  }
}
