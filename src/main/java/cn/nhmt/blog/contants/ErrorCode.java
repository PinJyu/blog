package cn.nhmt.blog.contants;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * @Description: TODO
 * @Date: 2020-04-04 00:33
 * @Author: PinJyu
 * @Version: 1.0
 **/
public abstract class ErrorCode {

    // unknow
    public static final int SERVER_ERROR = 1;
    public static final int PERSISTENCE_ERROR = 2;
    public static final int ARGUMENTS_UNLEGAL = 3;

    // 未知错误
    public static final int CREATE_FAIL = 101;
    public static final int UPDATE_FAIL = 102;
    public static final int DELETE_FAIL = 103;
    public static final int RETRIEVE_FAIL = 104;

    // 分类为空
    public static final int Catatory_NULL = 300;

    // 文件上传失败
    public static final int FileUpload_ERROR = 400;

    // 没有任何文章
    public static final int ARTICLE_NULL = 500;

    // user register
    public static final int REGISTER_NANE_EXISTS = 602;
    public static final int REGISTER_NAME_UNLEGAL = 603;
    public static final int REGISTER_EMAIL_EXISTS = 608;
    public static final int REGISTER_EMAIL_UNLEGAL = 609;
    public static final int REGISTER_EMAIL_CAPTCHA_UNLEGAL = 610;
    public static final int REGISTER_EMAIL_NOT_VALID = 611;
    public static final int REGISTER_EMAIL_SEND_FAIL = 613;
    public static final int REGISTER_TIMEOUT = 612;

    public static final int LOGIN_USER_NAME_NOT_EXISTS = 200;
    public static final int LOGIN_USER_PWD_NOT_CORRECT = 201;
}
