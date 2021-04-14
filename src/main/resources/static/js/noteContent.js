var date;


$(function () {
    $.ajax({
        url: "notes/getNotesByNoteId/",
        data: {"noteId": getUrl()["noteId"]},
        dataType: "json",
        type: "post",
        success: function (data) {
            var status = data["status"];
            var values = data["data"];
            if (status == 10) {
                location.href = "login.html";
            } else if (status == 0) {
                date = values["noteStartDate"];
                var $noteName = $("#noteName");
                $noteName.val(values["noteName"]);
                $noteName.data("id", values["noteId"]);
                $("#status").val(values["status"]);
                $("#startTime").text(values["noteStartDate"] + " " + values["noteStartTime"]);
                $("#startTime+input[name='startTime']").addClass("no_see");
                $("#createTime").val(values["noteCreateTime"]);
                $("#updateTime").val(values["noteUpdateTime"]);
                $("#noteDesc").val(values["describe"]);
            }
        }
    });

/*    $("#return").click(function () {
        var time = date.split("-");
        var keyword = getKeyword();
        location.href = "index.html?year=" + time[0] + "&month=" + time[1] + "&day=" + time[2] + "&keyword=" + keyword;
    });*/

    $("#updateStartTime").click(function () {
        $("#startTime").empty();
        $("#startTime+input[name='startTime']").removeClass("no_see");
        $("#saveUpdateStartTime").data("isUpdate","true");
        return false;
    });

    $("#updateNoteDesc").click(function () {
        var $textarea = $("#noteDesc");
        $textarea.val("");
        $textarea.removeAttr("readonly");
        $("#saveUpdateNoteDesc").data("isUpdate","true");
        return false;
    });

    $("#updateNoteName").click(function () {
        var $noteName = $("#noteName");
        $noteName.val("");
        $noteName.removeAttr("readonly");
        $("#saveUpdateNoteName").data("isUpdate","true");
        return false;
    });


    $("#saveUpdateNoteDesc").click(function () {
        if ($(this).data("isUpdate")!=="true"){
            return;
        }
        var $noteDesc = $("#noteDesc");
        var noteDesc = $noteDesc.val();
        var $noteName = $("#noteName");
        var noteName = $noteName.val();
        var noteId = $noteName.data("id");
        $.ajax({
            url: "notes/updateNoteContent/",
            data: {
                "noteId": noteId,
                "describe": noteDesc,
                "noteName": noteName
            },
            dataType: "json",
            type: "post",
            success: function (data) {
                if (data["status"]==0){
                    alert("修改成功");
                }
            }
        });
    });


    $("#saveUpdateNoteName").click(function () {
        if ($(this).data("isUpdate")!=="true"){
            return;
        }
        var $noteName = $("#noteName");
        var noteName = $noteName.val();
        var noteId = $noteName.data("id");
        if (!noteName){
            return;
        }else if (noteName.length>16){
            alert("长度不大于16个字");
            return;
        }
        $.ajax({
            url: "notes/updateNoteContent/",
            data: {
                "noteId": noteId,
                "noteName": noteName
            },
            dataType: "json",
            type: "post",
            success: function (data) {
                if (data["status"]==0){
                    alert("修改成功");
                }
            }
        });
    });

    $("#saveUpdateStartTime").click(function () {
        if ($(this).data("isUpdate")!=="true"){
            return;
        }
        var $startTime = $("input[name='startTime']");
        var startTime = $startTime.val();
        var $noteName = $("#noteName");
        var noteName = $noteName.val();
        var noteId = $noteName.data("id");
        if (!startTime){
            return;
        }else if (new Date().getTime()>strToDate(startTime).getTime()){
            alert("该时间已经过了，请重新设置");
            return;
        }
        $.ajax({
            url: "notes/updateNoteContent/",
            data: {
                "noteId": noteId,
                "startTime": startTime,
                "noteName": noteName
            },
            dataType: "json",
            type: "post",
            success: function (data) {
                if (data["status"]==0){
                    var values = data["data"];
                    alert("修改成功");
                    $("#startTime").text(values["noteStartDate"] + " " + values["noteStartTime"]);
                    $("#startTime+input[name='startTime']").addClass("no_see");
                }
            }
        });
    });
});
