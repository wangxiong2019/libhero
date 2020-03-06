package com.hero.libhero.utils;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

    // 算法名
    public static final String KEY_NAME = "AES";


    private static String KEY = "";
    private static String IV = "";

    public static String getKEY() {
        return KEY;
    }

    public static void setKEY(String KEY) {
        AESUtil.KEY = KEY;
    }

    public static String getIV() {
        return IV;
    }

    public static void setIV(String IV) {
        AESUtil.IV = IV;
    }

    //加密
    public static String Encrypt(String content) {
        try {
            byte[] raw = getKEY().getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_NAME);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
            //使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec ips = new IvParameterSpec(getIV().getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ips);
            byte[] encrypted = cipher.doFinal(content.getBytes());
            return Base64Util.encode(encrypted);
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    //解密
    public static String Decrypt(String content) {
        try {
            byte[] raw = getKEY().getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_NAME);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(getIV().getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ips);
            byte[] encrypted1 = Base64Util.decode(content);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original);
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        String content = "wx147258";
        //加密
        String ens = Encrypt(content);
        System.out.println("加密后：" + ens);
        String des0 = Decrypt(ens);
        System.out.println("解密后：" + des0);

        //解密

        String data = "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";
        String des = Decrypt(data);
        System.out.println("解密后：" + des);
    }


}
