package com.tarang.dpq2.base.terminal_sdk;

import android.util.Base64;
import android.util.Log;

import com.tarang.dpq2.base.jpos_class.ByteConversionUtils;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

public class RSAEncrypt {
    public static String publicExponent;
    public static String privateExponent;
    public static String publicModulus;
    public static String privateModulus;
    public static RSAPublicKey publicKey;
    public static RSAPrivateKey privateKey;
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    public static byte[] decryptBASE64(String key) throws Exception {
        return Base64.decode(key, Base64.DEFAULT);
    }

    public static String encryptBASE64(byte[] key) throws Exception {
        return Base64.encodeToString(key, Base64.DEFAULT);
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        // 解密由base64编码的私钥
        byte[] keyBytes = decryptBASE64(privateKey);

        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);

        return encryptBASE64(signature.sign());
    }

    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return 校验成功返回true 失败返回false
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {

        // 解密由base64编码的公钥
        byte[] keyBytes = decryptBASE64(publicKey);

        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);

        // 验证签名是否正常
        return signature.verify(decryptBASE64(sign));
    }

    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key)
            throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    /**
     * 解密<br>
     * 用公钥解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, BigInteger publicModulus,BigInteger publicExponent)
            throws Exception {
        // 取得公钥
        RSAPublicKeySpec keySpec=new RSAPublicKeySpec(publicModulus,publicExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(keySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }

    public static byte[] decryptByPrivateKey(byte[] data, BigInteger privateModulus,BigInteger privateExponent)
            throws Exception {
        // 取得公钥
        RSAPrivateKeySpec keySpec=new RSAPrivateKeySpec(privateModulus,privateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(keySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 加密<br>
     * 用公钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String key)
            throws Exception {
        // 对公钥解密
        byte[] keyBytes = decryptBASE64(key);
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }


    public static byte[] encryptByPublicKey(byte[] data,BigInteger publicModulus,BigInteger publicExponent)
            throws Exception {

        // 取得私钥
        // PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        RSAPublicKeySpec keySpec=new RSAPublicKeySpec(publicModulus,publicExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(keySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey );

        return cipher.doFinal(data);
    }
    public static String sha1ToPkcs1(byte[] data,int keysize){
        int datasize=data.length;
        String output=null;
        output="00"+"01";
        int PS_Size=keysize-3-datasize-16;
        for(int i=0;i<=PS_Size;i++){
            output=output+"FF";
        }
        output=output+"00"+"3021300906052B0E03021A05000414"+ ByteConversionUtils.byteArrayToHexString(data,data.length,false);
        return output;

    }

    /**
     * 加密<br>
     * 用私钥加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data,String pubModule,String Exponent)
            throws Exception {

        // 取得私钥
        // PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        RSAPrivateKeySpec keySpec=new RSAPrivateKeySpec(new BigInteger(pubModule,16),new BigInteger(Exponent,16));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(keySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    /**
     * 取得私钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);

        return encryptBASE64(key.getEncoded());
    }

    /**
     * 取得公钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 初始化密钥
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initKey(int keysize) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator
                .getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(keysize);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 公钥
        publicKey = (RSAPublicKey) keyPair.getPublic();
        privateKey=(RSAPrivateKey)keyPair.getPrivate();
        // 私钥privateKey = (RSAPrivateKey) keyPair.getPrivate();
        publicExponent=publicKey.getPublicExponent().toString(16);
        publicModulus=publicKey.getModulus().toString(16);
        privateExponent=  (privateKey.getPrivateExponent().toString(16));
        privateModulus=privateKey.getModulus().toString(16);
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        Log.i("Info","publicExponent: "+publicExponent);
        Log.i("Info"," publicModulus: "+ publicModulus);
        Log.i("Info"," privateExponent: "+ privateExponent);
        Log.i("Info","  privateModulus: "+  privateModulus);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }
    public static byte[] rsaRecover(String pubModule, String  Exponent, byte[] inputData){

        try {
            byte[] encryresult=RSAEncrypt.encryptByPrivateKey(inputData,pubModule,Exponent);
          //  Log.i("Info","parameter15: "+bytesToHexString(encryresult));
            return encryresult;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public static byte[] rsaDecryptRecover(String pubModule, byte[] inputData){

        try {
            byte[] encryresult=RSAEncrypt.decryptByPublicKey(inputData,new BigInteger(pubModule,16),new BigInteger("10001",16));
           // Log.i("Info","parameter15: "+bytesToHexString(encryresult));
            return encryresult;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }
}