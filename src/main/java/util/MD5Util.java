package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @author wuxianwei on 2017/1/17.
 */
public class MD5Util {

  private static final Logger LOGGER = LoggerFactory.getLogger(MD5Util.class);

  /**
   * 生成32位的md5编码 小写
   * "fcea920f7412b5da7be0cf42b8c93759"
   */
  public static String getMD5(String str) {
    try {
      // 生成一个MD5加密计算摘要
      MessageDigest md = MessageDigest.getInstance("MD5");
      // 计算md5函数
      md.update(str.getBytes());
      // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
      // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
      return new BigInteger(1, md.digest()).toString(16);
    } catch (Exception e) {
      LOGGER.error("MD5Util ERROR,str:{}", str, e);
      return null;
    }
  }

  public static String getLocalMD5() {
    String localString = Thread.currentThread().getId() + "+" + Thread.currentThread().getName()
            + "+" + System.currentTimeMillis();
    String md5 = getMD5(localString);
    LOGGER.info("getLocalMD5,localString:{},md5:{}", localString, md5);
    return md5;
  }
}
