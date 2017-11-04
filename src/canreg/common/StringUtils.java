//<ictl.co>
package canreg.common;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * Created by amin on 8/10/2015.
 */
public class StringUtils {

    static CharsetEncoder asciiEncoder =
            Charset.forName("US-ASCII").newEncoder(); // or "ISO-8859-1" for ISO Latin 1

    StringUtils() {
    }

    public static boolean isPureAscii(String v) {
        return asciiEncoder.canEncode(v);
    }

    public static boolean isNumeric(String str) {
        return str.matches("[+-]?\\d*(\\.\\d+)?");
    }

    public static boolean isAscii(String str) {
        try {
            byte[] buffer = str.getBytes("UTF-8");
            for (int i = 0; i < buffer.length; ++i) {
                if (buffer[i] > 0 && buffer[i] < 0x7A) {
                    return true;
                }
            }
            return false;
        } catch (UnsupportedEncodingException e) {
            return true;
        }
    }
}

//</ictl.co>