package cn.nhmt.blog.bo.mail;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * @Description: 邮件全局配置类
 * @Date: 2020-05-09 15:15
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Deprecated
public class MailSenderConfig {

    private Properties prop = defaultProp;
    private Authenticator auth = defaultAuth;
    private Predicate<String> fromTest = DEFAULTTEST;
    private Predicate<String> toTest = DEFAULTTEST;
    private boolean debug = false;
    private int version = 1;

    public Properties getProp() {
        return prop;
    }

    public void setProp(Properties prop) {
        this.prop = prop;
        version += 1;
    }

    public void setProperty(String key, String value) {
        if (prop == null) {
            prop = new Properties();
        }
        prop.setProperty(key, value);
    }

    public Authenticator getAuth() {
        return auth;
    }

    public void setAuth(Authenticator auth) {
        this.auth = auth;
        version += 1;
    }

    public Predicate<String> getFromTest() {
        return fromTest;
    }

    public void setFromTest(Predicate<String> fromTest) {
        this.fromTest = fromTest;
        version += 1;
    }

    public Predicate<String> getToTest() {
        return toTest;
    }

    public void setToTest(Predicate<String> toTest) {
        this.toTest = toTest;
        version += 1;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
        version += 1;
    }

    public void setDefault() {
        prop = defaultProp;
        auth = defaultAuth;
        fromTest = DEFAULTTEST;
        toTest = DEFAULTTEST;
        debug = false;
        version += 1;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "MailSenderConfig{" +
                "prop=" + prop +
                ", auth=" + auth +
                ", fromTest=" + fromTest +
                ", toTest=" + toTest +
                ", debug=" + debug +
                '}';
    }

    private MailSenderConfig() { }

    public static MailSenderConfig getGlobalConfig() {
        return singleTon;
    }

    public static void load(InputStream inputStream) throws IOException, GeneralSecurityException {
        Properties load = new Properties(), out = new Properties();
        Authenticator auth = null;

        load.load(inputStream);
        for (Object keyObj : load.keySet()) {
            String key = keyObj instanceof String ? (String) keyObj : null;
            if (key != null && key.startsWith("mail") && !key.startsWith("mail.stmp.ssl")) {
                out.put(key, load.get(key));
            }
        }

        String user = load.getProperty("user"), pwd = load.getProperty("pwd");
        if (user != null && pwd != null && DEFAULTTEST.test(user)) {
            auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, pwd);
                }
            };
        }

        MailSSLSocketFactory mailSSLSocketFactory = new MailSSLSocketFactory();
        mailSSLSocketFactory.setTrustAllHosts(true);
        out.put("mail.smtp.ssl.enable", true);
        out.put("mail.smtp.ssl.socketFactory", mailSSLSocketFactory);

        defaultProp = out;
        defaultAuth = auth;
        singleTon.prop = defaultProp;
        singleTon.auth = defaultAuth;
    }

    public static MailSenderConfig getDefaultConfig() {
        return new MailSenderConfig();
    }

    private static final MailSenderConfig singleTon = new MailSenderConfig();

    private static Properties defaultProp;

    private static Authenticator defaultAuth;

    private static final Predicate<String> DEFAULTTEST = str ->
            Pattern.matches("^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$", str);

}
