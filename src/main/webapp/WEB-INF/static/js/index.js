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

// 分页
var page_ul = new Vue({
    el: "#page-ul",
    data: {
        page_list: null,
        current_page: null,
        page_count: null,
    },
    methods: {
        re_build: function () {
            let list_size;
            let is_odd = PAGE_LIST_SIZE % 2 === 0;
            let previous_limit = is_odd ? PAGE_LIST_SIZE / 2 + 1 : parseInt(PAGE_LIST_SIZE / 2) + 2;
            let next_limit = is_odd ? this.page_count - PAGE_LIST_SIZE / 2 : this.page_count - parseInt(PAGE_LIST_SIZE / 2);
            let pad_previous = is_odd ? PAGE_LIST_SIZE / 2 - 1 : parseInt(PAGE_LIST_SIZE / 2);
            let start_page = (list_size = this.page_count > PAGE_LIST_SIZE ? PAGE_LIST_SIZE : this.page_count) < PAGE_LIST_SIZE ? 1
                : this.current_page < previous_limit ? 1
                    : this.current_page > next_limit ? this.page_count - PAGE_LIST_SIZE + 1
                        : this.current_page - pad_previous;
            let page_list = new Array();
            for (var i = 0; i < list_size; ++i) {
                page_list.push(start_page++);
            }
            this.page_list = page_list;
        },
        on_click: function (page) {
            get_page_info(page, []);
        },
        on_click_next: function () {
            let next = this.current_page + 1;
            if (next <= this.page_count)
                get_page_info(next, []);
        },
        on_click_previous: function () {
            let previous = this.current_page - 1;
            if (previous >= 1)
                get_page_info(previous, []);
        },
        scroll_to_bottom: function () {
            window.scrollTo(0, document.body.scrollHeight);
        }
    }
});

// navbar catatory render
var navbar_ul = new Vue({
    el: "#navbar-ul",
    data: {
        catatory_list : null,
    },
    methods: {
        on_click: function (catatory) {
            current_catatory = catatory;
            get_page_info(1, []);
        }
    }
});

// sidebar catatory and count render
var sidebar_ul= new Vue({
    el: "#sidebar-ul",
    data: {
        catatory_list : null,
    },
    methods: {
        on_click: function (catatory) {
            navbar_ul.on_click(catatory);
        }
    }
});

// main article intrduction render
var main_container= new Vue({
    el: "#main-container",
    data: {
        intro_list: null,
    },
    methods: {
        format_timeStamp: function (timeStamp) {
            return new Date(timeStamp).Format("yyyy-MM-dd hh:mm");
        }
    }
});

var cache_put = function (page_info) {
    let current_page = page_info.currentPage;
    PAGE_INFO_CACHE[current_page % PAGE_INFO_CACHE.length] = page_info;
};

var cache_get = function (page) {
    let page_info = PAGE_INFO_CACHE[page % PAGE_INFO_CACHE.length];
    if (page_info !== undefined && page_info.currentPage === page)
        return page_info;
    return null;
};

// callback for getPageInfo, render page
var re_render = function (response) {
    message_data = response.data;
    if (!message_data.ok) {
        console.log(message_data.errorCode);
        return -1;
    }
    cache_put(message_data);
    console.log(message_data);

    main_container.intro_list = message_data.introductions;
    navbar_ul.catatory_list = message_data.catatorys;
    sidebar_ul.catatory_list = message_data.catatorys;
    current_catatory = message_data.currentCatatory;

    page_ul.page_count = message_data.pageCount;
    page_ul.current_page = message_data.currentPage;
    page_ul.re_build();
};

var generate_current_base_url = function () {
    return BASE_URL + current_catatory + "/";
};

// index page data request
var get_page_info = function (page, callback_after_render) {
    function callback (response) {
        re_render(response);
        Vue.nextTick(function () {
            callback_after_render instanceof Array ? callback_after_render.forEach(func => func())
                : callback_after_render();
        });
    }
    let page_info;
    if (cached && (page_info = cache_get(page)) != null && page_info.currentCatatory === current_catatory) {
        callback({ data : page_info});
        console.log("message_data in cache.");
    } else {
        axios
            .get(generate_current_base_url() + page)
            .then(callback)
            .catch(error => console.log("raise unknow exception " + error));
        console.log("message_data form ajax.");
    }
    console.log(PAGE_INFO_CACHE);
};

const BASE_URL = "/blog/article/";
const PAGE_LIST_SIZE = 5;
const PAGE_INFO_CACHE = new Array(10);
var cached = true;
var current_catatory = "all";
var message_data = null;

// get page data
get_page_info( "1", []);
