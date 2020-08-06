package cn.z201.cloud.auth.utils;


import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author z201.coding@gmail.com
 **/
public class EncryptPassword {

    private final static String salt = "6849794470754667710";

    public static String encrypt(String password) {
        String newPassword = salt + password;
        return DigestUtils.md5Hex(newPassword);
    }

}
