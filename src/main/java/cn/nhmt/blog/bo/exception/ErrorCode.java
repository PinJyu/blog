package cn.nhmt.blog.bo.exception;

/**
 * @Description: TODO
 * @Date: 2020-04-04 00:33
 * @Author: PinJyu
 * @Version: 1.0
 **/
public class ErrorCode {

    // 未知错误
    public static final int SEARCH_RESULT_NOT_EXISTS = 100;
    public static final int CREATE_FAIL = 110;
    public static final int UPDATE_FAIL = 120;
    public static final int DELETE_FAIL = 130;
    public static final int SEARCH_FAIL = 140;
    public static final int CREATE_IMAGE_FAIL = 150;
    public static final int UPDATE_IMAGE_FAIL = 160;
    public static final int DELETE_IMAGE_FAIL = 170;

    // 已知错误
    public static final int CREATE_FAIL_NOT_LOGIN = 200;
    public static final int UPDATE_FAIL_NOT_LOGIN = 210;
    public static final int DELETE_FAIL_NOT_LOGIN = 220;
    public static final int CREATE_FAIL_FIELD_NULL = 230;
    public static final int UPDATE_FAIL_FIELD_NULL = 240;

    // 分类为空
    public static final int Catatory_NULL = 300;

    // 文件上传失败
    public static final int FileUpload_FAIL = 400;

    // 没有任何文章
    public static final int ARTICLE_NULL = 500;
}
