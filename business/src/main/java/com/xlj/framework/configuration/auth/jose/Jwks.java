package com.xlj.framework.configuration.auth.jose;

import cn.hutool.core.io.resource.ResourceUtil;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.UUID;

/**
 * 生成jwt验签需要的RsaKey
 * @author zhangkun
 */
@Slf4j
public final class Jwks {

    private Jwks() {
    }

	public static RSAKey generateRsa() {
		KeyPair keyPair = KeyGeneratorUtils.generateRsaKey();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		return new RSAKey.Builder(publicKey)
				.privateKey(privateKey)
				.keyID(UUID.randomUUID().toString())
				.build();
	}

    public static RSAKey generateRsaByStaticFile() {
        RSAPublicKey publicKey;
        RSAPrivateKey privateKey;
        try {
            String publicKeyStr = ResourceUtil.readUtf8Str("publicKey.txt");
            String privateKeyStr = ResourceUtil.readUtf8Str("privateKey.txt");

            privateKey = KeyGeneratorUtils.generateRsaPrivateKey(Base64.getDecoder().decode(privateKeyStr));
            publicKey = KeyGeneratorUtils.generateRsaPublicKey(Base64.getDecoder().decode(publicKeyStr));
        } catch (Exception e) {
            log.error("read publicKey.txt error,please check resource dir has 'publicKey.txt' file ?!", e);
            throw new RuntimeException("没有找到公钥私钥文件，请检查！！！");
        }
        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID("static-file-key")
                .build();
    }

    public static ECKey generateEc() {
        KeyPair keyPair = KeyGeneratorUtils.generateEcKey();
        ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
        ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
        Curve curve = Curve.forECParameterSpec(publicKey.getParams());

        return new ECKey.Builder(curve, publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    public static OctetSequenceKey generateSecret() {

        SecretKey secretKey = KeyGeneratorUtils.generateSecretKey();
        return new OctetSequenceKey.Builder(secretKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }



}
