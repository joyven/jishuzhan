package com.openmind.helper;

import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;

/**
 * PasswordHelper
 *
 * @author zhoujunwen
 * @date 2020-01-16
 * @time 11:20
 * @desc
 */
public class PasswordHelper {

    /**
     * 密码加密器
     *
     * @param password 原password
     * @param strength 密码强度，4-31之间
     * @param random   SecureRandom
     * @return
     */
    public static String encode(String password, int strength, SecureRandom random) {
        String salt;
        if (strength > 0) {
            if (random != null) {
                salt = BCrypt.gensalt(strength, random);
            } else {
                salt = BCrypt.gensalt(strength);
            }
        } else {
            salt = BCrypt.gensalt();
        }
        return BCrypt.hashpw(password, salt);
    }

    /**
     * @param password
     * @param hashed
     * @return
     */
    public static boolean matcher(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}
