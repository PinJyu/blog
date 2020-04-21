package cn.nhmt.blog.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: TODO
 * @Date: 2020-04-03 17:35
 * @Author: PinJyu
 * @Version: 1.0
 **/
public class OkMessage {

    private boolean ok;
    private int errorCode;
    private Map<String, Object> optional;

    public OkMessage(boolean ok) {
        this.ok = ok;
    }

    public OkMessage(int errorCode) {
        this.errorCode = errorCode;
        this.ok = false;
    }

    public OkMessage(Map<String, Object> optionalMessage) {
        this.optional= optionalMessage;
        this.ok = true;
    }

    public OkMessage put(String key, Object value) {
        if (optional== null) {
            optional= new HashMap<>();
        }
        optional.put(key, value);
        return this;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public Map<String, Object> getOptional() {
        return optional;
    }

    public void setOptional(Map<String, Object> optional) {
        this.optional = optional;
    }

    public static OkMessage withErrorCode(int errorCode) {
        return new OkMessage(errorCode);
    }

    @Override
    public String toString() {
        return "OkMessage{" +
                "ok=" + ok +
                ", errorCode=" + errorCode +
                ", optional=" + optional +
                '}';
    }
}
