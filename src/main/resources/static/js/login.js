var patt_email = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
var patt_phone = /^1[3456789]\d{9}$/;


$(function () {
    var $account = $("input[name='account']");
    var $password = $("input[name='password']");

    $account.blur(function () {
        var account = $account.val();
        var $span = $account.next();
        if (!account){
            $span.text("账号信息未填写");
        }else if (!(patt_email.test(account)||patt_phone.test(account))){
            $span.text("账号格式错误");
        }else {
            $.ajax({
                //检测用户是否注册过
                url: "user/isRepeat/",
                type: "get",
                data: {"account": account},
                dataType: "json",
                success: function (data) {
                    if (!data) {
                        $span.text("用户不存在");
                    }else {
                        $span.text("");
                    }
                }
            });
        }
    });

    $password.blur(function () {
        var password = $password.val();
        var $span = $password.next();
        if (!password){
            $span.text("密码信息未填写");
        }else {
            $span.text("");
        }
    });


    $("#login").click(function () {
        $account.trigger('blur');
        $password.trigger('blur');
        var span = $(".content span").text();
        if (span){
            return;
        }
        var account = $account.val();
        var password = $password.val();
        $.ajax({
            url: "user/login/",
            data: {"account": account, "password": password},
            dataType: "json",
            type: "post",
            success: function (data) {
                var status = data["status"];
                if (status == 1) {
                    alert(data["msg"]);
                }else if (status == 102){
                    $password.next().text(data["msg"]);
                }else{
                    alert("登录成功");
                    location.href="index.html";
                }
            }
        });
    });
});