const baseUrl = "/blog/article/page/";
const pageListSize = 5;
const pageInfoCache = new Array(10);
var globalData;

function put (pageInfo) {
    var currentPage = pageInfo.currentPage;
    pageInfoCache[currentPage % pageInfoCache.length] = pageInfo;
}

function get(page) {
    var pageInfo = pageInfoCache[page % pageInfoCache.length];
    if (pageInfo != undefined && pageInfo.currentPage == page)
        return pageInfo;
    return null;
}

// index page data request
function getPageInfo (baseUrl, page, callbackInRender, callbackAfterRender) {
    function callback (response) {
        callbackInRender(response);
        Vue.nextTick(function () {
            callbackAfterRender instanceof Array ? callbackAfterRender.forEach(value => value())
                : callbackAfterRender();
        });
    }
    var pageInfo;
    if ((pageInfo = get(page)) != null) {
        callback({ data : pageInfo});
        console.log("cache");
    } else {
        axios
            .get(baseUrl + page)
            .then(callback)
            .catch(error => console.log("Raise unknow exception " + error));
        console.log("ajax");
    }
    console.log(pageInfoCache);
}

// pageList render and display
var pageList = new Vue({
    el: "#pageList",
    data: {
        pageList: null,
        currentPage: null,
        pageCount: null,
    },
    methods: {
        rePosition: function () {
            var plClassList = this.$refs.pl.classList;
            if (plClassList.contains("d-none"))
                plClassList.remove("d-none");
        },
        reBuild: function () {
            var listSize;
            var lowLimit = pageListSize % 2 == 0 ? pageListSize / 2 + 1 : parseInt(pageListSize / 2) + 2;
            var highLimit = pageListSize % 2 == 0 ? this.pageCount - pageListSize / 2 : this.pageCount - parseInt(pageListSize / 2);
            var padPrevious = pageListSize % 2 == 0 ? pageListSize / 2 - 1 : parseInt(pageListSize / 2);
            var startpage = (listSize = this.pageCount > pageListSize ? pageListSize : this.pageCount) < pageListSize ? 1
                : this.currentPage < lowLimit ? 1
                    : this.currentPage > highLimit ? this.pageCount - pageListSize + 1
                        : this.currentPage - padPrevious;
            var array = new Array();
            for (var i = 0; i < listSize; ++i) {
                array.push(startpage++);
            }
            this.pageList = array;
        },
        onClick: function (page) {
            getPageInfo(baseUrl, page, reRender, [pageList.rePosition, foot.rePosition]);
        },
        onClickNext: function () {
            var next = this.currentPage + 1;
            if (next <= this.pageCount)
                getPageInfo(baseUrl, next, reRender, [pageList.rePosition, foot.rePosition]);
        },
        onClickPrevious: function () {
            var previous = this.currentPage - 1;
            function scrollToBottom () {
                window.scrollTo(0, document.body.scrollHeight);
            };
            previous >= 1 ?
                this.currentPage == this.pageCount ?
                    getPageInfo(baseUrl, previous, reRender, [pageList.rePosition, foot.rePosition, scrollToBottom])
                    : getPageInfo( baseUrl, previous, reRender, [pageList.rePosition, foot.rePosition])
                : null;
        }
    }
})

// foot display and postion, backToTop display or not
var foot = new Vue({
    el: "#foot",
    methods: {
        rePosition: function () {
            var innerHeight = window.innerHeight;
            var clientHeight = document.body.clientHeight;
            var ftClassList = this.$refs.ft.classList;
            var btClassList = this.$refs.bt.classList;
            if (innerHeight >= clientHeight) {
                if (!ftClassList.contains("fixed-bottom"))
                    ftClassList.add("fixed-bottom");
                if (!btClassList.contains("d-none"))
                    btClassList.add("d-none");
            } else {
                if (ftClassList.contains("fixed-bottom"))
                    ftClassList.remove("fixed-bottom");
                if (btClassList.contains("d-none"))
                    btClassList.remove("d-none");
            }
            if (ftClassList.contains("d-none"))
                ftClassList.remove("d-none");
        }
    }
})

// navbar catatory render
var navCatalog = new Vue({
    el: "#navCatatory",
    data: {
        catatoryList : null,
    }
});

// sidebar catatory and count render
var sideBarCatalog = new Vue({
    el: "#sideBarCatatory",
    data: {
        catatoryList : null,
    }
})

// main article intrduction render
var articleContainer = new Vue({
    el: "#articleContainer",
    data: {
        introductionList: null,
    }
})

// callback for getPageInfo, render page
function reRender (response) {
    globalData = response.data;
    put(globalData);
    // console.log(globalData);

    articleContainer.introductionList = globalData.introductions;
    navCatalog.catatoryList = globalData.catalogs;
    sideBarCatalog.catatoryList = globalData.catalogs;

    pageList.pageCount = globalData.pageCount;
    pageList.currentPage = globalData.currentPage;
    pageList.reBuild();
}


// get page data
getPageInfo(baseUrl, "1", reRender, [pageList.rePosition, foot.rePosition]);
