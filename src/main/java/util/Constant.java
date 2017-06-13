package util;

/**
 * @author: wangruirui
 * @date: 2017/6/9
 * @description:
 */
public class Constant {

    // 域分隔符
    public final char SEPARATOR_SEGMENT = '#';
    public static String FC_ORDER_STATE_SENDED = "";
    public static String FC_PRICE_MODE_LIMIT = "2";
    public static String FC_PRICE_MODE_LAST = "3";
    // 广播包段分隔符 #
    public final char GSI_EOREC = '#';

    // 广播包域分隔符 ^
    public final char GSI_EOFLD = '#';

    // 多级记录分隔符
    public static char[] SEPARATOR_RECORD = new char[] { '∧', '｜', 'ˇ', '¨', };


    // 服务器证书路径
    public final String SERVER_CERT_PATH = "servercert_path";

    // 客户端证书路径
    public final String CLIENT_CERT_PATH = "clientcert_path";

    // 服务器证书名
    public final String SERVER_CERT_NAME = "servercert_name";

    // 证书密码
    public final String CERT_PWD = "cert_password";



    // 报文体长度说明的长度
    public static int MSG_LEN = 8;

    // 加密标识+证书编号+会话ID长度
    public static final int MSG_LEN2 = 15;


    // 响应报文头截取长度
    public final int GSI_RSP_LEN = 44;

    // 加密模式：0：不加密
    public final int ENCRYPT_MODEL_0 = 0;

    // 加密模式：1：RSA算法加密
    public final int ENCRYPT_MODEL_1 = 1;

    // 加密模式：2：3DES加密（会话密钥）
    public final int ENCRYPT_MODEL_2 = 2;

    // 加密模式：3：3DES加密（默认密钥）
    public final int ENCRYPT_MODEL_3 = 3;

    // 加密模式：4：ZIP压缩
    public final int ENCRYPT_MODEL_4 = 4;

    // 加密模式：5：先ZIP压缩后再3DES加密(会话密钥)
    public final int ENCRYPT_MODEL_5 = 5;

    // 加密模式：6：先ZIP压缩后再3DES加密(默认密密钥)
    public final int ENCRYPT_MODEL_6 = 6;

    // 默认3DES密钥（24位）
    public static String SESSION_KEY_DEFAULT = "FAJA2LFA9WR9FSASF92FMMAF";
    // 密钥长度
    public static int SESSION_KEY_DEFAULT_Len = 24;
    // 加密向量
    public static String IV_DEFAULT = "8A402C31";

    // 加密模式长度
    public static int ENCRYPT_MODEL_LEN = 1;

    // 会话ID长度
    public static int SESSION_LEN = 10;

    // 证书编码长度
    public static int CODE_LEN = 4;

    // 会话ID
    public static String SESSION_ID = "";
    // 会话密钥
    public static String SESSION_KEYS = "";
    // 认证临时密钥
    public static String SESSION_KEY = "";

    // 证书编码
    public static String SESSION_CODE = "C000";

    // 数字证书
    public static String CCIEPATH = "";

    // 设置socket链接时间
    public static int SOCKETTIME = 20000;
}
