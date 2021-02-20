package cn.nhmt.blog.bo.impl;

import cn.nhmt.blog.bo.UserRegisterService;
import cn.nhmt.blog.contants.ErrorCode;
import cn.nhmt.blog.dao.RoleDao;
import cn.nhmt.blog.dao.UserDao;
import cn.nhmt.blog.dto.Message;
import cn.nhmt.blog.po.Role;
import cn.nhmt.blog.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 用户注册
 * @Date: 2020-04-22 22:27
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Slf4j
@Service
public class UserRegisterServiceImpl implements UserRegisterService {

    private static final ConcurrentHashMap<String, User> registeringUserCache=  new ConcurrentHashMap<>();

    private static final String mailContent = "<p>请确认您的邮箱:%s</p>" +
            "<p>验证码:<span style='color: #00aaaa'>%d</span></p>" +
            "<p>请在您打开的网页端填写验证码</p>" +
            "<p>不需回复</p>";

    private Set<String> nameIgnoreSet;

    private Set<String> emailIgnoreSet;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SimpleMailMessage mailTemplate;

    @Autowired
    private TaskScheduler taskScheduler;

    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @PostConstruct
    public void init() {
        nameIgnoreSet = new HashSet<>();
        nameIgnoreSet.add("password");
        nameIgnoreSet.add("email");
        nameIgnoreSet.add("creatGmt");
        nameIgnoreSet.add("lastModifyGmt");
        emailIgnoreSet = new HashSet<>();
        emailIgnoreSet.add("password");
        emailIgnoreSet.add("name");
        emailIgnoreSet.add("creatGmt");
        emailIgnoreSet.add("lastModifyGmt");
    }

    private User getUser(String jSessionId) {
        User user = registeringUserCache.get(jSessionId);
        if (user == null) {
            user = new User();
            registeringUserCache.put(jSessionId, user);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 25);
            taskScheduler.schedule(() -> registeringUserCache.remove(jSessionId), calendar.getTime());
        }
        return user;
    }

    @Transactional
    @Override
    public Message register(User user, String CAPTCHA, String jSessionId) {
        Message message = new Message(false);
        User userInCache = registeringUserCache.get(jSessionId);

        if (Objects.isNull(userInCache)) {
            message.setErrorCode(ErrorCode.REGISTER_TIMEOUT);
        } else if (!CAPTCHA.equals(String.valueOf(userInCache.getId()))) {
            message.setErrorCode(ErrorCode.REGISTER_EMAIL_CAPTCHA_UNLEGAL);
        } else if (!user.getName().equals(userInCache.getName())) {
            message.setErrorCode(ErrorCode.REGISTER_NAME_UNLEGAL);
        } else if (!user.getEmail().equals(userInCache.getEmail())) {
            message.setErrorCode(ErrorCode.REGISTER_EMAIL_UNLEGAL);
        } else {
            user.setCreateGmt(new Date());
            user.setLastModifyGmt(user.getCreateGmt());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            if (Objects.nonNull(userDao.retrieveByName(user.getName(), nameIgnoreSet))) {
                message.setErrorCode(ErrorCode.REGISTER_NANE_EXISTS);
            } else if (Objects.nonNull(userDao.retrieveByEmail(user.getEmail(), emailIgnoreSet))) {
                message.setErrorCode(ErrorCode.REGISTER_EMAIL_EXISTS);
            } else {
                userDao.create(user);
                registeringUserCache.remove(jSessionId);
                Role role = new Role();
                role.setCreateGmt(new Date());
                role.setLastModifyGmt(role.getCreateGmt());
                role.setUserId(user.getId());
                role.addGrants(cn.nhmt.blog.contants.Role.VISITOR.getLiteRole());
                roleDao.create(role);
                message.setOk(true);
            }
        }

        return message;
    }

    @Transactional(readOnly = true)
    @Override
    public Message isLegalUserName(String userName, String jSessionId) {
        Message message = new Message(false);
        User userWithTheName = userDao.retrieveByName(userName, nameIgnoreSet);
        if (Objects.isNull(userWithTheName)) {
            for (User user : registeringUserCache.values()) {
                if (userName.equals(user.getName())) {
                    message.setErrorCode(ErrorCode.REGISTER_NANE_EXISTS);

                    return message;
                }
            }
            message.setOk(true);
            User user = getUser(jSessionId);
            user.setName(userName);
        }

        return message;
    }

    @Transactional(readOnly = true)
    @Override
    public Message isLegalUserEmail(String userEmail, String jSessionId) {
        Message message = new Message(false);
        User userWithTheEmail = userDao.retrieveByEmail(userEmail, emailIgnoreSet);
        if (Objects.isNull(userWithTheEmail)) {
            for (User user : registeringUserCache.values()) {
                if (userEmail.equals(user.getEmail())) {
                    message.setErrorCode(ErrorCode.REGISTER_EMAIL_EXISTS);

                    return message;
                }
            }
            message.setOk(true);
            User user = getUser(jSessionId);
            user.setEmail(userEmail);
        }

        return message;
    }

    @Override
    public Message sendEmailCAPTCHA(String userEmail, String jSessionId) {
        Message message = new Message(false);
        User user = getUser(jSessionId);
        if (!userEmail.equals(user.getEmail())) {
            message.setErrorCode(ErrorCode.REGISTER_EMAIL_NOT_VALID);

            return message;
        }

        int CAPTCHA = generateCAPTCHA();
        try {
            mailSender.send(generateCAPTCHAMail(userEmail, CAPTCHA));
            user.setId(CAPTCHA);
        } catch (Exception e) {
            log.error("Send CAPTCHA email with %s exception in session id \"%s\":\n", e.getClass().getSimpleName(), jSessionId, e);
            message.setErrorCode(ErrorCode.REGISTER_EMAIL_SEND_FAIL);

            return message;
        }
        message.setOk(true);

        return message;
    }

    private MimeMessage generateCAPTCHAMail(String userEmail, int CAPTCHA) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage);
        mmh.setFrom(Objects.requireNonNull(mailTemplate.getFrom()));
        mmh.setTo(userEmail);
        mmh.setSubject("PinJYu Blog 用户注册-邮箱认证");
        mmh.setText(String.format(mailContent, userEmail, CAPTCHA), true);
        return mimeMessage;
    }

    private int generateCAPTCHA() {
        Random random = new Random();
        // [100_000, 1000_000) [0, 900_000)
        return random.nextInt(900_000) + 100_000;
    }

}
