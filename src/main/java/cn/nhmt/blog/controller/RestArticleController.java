package cn.nhmt.blog.controller;

import cn.nhmt.blog.bo.service.ArticleService;
import cn.nhmt.blog.bo.service.FileUploadService;
import cn.nhmt.blog.po.Article;
import cn.nhmt.blog.dto.upload.FileNameAndUrl;
import cn.nhmt.blog.dto.OkMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: article正删改查，及一些article的量化信息
 * @Date: 2020-04-03 02:15
 * @Author: PinJyu
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/article")
public class RestArticleController {

    @Autowired
    private ArticleService.Rest restArticleService;

    @Autowired
    private ArticleService.Info infoArticleService;

    @GetMapping("/{catatory}/{number}")
    OkMessage query(@PathVariable("catatory") String catatory, @PathVariable("number") int number) {
        return restArticleService.handleGetMethod(catatory, number);
    }

    @PostMapping
    OkMessage create(Article article) {
        return restArticleService.handlePostMethod(article);
    }

    @PutMapping
    OkMessage update(Article article) {
        return restArticleService.handlePutMethod(article);
    }

    @DeleteMapping
    OkMessage delete(@RequestParam("id") int id) {
        return restArticleService.handleDeleteMethod(id);
    }

    // 附加接口,一些信息
    @GetMapping("/catatories")
    OkMessage catatories() {
        return infoArticleService.handleGetCatatories();
    }

    @GetMapping("/archive")
    OkMessage archive() {
        return infoArticleService.handleGetArchive();
    }

}
