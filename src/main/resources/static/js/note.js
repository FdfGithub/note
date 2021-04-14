var $page_info;
var selectStatus = -1;
var keyword;

/*加载笔记*/
function loadNotes(pageNum, url) {
    if (getKeyword()) {
        keyword = getKeyword();
        $("#search").val(keyword);
    }
    var searchDate = getSearchDate();
    $.ajax({
        url: url,
        type: "post",
        dataType: "json",
        data: {
            "searchDate": searchDate,
            "keyword": keyword ? keyword : null,
            "pageNum": pageNum ? pageNum : 1
        },
        success: function (data) {
            //对page_info数据进行处理
            var $status = data["status"];
            if ($status == 0) {
                $page_info = data["data"];
                var $list = $page_info["list"];
                for (var i = 0; i < $list.length; i++) {
                    addNote(
                        $list[i]["noteId"],
                        $list[i]["noteStartTime"],
                        $list[i]["noteName"],
                        $list[i]["status"]
                    );
                }
                /*加载查看笔记详细内容的点击事件*/
                seeNoteContent();
            }
        }
    });
}

/*添加笔记*/
function addNote(noteId, startTime, noteName, status) {
    $("<div id=" + noteId + " class=\"note-class\">\n" +
        "        <div class=\"note_top\">\n" +
        "            <textarea readonly=\"readonly\">" + noteName + "</textarea>\n" +
        "        </div>\n" +
        "        <div class=\"note_bottom\">\n" +
        "            <div>开始时间：" + startTime + "&nbsp;&nbsp;&nbsp;&nbsp;状态：" + status + "</div>\n" +
        "        </div>\n" +
        "    </div>").appendTo($("#notes"));
}


function seeNoteContent() {
    $(".note_top").click(function () {
        var id = $(this).parent().attr("id");
        window.parent.location.href = "noteContent.html?noteId=" + id + "&keyword=" + keyword;
    });


  /*  $(".complete").click(function () {
        var $note = $(this).parent().parent();
        var noteId = $note.attr("id");
        $.ajax({
            url: "notes/doingOver/",
            dataType: "json",
            data: {
                "noteId": noteId
            },
            type: "post",
            success: function (data) {
                if (data["status"] == 10) {
                    location.href = "login.html";
                } else if (data["status"] == 0) {
                    $note.remove();
                    alert("恭喜你，成功完成了一件事");
                }
            }
        })
    });*/
}

$(function () {

    $("#all").css("background", "darkgray");
    loadNotes(null, "notes/getNotesByOrdinary/");

    $(document).scroll(function () {
        var documentHeight = $(document).height();//1188
        var windowHeight = $(window).height();//706
        var scrollTop = $(document).scrollTop();//482
        var sh = windowHeight + scrollTop;
        if (windowHeight + scrollTop == documentHeight) {
            if (!$page_info["isLastPage"]) {
                if (selectStatus == -1) {
                    loadNotes($page_info["pageNum"] + 1, "notes/getNotesByOrdinary/");
                } else if (selectStatus == 0) {
                    loadNotes($page_info["pageNum"] + 1, "notes/getNotesByStatusIsNoStart/");
                } else if (selectStatus == 1) {
                    loadNotes($page_info["pageNum"] + 1, "notes/getNotesByStatusIsDoing/");
                } else if (selectStatus == 2) {
                    loadNotes($page_info["pageNum"] + 1, "notes/getNotesByStatusIsEnd/");
                }
            }
        }
    });

    $("#all").click(function () {
        $("#notes").empty();
        var $btn = $(".button_nav button");
        $btn.css("background", "#fff");
        $(this).css("background", "darkgray");
        selectStatus = -1;
        loadNotes(null, "notes/getNotesByOrdinary/");
    });

    $("#no_start").click(function () {
        $("#notes").empty();
        var $btn = $(".button_nav button");
        $btn.css("background", "#fff");
        $(this).css("background", "darkgray");
        selectStatus = 0;
        loadNotes(null, "notes/getNotesByStatusIsNoStart/");
    });

    $("#doing").click(function () {
        $("#notes").empty();
        var $btn = $(".button_nav button");
        $btn.css("background", "#fff");
        $(this).css("background", "darkgray");
        selectStatus = 1;
        loadNotes(null, "notes/getNotesByStatusIsDoing/");
    });

    $("#end").click(function () {
        $("#notes").empty();
        var $btn = $(".button_nav button");
        $btn.css("background", "#fff");
        $(this).css("background", "darkgray");
        selectStatus = 2;
        loadNotes(null, "notes/getNotesByStatusIsEnd/");
    });


    $("#search")[0].oninput = function () {
        keyword = $(this).val();
        $("#all").trigger('click');
    }
});

