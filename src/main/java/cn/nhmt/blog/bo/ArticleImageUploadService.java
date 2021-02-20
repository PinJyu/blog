package cn.nhmt.blog.bo;

import cn.nhmt.blog.dto.Message;
import cn.nhmt.blog.dto.upload.EditormdImageUploadMessage;
import cn.nhmt.blog.po.User;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: article 封面或内部图片上传
 * @Date: 2020-05-22 17:47
 * @Author: PinJyu
 * @Version: 1.0
 **/
public interface ArticleImageUploadService {

    Message createCoverImage(int id, MultipartFile multipartFile, User user);

    Message DeleteCoverImage(int id, User user);

    EditormdImageUploadMessage createInnerImage(MultipartFile multipartFile);

}
