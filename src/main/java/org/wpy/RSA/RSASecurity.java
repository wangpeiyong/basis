package org.wpy.RSA;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSASecurity {


    public static void main(String[] args) {
        RSASecurity rsAsecurity = new RSASecurity();

        System.out.println("私钥加密公钥解密例：");
        rsAsecurity.priENpubDE("---------------wangpy------------");

        System.out.println("公钥加密私钥解密例：");
        rsAsecurity.pubENpriDE("--------王培勇---------");
    }

    /**
     * RSA接口签名案例：
     * <p>
     * 私钥加密，公钥解密
     */
    public void priENpubDE(String src) {
        try {
            //1.初始化秘钥
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            //秘钥长度
            keyPairGenerator.initialize(512);
            //初始化秘钥对
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            //公钥
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            //私钥
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            System.out.printf("保存的： 公钥：%s\n\t\t私钥: %s \n", Base64.encode(rsaPublicKey.getEncoded()), Base64.encode(rsaPrivateKey.getEncoded()));

            //2.私钥加密
            //生成私钥
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decode(Base64.encode(rsaPrivateKey.getEncoded())));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            //Cipher类为加密和解密提供密码功能，通过getinstance实例化对象
            Cipher cipher = Cipher.getInstance("RSA");
            //初始化加密
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] result = cipher.doFinal(src.getBytes());
            System.out.println("私钥加密，公钥解密----加密:" + Base64.encode(result));

            //3.公钥解密
            //生成公钥
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decode(Base64.encode(rsaPublicKey.getEncoded())));
            keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            cipher = Cipher.getInstance("RSA");
            //初始化解密
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            result = cipher.doFinal(result);
            System.out.println("私钥加密，公钥解密----解密:" + new String(result));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 公钥加密，私钥解密
     */
    public void pubENpriDE(String src) {
        try {
            //1.初始化秘钥
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            //秘钥长度
            keyPairGenerator.initialize(512);
            //初始化秘钥对
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            //公钥
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            //私钥
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            System.out.printf("保存的： 公钥：%s\n\t\t私钥: %s \n", Base64.encode(rsaPublicKey.getEncoded()), Base64.encode(rsaPrivateKey.getEncoded()));

            //2.公钥加密
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decode(Base64.encode(rsaPublicKey.getEncoded())));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            //初始化加密
            //Cipher类为加密和解密提供密码功能，通过getinstance实例化对象
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            //加密字符串
            byte[] result = cipher.doFinal(src.getBytes());
            System.out.printf("公钥加密，私钥解密----加密:%s\n", Base64.encode(result));

            //3.私钥解密
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decode(Base64.encode(rsaPrivateKey.getEncoded())));
            keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            //初始化解密
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            //解密字符串
            result = cipher.doFinal(result);
            System.out.println("公钥加密，私钥解密-----解密:" + new String(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
