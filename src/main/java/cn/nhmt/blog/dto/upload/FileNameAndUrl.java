package cn.nhmt.blog.dto.upload;

import cn.nhmt.blog.dto.OkMessage;

/**
 * @Description: TODO
 * @Date: 2020-04-10 20:40
 * @Author: PinJyu
 * @Version: 1.0
 **/
public class FileNameAndUrl {

    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
