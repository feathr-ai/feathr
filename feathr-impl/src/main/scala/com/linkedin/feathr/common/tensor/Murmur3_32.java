package com.linkedin.feathr.common.tensor;

// Re-implemented here to avoid pulling the entire Google commons or similar lib.
// If we decide to support more hash functions, it's better to switch to 3rd-party library.

public class Murmur3_32 implements HashFunction {
  private static final int DEFAULT_SEED = 0xd5abeba8;

  private final int seed;

  public Murmur3_32(Integer seed) {
    this.seed = seed == null ? DEFAULT_SEED : seed;
  }

  public Murmur3_32() {
    this(null);
  }

  @Override
  public long stringToIndex(String string) {
    return murmurhash3_x86_32(string, 0, string.length(), seed) & 0x7fffffff;
  }

  @SuppressWarnings("fallthrough")
  public static int murmurhash3_x86_32(byte[] data, int offset, int len, int seed) {

    final int c1 = 0xcc9e2d51;
    final int c2 = 0x1b873593;

    int h1 = seed;
    int roundedEnd = offset + (len & 0xfffffffc);  // round down to 4 byte block

    for (int i = offset; i < roundedEnd; i += 4) {
      // little endian load order
      int k1 = (data[i] & 0xff) | ((data[i + 1] & 0xff) << 8) | ((data[i + 2] & 0xff) << 16) | (data[i + 3] << 24);
      h1 = murmurhash3_accumulate(k1, h1);
    }

    // tail
    int k1 = 0;

    // This switch is intentionally avoiding 'break' statements.
    switch (len & 0x03) {
      case 3:
        k1 = (data[roundedEnd + 2] & 0xff) << 16;
        // fallthrough
      case 2:
        k1 |= (data[roundedEnd + 1] & 0xff) << 8;
        // fallthrough
      case 1:
        k1 |= (data[roundedEnd] & 0xff);
        k1 *= c1;
        k1 = (k1 << 15) | (k1 >>> 17);  // ROTL32(k1,15);
        k1 *= c2;
        h1 ^= k1;
      default:
    }

    return murmurhash3_finalize(h1, len);
  }

  /**
   * Progrssively generate MurmurHash3 for integers. Assumes that integers are already converted into little-endian
   * @param input Next int in little-endian format to add to hash
   * @param seed  Initial seed or previously accumulated hash
   * @return accumulated hash
   */
  public static int murmurhash3_accumulate(int input, int seed) {

    final int c1 = 0xcc9e2d51;
    final int c2 = 0x1b873593;

    int h1 = seed;
    int k1 = input;
    k1 *= c1;
    k1 = (k1 << 15) | (k1 >>> 17);  // ROTL32(k1,15);
    k1 *= c2;

    h1 ^= k1;
    h1 = (h1 << 13) | (h1 >>> 19);  // ROTL32(h1,13);
    h1 = h1 * 5 + 0xe6546b64;

    return h1;
  }

  /**
   * Generates the final MumrmurHash3 from accumulated hash
   * @param accumulatedHash Output of murmurhash3_accumulate
   * @param numBytes Number of bytes that were accumulated to generate accumulatedHash
   * @return Final murmur3 hash for numBytes integers
   */
  public static int murmurhash3_finalize(int accumulatedHash, int numBytes) {
    int h1 = accumulatedHash;
    h1 ^= numBytes;

    h1 ^= h1 >>> 16;
    h1 *= 0x85ebca6b;
    h1 ^= h1 >>> 13;
    h1 *= 0xc2b2ae35;
    h1 ^= h1 >>> 16;

    return h1;
  }

  /** Returns the MurmurHash3_x86_32 hash of the UTF-8 bytes of the String without actually encoding
   * the string to a temporary buffer.  This is more than 2x faster than hashing the result
   * of String.getBytes().
   */
  public static int murmurhash3_x86_32(CharSequence data, int offset, int len, int seed) {

    final int c1 = 0xcc9e2d51;
    final int c2 = 0x1b873593;

    int h1 = seed;

    int pos = offset;
    int end = offset + len;
    int k1 = 0;
    int shift = 0;
    int nBytes = 0;   // length in UTF8 bytes
    int k2;
    int bits;

    while (pos < end) {
      int code = data.charAt(pos++);
      if (code < 0x80) {
        k2 = code;
        bits = 8;
      } else if (code < 0x800) {
        k2 = (0xC0 | (code >> 6)) | ((0x80 | (code & 0x3F)) << 8);
        bits = 16;
      } else if (code < 0xD800 || code > 0xDFFF || pos >= end) {
        // we check for pos>=end to encode an unpaired surrogate as 3 bytes.
        k2 = (0xE0 | (code >> 12)) | ((0x80 | ((code >> 6) & 0x3F)) << 8) | ((0x80 | (code & 0x3F)) << 16);
        bits = 24;
      } else {
        // surrogate pair
        // int utf32 = pos < end ? (int) data.charAt(pos++) : 0;
        int utf32 = (int) data.charAt(pos++);
        utf32 = ((code - 0xD7C0) << 10) + (utf32 & 0x3FF);
        k2 = (0xff & (0xF0 | (utf32 >> 18))) | ((0x80 | ((utf32 >> 12) & 0x3F))) << 8
            | ((0x80 | ((utf32 >> 6) & 0x3F))) << 16 | (0x80 | (utf32 & 0x3F)) << 24;
        bits = 32;
      }

      k1 |= k2 << shift;

      // int used_bits = 32 - shift;  // how many bits of k2 were used in k1.
      // int unused_bits = bits - used_bits; //  (bits-(32-shift)) == bits+shift-32  == bits-newshift

      shift += bits;
      if (shift >= 32) {
        // mix after we have a complete word

        k1 *= c1;
        k1 = (k1 << 15) | (k1 >>> 17);  // ROTL32(k1,15);
        k1 *= c2;

        h1 ^= k1;
        h1 = (h1 << 13) | (h1 >>> 19);  // ROTL32(h1,13);
        h1 = h1 * 5 + 0xe6546b64;

        shift -= 32;
        // unfortunately, java won't let you shift 32 bits off, so we need to check for 0
        if (shift != 0) {
          k1 = k2 >>> (bits - shift);   // bits used == bits - newshift
        } else {
          k1 = 0;
        }
        nBytes += 4;
      }
    } // inner

    // handle tail
    if (shift > 0) {
      nBytes += shift >> 3;
      k1 *= c1;
      k1 = (k1 << 15) | (k1 >>> 17);  // ROTL32(k1,15);
      k1 *= c2;
      h1 ^= k1;
    }

    // finalization
    h1 ^= nBytes;

    // fmix(h1);
    h1 ^= h1 >>> 16;
    h1 *= 0x85ebca6b;
    h1 ^= h1 >>> 13;
    h1 *= 0xc2b2ae35;
    h1 ^= h1 >>> 16;

    return h1;
  }
}
