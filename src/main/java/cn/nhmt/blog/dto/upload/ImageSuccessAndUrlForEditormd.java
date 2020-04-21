package cn.nhmt.blog.dto.upload;

/**
 * @Description: TODO
 * @Date: 2020-04-07 16:13
 * @Author: PinJyu
 * @Version: 1.0
 **/
public class ImageSuccessAndUrlForEditormd {

//    success : 0 | 1,           // 0 表示上传失败，1 表示上传成功
//    message : "提示的信息，上传成功或上传失败及错误信息等。",
//    url     : "图片地址"        // 上传成功时才返回
    private int success;
    private String message;
    private String url;

    public ImageSuccessAndUrlForEditormd() {
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "MarkdownImageUploadMessage{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
