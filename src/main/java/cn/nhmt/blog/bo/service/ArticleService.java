package cn.nhmt.blog.bo.service;

import cn.nhmt.blog.bo.exception.FileUploadException;
import cn.nhmt.blog.dto.OkMessage;
import cn.nhmt.blog.po.Article;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: TODO
 * @Date: 2020-04-19 16:00
 * @Author: PinJyu
 * @Version: 1.0
 **/
public interface ArticleService {
     interface Rest {

        OkMessage handleGetMethod(String catatory, int number);

        OkMessage handlePostMethod(Article a);

        OkMessage handlePutMethod(Article a);

        OkMessage handleDeleteMethod(int id);

    }

    interface Info {

        OkMessage handleGetCatatories();

        OkMessage handleGetArchive();

    }

    interface Image {

         OkMessage handlePostMethod(int id, MultipartFile multipartFile) throws FileUploadException.FileUploadInArticleImgExcepiton;

         OkMessage handlePutMethod(int id, MultipartFile multipartFile) throws FileUploadException.FileUploadInArticleImgExcepiton;

         OkMessage handleDeleteMethod(int id);

    }
}
