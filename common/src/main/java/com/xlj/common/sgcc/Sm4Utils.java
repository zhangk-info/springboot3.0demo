package com.xlj.common.sgcc;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

/**
 * sm4工具类
 *
 * @author zhangkun
 */
public class Sm4Utils {

    public static final String KEY = "gdMfxJAPzBItGBxo724Byg==";

    private final SM4 sm4;

    public Sm4Utils() {
        this.sm4 = SmUtil.sm4(Base64Utils.decodeFromString(KEY));
    }

    /**
     * 加密
     *
     * @param str
     * @return
     * @throws Exception
     */
    public String decrypt(String str) throws Exception {
        try {
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
    public String encrypt(String str) throws Exception {
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
        String str = "fxJAPzBItGBxo";
        try {
            String encodeStr = encrypt(str);
            System.out.println(encodeStr);
            System.out.println(decrypt(encodeStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
