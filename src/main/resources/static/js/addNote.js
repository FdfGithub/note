$(function () {
    var $noteName = $("input[name='noteName']");
    var $startTime = $("input[name='startTime']");
    var $noteDesc = $("textarea[name='noteDesc']");

    $noteName.blur(function () {
        var noteName = $noteName.val();
        var $span = $("#noteName_warning");
        if (!noteName) {
            $span.text("笔记名称信息未填写");
        } else if (noteName.length > 16) {
            $span.text("长度不大于16个字");
        } else {
            $span.text("");
        }
    });

    $startTime.blur(function () {
        var startTime = $startTime.val();
        var $span = $("#startTime_warning");
        /*        alert(startTime);//2020-04-05T12:03*/
        if (!startTime) {
            $span.text("开始时间信息未填写");
        } else if (new Date().getTime() > strToDate(startTime).getTime()) {
            $span.text("该时间已经过了，请重新设置");
        } else {
            $span.text("");
        }
    });


    $("#addNote").click(function () {
        $noteName.trigger('blur');
        $startTime.trigger('blur');
        var span = $(".content>span").text();
        if (span) {
            return;
        }
        var noteName = $noteName.val();
        var noteDesc = $noteDesc.val();
        // var a =  new Date().toJSON();
        // var startTime = a.substring(0,a.lastIndexOf(":"));
        $.ajax({
            type: "post",
            url: "notes/addNoteByOrdinaryTemplate",
            data: {
                "noteName": noteName,
                "describe": noteDesc,
                "startTime": null
            },
            dataType: "json",
            success: function (data) {
                var status = data["status"];
                if (status == 10) {
                    location.href = "login.html";
                } else if (status == 0) {
                    alert("添加成功");
                    $noteName.val("");
                    $noteDesc.val("");
                    $startTime.val("");
                }
            }
        });
    });
});