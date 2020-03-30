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
    public static String strTo16Str(String str2) {
//        String hs = "";
//
//        try {
//            byte[] byStr = str.getBytes("UTF-8");
//
//            for (int i = 0; i < byStr.length; i++) {
//                String temp = "";
//                temp = (Integer.toHexString(byStr[i] & 0xFF));
//                if (temp.length() == 1) temp = "%0" + temp;
//                else temp = "%" + temp;
//                hs = hs + temp;
//            }
//            LogUtil.e("hs=" + hs.toUpperCase());
//            return hs.toUpperCase();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//
//        return "";

        String str = "";
        for (int i = 0; i < str2.length(); i++) {
            int ch = (int) str2.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        LogUtil.e("hs=" + str);
        return str;
    }

    /**
     * 16进制转换成为string类型字符串
     *
     * @param s
     * @return
     */
    public static String s16ToStr(String s) {
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
        LogUtil.e("hs=" + s);
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
        LogUtil.e("hs=" + unicode.toString());
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
        LogUtil.e("hs=" + string.toString());
        return string.toString();
    }

    /**
     * 字符串转换成为16进制(无需Unicode编码)
     *
     * @param str
     * @return
     */
    public static String strToHexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            // sb.append(' ');
        }
        LogUtil.e(str + "strToHexStr=" + sb.toString().trim());
        return sb.toString().trim();
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
        LogUtil.e("hs=" + new String(bytes));
        return new String(bytes);
    }


    /**
     * 将字符串形式表示的十六进制数转换为byte数组
     */
    public static byte[] hexStringToBytes(String hex) {


        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (charToByte(achar[pos]) << 4 | charToByte(achar[pos + 1]));
        }
        LogUtil.e("result.length=" + result.length);
        return result;
    }


    /**
     * 将byte数组转换为字符串形式表示的十六进制数方便查看
     */
    public static String bytesToString(byte[] bytes) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String s = Integer.toHexString(bytes[i] & 0xff);
            if (s.length() < 2)
                sBuffer.append('0');
            sBuffer.append(s + " ");
        }
        return sBuffer.toString();
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789abcdef".indexOf(c);
    }


    public static byte[] str2bytearray(String str) {
        int length = str.length();
        int arrlength = length >> 1;
        if ((length & 1) == 1) {
            arrlength++;
        }
        byte[] ret = new byte[arrlength];
        int i = 0, j = 0;
        char ch0, ch1;
        if ((length & 1) == 1) {
            ch1 = str.charAt(i++);
            if (ch1 <= '9' && ch1 >= '0') {
                ch1 -= '0';
            } else if (ch1 >= 'A' && ch1 <= 'F') {
                ch1 -= ('A' - 10);
            }
            ret[j++] = (byte) ch1;
        }
        for (; i < length; i += 2, j++) {
            ch0 = str.charAt(i);
            ch1 = str.charAt(i + 1);
            if (ch0 <= '9' && ch0 >= '0') {
                ch0 -= '0';
            } else if (ch0 >= 'A' && ch0 <= 'F') {
                ch0 -= ('A' - 10);
            }
            if (ch1 <= '9' && ch1 >= '0') {
                ch1 -= '0';
            } else if (ch1 >= 'A' && ch1 <= 'F') {
                ch1 -= ('A' - 10);
            }
            ret[j] = (byte) ((ch0 << 4) | ch1);
        }
        LogUtil.e(str + "==ret.length=" + ret.length);
        return ret;
    }


    public static void main(String[] args) {
        String text = "ZLSCSXS";
        String hexString = "EF EB EC EA ED E0 F0 FF F9 FE 01 10 A0 0A 0F AB ae 9e 9c 2c 30 ef ff ";
        byte[] bytes = hexStringToBytes(hexString);
        String sBuffer = bytesToString(bytes);
        LogUtil.e(sBuffer);
    }
//////////////////////十六进制字符串转byte和byte[]//////////////////////////

    /**
     * Hex转byte,hex只能含两个字符，如果是一个字符byte高位会是0
     */
    public static byte hexTobyte(String hex) {
        return (byte) Integer.parseInt(hex, 16);
    }

    /**
     * Hex转byte[]，两种情况，Hex长度为奇数最后一个字符会被舍去
     */
    public static byte[] hexTobytes(String hex) {
        if (hex.length() < 1) {
            return null;
        } else {
            byte[] result = new byte[hex.length() / 2];
            int j = 0;
            for (int i = 0; i < hex.length(); i += 2) {
                result[j++] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
            }
            LogUtil.e("result.length=" + result.length);
            return result;
        }
    }


//////////////////byte和byte[]转十六进制字符串/////////////////////////

    /**
     * byte转Hex
     */
    public static String byteToHex(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * byte[]转Hex
     */
    public static String bytesToHex(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() < 2) {
                hex = "0" + hex;
            }
            sb.append(hex.toUpperCase());
        }

        return sb.toString();
    }

    ////////////////////////////////////////////
}
