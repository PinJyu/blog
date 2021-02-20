package cn.nhmt.blog.dto.upload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: editormd 编辑markdown上传图片，返回message
 * @Date: 2020-04-07 16:13
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditormdImageUploadMessage {

//    success : 0 | 1,           // 0 表示上传失败，1 表示上传成功
//    message : "提示的信息，上传成功或上传失败及错误信息等。",
//    url     : "图片地址"        // 上传成功时才返回
    private int success;
    private String message;
    private String url;

}
