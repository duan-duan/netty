package util;

import java.io.UnsupportedEncodingException;

/**
 * @author: wangruirui
 * @date: 2017/6/9
 * @description:
 */
public class CommUtil {

    // 将字符串转换为字节数组
    public static byte[] convertBytes(String str){
        if (str == null)
            str = "";
        byte[] srtbyte = new byte[0];
        try {
            srtbyte = str.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return srtbyte;
    }

    /**
     * byte数组转为string
     *
     * @param encrytpByte
     * @return
     */
    public static String bytesToString(byte[] encrytpByte) {
//        String result = "";
//        for (Byte bytes : encrytpByte) {
//            result += (char) bytes.intValue();
//        }
//        return result;

        String result = null;
        try {
            result = new String(encrytpByte,"GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;

    }


    // 填充字符串
    // str 原始字符串
    // fill 填充字符
    // len 填充后整体长度
    //填充方向,'L/R'
    public static String fill(String str, char fill, int len, char dire) {
        int iSrcLen = str.getBytes().length;
        if (iSrcLen > len) {
            return str;
        }else {
            String file = "";
            for (int i = 0; i < len - iSrcLen; i++) {
                file += fill;
            }
            if ('L'==dire) {
                return file + str;
            } else {
                return str + file;
            }
        }
    }
}
