var archive_container = new Vue({
    el: "#archive-container",
    data: {
        archive_List: null,
    },
    created: function () {
        axios
            .get("/blog/article/archive")
            .then(response => {
                let data = response.data;
                let self = this;
                Vue.nextTick(function () {
                    console.log(data);
                    if (data.ok) {
                        self.archive_List = data.archiveList;
                    }
                })
            })
            .catch(error => console.log("unknow exception " + error));
    },
    methods: {
        getMonth: function (timestamp) {
            return new Date(timestamp).getMonth() + 1;
        }
    }
})