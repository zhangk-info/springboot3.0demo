package com.xlj.common.sgcc;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import com.xlj.common.encrypted_transfer.Sm2EnableCondition;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.PublicKey;

/**
 * sm2加密工具类
 * 使用hutool
 * Hutool针对Bouncy Castle做了简化包装，用于实现国密算法中的SM2、SM3、SM4。
 * BCUtil
 *
 * @author zhangkun
 */
@Slf4j
@Conditional(Sm2EnableCondition.class)
public class Sm2Utils {


    private final SM2 sm2A;
    private final SM2 sm2B;
    @Value("${sm2.private-key-a}")
    private String privateKeyA;
    @Value("${sm2.public-key-a}")
    private String publicKeyA;
    @Value("${sm2.private-key-b}")
    private String privateKeyB;
    @Value("${sm2.public-key-b}")
    private String publicKeyB;

    public Sm2Utils() {
        this.sm2A = sm2A();
        this.sm2B = sm2B();
    }

    public SM2 sm2A() {
        //提取公钥点
        SM2 sm2 = new SM2(privateKeyA, publicKeyA);
        SM2Engine.Mode mode = SM2Engine.Mode.C1C3C2;
        sm2.setMode(mode);
        return sm2;
    }

    public SM2 sm2B() {
        //提取公钥点
        SM2 sm2 = new SM2(privateKeyB, publicKeyB);
        SM2Engine.Mode mode = SM2Engine.Mode.C1C3C2;
        sm2.setMode(mode);
        return sm2;
    }

    /**
     * 解密
     *
     * @param str
     * @return
     * @throws Exception
     */
    public String decryptA(String str) throws Exception {
        try {
            return sm2A.decryptStr(str, KeyType.PrivateKey);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 加密
     *
     * @param str
     * @return
     * @throws Exception
     */
    public String encryptA(String str) throws Exception {
        return sm2A.encryptHex(str, KeyType.PublicKey);
    }

    /**
     * 解密
     *
     * @param str
     * @return
     * @throws Exception
     */
    public String decryptB(String str) throws Exception {
        try {
            return sm2B.decryptStr(str, KeyType.PrivateKey);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 加密
     *
     * @param str
     * @return
     * @throws Exception
     */
    public String encryptB(String str) throws Exception {
        return sm2B.encryptHex(str, KeyType.PublicKey);
    }

    /**
     * 解密
     *
     * @param str
     * @return
     * @throws Exception
     */
    public String decrypt(String str, String privateKeyStr) throws Exception {
        try {
            ECPrivateKeyParameters privateKeyParams = BCUtil.toSm2Params(privateKeyStr);
            SM2 sm2 = new SM2(privateKeyParams, null);
            SM2Engine.Mode mode = SM2Engine.Mode.C1C3C2;
            sm2.setMode(mode);
            return sm2.decryptStr(str, KeyType.PrivateKey);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 加密
     *
     * @param str
     * @return
     * @throws Exception
     */
    public String encrypt(String str, String publicKeyStr) throws Exception {
        //提取公钥点
        PublicKey publicKey = BCUtil.decodeECPoint(publicKeyStr, "sm2p256v1");
        SM2 sm2 = new SM2(null, publicKey);
        SM2Engine.Mode mode = SM2Engine.Mode.C1C3C2;
        sm2.setMode(mode);
        return sm2.encryptHex(str, KeyType.PublicKey);
    }

    /**
     * 测试
     */
    @Test
    public void testEncodeAndDecode() {
        try {
            String encodeStr = encryptA("123456");
            System.out.println(encodeStr);
            System.out.println(decryptA(encodeStr));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    /**
     * 测试 这里包含生成一个新的public_key 和 private_key
     */
    @Test
    public void testGenerateKeyAndDoEncodeDecode() {
        System.out.println("==========SM2 加解密测试 开始==========");
        String text = "123456";

        KeyPair pair = SecureUtil.generateKeyPair("SM2");
        byte[] privateKey = pair.getPrivate().getEncoded();
        byte[] publicKey = pair.getPublic().getEncoded();

        //公钥前面的02或者03表示是压缩公钥,04表示未压缩公钥,04的时候,可以去掉前面的04
//        String pubKey = Hex.toHexString(BCUtil.encodeECPublicKey(pair.getPublic()));
        // 前端可能需要的是没压缩的公钥
        String pubKey = Hex.toHexString(((BCECPublicKey) pair.getPublic()).getQ().getEncoded(false));
        String priKey = Hex.toHexString(BCUtil.encodeECPrivateKey(pair.getPrivate()));

        System.out.println("公钥：" + pubKey);
        System.out.println("私钥：" + priKey);

        SM2 sm2 = SmUtil.sm2(privateKey, publicKey);
        SM2Engine.Mode mode = SM2Engine.Mode.C1C3C2;
        sm2.setMode(mode);
        // 公钥加密，私钥解密
        String encryptStr = sm2.encryptHex(text, KeyType.PublicKey);
        System.out.println("密文：" + encryptStr);
        String decryptStr = StrUtil.utf8Str(sm2.decryptStr(encryptStr, KeyType.PrivateKey));
        System.out.println("明文：" + decryptStr);

        System.out.println("==========SM2 加解密测试 结束==========");
    }

}
