package cn.nhmt.blog.bo.mail;

import java.util.Arrays;
import java.util.List;

/**
 * @Description: TODO
 * @Date: 2020-05-09 15:08
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Deprecated
public class Mail {

    private String from;
    private List<String> to;
    private String subject;
    private String content;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public void setTo(String... to) {
        this.to = Arrays.asList(to);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Mail{" +
                "from='" + from + '\'' +
                ", to=" + to +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
