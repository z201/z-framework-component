package cn.z201.cloud.encrypt.core.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
 *
 * @author z201.coding@gmail.com
 */
@Slf4j
public class AESEncryptOperator {

    /**
     * key，可自行修改
     */
    private static String KEY = "aaaaaaaaaaaaaaa";
    /**
     * 偏移量,可自行修改
     */
    private static String IV_PARAMETER = "PuQL2Dsk74deXwGo";

    private static String ASCII = "ASCII";

    private static String AES = "AES";

    private static String UTF_8 = "utf-8";

    private static String CIPHER = "AES/CBC/PKCS5Padding";

    private static AESEncryptOperator instance = null;

    public AESEncryptOperator() {

    }

    public static AESEncryptOperator getInstance() {
        if (instance == null) {
            instance = new AESEncryptOperator();
        }
        return instance;
    }

    /**
     * 加密
     *
     * @param sSrc
     * @return
     * @throws Exception
     */
    public static String encrypt(String sSrc) {
        return encrypt(KEY, IV_PARAMETER, sSrc);
    }


    /**
     * 加密
     *
     * @param encData   原始数据
     * @param secretKey
     * @param vector
     * @return
     */
    public static String encrypt(String secretKey, String vector, String encData) {
        if (secretKey == null || encData == null) {
            return null;
        }
        if (secretKey.length() != 16) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(CIPHER);
            byte[] raw = secretKey.getBytes();
            SecretKeySpec secretKeySpec = new SecretKeySpec(raw, AES);
            IvParameterSpec iv = new IvParameterSpec(vector.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
            byte[] encrypted = cipher.doFinal(encData.getBytes(UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            log.error(" 加密失败 e {} \n {} ", ex.getMessage(), ex);
            return null;
        }
    }


    public static String decrypt(String key, String ivs, String sSrc) {
        try {
            byte[] raw = key.getBytes(ASCII);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
            Cipher cipher = Cipher.getInstance(CIPHER);
            IvParameterSpec iv = new IvParameterSpec(ivs.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted = Base64.getDecoder().decode(sSrc);
            byte[] original = cipher.doFinal(encrypted);
            return new String(original, UTF_8);
        } catch (Exception ex) {
            log.error(" 解密失败 e {} \n {} ", ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * 解密
     * @param sSrc
     * @return
     */
    public static String decrypt(String sSrc) {
        return decrypt(KEY, IV_PARAMETER, sSrc);
    }


}
