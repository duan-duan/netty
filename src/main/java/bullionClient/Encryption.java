package bullionClient;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author: wangruirui
 * @date: 2017/6/5
 * @description:
 */
public class Encryption {

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
