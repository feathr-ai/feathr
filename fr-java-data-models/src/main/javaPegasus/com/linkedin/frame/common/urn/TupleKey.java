package com.linkedin.frame.common.urn;

import com.linkedin.data.template.DataTemplateUtil;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Represents the entity key portion of a Urn, encoded as a tuple of Strings.
 * A single-element tuple is encoded simply as the value of that element.  A tuple with multiple
 * elements is encoded as a parenthesized list of strings, comma-delimited.
 */
public class TupleKey {
  public static final char START_TUPLE = '(';
  public static final char END_TUPLE = ')';
  public static final char DELIMITER = ',';

  private final List<StringPart> _tupleParts;

  public TupleKey(String... tupleParts) {
    _tupleParts = Arrays.stream(tupleParts).map(value -> {
      if (value == null) {
        throw new NullPointerException("Cannot create URN with null part.");
      }

      return new StringPart(value, 0, value.length());
    }).collect(Collectors.toList());
  }

  public TupleKey(List<String> tupleParts) {
    _tupleParts = tupleParts.stream().map(value -> {
      if (value == null) {
        throw new NullPointerException("Cannot create URN with null part.");
      }

      return new StringPart(value, 0, value.length());
    }).collect(Collectors.toList());
  }

  /**
   * Constructs a {@code TupleKey} given a list of tuple parts.
   *
   * @param tupleParts tuple parts
   * @param placeholder Placeholder boolean to get over java performing generics erasure.
   */
  private TupleKey(List<StringPart> tupleParts, boolean placeholder) {
    _tupleParts = tupleParts;
  }

  // This constructor is intentionally made non-public and should only be
  // invoked by the convenient method createWithOneKeyPart.
  // The reason why the String-vararg overload is insufficient is because it
  // creates needless garbage in the case of a single element. That vararg
  // methods allocates an array for the call, then copies that into a list.
  private TupleKey(String oneElement) {
    if (oneElement == null) {
      throw new NullPointerException("Cannot create URN with null part.");
    }
    _tupleParts = Collections.singletonList(new StringPart(oneElement, 0, oneElement.length()));
  }

  public static TupleKey createWithOneKeyPart(String input) {
    return new TupleKey(input);
  }

  /**
   * Create a tuple key from a sequence of Objects.  The resulting tuple
   * consists of the sequence of String values resulting from calling .toString() on each
   * object in the input sequence
   *
   * @param tupleParts - a sequence of Objects to be represented in the tuple
   * @return - a TupleKey representation of the object sequence
   */
  public static TupleKey create(Object... tupleParts) {
    return new TupleKey(Arrays.stream(tupleParts).map(value -> {
      if (value == null) {
        throw new NullPointerException("Cannot create URN with null part.");
      }

      String stringValue = value.toString();
      return new StringPart(stringValue, 0, stringValue.length());
    }).collect(Collectors.toList()), false);
  }

  /**
   * Create a tuple key from a sequence of Objects.  The resulting tuple
   * consists of the sequence of String values resulting from calling .toString() on each
   * object in the input sequence
   *
   * @param tupleParts - a sequence of Objects to be represented in the tuple
   * @return - a TupleKey representation of the object sequence
   */
  public static TupleKey create(Collection<?> tupleParts) {
    return new TupleKey(tupleParts.stream().map(value -> {
      if (value == null) {
        throw new NullPointerException("Cannot create URN with null part.");
      }

      String stringValue = value.toString();
      return new StringPart(stringValue, 0, stringValue.length());
    }).collect(Collectors.toList()), false);
  }

  public String getFirst() {
    return _tupleParts.get(0).toString();
  }

  public String get(int index) {
    return _tupleParts.get(index).toString();
  }

  public short getAsShort(int index) {
    long value = _tupleParts.get(index).parseLong();
    if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
      throw new NumberFormatException("Value out of short range. Value:\"" + value + "\"");
    }
    return (short) value;
  }

  public int getAsInt(int index) {
    long value = _tupleParts.get(index).parseLong();
    if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
      throw new NumberFormatException("Value out of int range. Value:\"" + value + "\"");
    }
    return (int) value;
  }

  public long getAsLong(int index) {
    return _tupleParts.get(index).parseLong();
  }

  public boolean getAsBoolean(int index) {
    StringPart value = _tupleParts.get(index);

    if (value.length() == 4 && value._value.regionMatches(true, value._start, "true", 0, 4)) {
      return true;
    }

    if (value.length() == 5 && value._value.regionMatches(true, value._start, "false", 0, 5)) {
      return false;
    }

    throw new IllegalArgumentException("Invalid boolean value: " + value);
  }

  /**
   * Return a tuple element coerced to a specific type
   *
   * @param index - the index of the tuple element to be returned
   * @param clazz - the Class object for the return type.  Must be String, Short, Boolean, Integer, Long, or an Enum subclass
   * @param <T> - the desired type for the returned object.
   * @return The specified element of the tuple, coerced to the specified type T.
   */
  @SuppressWarnings({"unchecked"})
  public <T> T getAs(int index, Class<T> clazz) {
    if (String.class.equals(clazz)) {
      return (T) get(index);
    } else if (Short.TYPE.equals(clazz) || Short.class.equals(clazz)) {
      return (T) (Short) getAsShort(index);
    } else if (Integer.TYPE.equals(clazz) || Integer.class.equals(clazz)) {
      return (T) (Integer) getAsInt(index);
    } else if (Long.TYPE.equals(clazz) || Long.class.equals(clazz)) {
      return (T) (Long) getAsLong(index);
    } else if (Boolean.TYPE.equals(clazz) || Boolean.class.equals(clazz)) {
      return (T) (Boolean) getAsBoolean(index);
    }

    String value = get(index);
    if (Enum.class.isAssignableFrom(clazz)) {
      return (T) Enum.valueOf(clazz.asSubclass(Enum.class), value);
    } else if (DataTemplateUtil.hasCoercer(clazz)) {
      return DataTemplateUtil.coerceOutput(value, clazz);
    } else {
      throw new IllegalArgumentException("Cannot coerce String to type: " + clazz.getName());
    }
  }

  public int size() {
    return _tupleParts.size();
  }

  public List<String> getParts() {
    return _tupleParts.stream().map(StringPart::toString).collect(Collectors.toList());
  }

  @Override
  public String toString() {
    if (_tupleParts.size() == 1) {
      return _tupleParts.get(0).toString();
    } else {
      StringBuilder result = new StringBuilder();

      result.append(START_TUPLE);
      boolean delimit = false;
      for (StringPart value : _tupleParts) {
        if (delimit) {
          result.append(DELIMITER);
        }
        result.append(value._value, value._start, value._end);
        delimit = true;
      }
      result.append(END_TUPLE);
      return result.toString();
    }
  }

  @Override
  public int hashCode() {
    return _tupleParts.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    return _tupleParts.equals(((TupleKey) obj)._tupleParts);
  }

  public static TupleKey fromString(String s) throws URISyntaxException {
    return fromString(s, 0);
  }

  /**
   * Create a tuple key from a string starting at the given index.
   * @param input raw urn string or urn type specific string.
   * @param startIndex index where urn type specific string starts.
   * @return entity tuple key.
   * @throws URISyntaxException if type specific string format is invalid.
   */
  public static TupleKey fromString(String input, int startIndex) throws URISyntaxException {

    if (startIndex >= input.length()) {
      return new TupleKey(Collections.emptyList(), false);
    }

    // If there's no opening paren, there's only one tuple part. This is a very
    // common case so we special-case it for perf. We must still verify that
    // parens are balanced though.
    if (input.charAt(startIndex) != START_TUPLE) {
      if (!hasBalancedParens(input, startIndex)) {
        throw new URISyntaxException(input, "mismatched paren nesting");
      }
      return new TupleKey(Collections.singletonList(new StringPart(input, startIndex, input.length())), false);
    }

    /* URNs with multiple-part ids overwhelmingly have just two or three parts. As of May 5, a check of
     * existing typed URNs showed
     *
     *  890 single-part URN ids
     *  397 two-part URN ids
     *   86 three-part URN ids
     *   10 four-part URN ids
     *    1 five-part URN id
     *    1 seven-part URN id
     *
     * One-part URN ids should not even reach this point.
     * Specifying an initial capacity of three limits the wasted space for two-part URNs to one slot rather than
     * eight (as it would be for a default ArrayList capacity of 10) while providing enough slots for the 97.5%
     * of URN types which use three parts or fewer -- the rest will require some array expansion.
     */
    List<StringPart> parts = new ArrayList<>(3);

    int numStartedParenPairs = 1; // We know we have at least one starting paren
    int partStart = startIndex + 1;  // +1 to skip opening paren
    for (int i = startIndex + 1; i < input.length(); i++) {
      char c = input.charAt(i);
      if (c == START_TUPLE) {
        numStartedParenPairs++;
      } else if (c == END_TUPLE) {
        numStartedParenPairs--;
        if (numStartedParenPairs < 0) {
          throw new URISyntaxException(input, "mismatched paren nesting");
        }
      } else if (c == DELIMITER) {
        // If numStartedParenPairs == 0, then a comma is ignored because
        // we're not in parens. If numStartedParenPairs >= 2, we're inside an
        // nested paren pair and should also ignore the comma.
        // Don't forget: (foo,bar(zoo,moo)) parsed is ["foo", "bar(zoo,moo)"]!
        if (numStartedParenPairs != 1) {
          continue;
        }

        // Case: "(,,)" or "(,foo)" etc
        if (i - partStart <= 0) {
          throw new URISyntaxException(input, "empty part disallowed");
        }
        parts.add(new StringPart(input, partStart, i));
        partStart = i + 1;
      }
    }

    if (numStartedParenPairs != 0) {
      throw new URISyntaxException(input, "mismatched paren nesting");
    }

    int lastPartEnd = input.charAt(input.length() - 1) == END_TUPLE ? input.length() - 1 : input.length();

    if (lastPartEnd - partStart <= 0) {
      throw new URISyntaxException(input, "empty part disallowed");
    }

    parts.add(new StringPart(input, partStart, lastPartEnd));
    // If we got here and have 1 part then it means that this was parsed from a legacy string
    // representation that encoded single parts inside parentheses.
    return new TupleKey(Collections.unmodifiableList(parts), parts.size() == 1);
  }

  private static boolean hasBalancedParens(String input, int startIndex) {
    int numStartedParenPairs = 0;
    for (int i = startIndex; i < input.length(); i++) {
      char c = input.charAt(i);
      if (c == START_TUPLE) {
        numStartedParenPairs++;
      } else if (c == END_TUPLE) {
        numStartedParenPairs--;
        if (numStartedParenPairs < 0) {
          return false;
        }
      }
    }
    return numStartedParenPairs == 0;
  }

  /**
   * Represents a view into a part of a String. This internal structure helps avoid memory copies and garbage caused by
   * splitting and constructing strings for parts of URNs.
   */
  private static final class StringPart {
    private final String _value;
    private final int _start;
    private final int _end;
    private volatile String _cachedString = null;
    private volatile int _cachedHashCode = 0;

    private StringPart(String value, int start, int end) {
      _value = value;
      _start = start;
      _end = end;
    }

    @Override
    public String toString() {
      if (_cachedString == null) {
        synchronized (this) {
          if (_cachedString == null) {
            if (_start == 0 && _end == _value.length()) {
              _cachedString = _value;
            } else {
              _cachedString = _value.substring(_start, _end);
            }
          }
        }
      }

      return _cachedString;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      StringPart that = (StringPart) o;

      if (length() != that.length()) {
        return false;
      }

      return _value.regionMatches(false, _start, that._value, that._start, that._end - that._start);
    }

    @Override
    public int hashCode() {
      if (_cachedHashCode == 0) {
        synchronized (this) {
          if (_cachedHashCode == 0) {
            int hash = 0;
            for (int i = _start; i < _end; i++) {
              hash = 31 * hash + _value.charAt(i);
            }
            _cachedHashCode = hash;
          }
        }
      }

      return _cachedHashCode;
    }

    public int length() {
      return _end - _start;
    }

    public long parseLong() {
      boolean negative = false;
      boolean atLeastOneDigit = false;
      long result = 0;
      long limit = -Long.MAX_VALUE;
      long minMultiple = limit / 10;

      for (int i = _start; i < _end; i++) {
        char character = _value.charAt(i);
        if (i == _start) {
          if (character == '-') {
            negative = true;
            limit = Long.MIN_VALUE;
            minMultiple = limit / 10;
            continue;
          }
        }

        int digit = Character.digit(character, 10);
        if (digit < 0 || result < minMultiple) {
          throw new NumberFormatException("Invalid number: " + this);
        }
        atLeastOneDigit = true;

        result *= 10;
        if (result < limit + digit) {
          throw new NumberFormatException("Invalid number: " + this);
        }

        result -= digit;
      }

      if (!atLeastOneDigit) {
        throw new NumberFormatException("Invalid number: " + this);
      }

      return negative ? result : (-1 * result);
    }
  }
}