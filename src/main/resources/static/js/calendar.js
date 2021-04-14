var date = new Date();


/*判断当前月份的第一天是星期几*/
function getWeek(year,month){
    /*
    * 0 代表 星期日
    * 1 代表 星期一
    * 2 代表 星期二
    * 3 代表 星期三
    * 4 代表 星期四
    * 5 代表 星期五
    * 6 代表 星期六
    * */
    var firstDay = new Date(year,month,1);
    return firstDay.getDay();
}

/*获得当前月份的总天数*/
function getDays(year,month){
    /*
    * 月份是从0开始计数的，即0代表1月份
    * */
    switch (month) {
        case 0:
        case 2:
        case 4:
        case 6:
        case 7:
        case 9:
        case 11:
            return 31;
        case 3:
        case 5:
        case 8:
        case 10:
            return 30;
        case 1:
            if (year % 400 === 0 || (year % 4 === 0 && year % 100 !== 0)) {
                return 29;
            }
            return 28;
    }
}
/*判断是否是今天*/
function isToday(date){
    var today = new Date();
    if (today.getFullYear()===date.getFullYear()&&today.getMonth()===date.getMonth()&&
        today.getDate()===date.getDate()){
        return true;
    }
    return false;
}



/*加载日历*/
function  loadCalendar(date){
    /*年*/
    var year = date.getFullYear();
    /*月*/
    var month = date.getMonth();
    /*日*/
    var day = date.getDate();

    document.getElementById("date").innerText = year + "年" +
        (month+1) + "月份";



    var lis = "";
    for (var i = 0;i<getWeek(year,month);i++){
        lis += "<li></li>";
    }
    for (var j = 1;j<=getDays(year,month);j++){
        if (isToday(new Date(year,month,j))){
            lis += "<li style='color: red' id="+j+">"+j+"</li>";
        }else {
            lis += "<li id="+j+">"+j+"</li>";
        }
    }
    document.getElementById("date_list").innerHTML = lis;


    $("#date_list>li").click(function () {
    /*    var keyword = $("input[type='search']").val();*/
        $(this).css("background","darkgray");
        location.href = "index.html?year="+year+"&month="+(month+1)+"&day="+$(this).attr("id");
    });


    $.ajax({
        url: "notes/calendarView",
        data: {
            "pointerDate": date
        },
        type: "post",
        dataType: "json",
        success: function (data) {
            var status = data["status"];
            if (status == 10) {
                location.href="login.html";
            } else if (status == 0){
                /*$("#date_list>li>span").remove();*/
                var $info = data["data"];
                for (var key in $info) {
                    $("#"+key+"").append("<span>"+$info[key]+"</span>");
                }
            }
        }
    });
}


window.onload = function () {
    loadCalendar(date);

    document.getElementById("pre").onclick = function () {
        date.setMonth(date.getMonth() - 1);
        loadCalendar(date);
/*        $("#search").trigger('click');*/
        return false;
    };

    document.getElementById("next").onclick = function () {
        date.setMonth(date.getMonth()+1);
        loadCalendar(date);
     /*   $("#search").trigger('click');*/
        return false;
    };


};

