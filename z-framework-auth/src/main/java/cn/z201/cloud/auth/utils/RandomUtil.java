package cn.z201.cloud.auth.utils;

import java.util.Random;

/*
 *
 */
public class RandomUtil {

    public enum CodeType {
        SMALL_LETTER,
        BIG_LETTER,
        ALL_LETTER,
        ALL_NUMBER,
        letterAndNimbers, ALL
    }

    final static String ALL_SMALL_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    final static String ALL_BIG_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    final static String ALL_LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    final static String ALL_NUMBERS = "0123456789";
    final static String letterAndNimbers = "0123456789abcdefghijklmnopqrstuvwxyz";
    final static String ALL = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String getRandomLetter(int length, CodeType type) {
        if (type == CodeType.SMALL_LETTER) {
            return getRandomSmallLetter(length);
        } else if (type == CodeType.BIG_LETTER) {
            return getRandomBigLetter(length);
        } else if (type == CodeType.ALL_LETTER) {
            return getRandomAllLetter(length);
        } else if (type == CodeType.letterAndNimbers) {
            return getletterAndNimbers(length);
        } else if (type == CodeType.ALL_NUMBER) {
            return getRandomAllNumber(length);
        } else {
            return getAll(length);
        }
    }

    private static String getletterAndNimbers(int length) {
        char[] str = new char[length];
        for (int i = 0; i < length; i++) {
            str[i] = letterAndNimbers.charAt(new Random().nextInt(36));
        }

        return new String(str);
    }

    private static String getRandomSmallLetter(int length) {
        char[] str = new char[length];
        for (int i = 0; i < length; i++) {
            str[i] = ALL_SMALL_LETTERS.charAt(new Random().nextInt(26));
        }

        return new String(str);
    }

    private static String getRandomBigLetter(int length) {
        char[] str = new char[length];
        for (int i = 0; i < length; i++) {
            str[i] = ALL_BIG_LETTERS.charAt(new Random().nextInt(26));
        }

        return new String(str);
    }

    private static String getRandomAllLetter(int length) {
        char[] str = new char[length];
        for (int i = 0; i < length; i++) {
            str[i] = ALL_LETTERS.charAt(new Random().nextInt(52));
        }

        return new String(str);
    }

    private static String getRandomAllNumber(int length) {
        char[] str = new char[length];
        for (int i = 0; i < length; i++) {
            str[i] = ALL_NUMBERS.charAt(new Random().nextInt(10));
        }

        return new String(str);
    }

    private static String getAll(int length) {
        char[] str = new char[length];
        for (int i = 0; i < length; i++) {
            str[i] = ALL.charAt(new Random().nextInt(56));
        }

        return new String(str);
    }
}
