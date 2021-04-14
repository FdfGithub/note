function updateHeadSize(url){
    var img = new Image();
    img.src = url;
    img.onload = function(){
        var $head = $("#yourHead");
        if (img.width>img.height&&img.width>200){
            $head.attr({height:"100%"});
        }else if (img.width<img.height&&img.height>200){
            $head.attr({width:"100%"});
        }else {
            $head.attr({width:"100%"});
            $head.attr({height:"100%"});
        }
    };
}



$(function () {
    $.ajax({
        url:"userInfo/selectUserInfo",
        dataType:"json",
        type:"get",
        success:function (data) {
            var status = data["status"];
            if (status==10){
                window.parent.location.href = "login.html";
            }else if (status==0){
                var url = data["data"]["headUrl"];
                var $head = $("#yourHead");
                updateHeadSize(url);
                $head.attr({src:url});
            }
        }
    });

    $("#uploadHead").change(function () {
        var formData = new FormData();
        formData.append('upload', document.getElementById('uploadHead').files[0]);
        $.ajax({
            url:"userInfo/changeHead/",
            type:"post",
            data: formData,
            dataType:"json",
            contentType: false,
            processData: false,
            success: function(data) {
                var status = data["status"];
                if (status==10){
                    location.href = "login.html";
                }else if (status==0){
                    var url = data["data"];
                    var $head = $("#yourHead");
                    updateHeadSize(url);
                    $head.attr({src:url});
                }
            }
        });
    });


    $("#changeHead").click(function () {
        $("#uploadHead").trigger('click');
    });


    $("#logout").click(function () {
        $.ajax({
            url: "user/logout/",
            type: "get",
            success:function () {
                location.href = "login.html";
            }
        })
    });


    $("#userInfo").click(function () {
        $(".content").html("<iframe src=\"userInfo.html\" ></iframe>");
    });

    $("#history").click(function () {
        location.href = "history.html";
    });

    $("#recycle").click(function () {
        alert("recycle");
        location.href = "test.html";
    });
});