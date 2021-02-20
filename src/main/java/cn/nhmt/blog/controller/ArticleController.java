package cn.nhmt.blog.controller;

import cn.nhmt.blog.ao.UserAo;
import cn.nhmt.blog.bo.RestService;
import cn.nhmt.blog.bo.ArticleTableInfoService;
import cn.nhmt.blog.po.Article;
import cn.nhmt.blog.dto.Message;
import cn.nhmt.blog.po.User;
import cn.nhmt.blog.po.group.ArticleGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @Description: article增删改查，及一些article的量化信息
 * @Date: 2020-04-03 02:15
 * @Author: PinJyu
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private RestService<Article> articleRestService;

    @Autowired
    private ArticleTableInfoService articleTableInfoService;

    @PreAuthorize("hasRole('VISITOR')")
    @GetMapping("/catatories")
    Message retrieveCatatories() {
        return articleTableInfoService.doGetCatatory();
    }

    @PreAuthorize("hasRole('VISITOR')")
    @GetMapping("/archive")
    Message retrieveArchive() {
        return articleTableInfoService.doGetArchive();
    }

    @PreAuthorize("hasRole('VISITOR')")
    @GetMapping(value = "/{id}")
    Message retrieveOne(
            @PathVariable(value = "id") int id
            , @RequestParam(value = "ignore", required = false) Set<String> ignoreSet
            , @RequestParam(value = "foreign", required = false) Set<String> foreignSet
    ) {
        return articleRestService.doGet(id ,ignoreSet, foreignSet);
    }

    @PreAuthorize("hasRole('VISITOR')")
    @GetMapping(value = "/list")
    Message retrieveAll(
            @RequestParam(value = "page-number", required = false) Integer pageNumber
            , @RequestParam(value = "per-page", required = false) Integer perPage
            , @RequestParam(value = "offset", required = false) Integer offset
            , @RequestParam(value = "limit", required = false) Integer limit
            , @RequestParam(value = "sort-by", required = false) String sortBy
            , @RequestParam(value = "is-asc", required = false) Boolean isAsc
            , @RequestParam(value = "key", required = false) String key
            , @RequestParam(value = "ignore", required = false) Set<String> ignoreSet
            , @RequestParam(value = "foreign", required = false) Set<String> foreignSet
    ) {
        return articleRestService.doGet(
                pageNumber, perPage, offset, limit, sortBy, isAsc, key, ignoreSet, foreignSet
        );
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @PostMapping
    Message createArticle(
            @Validated(ArticleGroup.Create.class) @RequestBody Article article
            , @AuthenticationPrincipal UserAo userAo
    ) {
        return articleRestService.doPost(article, userAo.toUser());
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @PutMapping
    Message updateArticle(
            @Validated(ArticleGroup.Update.class) @RequestBody Article article
            , @AuthenticationPrincipal UserAo userAo
    ) {
        return articleRestService.doPut(article, userAo.toUser());
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @DeleteMapping
    Message deleteArticle(@RequestParam("id") int id, @AuthenticationPrincipal UserAo userAo) {
        return articleRestService.doDelete(id, userAo.toUser());
    }

}
