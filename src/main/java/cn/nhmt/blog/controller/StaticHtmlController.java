package cn.nhmt.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/")
public class StaticHtmlController {

    @GetMapping("/")
    String index() {
        return "index";
    }

    @GetMapping("/about")
    String about() {
        return "about";
    }

    @GetMapping({"/edit", "/edit/{id}"})
    String edit() {
        return "edit";
    }

    @GetMapping("/archive")
    String archive() {
        return "archive";
    }

    @GetMapping("/detail/{id}")
    String detail() {
        return "detail";
    }

}
