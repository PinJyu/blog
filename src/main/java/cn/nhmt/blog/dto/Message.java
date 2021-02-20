package cn.nhmt.blog.dto;


import lombok.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: dto的统一构形
 * @Date: 2020-04-03 17:35
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Message {

    @NonNull
    private boolean ok;
    private int errorCode;
    private String message;
    private Map<String, Object> data;

    public static Message errorCodeOf(int errorCode) {
        Message m = new Message(false);
        m.setErrorCode(errorCode);
        return m;
    }

    public Message put(String key, Object value) {
        if (this.data == null) {
            data = new HashMap<>();
        }
        data.put(key, value);
        return this;
    }

}
