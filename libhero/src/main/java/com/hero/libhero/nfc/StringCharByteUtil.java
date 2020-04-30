package com.hero.libhero.nfc;

import com.hero.libhero.mydb.LogUtil;

import java.math.BigInteger;

/**
 * 创建 by hero
 * 时间 2020/3/27
 * 类名 字符串互转
 */
public class StringCharByteUtil {

    /*
     * 字节转10进制
     */
    public static int byte2Int(byte b) {
        int r = (int) b;
        return r;
    }

    /*
     * 10进制转字节
     */
    public static byte int2Byte(int i) {
        byte r = (byte) i;
        return r;
    }

    //10进制数字转换为16进制
    public static String numToHex(int num) {
        String hex = Integer.toHexString(num);
        LogUtil.e(num + "==numToHex=" + hex);
        return hex;
    }

    //16进制字符转换为10进制数字
    public static int hexToNum(String hexs) {
        BigInteger bigint = new BigInteger(hexs, 16);
        int numb = bigint.intValue();
        LogUtil.e(hexs + "==numToHex=" + numb);
        return numb;
    }

    /**
     * 普通字符串转为16进制字符串方法
     */
    public static String strToHexStr(String str2) {

        String str = "";
        for (int i = 0; i < str2.length(); i++) {
            int ch = (int) str2.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        LogUtil.e("strToHexStr=" + str);
        return str;
    }

    /**
     * 16进制转换成为string类型字符串
     *
     * @param s
     * @return
     */
    public static String hexToStr(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        LogUtil.e("hexToStr=" + s);
        return s;
    }

    /**
     * 字符串转换unicode
     */
    public static String strToUnicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }
        LogUtil.e("strToUnicode=" + unicode.toString());
        return unicode.toString();
    }

    /**
     * unicode 转字符串
     */
    public static String unicodeToStr(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);
            // 追加成string
            string.append((char) data);
        }
        LogUtil.e("unicodeToStr=" + string.toString());
        return string.toString();
    }


    /**
     * 16进制直接转换成为字符串(无需Unicode解码)
     *
     * @param hexStr
     * @return
     */
    public static String hexStrToStr(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        LogUtil.e("hexStrToStr=" + new String(bytes));
        return new String(bytes);
    }



    /////////////////////////////////////////


    /**
     * hex字符串转byte数组
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte数组结果
     */
    public static byte[] hexToByteArray(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1) {
            //奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            //偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        LogUtil.e("hexToByteArray-->result.length=" + result.length);
        return result;
    }


    /**
     * Hex字符串转byte
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte
     */
    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    /**
     * 将byte数组转换为字符串形式表示的十六进制数方便查看
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String s = Integer.toHexString(bytes[i] & 0xff);
            if (s.length() < 2) {
                sBuffer.append('0');
            }
            sBuffer.append(s + " ");

        }
        LogUtil.e("bytesToHexString-->sBuffer=" + sBuffer.toString());
        return sBuffer.toString();
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789abcdef".indexOf(c);
    }


    /**
     * 10进制字符序列转换为16进制字符串
     */
    public static String bytesToHexString2(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }


}
