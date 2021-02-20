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

var set_csrf_token = function (xmlHttpRequest) {
    xmlHttpRequest.setRequestHeader(X_XSRF_TOKEN, getCookie(XSRF_TOKEN));
};

var icons = {
    _DEFAULT_REGISTER_ICON  : "register",
    _DEFAULT_SENDEMAIL_ICON : '<i class="fa fa-paper-plane" aria-hidden="true"></i>',
    _DEFAULT_DUIHAO_ICON    : '<i class="iconfont icon-duihao text-success"></i>',
    _DEFAULT_CUOHAO_ICON    : '<i class="iconfont icon-error-1 text-danger"></i>',
    _DEFAULT_SPINNER_ICON   : '<i class="fa fa-spinner fa-pulse fa-fw" aria-hidden="true"></i>',
};

var register = new Vue({
    el      : "#register",
    data    : {
        user_name                       : "",
        user_email                      : "",
        user_email_CAPTCHA              : "",
        user_pwd                        : "",
        user_pwd_again                  : "",

        user_name_warn                  : "",
        user_email_warn                 : "",
        user_email_CAPTCHA_warn         : "",
        user_pwd_warn                   : "",
        user_pwd_again_warn             : "",

        is_legal_user_name              : false,
        is_legal_user_email             : false,
        is_legal_user_email_CAPTCHA     : false,
        is_legal_user_pwd               : false,
        is_legal_user_pwd_again         : false,

        reg1                            : new RegExp(/^(?=[a-zA-Z\u4e00-\u9fa5])[a-zA-Z\u4e00-\u9fa5\d]{4,10}$/),
        reg2                            : new RegExp(/^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/),
        reg3                            : new RegExp(/^\d{6}$/),
        reg4                            : new RegExp(/^(?=.*[a-z].*)(?=.*[A-Z].*)(?=.*\d.*)(?=.*[,.;:_\-+!@#$%^&*()].*)[a-zA-Z\d,.;:_\-+!@#$%^&*()]{8,16}$/),

        register_icon                   : icons._DEFAULT_REGISTER_ICON,
        send_email_CAPTCHA_icon         : icons._DEFAULT_SENDEMAIL_ICON,

        origin_valid_user_name_stamp    : 1,
        origin_valid_user_email_stamp   : 1,

        input_visiblity_flag            : true,

        is_sending_email                : false,
        is_enable_send_email            : false,
        is_enable_send_email_backup     : false,
        success_send_email              : false,

        is_registering                  : false,
        is_enable_register              : false,
        is_enable_register_backup       : false,
    },
    created : function () {
        this.valid_user_name            = _.debounce(this._valid_user_name, 500);
        this.valid_user_email           = _.debounce(this._valid_user_email, 500);
        this.valid_user_email_CAPTCHA   = _.debounce(this._valid_user_email_CAPTCHA, 500);
        this.valid_user_pwd             = _.debounce(this._valid_user_pwd, 500);
        this.valid_user_pwd_again       = _.debounce(this._valid_user_pwd_again, 500);
    },
    watch   : {
        user_name: function () {
            this.user_name_warn = "";
            this.valid_user_name();
        },
        user_email: function () {
            this.user_email_warn = "";
            this.valid_user_email();
        },
        user_email_CAPTCHA: function () {
            this.user_email_valid_warn = "";
            this.valid_user_email_CAPTCHA();
        },
        user_pwd: function () {
            this.user_pwd_warn = "";
            this.user_pwd_again_warn = "";
            this.valid_user_pwd();
        },
        user_pwd_again: function () {
            this.user_pwd_again_warn = "";
            this.valid_user_pwd_again();
        },
        success_send_email: function (val) {
            this.$refs.captcha.disabled = !val;
        }
    },
    methods : {
        legal_or_not_warn: function (is_legal) {
            return is_legal ? icons._DEFAULT_DUIHAO_ICON : icons._DEFAULT_CUOHAO_ICON;
        },
        set_is_enable_register: function () {
            this.is_enable_register_backup = this.is_legal_user_name
                && this.is_legal_user_email
                && this.is_legal_user_email_CAPTCHA
                && this.is_legal_user_pwd
                && this.is_legal_user_pwd_again
            if (!this.is_registering) {
                this.is_enable_register = this.is_enable_register_backup;
            }
        },
        set_is_enable_send_email: function (flag) {
            this.is_enable_send_email_backup = flag;
            if (!this.is_sending_email) {
                this.is_enable_send_email = this.is_enable_send_email_backup;
            }
        },
        _origin_valid_user_name: function () {
            let self = this;
            let stamp = this.origin_valid_user_name_stamp;
            $.ajax({
                url         : "/blog/register/valid-name",
                type        : "post",
                dataType    : "json",
                data        : { name: self.user_name },
                beforeSend : set_csrf_token,
                success     : function (data) {
                    if (stamp !== self.origin_valid_user_name_stamp) return;
                    let is_legal = data.ok;
                    self.is_legal_user_name = is_legal;
                    self.user_name_warn = self.legal_or_not_warn(is_legal);
                    self.set_is_enable_register();
                },
                error       : function (error) {
                    if (stamp !== self.origin_valid_user_name_stamp) return;
                    self.is_legal_user_name = false;
                    self.user_name_warn = self.legal_or_not_warn(false);
                    self.set_is_enable_register();
                },
            });
        },
        _valid_user_name: function () {
            let is_legal, is_empty;
            is_empty = this.user_name === "";
            is_legal = this.reg1.test(this.user_name);
            this.is_legal_user_name = false;
            this.origin_valid_user_name_stamp += 1;
            if (is_legal) {
                this.user_name_warn = icons._DEFAULT_SPINNER_ICON;
                this._origin_valid_user_name();
            } else {
                this.user_name_warn = is_empty ? "" : this.legal_or_not_warn(false);
                this.set_is_enable_register();
            }
        },
        _origin_valid_user_email: function () {
            let self = this;
            let stamp = this.origin_valid_user_email_stamp;
            $.ajax({
                url         : "/blog/register/valid-email",
                type        : "post",
                dataType    : "json",
                data        : {email: self.user_email},
                beforeSend : set_csrf_token,
                success     : function (data) {
                    if (stamp !== self.origin_valid_user_email_stamp) return;
                    let is_legal = data.ok;
                    self.is_legal_user_email    = is_legal;
                    self.user_email_warn        = self.legal_or_not_warn(is_legal);
                    self.success_send_email     = false;
                    self.user_email_CAPTCHA     = "";
                    self._valid_user_email_CAPTCHA();
                    self.set_is_enable_send_email(is_legal);
                    self.set_is_enable_register();
                },
                error       : function (error) {
                    if (stamp !== self.user_email_stamp) return;
                    self.is_legal_user_email    = false;
                    self.user_email_warn        = self.legal_or_not_warn(false);
                    self.success_send_email     = false;
                    self.user_email_CAPTCHA     = "";
                    self._valid_user_email_CAPTCHA();
                    self.set_is_enable_send_email(false);
                    self.set_is_enable_register();
                },
            });
        },
        _valid_user_email: function () {
            let is_legal, is_empty;
            is_empty = this.user_email === "";
            is_legal = this.reg2.test(this.user_email);
            this.is_legal_user_email = false;
            this.origin_valid_user_email_stamp += 1;
            if (is_legal) {
                this.user_email_warn = icons._DEFAULT_SPINNER_ICON;
                this._origin_valid_user_email();
            } else {
                this.user_email_warn    = is_empty ? "" : this.legal_or_not_warn(false);
                this.success_send_email = false;
                this.user_email_CAPTCHA = "";
                this._valid_user_email_CAPTCHA();
                this.set_is_enable_send_email(false);
                this.set_is_enable_register();
            }
        },
        _valid_user_email_CAPTCHA: function () {
            let is_legal, is_empty;
            this.is_legal_user_email_CAPTCHA = !(is_empty = this.user_email_CAPTCHA === "")
                && (is_legal = this.reg3.test(this.user_email_CAPTCHA) && this.success_send_email);
            this.user_email_CAPTCHA_warn = is_empty ? "" : is_legal ? "" : icons._DEFAULT_CUOHAO_ICON;
            this.set_is_enable_register();
        },
        _valid_user_pwd: function () {
            let is_legal, is_empty;
            this.is_legal_user_pwd = !(is_empty = this.user_pwd === "")
                && (is_legal = this.reg4.test(this.user_pwd));
            this.user_pwd_warn = is_empty ? "" : this.legal_or_not_warn(is_legal);
            if (this.user_pwd_again !== "") {
                this._valid_user_pwd_again();
            }
            this.set_is_enable_register();
        },
        _valid_user_pwd_again: function () {
            let is_legal, is_empty;
            this.is_legal_user_pwd_again = !(is_empty = this.user_pwd_again === "")
                && (is_legal = this.reg4.test(this.user_pwd_again) && this.user_pwd === this.user_pwd_again);
            this.user_pwd_again_warn = is_empty ? "" : this.legal_or_not_warn(is_legal);
            this.set_is_enable_register();
        },
        input_visiblity: function () {
            if (this.input_visiblity_flag) {
                this.input_visiblity_flag = false;
                this.$refs.user_pwd_input.type = "text";
                this.$refs.user_pwd_again_input.type = "text";
            }
        },
        input_disvisiblity: function () {
            this.input_visiblity_flag = true;
            let self = this;
            setTimeout(function () {
                if (self.input_visiblity_flag) {
                    self.$refs.user_pwd_input.type = "password";
                    self.$refs.user_pwd_again_input.type = "password";
                }
            }, 500);
        },
        send_email_CAPTCHA: function () {
            if (!this.is_legal_user_email || !this.is_enable_send_email || this.is_sending_email) {
                return;
            }
            this.is_sending_email = true;
            this.is_enable_send_email = false;
            this.send_email_CAPTCHA_icon = icons._DEFAULT_SPINNER_ICON;
            let self = this;
            $.ajax({
                url         : "/blog/register/send-email-CAPTCHA",
                type        : "post",
                dataType    : "json",
                data        : {email: self.user_email},
                beforeSend  : set_csrf_token,
                success     : function (data) {
                    let is_success              = data.ok;
                    self.is_legal_user_email    = is_success;
                    self.user_email_warn        = self.legal_or_not_warn(is_success);
                    let timeout                 = 10;
                    let count_id                = setInterval(function () {
                        if (timeout === -1) {
                            self.is_sending_email = false;
                            self.send_email_CAPTCHA_icon = icons._DEFAULT_SENDEMAIL_ICON;
                            self.set_is_enable_send_email(self.is_enable_send_email_backup);
                            clearInterval(count_id);
                            return;
                        } else if (timeout === 10) {
                            self.success_send_email = is_success;
                        }
                        self.send_email_CAPTCHA_icon = timeout--;
                    }, 1000);
                },
                error: function (error) {
                    self.success_send_email = false;
                    self.is_sending_email = false;
                    self.send_email_CAPTCHA_icon = icons._DEFAULT_SENDEMAIL_ICON;
                    self.set_is_enable_send_email(self.is_enable_send_email_backup);
                }
            });
        },
        register: function () {
            if (is_timeout) {
                alert("页面超时，请刷新页面");
                return;
            }
            if (!this.is_enable_register || this.is_registering) {
                return;
            }
            this.is_enable_register = false;
            this.is_registering = true;
            this.register_icon = icons._DEFAULT_SPINNER_ICON;
            let self = this;
            $.ajax({
                url         : "/blog/register/go",
                type        : "post",
                dataType    : "json",
                data        : {
                    name        : self.user_name,
                    email       : self.user_email,
                    password    : self.user_pwd,
                    CAPTCHA     : self.user_email_CAPTCHA,
                },
                beforeSend : set_csrf_token,
                success     : function (data) {
                    self.is_registering     = false;
                    self.register_icon      = icons._DEFAULT_REGISTER_ICON;
                    self.is_enable_register = self.is_enable_register_backup;
                    if (data.ok) {
                        location.href = "/blog/login";
                    } else {
                        let errorCode = data.errorCode;
                        switch (errorCode) {
                            case 604:
                                self.user_pwd_warn      = self.legal_or_not_warn(false);
                                self.is_legal_user_pwd  = false;
                                break;
                            case 610:
                            case 611:
                                self.user_email_CAPTCHA_warn        = self.legal_or_not_warn(false);
                                self.is_legal_user_email_CAPTCHA    = false;
                                break;
                            case 609:
                            case 612:
                                is_timeout = true;
                                alert("未知页面超时，请刷新页面");
                        }
                    }
                },
                error: function (error) {
                    self.is_registering     = false;
                    self.register_icon      = icons._DEFAULT_REGISTER_ICON;
                    self.is_enable_register = self.is_enable_register_backup;
                },
            });
        }
    }
});

const timeout   = 25 * 60 * 1000;
var is_timeout  = false;

setTimeout(function () {
    is_timeout = true;
}, timeout);
