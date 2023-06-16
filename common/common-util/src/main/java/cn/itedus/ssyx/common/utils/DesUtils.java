package cn.itedus.ssyx.common.utils;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-13 18:11
 * @description: DES加密算法
 */

public class DesUtils {
    private DesUtils() {
    }


    /**
     * iv向量
     */
    private static final String DESIV = "12345678";

//    /**
//     * AlgorithmParameterSpec
//     */
//    private static AlgorithmParameterSpec IV = null;

    /**
     * SHA1PRNG
     */
    private static final String SHA1PRNG = "SHA1PRNG";


    /**
     * 密钥
     */
    public static final String CRYPT_KEY = "10f5dd7c2d45d247";

    /**
     * CBC加密模式
     */
    private static final String DES_CBC_PKCS5PADDING = "DES/CBC/PKCS5Padding";

    /**
     * OFB加密模式
     */
    private static final String DES_OFB_PKCS5PADDING = "DES/OFB/PKCS5Padding";

    /**
     * CFB加密模式
     */
    private static final String DES_CFB_PKCS5_PADDING = "DES/CFB/PKCS5Padding";

    /**
     * ECB加密模式
     */
    private static final String DES_ECB_PKCS5_PADDING = "DES/ECB/PKCS5Padding";

    /**
     * DES模式
     */
    private static final String DES = "DES";

    /**
     * 加密模式
     */
    private static final int ENCRYPT_MODE = 1;

    /**
     * 解密模式
     */
    private static final int DECRYPT_MODE = 2;

    /**
     * 字符串des加密
     *
     * @param data   需要加密的字符串
     * @param mode   加密模式 (ECB/CBC/OFB/CFB)
     * @param secret 密钥
     * @return 加密结果
     * @throws Exception 异常
     */
    public static String encrypt(String data, Mode mode, String secret) throws Exception {
        Cipher cipher = getPattern(mode, ENCRYPT_MODE, secret);
        byte[] pasByte = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(pasByte);
    }

    /**
     * 字符串des加密
     *
     * @param data   需要加密的字符串
     * @param secret 密钥
     * @return 加密结果
     * @throws Exception 异常
     */
    public static String encrypt(String data, String secret) throws Exception {
        Cipher cipher = getPattern(Mode.CBC, ENCRYPT_MODE, secret);
        byte[] pasByte = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(pasByte);
    }

    /**
     * 字符串des加密
     *
     * @param data 需要加密的字符串
     * @return 加密结果
     * @throws Exception 异常
     */
    public static String encrypt(String data) throws Exception {
        Cipher cipher = getPattern(Mode.CBC, ENCRYPT_MODE, CRYPT_KEY);
        byte[] pasByte = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(pasByte);
    }

    /**
     * 字符串des解密
     *
     * @param data 需要解密的字符串
     * @param mode 解密模式 (ECB/CBC/OFB/CFB)
     * @return 解密结果
     * @throws Exception 异常
     * @author sucb
     */
    public static String decrypt(String data, Mode mode, String secret) throws Exception {
        Cipher cipher = getPattern(mode, DECRYPT_MODE, secret);
        byte[] pasByte = cipher.doFinal(Base64.getDecoder().decode(data));
        return new String(pasByte, StandardCharsets.UTF_8);
    }

    /**
     * 字符串des解密
     *
     * @param data 需要解密的字符串
     * @return 解密结果
     * @throws Exception 异常
     * @author sucb
     */
    public static String decrypt(String data, String secret) throws Exception {
        Cipher cipher = getPattern(Mode.CBC, DECRYPT_MODE, secret);
        byte[] pasByte = cipher.doFinal(Base64.getDecoder().decode(data));
        return new String(pasByte, StandardCharsets.UTF_8);
    }

    /**
     * 字符串des解密
     *
     * @param data 需要解密的字符串
     * @return 解密结果
     * @throws Exception 异常
     * @author sucb
     */
    public static String decrypt(String data) throws Exception {
        Cipher cipher = getPattern(Mode.CBC, DECRYPT_MODE, CRYPT_KEY);
        byte[] pasByte = cipher.doFinal(Base64.getDecoder().decode(data));
        return new String(pasByte, StandardCharsets.UTF_8);
    }

    /**
     * 文件 file 进行加密并保存目标文件 destFile 中
     *
     * @param file     要加密的文件 如 c:/test/file.txt
     * @param destFile 加密后存放的文件名 如 c:/ 加密后文件 .txt
     * @param mode     加密模式 (ECB/CBC/OFB/CFB)
     * @return 加密结果   0：异常 1：加密成功； 5：未找到需要加密的文件
     */
    public int encryptFile(String file, String destFile, Mode mode) throws Exception {
        int result = 0;
        try {
            Cipher cipher = getPattern(mode, ENCRYPT_MODE, CRYPT_KEY);
            InputStream is = new FileInputStream(file);
            OutputStream out = new FileOutputStream(destFile);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = cis.read(buffer)) > 0) {
                out.write(buffer, 0, r);
            }
            cis.close();
            is.close();
            out.close();
            result = 1;
        } catch (FileNotFoundException e) {
            result = 5;
        }
        return result;
    }

    /**
     * 文件 file 进行解密并保存目标文件 destFile 中
     *
     * @param file     要解密的文件 如 c:/test/file.txt
     * @param destFile 解密后存放的文件名 如 c:/ 解密后文件 .txt
     * @param mode     解密模式 (ECB/CBC/OFB/CFB)
     * @return 解密结果 0：解密异常；1：解密正常；5：未找到需要解密的文件
     * @author sucb
     * @date 2017年3月2日下午7:58:56
     */
    public int decryptFile(String file, String destFile, Mode mode) throws Exception {
        int result = 0;
        try {
            Cipher cipher = getPattern(mode, DECRYPT_MODE, CRYPT_KEY);
            InputStream is = new FileInputStream(file);
            OutputStream out = new FileOutputStream(destFile);
            CipherOutputStream cos = new CipherOutputStream(out, cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = is.read(buffer)) >= 0) {
                cos.write(buffer, 0, r);
            }
            cos.close();
            out.close();
            is.close();
            result = 1;
        } catch (FileNotFoundException e) {
            result = 5;
        }
        return result;
    }

    /**
     * 通过密钥获得key
     *
     * @param secretKey 密钥
     */
    public static Key getKey(String secretKey) throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance(SHA1PRNG);
        secureRandom.setSeed(secretKey.getBytes());
        KeyGenerator generator = KeyGenerator.getInstance("DES");
        generator.init(secureRandom);
        return generator.generateKey();
    }

    /**
     * 初始化cipher
     *
     * @param mode       加密/解密模式 (ECB/CBC/OFB/CFB)
     * @param cipherMode cipher工作模式 1：加密； 2：解密
     * @return cipher
     * @throws Exception 异常
     */
    private static Cipher getPattern(Mode mode, int cipherMode, String secret) throws Exception {
        AlgorithmParameterSpec IV = new IvParameterSpec(DESIV.getBytes(StandardCharsets.UTF_8));
        secret = secret == null ? CRYPT_KEY : secret;
        Key key = getKey(secret);
        Cipher cipher;
        switch (mode) {
            case OFB:
                cipher = Cipher.getInstance(DES_OFB_PKCS5PADDING);
                cipher.init(cipherMode, key, IV);
                break;
            case CFB:
                cipher = Cipher.getInstance(DES_CFB_PKCS5_PADDING);
                cipher.init(cipherMode, key, IV);
                break;
            case DES:
                cipher = Cipher.getInstance(DES);
                cipher.init(cipherMode, key);
                break;
            case ECB:
                cipher = Cipher.getInstance(DES_ECB_PKCS5_PADDING);
                cipher.init(cipherMode, key);
                break;
            default:
                cipher = Cipher.getInstance(DES_CBC_PKCS5PADDING);
                cipher.init(cipherMode, key, IV);
                break;
        }
        return cipher;
    }


    public enum Mode {

        /**
         * DES
         */
        DES,

        /**
         * CFB
         */
        CFB,

        /**
         * OFB
         */
        OFB,

        /**
         * CBC,默认模式
         */
        CBC,

        /**
         * ECB
         */
        ECB;
    }



}
