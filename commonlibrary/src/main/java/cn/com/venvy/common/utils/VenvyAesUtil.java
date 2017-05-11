
package cn.com.venvy.common.utils;

import android.annotation.SuppressLint;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密解密算法
 *
 * @author long
 */
public class VenvyAesUtil {

    // /** 算法/模式/填充 **/
    private static final String CipherMode = "AES/CBC/PKCS7Padding";

    private static final int LENGTH = 32;


    public static String encrypt(String key, String iv, String content) throws Exception {
        byte[] rawKey = getRawKey(key.getBytes());
        byte[] result = encrypt(rawKey, iv, content.getBytes());
        return toHex(result);
    }

    public static String decrypt(String key, String iv, String content) throws Exception {
        byte[] rawKey = getRawKey(key.getBytes());
        byte[] enc = toByte(content);
        byte[] result = decrypt(rawKey,iv, enc);
        return new String(result);
    }

    @SuppressLint("TrulyRandom")
    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = null;
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.JELLY_BEAN) {
            sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        } else {
            sr = SecureRandom.getInstance("SHA1PRNG");
        }
        sr.setSeed(seed);
        kgen.init(256, sr); // 256 bits or 128 bits,192bits
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    private static byte[] encrypt(byte[] key, String iv, byte[] src) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance(CipherMode);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, createIV(iv));
        byte[] encrypted = cipher.doFinal(src);
        return encrypted;
    }

    private static byte[] decrypt(byte[] key, String iv, byte[] encrypted)
            throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance(CipherMode);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, createIV(iv));
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(
                    hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private final static String HEX = "0123456789ABCDEF";

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }



//    // /** 创建密钥 **/
//    private static SecretKeySpec createKey(String key) {
//        byte[] data = null;
//        if (key == null) {
//            key = "";
//        }
//        StringBuffer sb = new StringBuffer(LENGTH);
//        sb.append(key);
//        while (sb.length() < LENGTH) {
//            sb.append("0");
//        }
//        if (sb.length() > LENGTH) {
//            sb.setLength(LENGTH);
//        }
//
//        try {
//            data = sb.toString().getBytes("UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return new SecretKeySpec(data, "AES");
//    }

    private static IvParameterSpec createIV(String password) {
        byte[] data = null;
        if (password == null) {
            password = "";
        }
        StringBuffer sb = new StringBuffer(LENGTH);
        sb.append(password);
        while (sb.length() < LENGTH) {
            sb.append("0");
        }
        if (sb.length() > LENGTH) {
            sb.setLength(LENGTH);
        }

        try {
            data = sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new IvParameterSpec(data);
    }

//    // /** 加密字节数据 **/
//    public static byte[] encrypt(byte[] content, String password, String iv) {
//        try {
//            SecretKeySpec key = createKey(password);
//            Cipher cipher = Cipher.getInstance(CipherMode);
//            cipher.init(Cipher.ENCRYPT_MODE, key, createIV(iv));
//            byte[] result = cipher.doFinal(content);
//            return result;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    // /** 加密(结果为16进制字符串) **/
//    public static String encrypt(String content, String password, String iv) {
//        byte[] data = null;
//        try {
//            data = content.getBytes("UTF-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        data = encrypt(data, password, iv);
//        String result = VenvyStringUtil.byte2hex(data);
//        return result;
//    }
//
//    // /** 解密字节数组 **/
//    public static byte[] decrypt(byte[] content, String password, String iv) {
//        try {
//            SecretKeySpec key = createKey(password);
//            Cipher cipher = Cipher.getInstance(CipherMode);
//            cipher.init(Cipher.DECRYPT_MODE, key, createIV(iv));
//            byte[] result = cipher.doFinal(content);
//            return result;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    // /** 解密 **/
//    public static String decrypt(String content, String password, String iv) {
//        byte[] data = null;
//        try {
//            data = VenvyStringUtil.hex2byte(content);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        data = decrypt(data, password, iv);
//        if (data == null)
//            return null;
//        String result = null;
//        try {
//            result = new String(data, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
}
