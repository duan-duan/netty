package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionUtil.class);

    /**
     *  java 加密代码  rsa
     * @param src
     * @param key
     */
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

    /**
     *  java 解密代码  rsa
     * @param src
     * @param key
     */
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
     * key-24字节的密钥， ivByte-8字节向量，value-需加密的数据   3DES
     * @param key
     * @param ivByte
     * @param src
     */
    public static byte[] encrypt (byte[] key, byte[] ivByte, byte[] src) throws IOException,GeneralSecurityException{
        SecureRandom sr = new SecureRandom();
        DESedeKeySpec dks = new DESedeKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey securekey = keyFactory.generateSecret(dks);
        IvParameterSpec iv = new IvParameterSpec(ivByte);
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, securekey, iv, sr);
        return cipher.doFinal(src);
    }

    /**
     * key-24字节的密钥， ivByte-8字节向量，src-需解密的数据  3DES
     * @param key
     * @param ivByte
     * @param src
     */
    public static byte[] decrypt (byte[] key, byte[] ivByte, byte[] src) throws IOException,GeneralSecurityException{
        SecureRandom sr = new SecureRandom();
        DESedeKeySpec dks = new DESedeKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey securekey = keyFactory.generateSecret(dks);
        IvParameterSpec iv = new IvParameterSpec(ivByte);
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, securekey, iv, sr);
        return cipher.doFinal(src);
    }

    /**
     * 加密原报文
     * @param iEncryptMode
     * @param bSrcMsgBuff
     * @return
     */
    public static byte[] encryptMsg(int iEncryptMode, byte[] bSrcMsgBuff){
        byte[] arrSession = CommUtil.convertBytes(CommUtil.fill(Constant.SESSION_KEYS, ' ', Constant.SESSION_KEY_DEFAULT_Len, 'R'));
        byte[] arrDefault = CommUtil.convertBytes(CommUtil.fill(Constant.SESSION_KEY_DEFAULT, ' ', Constant.SESSION_KEY_DEFAULT_Len, 'R'));
        byte[] arrIv = CommUtil.convertBytes(Constant.IV_DEFAULT);
        try{
            switch (iEncryptMode){
                //不加密，原报文返回
                case 0:
                    break;
                //RSA算法加密
                case 1:
                    Key key = FileUtil.getKeyFromCRT();
                    bSrcMsgBuff = rsaEncryptGess(bSrcMsgBuff,key);
                    break;
                //3DES加密（会话密钥）
                case 2:
                    bSrcMsgBuff = decrypt(arrSession, arrIv, bSrcMsgBuff);

                    break;
                //3DES加密（默认密钥）
                case 3:
                    bSrcMsgBuff = decrypt(arrDefault, arrIv, bSrcMsgBuff);
                    break;
                //ZIP压缩
                case 4:
                    bSrcMsgBuff = ZipUtil.zip(bSrcMsgBuff);
                    break;
                //先ZIP压缩后再3DES加密(会话密钥)
                case 5:
                    bSrcMsgBuff = ZipUtil.zip(bSrcMsgBuff);
                    bSrcMsgBuff = encrypt(arrSession, arrIv, bSrcMsgBuff);
                    break;
                //先ZIP压缩后再3DES加密(默认密密钥)
                case 6:
                    bSrcMsgBuff = ZipUtil.zip(bSrcMsgBuff);
                    bSrcMsgBuff = encrypt(arrDefault, arrIv, bSrcMsgBuff);
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("加密失败");
        }

        bSrcMsgBuff = addLenByte(iEncryptMode, Constant.SESSION_CODE, Constant.SESSION_ID, bSrcMsgBuff);
        return bSrcMsgBuff;
    }

    public static byte[] addLenByte(int iEncryptMode, String Code, String ID, byte[] bCryptBuff) {
        //完整的待发送报文
        byte[] bFullBuff = new byte[Constant.MSG_LEN + Constant.ENCRYPT_MODEL_LEN + Constant.CODE_LEN + Constant.SESSION_LEN + bCryptBuff.length];

        //填充报文长度
        int index = 0;
        byte[] bMsgLen = CommUtil.convertBytes(CommUtil.fill("" + (bFullBuff.length - Constant.MSG_LEN), '0', Constant.MSG_LEN, 'L'));
        System.arraycopy(bMsgLen, 0, bFullBuff, index, Constant.MSG_LEN);

        System.out.println(CommUtil.bytesToString(bMsgLen));

        //填充加密模式
        index += Constant.MSG_LEN;
        bMsgLen = CommUtil.convertBytes(CommUtil.fill(iEncryptMode+"", ' ', Constant.ENCRYPT_MODEL_LEN, 'R'));
        System.arraycopy(bMsgLen, 0, bFullBuff, index, Constant.ENCRYPT_MODEL_LEN);

        //填充证书编码
        index += Constant.ENCRYPT_MODEL_LEN;
        byte[] bEncryptMode = CommUtil.convertBytes(CommUtil.fill(Constant.SESSION_CODE, ' ', Constant.CODE_LEN, 'R'));
        System.arraycopy(bEncryptMode, 0, bFullBuff, index, Constant.CODE_LEN);

        //填充SessionID
        index += Constant.CODE_LEN;
        byte[] bsessionMode = CommUtil.convertBytes(CommUtil.fill(Constant.SESSION_ID, ' ', Constant.SESSION_LEN, 'R'));
        System.arraycopy(bsessionMode, 0, bFullBuff, index, Constant.SESSION_LEN);

        //填充原报文加密后的内容
        index += Constant.SESSION_LEN;
        System.arraycopy(bCryptBuff, 0, bFullBuff, index, bCryptBuff.length);

        //返回加密后的完整报文
        return bFullBuff;

    }

    /**
     *  接收符合黄金交易二级系统接口规范的报文  解密报文
     * @param decryptMsgBuff
     * @return
     */
    public static String decryptMsg(byte[] decryptMsgBuff) {

        int index = 0;
        byte[] bLens = new byte[Constant.MSG_LEN];
        System.arraycopy(decryptMsgBuff, 0, bLens, index, Constant.MSG_LEN);
        int len = Integer.parseInt(CommUtil.bytesToString(bLens));

        //加密模式
        index += Constant.MSG_LEN;
        byte[] bKeys = new byte[Constant.ENCRYPT_MODEL_LEN];
        System.arraycopy(decryptMsgBuff, index, bKeys, 0, Constant.ENCRYPT_MODEL_LEN);

        //证书编码
        index += Constant.ENCRYPT_MODEL_LEN;
        byte[] bCcieKey = new byte[Constant.CODE_LEN];
        System.arraycopy(decryptMsgBuff, index, bCcieKey, 0, Constant.CODE_LEN);

        //SessionID
        index += Constant.CODE_LEN;
        byte[] bSessionID = new byte[Constant.SESSION_LEN];
        System.arraycopy(decryptMsgBuff, index, bSessionID, 0, Constant.SESSION_LEN);

        //报文加密后的内容
        index += Constant.SESSION_LEN;
        byte[] bMsgs = new byte[len-Constant.MSG_LEN2];
        System.arraycopy(decryptMsgBuff, index, bMsgs, 0, bMsgs.length);


        byte[] arrSession = CommUtil.convertBytes(CommUtil.fill(Constant.SESSION_KEYS, ' ', Constant.SESSION_KEY_DEFAULT_Len, 'R'));
        byte[] arrDefault = CommUtil.convertBytes(CommUtil.fill(Constant.SESSION_KEY_DEFAULT, ' ', Constant.SESSION_KEY_DEFAULT_Len, 'R'));
        byte[] arrIv = CommUtil.convertBytes(Constant.IV_DEFAULT);

        int iType = Integer.parseInt(CommUtil.bytesToString(bKeys));
        String recvMsg = "";
        try {
            switch (iType) {
                //不加密，原报文返回
                case 0:
                    recvMsg = CommUtil.bytesToString(bMsgs);
                    break;
                //RSA算法加密
                case 1:
                    Key key = FileUtil.getKeyFromCRT();
                    bMsgs = rsaDescryptGess(bMsgs,key);
                    recvMsg = CommUtil.bytesToString(bMsgs);
                    break;
                //3DES加密（会话密钥）
                case 2:
                    bMsgs = decrypt(arrSession, arrIv, bMsgs);
                    recvMsg = CommUtil.bytesToString(bMsgs);
                    break;
                //3DES加密（默认密钥）
                case 3:
                    bMsgs = decrypt(arrDefault, arrIv, bMsgs);
                    recvMsg = CommUtil.bytesToString(bMsgs);
                    break;
                //ZIP压缩
                case 4:
                    bMsgs = ZipUtil.unZip(bMsgs);
                    recvMsg = CommUtil.bytesToString(bMsgs);
                    break;
                //先ZIP压缩后再3DES加密(会话密钥)
                case 5:
                    bMsgs = decrypt(arrSession, arrIv, bMsgs);
                    bMsgs = ZipUtil.unZip(bMsgs);
                    recvMsg = CommUtil.bytesToString(bMsgs);
                    break;
                //先ZIP压缩后再3DES加密(默认密密钥)
                case 6:
                    bMsgs = decrypt(arrDefault, arrIv, bMsgs);
                    bMsgs = ZipUtil.unZip(bMsgs);
                    recvMsg = CommUtil.bytesToString(bMsgs);
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("解密失败");
        }
        return recvMsg;
    }


    public static void main(String[] args) {
        try {

            String encryptText = "{\"SerialNo\":\"\",\"ExchCode\":\"C999\",\"oper_flag\":1,\"UserID\":\"0000000205\",\"RspCode\":\"\",\"RspMsg\":\"\",\"rsp_encrypt_mode\":\"1\"}";
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // Generate keys
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate(); // 私钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic(); // 公钥
            byte[] e = rsaEncryptGess( encryptText.getBytes(),publicKey);
            byte[] de = rsaDescryptGess(e,privateKey);
            System.out.println(CommUtil.bytesToString(e));
            System.out.println("------");

            System.out.println(CommUtil.bytesToString(de));

//            byte[] send = CommUtil.convertBytes(encryptText);
//            send = encryptMsg(0,send);
//            System.out.println("1------");
//            System.out.println(CommUtil.bytesToString(send));
//            byte[]  rev = SocketClientUtil.sendSocket(send,Constant.SOCKETTIME,"117.141.138.101",41901);
//            System.out.println("2------");
//            System.out.println(CommUtil.bytesToString(rev));
//
//            String revStr = decryptMsg(rev);
//            System.out.println(revStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
