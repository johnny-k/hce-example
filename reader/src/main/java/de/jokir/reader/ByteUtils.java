package de.jokir.reader;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created by johnny on 18.12.2016.
 */
public class ByteUtils {

    private static final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};


    public static String toHexString(byte[] array) {
        if (array == null || array.length == 0)
            return "";

        StringBuilder sb = new StringBuilder();


        for (int i = 0; i < array.length; i++) {
            sb.append(Integer.toHexString(array[i] & 0xFF));
            if (i + 1 < array.length)
                sb.append(" ");
        }

        return sb.toString();
    }


    public static String byteArrayToHexString(byte[] array) {
        if (array == null || array.length == 0)
            return "";

        StringBuilder sb = new StringBuilder(array.length * 2);
        int currentByte;
        for (int i = 0; i < array.length; i++) {
            currentByte = array[i] & 0xFF;

            sb.append(hexArray[currentByte >>> 4]);// upper nibble
            sb.append(hexArray[currentByte & 0x0F]);// lower nibble
        }
        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String hexString) throws IllegalArgumentException {
        int len = hexString.length();
        if (len % 2 == 1) {
            throw new IllegalArgumentException("hex string needs even number of characters");
        }

        byte[] data = new byte[len / 2]; // two chars become 1 byte
        for (int i = 0; i < len; i += 2) {
            // Convert each character into a integer (base-16), then bit-shift into place
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) // upper nibble
                    + Character.digit(hexString.charAt(i + 1), 16)); //lower nibble
        }
        return data;
    }

    public static byte[] concatArrays(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    public static byte[] stringToAsciiByteArray(String str) {
        if (str == null || str.length() == 0)
            return new byte[0];

        return str.getBytes(StandardCharsets.US_ASCII);
    }
}
