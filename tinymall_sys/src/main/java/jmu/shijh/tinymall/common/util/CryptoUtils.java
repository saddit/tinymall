package jmu.shijh.tinymall.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

public class CryptoUtils {
    private static final String AES_ECB_PKCS5P = "AES/ECB/PKCS5Padding";

    public static Md5Hash MD5Hash(String key, Object salt, int time) {
        return new Md5Hash(key,salt,time);
    }

    public static Md5Hash MD5Hash(String key, Object salt) {
        return MD5Hash(key,salt,1);
    }
    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @param aesKey  加密密码
     * @return b64str
     */
    public static String encryptAES(String content, String aesKey) throws GeneralSecurityException{
        SecretKeySpec key = getSecretKeySpec("AES", aesKey);
        Cipher cipher = Cipher.getInstance(AES_ECB_PKCS5P);// 创建密码器
        byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
        cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
        return Str.b64Encode(cipher.doFinal(byteContent)); // 加密
    }

    private static SecretKeySpec getSecretKeySpec(String alName , String key) throws GeneralSecurityException {
        KeyGenerator gen = KeyGenerator.getInstance(alName);
        gen.init(128, new SecureRandom(key.getBytes()));
        SecretKey secretKey = gen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        return new SecretKeySpec(enCodeFormat, alName);
    }

    /**解密
     * @param content  待解密内容 base64string
     * @param aesKey 解密密钥
     * @return
     */
    public static String decryptAES(String content, String aesKey) throws GeneralSecurityException {
        byte[] contentBytes = Str.b64Decode(content);
        SecretKeySpec key = getSecretKeySpec("AES", aesKey);
        Cipher cipher = Cipher.getInstance(AES_ECB_PKCS5P);// 创建密码器
        cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
        try {
            return StringUtils.toString(cipher.doFinal(contentBytes), "utf-8"); // 解密
        } catch (UnsupportedEncodingException e) {
            throw new GeneralSecurityException("解密失败");
        }
    }
}
