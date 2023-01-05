package com.xlj.framework.configuration.auth.jose;

import cn.hutool.core.io.FileUtil;
import org.springframework.util.Base64Utils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.FileWriter;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;

final class KeyGeneratorUtils {

	private KeyGeneratorUtils() {
	}

	static SecretKey generateSecretKey() {
		SecretKey hmacKey;
		try {
			hmacKey = KeyGenerator.getInstance("HmacSha256").generateKey();
		} catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
		return hmacKey;
	}

	static KeyPair generateRsaKey() {
		KeyPair keyPair;
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			keyPair = keyPairGenerator.generateKeyPair();
		} catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
		return keyPair;
	}

	static KeyPair generateEcKey() {
		EllipticCurve ellipticCurve = new EllipticCurve(
				new ECFieldFp(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951")),
				new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853948"),
				new BigInteger("41058363725152142129326129780047268409114441015993725554835256314039467401291"));
		
		ECPoint ecPoint = new ECPoint(new BigInteger("48439561293906451759052585252797914202762949526041747995844080717082404635286"),
				new BigInteger("36134250956749795798585127919587881956611106672985015071877198253568414405109"));
		
		ECParameterSpec ecParameterSpec = new ECParameterSpec(ellipticCurve, ecPoint,
				new BigInteger("115792089210356248762697446949407573529996955224135760342422259061068512044369"), 1);

		KeyPair keyPair;
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
			keyPairGenerator.initialize(ecParameterSpec);
			keyPair = keyPairGenerator.generateKeyPair();
		} catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
		return keyPair;
	}


	public static void main(String[] args) throws Exception {
		String exportPrivateFile = "D:\\jks\\privateKey.txt";
		String exportPublicFile = "D:\\jks\\publicKey.txt";
		FileUtil.touch(exportPrivateFile);
		FileUtil.touch(exportPrivateFile);

		KeyPair keyPair = KeyGeneratorUtils.generateRsaKey();
		exportPrivateKey(keyPair.getPrivate(), exportPrivateFile);
		exportPublicKey(keyPair.getPublic(), exportPublicFile);

		System.out.println("OK");

	}

	//导出证书 base64格式
	public static void exportCert(KeyStore keyStore, String alias, String exportFile) throws Exception {
		Certificate certificate = keyStore.getCertificate(alias);
		String encoded = Base64Utils.encodeToString(certificate.getEncoded());
		FileWriter fw = new FileWriter(exportFile);
		fw.write("------Begin Certificate----- \r\n ");//非必须
		fw.write(encoded);
		fw.write("\r\n-----End Certificate-----");//非必须
		fw.close();
	}

	//导出私钥
	public static void exportPrivateKey(PrivateKey privateKey, String exportFile) throws Exception {
		String encoded = Base64Utils.encodeToString(privateKey.getEncoded());
		FileWriter fileWriter = new FileWriter(exportFile);
		fileWriter.write("-----Begin Private Key-----\r\n");//非必须
		fileWriter.write(encoded);
		fileWriter.write("\r\n-----End Private Key-----");//非必须
		fileWriter.close();
	}

	//导出公钥
	public static void exportPublicKey(PublicKey publicKey, String exportFile) throws Exception {
		String encoded = Base64Utils.encodeToString(publicKey.getEncoded());
		FileWriter fileWriter = new FileWriter(exportFile);
		fileWriter.write("-----BEGIN PUBLIC KEY-----\r\n");//非必须
		fileWriter.write(encoded);
		fileWriter.write("\r\n-----END PUBLIC KEY-----");//非必须
		fileWriter.close();
	}

	/**
	 * 获得“RSA”算法的{@link KeyFactory}实例。
	 *
	 * @return “RSA”算法的{@link KeyFactory}实例
	 * @throws NoSuchAlgorithmException NoSuchAlgorithmException
	 */
	private static KeyFactory getRsaKeyFactoryInstance() throws NoSuchAlgorithmException {
		return KeyFactory.getInstance("RSA");
	}

	/**
	 * 根据指定的“字节数组”生成“RSA”算法的公钥。
	 *
	 * @param bytes 字节数组
	 * @return “RSA”算法的公钥
	 * @throws NoSuchAlgorithmException NoSuchAlgorithmException
	 * @throws InvalidKeySpecException  InvalidKeySpecException
	 */
	public static RSAPublicKey generateRsaPublicKey(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
		return (RSAPublicKey) getRsaKeyFactoryInstance().generatePublic(new X509EncodedKeySpec(bytes));
	}

	/**
	 * 根据指定的“字节数组”生成“RSA”算法的私钥。
	 *
	 * @param bytes 字节数组
	 * @return “RSA”算法的私钥
	 * @throws NoSuchAlgorithmException NoSuchAlgorithmException
	 * @throws InvalidKeySpecException  InvalidKeySpecException
	 */
	public static RSAPrivateKey generateRsaPrivateKey(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
		return (RSAPrivateKey) getRsaKeyFactoryInstance().generatePrivate(new PKCS8EncodedKeySpec(bytes));
	}
}
