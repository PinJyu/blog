package cn.nhmt.blog.controller;

import cn.nhmt.blog.bo.exception.FileUploadException;
import cn.nhmt.blog.bo.service.ArticleService;
import cn.nhmt.blog.bo.service.FileUploadService;
import cn.nhmt.blog.dto.upload.ImageSuccessAndUrlForEditormd;
import cn.nhmt.blog.dto.upload.FileNameAndUrl;
import cn.nhmt.blog.dto.OkMessage;
import com.mysql.cj.x.protobuf.Mysqlx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: TODO
 * @Date: 2020-04-07 15:58
 * @Author: PinJyu
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/image")
public class RestImageUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ArticleService.Image imageArticleService;


    @PostMapping
    OkMessage imageCreate(@RequestParam("id") int id, @RequestParam(value = "cover-image") MultipartFile multipartFile) throws FileUploadException.FileUploadInArticleImgExcepiton {
        return imageArticleService.handlePostMethod(id, multipartFile);
    }

    @PutMapping
    OkMessage imageUpdate(@RequestParam("id") int id, @RequestParam(value = "cover-image") MultipartFile multipartFile) throws FileUploadException.FileUploadInArticleImgExcepiton {
        return imageArticleService.handlePutMethod(id, multipartFile);
    }

    @DeleteMapping
    OkMessage imageDelete(@RequestParam("id") int id) {
        return imageArticleService.handleDeleteMethod(id);
    }

    // special for editormd
    @PostMapping("/markdown")
    ImageSuccessAndUrlForEditormd mk(@RequestParam("editormd-image-file") MultipartFile multipartFile) throws FileUploadException.FileUploadInMarkdownImgException {
        FileNameAndUrl fuau;
        try {
            fuau = fileUploadService.uploadWithAnymous(multipartFile);
        } catch (FileUploadException e) {
            throw FileUploadException.forMarkdownImg(e.getOriginalException());
        }
        ImageSuccessAndUrlForEditormd mium = new ImageSuccessAndUrlForEditormd();
        mium.setSuccess(1);
        mium.setMessage("");
        mium.setUrl(fuau.getUrl());
        return mium;
    }

}
