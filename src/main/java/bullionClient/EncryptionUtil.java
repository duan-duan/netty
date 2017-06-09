package bullionClient;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author: wangruirui
 * @date: 2017/6/5
 * @description:
 */
public class EncryptionUtil {


    //java 加密代码  rsa
    public static byte[] rsaEncryptGess (byte[] src,Key key) throws IOException,GeneralSecurityException{
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE,key);
        ByteArrayOutputStream daos = new ByteArrayOutputStream();
        for(int i=0;i<src.length;i=i+100){
            int len = 0;
            if((i+100)>src.length){
                len = src.length-i;
            }else{
                len = 100;
            }
            byte[] tmp = new byte[len];
            System.arraycopy(src,i,tmp,0,tmp.length);
            daos.write(cipher.doFinal(tmp));
        }
        return daos.toByteArray();
    }

    //java 解密代码  rsa
    public static byte[] rsaDescryptGess (byte[] src,Key key) throws IOException,GeneralSecurityException{
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE,key);
        ByteArrayOutputStream daos = new ByteArrayOutputStream();
        for(int i = 0; i < src.length; i = i+128){
            byte[] tmp = new byte[128];
            System.arraycopy(src,i,tmp,0,tmp.length);
            daos.write(cipher.doFinal(tmp));
        }
        return daos.toByteArray();
    }




    /**
     * byte数组转为string
     *
     * @param encrytpByte
     * @return
     */
    public static String bytesToString(byte[] encrytpByte) {
        String result = "";
        for (Byte bytes : encrytpByte) {
            result += (char) bytes.intValue();
        }
        return result;
    }

    //key-24字节的密钥， ivByte-8字节向量，value-需加密的数据   3DES
    public static byte[] encrypt (byte[] key, byte[] ivByte, byte[] src) throws IOException,GeneralSecurityException{

        SecureRandom sr = new SecureRandom();
        DESedeKeySpec dks = new DESedeKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey securekey = keyFactory.generateSecret(dks);
        IvParameterSpec iv = new IvParameterSpec(ivByte);
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, securekey, iv, sr);
        return new String(cipher.doFinal(src)).getBytes();
    }

    //key-24字节的密钥， ivByte-8字节向量，src-需解密的数据  3DES
    public static byte[] decrypt (byte[] key, byte[] ivByte, byte[] src) throws IOException,GeneralSecurityException{
        SecureRandom sr = new SecureRandom();
        DESedeKeySpec dks = new DESedeKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey securekey = keyFactory.generateSecret(dks);
        IvParameterSpec iv = new IvParameterSpec(ivByte);
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, securekey, iv, sr);
        return new String(cipher.doFinal(src)).getBytes();
    }




    public static void main(String[] args) {
        try {

            String encryptText = "12345678";
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // Generate keys
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate(); // 私钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic(); // 公钥
            byte[] e = rsaEncryptGess( encryptText.getBytes(),publicKey);
            byte[] de = rsaDescryptGess(e,privateKey);
            System.out.println(bytesToString(e));
            System.out.println();

            System.out.println(bytesToString(de));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
