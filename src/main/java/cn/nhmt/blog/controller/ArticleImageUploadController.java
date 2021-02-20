package cn.nhmt.blog.controller;

import cn.nhmt.blog.ao.UserAo;
import cn.nhmt.blog.bo.ArticleImageUploadService;
import cn.nhmt.blog.dto.upload.EditormdImageUploadMessage;
import cn.nhmt.blog.dto.Message;
import cn.nhmt.blog.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

/**
 * @Description: article image 上传
 * @Date: 2020-04-07 15:58
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Validated
@RestController
@RequestMapping("/image")
public class ArticleImageUploadController {

    @Autowired
    private ArticleImageUploadService articleImageUploadService;


    @PreAuthorize("hasRole('AUTHOR')")
    @PostMapping
    Message createCoverImage(
            @RequestParam("id") int id
            , @RequestPart(value = "cover-image") MultipartFile multipartFile
            , @AuthenticationPrincipal UserAo userAo
    ) {
        return articleImageUploadService.createCoverImage(id, multipartFile, userAo.toUser());
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @DeleteMapping
    Message deleteCoverImage(@RequestParam("id") int id, @AuthenticationPrincipal UserAo userAo) {
        return articleImageUploadService.DeleteCoverImage(id, userAo.toUser());
    }

    // special for editormd
    @PreAuthorize("hasRole('AUTHOR')")
    @PostMapping("/markdown")
    EditormdImageUploadMessage createInnerImage(@RequestPart("editormd-image-file") MultipartFile multipartFile) {
        return articleImageUploadService.createInnerImage(multipartFile);
    }

}
