package cn.nhmt.blog.bo.exception;

/**
 * @Description: TODO
 * @Date: 2020-04-22 16:01
 * @Author: PinJyu
 * @Version: 1.0
 **/
public class UserExcepition extends RuntimeException implements ExceptionWrapper {

    private RuntimeException originalException;
    private int errorCode;

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public Exception getOriginalException() {
        return originalException;
    }

    @Override
    public void setOriginalException(Exception originalException) {
        this.originalException = (RuntimeException) originalException;
    }

    public static ForLogin forLogin(int errorCode, RuntimeException originalException) {
        ForLogin ule = new ForLogin();
        ule.setErrorCode(errorCode);
        ule.setOriginalException(originalException);
        return ule;
    }

    public static ForRegister forRegister(int errorCode, RuntimeException originalException) {
        ForRegister ure = new ForRegister();
        ure.setErrorCode(errorCode);
        ure.setOriginalException(originalException);
        return ure;
    }

    public static ForOffline forOffline(int errorCode, RuntimeException originalException) {
        ForOffline uoe = new ForOffline();
        uoe.setErrorCode(errorCode);
        uoe.setOriginalException(originalException);
        return uoe;
    }

    public static class ForLogin extends UserExcepition { }

    public static class ForRegister extends UserExcepition { }

    public static class ForOffline extends UserExcepition { }

}
