package site.xiaguliuxiang.crack.dbvisualizer.util;

import javax.xml.bind.DatatypeConverter;

/**
 * @author xiaguliuxiang@gmail.com
 * @date 2019-09-09 20:00:00
 */
public class Base64 {

    public static String encodeToString(byte[] src) {
        return DatatypeConverter.printBase64Binary(src);
    }

    public static byte[] decode(String src) {
        return DatatypeConverter.parseBase64Binary(src);
    }

}
