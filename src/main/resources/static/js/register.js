var patt_email = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
var patt_phone = /^1[3456789]\d{9}$/;
var patt_password = /^[a-zA-Z]\w{5,17}$/;

$(function () {

    var $account = $("input[name='account']");
    var $password = $("input[name='password']");
    var $confirm_pwd = $("input[name='confirm_pwd']");
    var $code = $("input[name='code']");


    $account.blur(function () {
        var account = $account.val();
        var $span = $account.next();
        if (!account) {
            $span.text("账号信息未填写");
        } else if (!(patt_email.test(account) || patt_phone.test(account))) {
            $span.text("账号格式错误");
        } else {
            $.ajax({
                //检测用户是否注册过
                url: "user/isRepeat/",
                type: "get",
                data: {"account": account},
                dataType: "json",
                success: function (data) {
                    if (data) {
                        $span.text("用户已存在");
                    } else {
                        $span.text("");
                    }
                }
            });
        }
    });

    $password.blur(function () {
        var password = $password.val();
        var confirm_pwd = $confirm_pwd.val();
        var $span = $password.next();
        if (!password) {
            $span.text("密码信息未填写");
        } else if (!patt_password.test(password)) {
            $span.text("以字母开头，长度在6~18之间")
        } else if (confirm_pwd) {
            if (password === confirm_pwd) {
                $span.text("");
            } else {
                $span.text("两次密码不一致");
            }
        } else {
            $span.text("");
        }
    });

    $confirm_pwd.blur(function () {
        var password = $password.val();
        var confirm_pwd = $confirm_pwd.val();
        var $span = $confirm_pwd.next();
        if (!password) {
            $span.text("确认密码信息未填写");
        } else if (!patt_password.test(confirm_pwd)) {
            $span.text("以字母开头，长度在6~18之间")
        } else if (password) {
            if (password === confirm_pwd) {
                $span.text("");
            } else {
                $span.text("两次密码不一致");
            }
        } else {
            $span.text("");
        }
    });

    $code.blur(function () {
        var code = $code.val();
        var $span = $code.next();
        if (!code) {
            $span.text("验证码信息未填写");
        } else {
            $span.text("");
        }
    });


    $("#getCode").click(function () {
        $account.trigger('blur');
        var span = $account.next().text();
        if (span){
            return;
        }
        var account = $account.val();
        var type = patt_email.test(account)?"email":"phone";
        $.ajax({
            type: "post",
            url: "user/getCode",
            data: {'account': account, 'type': type},
            dataType: "json",
            success: function (data) {
                if (data["status"]==0){
                    $("#getCode").text("重新发送");
                }
            }
        });

    });

    $("#register").click(function () {
        $account.trigger('blur');
        $password.trigger('blur');
        $confirm_pwd.trigger("blur");
        $code.trigger("blur");


        var span = $(".content span").text();
        if (span){
            return;
        }

        var account = $account.val();
        var password = $password.val();
        var code = $code.val();
        var type = patt_email.test(account)?"email":"phone";
        $.ajax({
            type: "post",
            url: "user/register/",
            data: {
                "account": account,
                "password": password,
                "code": code,
                "type": type
            },
            dataType: "json",
            success: function (data) {
                var status = data["status"];
                if (status==1){
                    alert(data["msg"]);
                } else if (status==100||status==101){
                    $code.next().text(data["msg"]);
                }else {
                    alert("注册成功");
                    location.href = "login.html";
                }
            }
        });
    });
});