// 日期格式化
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

var markdown_view_container = new Vue({
    el: "#markdown-view-container",
    data: {
        article: null,
    },
    methods: {
        format_timeStamp: function (timeStamp) {
            return new Date(timeStamp).Format("yyyy-MM-dd hh:mm");
        },
    }
});

$(function () {
    const base_url = "/blog/article/detail/"
    let rema = window.location.pathname.match(/\d+/);
    let article_id;
    if (rema !== null && rema.length !== 0) {
        article_id = rema[rema.length - 1];
    }
    if (article_id === undefined || article_id <= 0 || article_id === "") {
        window.location.assign("/blog");
        return;
    }
    let editormdView;
    $.ajax({
        url: base_url + article_id,
        dataType: "json",
        success: function (message) {
            if (message.ok) {
                markdown_view_container.article = message.article;
                editormdView = editormd.markdownToHTML("markdown-view", {
                    markdown        : message.article.markdown,//+ "\r\n" + $("#append-test").text(),
                    // htmlDecode      : true,       // 开启 HTML 标签解析，为了安全性，默认不开启
                    htmlDecode      : "style,script,iframe",  // you can filter tags decode
                    //toc             : false,
                    tocm            : true,    // Using [TOCM]
                    tocContainer    : "#toc-container", // 自定义 ToC 容器层
                    //gfm             : false,
                    //tocDropdown     : true,
                    // markdownSourceCode : true, // 是否保留 Markdown 源码，即是否删除保存源码的 Textarea 标签
                    emoji           : true,
                    taskList        : true,
                    tex             : true,  // 默认不解析
                    flowChart       : true,  // 默认不解析
                    sequenceDiagram : true,  // 默认不解析
                });
                document.getElementById("foot").classList.remove("d-none");
            }
        },
    });
});
