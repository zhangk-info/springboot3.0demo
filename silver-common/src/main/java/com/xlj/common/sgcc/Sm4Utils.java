package com.xlj.common.sgcc;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.junit.jupiter.api.Test;
import org.springframework.util.Base64Utils;

/**
 * sm4工具类
 *
 * @author zhangkun
 */
public class Sm4Utils {

    public static final String KEY = "gdMfxJAPzBItGBxo724Byg==";

    /**
     * 加密
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String decrypt(String str) throws Exception {
        try {
            SymmetricCrypto sm4 = SmUtil.sm4(Base64Utils.decodeFromString(KEY));
            return sm4.decryptStr(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解密
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String encrypt(String str) throws Exception {
        SymmetricCrypto sm4 = SmUtil.sm4(Base64Utils.decodeFromString(KEY));
        return sm4.encryptHex(str);
    }

    /**
     * 随机生成sm4需要的secretKey
     *
     * @return
     */
    private String getKey() {
        byte[] key = SmUtil.sm4().getSecretKey().getEncoded();
        return Base64Utils.encodeToString(key);
    }

    /**
     * 测试
     */
    @Test
    public void test() {
        String str = "123456";
        try {
            // 得到随机的key
            byte[] key = SmUtil.sm4().getSecretKey().getEncoded();
            System.out.println(Base64Utils.encodeToString(key));

            String encodeStr = encrypt(str);
            System.out.println(encodeStr);
            System.out.println(decrypt(encodeStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
