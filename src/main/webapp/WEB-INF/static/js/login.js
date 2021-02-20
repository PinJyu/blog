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
    _DEFAULT_LOGIN_ICON: "login",
    _DEFAULT_CUOHAO_ICON: '<i class="iconfont icon-error-1 text-danger"></i>',
    _DEFAULT_SPINNER_ICON: '<i class="fa fa-spinner fa-pulse fa-fw" aria-hidden="true"></i>',
};

var login = new Vue({
    el          : "#login",
    data        : {
        user_name       : "",
        user_pwd        : "",

        user_name_warn  : "",
        user_pwd_warn   : "",

        login_icon      : icons._DEFAULT_LOGIN_ICON,
        is_enable_login : false,
        is_logining     : false,
    },
    created     : function () {
        this.set_is_enable_login = _.debounce(this._set_is_enable_login, 500);
    },
    watch       : {
        user_name: function (val) {
            this.user_name_warn = "";
            this.set_is_enable_login();
        },
        user_pwd: function (val) {
            this.user_pwd_warn = "";
            this.set_is_enable_login();
        },
    },
    methods     : {
        _set_is_enable_login: function () {
            this.is_enable_login = this.user_name_or_email !== "" && this.user_pwd !== "";
        },
        login: function () {
            if (!this.is_enable_login || this.is_logining) {
                return;
            }
            this.is_enable_login    = false;
            this.is_logining        = true;
            this.login_icon         = icons._DEFAULT_SPINNER_ICON;
            let self = this;
            $.ajax({
                url         : "/blog/login/process",
                type        : "post",
                dataType    : "json",
                data        : {
                    username: self.user_name,
                    password: self.user_pwd,
                },
                beforeSend :set_csrf_token,
                success     : function (data, status, xhr) {
                    self._set_is_enable_login();
                    self.is_logining    = false;
                    self.login_icon     = icons._DEFAULT_LOGIN_ICON;
                    if (data.ok) {
                        let redirect = data.data.redirectUri;
                        location.href = redirect != null ? redirect : "/blog/";
                    } else {
                        let errorCode = data.errorCode;
                        switch (errorCode) {
                            case 200: {
                                self.user_name_warn = icons._DEFAULT_CUOHAO_ICON;
                                break;
                            }
                            case 201: {
                                self.user_pwd_warn = icons._DEFAULT_CUOHAO_ICON;
                                break;
                            }
                        }
                    }
                },
                error       : function (error) {
                    self._set_is_enable_login();
                    self.is_logining    = false;
                    self.login_icon     = icons._DEFAULT_LOGIN_ICON;
                    location.href = "/blog/error";
                },
            });
        },
    }
});