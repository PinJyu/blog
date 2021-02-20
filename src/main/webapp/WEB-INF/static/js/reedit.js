var syncScrollMounted = false;
var editor = editormd("test-editor", {
    width               : "90%",
    height              :  "100vh",
    path                : "/blog/plugins/editormd/lib/",
    placeholder         : "你的新一篇博客...",
    theme               : "default",
    previewTheme        : "dark",
    editorTheme         : "rubyblue",
    emoji               : true,
    // 支持的emoji
    // Github emoji : http://www.emoji-cheat-sheet.com/
    // Twitter Emoji(Twemoji) : http://twitter.github.io/twemoji/preview.html
    // FontAwesome icon font emoji : http://fortawesome.github.io/Font-Awesome/icons/
    // Editor.md logo icon font emoji
    // saveHTMLToTextarea : true,    // 保存 HTML 到 Textarea
    htmlDecode          : "style,script,iframe|on*",
    taskList            : true,
    tocm                : true,         // Using [TOCM]
    tex                 : true,                   // 开启科学公式TeX语言支持，默认关闭
    flowChart           : true,             // 开启流程图支持，默认关闭
    sequenceDiagram     : true,       // 时序图
    imageUpload         : true,
    imageFormats        : ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
    imageUploadURL      : "/blog/image/markdown",

    toolbarIcons        : function() {
        return ["undo", "redo", "|", "link", "reference-link", "code", "preformatted-text",
            "code-block", "table", "datetime", "emoji", "html-entities", "pagebreak", "|", "watch", "unwatch",
            "preview", "fullscreen", "syncScroll", "clear", "search", "|", "help","info", "|", "image",
            "save"];
    },

    toolbarIconsClass   : {
        syncScroll : "fa-sort",
    },
    // 用于增加自定义工具栏的功能，可以直接插入HTML标签，不使用默认的元素创建图标
    toolbarCustomIcons  : {
        save: '<button type="button" class="btn" title="保存" style="background-color: #fff" data-toggle="modal" data-target="#post-blog-modal">\n' +
            '<i class="fa fa-floppy-o"></i>' +
            '</button>',

    },

    toolbarHandlers     : {
        syncScroll: function () {
            editor.config("syncScrolling", (syncScrollMounted = !syncScrollMounted));
        },
    },

    lang                : {
        toolbar : {
            syncScroll: "开启或关闭同步滚动",
            image: "添加markdown内部图片",
        },
    },

    onchange            : function () {
        submit_modal.set_article_markdown(this.getValue());
    },
});

editor.setToolbarAutoFixed(false); // 固定工具栏
editormd.emoji = {
    path    : "/blog/plugins/emoji/",
    ext     : ".png",
};

// 版权声明：本文为CSDN博主「paranoidMao」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
// 原文链接：https://blog.csdn.net/web_yzm/java/article/details/81669772
function getCookie(c_name){
    if (document.cookie.length > 0){　　//先查询cookie是否为空，为空就return ""
        let c_start = document.cookie.indexOf(c_name + "=");　//通过String对象的indexOf()来检查这个cookie是否存在，不存在就为 -1　　
        if (c_start !== -1){
            c_start = c_start + c_name.length + 1;　　//最后这个+1其实就是表示"="号啦，这样就获取到了cookie值的开始位置
            let c_end = document.cookie.indexOf(";", c_start);　　//其实我刚看见indexOf()第二个参数的时候猛然有点晕，后来想起来表示指定的开始索引的位置...这句是为了得到值的结束位置。因为需要考虑是否是最后一项，所以通过";"号是否存在来判断
            if (c_end === -1)
                c_end = document.cookie.length;
            return document.cookie.substring(c_start,c_end);　　//通过substring()得到了值。想了解unescape()得先知道escape()是做什么的，都是很重要的基础，想了解的可以搜索下，在文章结尾处也会进行讲解cookie编码细节
        }
    }
    return null;
}

const XSRF_TOKEN    = "XSRF-TOKEN";
const X_XSRF_TOKEN  = "X-XSRF-TOKEN";


var set_csrf_token = function (request) {
    request.setRequestHeader(X_XSRF_TOKEN, getCookie(XSRF_TOKEN));
};

var icons = {
    _DEFAULT_SUBMIT_ICON    : "submit",
    _DEFAULT_DUIHAO_ICON    : '<i class="iconfont icon-duihao text-success"></i>',
    _DEFAULT_CUOHAO_ICON    : '<i class="iconfont icon-error-1 text-danger"></i>',
    _DEFAULT_SPINNER_ICON   : '<i class="fa fa-spinner fa-pulse fa-fw" aria-hidden="true"></i>',
};

var _DEFAULT_SUBMIT_MESSAGE = "上传博客";

var submit_modal = new Vue({
    el: "#post-blog-modal",
    data: {
        article                     : null,
        article_id                  : 0,
        article_title               : "",
        article_catatory            : "",
        article_markdown            : "",
        article_image               : null,

        article_title_warn          : "",
        article_catatory_warn       : "",

        is_legal_article_title      : false,
        is_legal_article_catatory   : false,
        is_legal_article_markdown   : false,

        reg1                        : new RegExp(/^.{1,127}$/),
        reg2                        : new RegExp(/^.{1,63}$/),
        reg3                        : new RegExp(/^\/blog\/reedit\/(\d+)$/),

        catatory_map                : null,

        image_option                : "true",
        is_save_image               : true,

        image_label_placeholder     : "Maybe have a cover image",
        submit_icon                 : icons._DEFAULT_SUBMIT_ICON,
        submit_message              : _DEFAULT_SUBMIT_MESSAGE,

        is_submitting               : false,
        is_enable_submit            : false,
        is_enable_submit_backup     : false,
    },
    created: function () {
        let self = this;
        let article_id = getCookie("ARTICLE_ID");
        if (article_id != null) {
            article_id  = parseInt(article_id);
            let urlSearchParams = new URLSearchParams();
            urlSearchParams.append("ignore", "introduction,userId,createGmt,lastModifyGmt")
            $.ajax({
                url: "/blog/articles/" + article_id + "?" + urlSearchParams.toString(),
                dataType: "json",
                success: function (data) {
                    if (data.ok) {
                        let article = data.data.article;
                        document.querySelector("textarea.editormd-markdown-textarea").innerHTML = article.markdown;
                        submit_modal.article = article;
                        $.ajax({
                            url: "/blog/article/catatories",
                            datatype: "json",
                            success: function (data) {
                                if (data.ok) {
                                    self.catatory_map = data.data.catatories;
                                }
                            },
                        });
                    } else {
                        location.href = "/blog";
                    }
                }
            });
        } else {
            location.href = "/blog";
        }

        this.valid_article_title = _.debounce(this._valid_article_title, 500);
        this.valid_article_catatory = _.debounce(this._valid_article_catatory, 500);
        this.set_article_markdown = _.debounce(this._set_article_markdown, 500);
    },
    watch: {
        article: function (article) {
            this.article_id = article.id;
            this.article_title = article.title;
            this.article_catatory = article.catatory;
            this.article_markdown = article.markdown;
        },
        article_title: function () {
            this.article_title_warn = "";
            this.valid_article_title();
        },
        article_catatory: function () {
            this.article_catatory_warn = "";
            this.valid_article_catatory();
        },
        article_markdown: function (val) {
            this._valid_article_markdown();
        },
        image_option: function (val) {
            this.is_save_image = val === "true";
            this.$refs.save_image_input.disabled = !this.is_save_image;
            if (this.is_save_image) {
                this.image_label_placeholder = "Maybe have a cover image";
            } else {
                this.article_image = null;
                this.image_label_placeholder = "Delete cover image";
            }
        },
    },
    methods: {
        _set_article_markdown: function (markdown) {
            this.article_markdown = markdown;
        },
        legal_or_not_warn: function (is_legal) {
            return is_legal ? icons._DEFAULT_DUIHAO_ICON : icons._DEFAULT_CUOHAO_ICON;
        },
        set_is_enable_submit: function () {
            this.is_enable_submit_backup = this.is_legal_article_title && this.is_legal_article_catatory
                && this.is_legal_article_markdown;
            if (!this.is_submitting) {
                this.is_enable_submit = this.is_enable_submit_backup;
            }
        },
        set_submit_message: function (message) {
            this.submit_message = _DEFAULT_SUBMIT_MESSAGE + "：" + message;
        },
        _valid_article_title: function () {
            let is_legal, is_empty;
            this.is_legal_article_title = !(is_empty = this.article_title === "")
                && (is_legal = this.reg1.test(this.article_title));
            this.article_title_warn = is_empty ? "" : this.legal_or_not_warn(is_legal);
            this.set_is_enable_submit();
        },
        _valid_article_catatory: function () {
            let is_legal, is_empty;
            this.is_legal_article_catatory = !(is_empty = this.article_catatory === "")
                && (is_legal = this.reg2.test(this.article_catatory));
            this.article_catatory_warn = is_empty ? "" : this.legal_or_not_warn(is_legal);
            this.set_is_enable_submit();
        },
        _valid_article_markdown: function () {
            this.is_legal_article_markdown = this.article_markdown !== "";
            this.set_is_enable_submit();
        },
        save_image: function (event) {
            let file = event.target.files[0];
            this.article_image = file;
            this.image_label_placeholder = file.name;
        },
        submit_blog: function () {
            if (!this.is_enable_submit || this.is_submitting){
                return;
            }
            this.is_submitting      = true;
            this.is_enable_submit   = false;
            this.submit_icon        = icons._DEFAULT_SPINNER_ICON;

            let self = this;
            let posts = {
                id      : self.article_id,
                title   : self.article_title,
                catatory: self.article_catatory,
                markdown: self.article_markdown,
            };
            $.ajax({
                url         : "/blog/articles?_method=PUT",
                type        : "post",
                // data        : JSON.stringify(posts),
                data        : JSON.stringify(posts),
                dataType    : "json",
                contentType : "application/json; charset=utf-8",
                beforeSend : set_csrf_token,
                success     : function (data) {
                    if (!data.ok) {
                        self.after_submit_fail("更新失败");
                        return;
                    }
                    let id = data.data.id;
                    if (self.is_save_image) {
                        if (self.article_image == null) {
                            self.after_submit_success("文章更新成功", id);
                        } else {
                            let image_form_data = new FormData();
                            image_form_data.append("cover-image", self.article_image);
                            $.ajax({
                                url         : "/blog/image?id=" + id,
                                type        : "post",
                                data        : image_form_data,
                                dataType    : "json",
                                processData : false,
                                contentType : false,
                                beforeSend : set_csrf_token,
                                success     : function (data) {
                                    self.after_submit_success(data.ok ? "更新成功" : "文章更新成功, 封面更新失败", id);
                                },
                                error       : function (error) {
                                    self.after_submit_success("文章更新成功, 封面更新错误", id);
                                }
                            });
                        }
                    } else {
                        $.ajax({
                            url         : "/blog/image?_method=DELETE&id=" + id,
                            type        : "post",
                            dataType    : "json",
                            contentType : "application/json; charset=utf-8",
                            beforeSend : set_csrf_token,
                            success: function (data) {
                                self.after_submit_success(data.ok ? "更新成功, 封面删除" : "文章更新成功, 封面删除失败", id);
                            },
                            error: function (error) {
                                self.after_submit_success("文章更新成功, 封面删除错误", id);
                            }
                        });
                    }
                },
                error: function (error) {
                    self.after_submit_fail("文章更新错误");
                }
            });

        },
        after_submit_success: function (message, id) {
            this.set_submit_message('<a href="/blog/detail/' + id + '">' + message + '</a>');
            this.is_submitting      = false;
            this.is_enable_submit   = this.is_enable_submit_backup;
            this.submit_icon        = icons._DEFAULT_SUBMIT_ICON;
        },
        after_submit_fail: function (message) {
            this.set_submit_message(message);
            this.is_submitting      = false;
            this.is_enable_submit   = this.is_enable_submit_backup;
            this.submit_icon        = icons._DEFAULT_SUBMIT_ICON;
        }
    }
});

$("#post-blog-modal").on("show.bs.modal", function (e) {
    submit_modal.submit_message = _DEFAULT_SUBMIT_MESSAGE;
});
