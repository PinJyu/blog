var editor;
var syncScrollMounted = false;
Vue.nextTick(function() {
    editor = editormd("test-editor", {
        width  : "90%",
        height :  "100vh",
        path   : "/blog/plugins/editormd/lib/",
        placeholder: "你的新一篇博客...",
        theme : "default",
        previewTheme : "dark",
        editorTheme : "rubyblue",
        emoji: true,
        // 支持的emoji
        // Github emoji : http://www.emoji-cheat-sheet.com/
        // Twitter Emoji(Twemoji) : http://twitter.github.io/twemoji/preview.html
        // FontAwesome icon font emoji : http://fortawesome.github.io/Font-Awesome/icons/
        // Editor.md logo icon font emoji
        // saveHTMLToTextarea : true,    // 保存 HTML 到 Textarea
        htmlDecode : "style,script,iframe|on*",
        taskList: true,
        tocm : true,         // Using [TOCM]
        tex : true,                   // 开启科学公式TeX语言支持，默认关闭
        flowChart : true,             // 开启流程图支持，默认关闭
        sequenceDiagram : true,       // 时序图
        imageUpload : true,
        imageFormats : ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
        imageUploadURL : "/blog/upload/image/markdown",

        toolbarIcons : function() {
            return ["undo", "redo", "|", "link", "reference-link", "code", "preformatted-text",
                "code-block", "table", "datetime", "emoji", "html-entities", "pagebreak", "|", "watch", "unwatch",
                "preview", "fullscreen", "syncScroll", "clear", "search", "|", "help","info", "|", "image",
                "save"];
        },

        toolbarIconsClass : {
            syncScroll : "fa-sort",
        },
        // 用于增加自定义工具栏的功能，可以直接插入HTML标签，不使用默认的元素创建图标
        toolbarCustomIcons : {
            save: '<button type="button" class="btn" title="保存" style="background-color: #fff" data-toggle="modal" data-target="#post-blog-modal">\n' +
                '<i class="fa fa-floppy-o"></i>' +
                '</button>',

        },

        toolbarHandlers : {
            syncScroll: function () {
                editor.config("syncScrolling", (syncScrollMounted = !syncScrollMounted));
            },
        },

        lang : {
            toolbar : {
                syncScroll: "开启或关闭同步滚动",
                image: "添加markdown内部图片",
            },
        },
    });
    editor.setToolbarAutoFixed(false); // 固定工具栏
    editormd.emoji = {
        path: "/blog/plugins/emoji/",
        ext: ".png",
    };

    (function () {
        let is_new = false;
        let article_id;
        let rema = window.location.pathname.match(/\d+/);
        if (rema !== null && rema.length !== 0) {
            article_id = rema[rema.length - 1];
        }
        if (article_id === undefined || article_id <= 0 || article_id === "") {
            is_new = true;
            upload_modal.is_new = is_new;
        }
        if (!is_new) {
            $.ajax({
                url: "/blog/article/detail/" + article_id,
                dataType: "json",
                success: function (message) {
                    if (message.ok) {
                        document.getElementsByClassName("editormd-markdown-textarea")[0].innerText = message.article.markdown;
                        upload_modal.article = message.article;
                    } else {
                        alert("update blog raise a error, redirect index after your click.");
                        location.assign("/blog");
                    }
                }
            });
        }
    })();
});

var upload_modal = new Vue({
    el: "#post-blog-modal",
    data: {
        article: null,
        catatory_list: null,
        id: null,
        title: null,
        image: null,
        catatory: null,
        cancel_image: false,
        cancel_image_list: [],
        is_uploading: false,
        has_empty: false,
        is_new: false,
        alert: null,
        icon: "submit",
    },
    watch: {
        cancel_image_list: function (val) {
            this.cancel_image = val.length !== 0;
            console.log(this.cancel_image);
            console.log(this.cancel_image_list);
        },
        article: function (val) {
            this.title = val.title;
            this.catatory = val.catatory;
            this.id = val.id;
        }
    },
    created: function () {
        let self = this;
        $.ajax({
            url: "/blog/article/catatory",
            dataType: "json",
            success: function (data) {
                if (data.ok) {
                    self.catatory_list = data.catatorys;
                }
            },
        });
    },
    methods: {
        on_click: function () {
            if (this.is_uploading) {
                this.alert = "正在上传";
                this.has_empty = true;
                return;
            }
            if (!this.valid_not_null(this.title)) {
                this.alert = "标题不能为空！！！";
                this.has_empty = true;
                return
            }

            if (!this.valid_not_null(this.image)) {
                this.alert = "您可以添加一个封面";
                this.has_empty= true;
            }

            if (!this.valid_not_null(this.catatory)) {
                this.alert = "分类不能为空！！！";
                this.has_empty = true;
                return;
            }
            let markdown;
            if (!this.valid_not_null(markdown = editor.getMarkdown())) {
                this.alert = "markdown正文不能为空";
                this.has_empty = true;
                return;
            }

            let self = this;
            this.has_empty = false;
            this.is_uploading = true;
            this.icon = '<i class="fa fa-spinner fa-pulse fa-fw"></i>';
            let formData = new FormData();
            if (!this.is_new) {
                formData.append("_method", "PUT");
                formData.append("id", this.id);
                formData.append("cancel-image", this.cancel_image);
            }
            if (!this.cancel_image && this.valid_not_null(this.image)) {
                formData.append("article-image", this.image);
            }
            formData.append("title", this.title);
            formData.append("catatory", this.catatory);
            formData.append("markdown", markdown);
            $.ajax({
                url: "/blog/article",
                type: "POST",
                data: formData,
                dataType: "json",
                processData: false,
                contentType: false,
                success: function (data) {
                    self.alert = self.is_new ? data.ok ? "上传成功" : "上传失败"
                        : data.ok ? "更新成功" : "更新失败";
                    self.has_empty = true;
                    self.icon = "submit";
                    self.is_uploading = false;
                },
                error: function (error) {
                    console.log(error);
                    document.write(error.responseText);
                    self.alert = self.is_new ? "上传错误" : "更新错误";
                    self.has_empty = true;
                    self.icon = "submit";
                    self.is_uploading = false;
                }
            })
        },
        valid_not_null: function (item) {
            return !(item == null || item === "");

        },
        on_file_change: function (event) {
            let file = event.target.files[0];
            this.image = file;
            $("#filename-label").html(file.name);
        }
    }
});