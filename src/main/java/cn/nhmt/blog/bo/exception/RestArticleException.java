package cn.nhmt.blog.bo.exception;

/**
 * @Description: TODO
 * @Date: 2020-04-04 00:50
 * @Author: PinJyu
 * @Version: 1.0
 **/
public class RestArticleException extends RuntimeException{

    private int errorCode;

    private Exception originalException;

    public RestArticleException(String message) {
        super(message);
    }

    public RestArticleException() {
    }

    public int getErrorCode() {
        return errorCode;
    }

    public Exception getOriginalException() {
        return originalException;
    }

    public void setOriginalException(Exception originalException) {
        this.originalException = originalException;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public static RestArticleGetException forGet(int errorCode, Exception originalException) {
        RestArticleGetException rage = new RestArticleGetException();
        rage.setErrorCode(errorCode);
        rage.setOriginalException(originalException);
        return rage;
    }

    public static RestArticlePostException forPost(int errorCode, Exception originalException) {
        RestArticlePostException rape= new RestArticlePostException();
        rape.setErrorCode(errorCode);
        rape.setOriginalException(originalException);
        return rape;
    }

    public static RestArticlePutException forPut(int errorCode, Exception originalException) {
        RestArticlePutException rape = new RestArticlePutException();
        rape.setErrorCode(errorCode);
        rape.setOriginalException(originalException);
        return rape;
    }

    public static RestArticleDeleteException forDelete(int errorCode, Exception originalException) {
        RestArticleDeleteException rade = new RestArticleDeleteException();
        rade.setErrorCode(errorCode);
        rade.setOriginalException(originalException);
        return rade;
    }

    public static class RestArticleGetException extends RestArticleException {

        public RestArticleGetException(String message) {
            super(message);
        }

        public RestArticleGetException() {
        }
    }

    public static class RestArticlePostException extends RestArticleException {

        public RestArticlePostException(String message) {
            super(message);
        }

        public RestArticlePostException() {
        }
    }

    public static class RestArticlePutException extends RestArticleException {

        public RestArticlePutException(String message) {
            super(message);
        }

        public RestArticlePutException() {
        }
    }

    public static class RestArticleDeleteException extends RestArticleException {

        public RestArticleDeleteException(String message) {
            super(message);
        }

        public RestArticleDeleteException() {
        }
    }
}
