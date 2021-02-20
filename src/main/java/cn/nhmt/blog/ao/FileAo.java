package cn.nhmt.blog.ao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: FileUploadService 与其他service或controller交互pojo
 * @Date: 2020-04-10 20:40
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileAo {

    private String name;
    private String url;

}
