package bullionClient;

import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * @author: wangruirui
 * @date: 2017/6/5
 * @description:
 */
public class File {
    /*
获取公钥key的方法（读取.crt认证文件）
*/
    private static String getKeyFromCRT(){
        String key="";
        CertificateFactory certificatefactory;
        X509Certificate Cert;
        InputStream bais;
        PublicKey pk;
        BASE64Encoder bse;
        try{
            //若此处不加参数 "BC" 会报异常：CertificateException - OpenSSLX509CertificateFactory$ParsingException
            certificatefactory= CertificateFactory.getInstance("X.509","BC");
            //读取放在项目中assets文件夹下的.crt文件；你可以读取绝对路径文件下的crt，返回一个InputStream（或其子类）即可。
            //bais = this.getAssets().open("xxx.crt");
            FileInputStream in1 = new  FileInputStream( " aa.crt " );
            Cert = (X509Certificate) certificatefactory.generateCertificate(in1);
            pk = Cert.getPublicKey();
            bse = new BASE64Encoder();
            key=bse.encode(pk.getEncoded());
//            Log.e("源key-----"+ Cert.getPublicKey());
//            Log.e("加密key-----"+bse.encode(pk.getEncoded()));
        }catch(Exception e){
            e.printStackTrace();
        }
        key=key.replaceAll("\\n", "").trim();//去掉文件中的换行符
        return key;
    }
}
