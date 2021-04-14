var searchDate = new Date();
var selectOperate;
var keyword;

function loadHistory(operate){
    keyword = $("#search").val();
    $.ajax({
        url: "history/selectHistory/",
        dataType: "json",
        data: {
            "searchDate": searchDate,
            "keyword": keyword,
            "operate": operate
        },
        type: "post",
        success: function (data) {
            var status = data["status"];
            if (status == 10) {
                window.parent.location.href = "login.html";
            } else if (status == 0) {
                var $data = data["data"];
                for (var i=0;i<$data.length;i++) {
                    if (i===$data.length-1){ /*更新已经查询的日期*/
                        var date = $data[i]["date"].split("-");
                        searchDate.setFullYear(date[0]);
                        searchDate.setMonth(date[1]-1);
                        searchDate.setDate(date[2]-1);
                    }
                    addHistory($data[i]);
                }

                $(".end-class-right").click(function () {
                    var id = $(this).find("a").attr("class");
                    window.parent.location.href = "noteContent.html?noteId="+id;
                    return false;
                });
            }
        }
    });


}




/*添加历史记录*/
function addHistory(data) {
    var date = data["date"];
    var map = data["map"];
    var histories = " <div class=\"date-class\">\n" +
        "            <a href=\"#\">" + date + "</a>\n" +
        "</div>";
    for (var key in map) {
        var noteName = map[key].split("_")[1];
        var str = "";
        if (noteName.length>11){
            for (var i = 0;i<10;i++){
                str += noteName[i];
            }
            str += "...";
        }else {
            str = noteName;
        }

        var id = map[key].split("_")[0];
        histories += "    <div class=\"split-class\"></div>\n" +
            "    <div class=\"end-class\">\n" +
            "        <div class=\"end-class-left\">\n" +
            "            <a href=\"#\">" + key + "</a>\n" +
            "        </div>\n" +
            "        <div class=\"end-class-right\">\n" +
            "            <a class="+id+" href=\"#\">"+str+"</a>\n" +
            "        </div>\n" +
            "    </div>";
    }
    $(histories).appendTo("#historyContent");
}

$(function () {
    $(document).scroll(function () {
        var documentHeight = $(document).height();//1188
        var windowHeight = $(window).height();//706
        var scrollTop = $(document).scrollTop();//482
        var sh = windowHeight + scrollTop;
        if (windowHeight + scrollTop == documentHeight) {
            loadHistory(selectOperate);
        }
    });

    $("#search")[0].oninput = function(){
        $("#all").trigger('click');
    };


    $("#all").click(function () {
        $("#historyContent").empty();
        searchDate = new Date();
        selectOperate = -1;
        var $btn = $(".button_nav button");
        $btn.css("background", "#fff");
        $(this).css("background", "darkgray");
        loadHistory(-1);
    });

    $("#all").trigger('click');

    $("#createHistory").click(function () {
        $("#historyContent").empty();
        searchDate = new Date();
        selectOperate = 1;
        var $btn = $(".button_nav button");
        $btn.css("background", "#fff");
        $(this).css("background", "darkgray");
        loadHistory(1);
    });

    $("#updateHistory").click(function () {
        $("#historyContent").empty();
        searchDate = new Date();
        selectOperate = 2;
        var $btn = $(".button_nav button");
        $btn.css("background", "#fff");
        $(this).css("background", "darkgray");
        loadHistory(2);
    });

    $("#deleteHistory").click(function () {
        $("#historyContent").empty();
        searchDate = new Date();
        selectOperate = 3;
        var $btn = $(".button_nav button");
        $btn.css("background", "#fff");
        $(this).css("background", "darkgray");
        loadHistory(3);
    });


});