package cn.z201.cloud.auth.utils;

import cn.z201.cloud.auth.exception.AuthException;
import cn.z201.cloud.auth.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

/**
 * @author wzn
 * @author z201.coding@gmail.com
 **/
@Slf4j
public class WXMiniAESOperator {

    // 解密
    public static String decrypt(String encryptedData, String sessionKey, String ivParameter) {
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            SecretKeySpec skeySpec = new SecretKeySpec(new Base64().decode(sessionKey), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            IvParameterSpec iv = new IvParameterSpec(new Base64().decode(ivParameter));
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            /**
             * 先用base64解密
             */
            byte[] encrypted = new Base64().decode(encryptedData);
            byte[] original = cipher.doFinal(encrypted);
            return new String(original, "utf8");
        } catch (Exception ex) {
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "注册失败,请从新尝试","微信注册失败,encryptedData 解密失败");
        }
    }
}
