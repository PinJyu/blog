package cn.nhmt.blog.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Description: 加密
 * @Date: 2020-05-22 23:01
 * @Author: PinJyu
 * @Version: 1.0
 **/
public abstract class DigestUtil {

    public static String byte2Hex(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        String temp;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1)
                sb.append("0");
            sb.append(temp);
        }
        return sb.toString();
    }

    public static String sha256(String pwdPlain) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] digest = sha256.digest(pwdPlain.getBytes(StandardCharsets.UTF_8));
        return byte2Hex(digest);
    }

}
