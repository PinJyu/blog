package cn.nhmt.blog.bo.mail;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Description: 对邮件一个简单的封包
 * @Date: 2020-05-09 14:03
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Deprecated
public class MailSender {

    private static final MailSenderConfig mailSenderGlobalConfig = MailSenderConfig.getGlobalConfig();

    private static final MailSender singleTon = new MailSender();

    private MailSender() { }

    public static MailSender commonSender() {
        return singleTon;
    }

    private boolean isEnoughArgument(MailSenderConfig msc) {
        final MailSenderConfig msgc = mailSenderGlobalConfig;
        if (msc == null) {
            return msgc != null && msgc.getProp() != null && msgc.getAuth() != null;
        } else if (msgc == null) {
            return msc.getProp() != null && msc.getAuth() != null;
        }
        return (msc.getProp() != null || msgc.getProp() != null)
                && (msc.getAuth() != null || msgc.getAuth() != null);
    }

    private Session getSession(MailSenderConfig msc) {
        final MailSenderConfig msgc = mailSenderGlobalConfig;
        Properties prop;
        Authenticator auth;
        boolean debug;
        if (msc == null) {
            prop = msgc.getProp();
            auth = msgc.getAuth();
            debug = msgc.isDebug();
        } else {
            prop = msc.getProp();
            if (prop == null) {
                prop = msgc.getProp();
            }
            auth = msc.getAuth();
            if (auth == null) {
                auth = msgc.getAuth();
            }
            debug = msc.isDebug();
        }
        if (prop == null) {
            throw ArgumentMissingException.missingWith("properties");
        }
        if (auth == null) {
            throw ArgumentMissingException.missingWith("authenticator");
        }
        Session session = Session.getInstance(prop, auth);
        session.setDebug(debug);
        return session;
    }

    private MimeMessage prepareMessage(Session session, Mail mail, MailSenderConfig msc) throws MessagingException {
        MimeMessage mm = new MimeMessage(session);
        if (mail == null) {
            throw ArgumentMissingException.missingWith("mail entry");
        } else {
            final MailSenderConfig msgc = mailSenderGlobalConfig;
            Predicate<String> fromTest, toTest;
            if (msc == null) {
                fromTest = msgc.getFromTest();
                toTest = msgc.getToTest();
            } else {
                fromTest = msc.getFromTest();
                if (fromTest == null) {
                    fromTest = msgc.getFromTest();
                }
                toTest = msc.getToTest();
                if (toTest == null) {
                    toTest = msgc.getToTest();
                }
            }

            String from = mail.getFrom();
            List<String> to = mail.getTo();
            if (from == null || fromTest != null && !fromTest.test(from)) {
                throw ArgumentMissingException.missingWith("from");
            }
            List<Address> toWithTest = null;
            if (to != null && to.size() != 0) {
                toWithTest = to.stream().filter(toTest != null ? toTest : Objects::nonNull).map(x-> {
                    try {
                        return new InternetAddress(x);
                    } catch (AddressException ignore) { }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toList());
            }
            if (toWithTest == null || toWithTest.size() == 0) {
                throw ArgumentMissingException.missingWith("to");
            }
            mm.setFrom(from);
            mm.setRecipients(Message.RecipientType.TO, toWithTest.toArray(new Address[0]));

            String subject = mail.getSubject();
            String content = mail.getContent();
            if (subject == null) {
                throw ArgumentMissingException.missingWith("subject");
            }
            if (content == null) {
                throw ArgumentMissingException.missingWith("content");
            }
            mm.setSubject(subject, "utf-8");
            mm.setContent(content, "text/html; charset=utf8");
        }
        return mm;
    }

    private void send(Session session, MimeMessage mimeMessage) throws MessagingException {
        try (Transport transport = session.getTransport()) {
            transport.connect();
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        }
    }

    public boolean send(Mail mail, MailSenderConfig msc) {
        boolean isSend = false;
        if (isEnoughArgument(msc)) {
            Session session = getSession(msc);
            try {
                MimeMessage mimeMessage = prepareMessage(session, mail, msc);
                send(session, mimeMessage);
                isSend = true;
            } catch (RuntimeException | MessagingException e) {
                if (session.getDebug()) {
                    e.printStackTrace();
                }
            }
        }
        return isSend;
    }

    public static class ArgumentMissingException extends RuntimeException {

        private static final String mainString = "Mail argument '%s' missing.";

        public ArgumentMissingException(String message) {
            super(message);
        }

        public static ArgumentMissingException missingWith(String field) {
            String message = String.format(mainString, field);
            return new ArgumentMissingException(message);
        }
    }

}
