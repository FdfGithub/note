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
                var $data = data["data"];
                $("#name").text($data["name"]);
                $("#gender").text($data["gender"]);
                $("#birth").text($data["birth"]);
                $("#constellation").text($data["constellation"]);
                $("#motto").val($data["motto"]);
            }
        }
    });
});