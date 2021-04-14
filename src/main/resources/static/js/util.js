

// 开始震动
function startVibrate(duration) {
    navigator.vibrate(duration);
}

// 停止震动
function stopVibrate() {
    navigator.vibrate(0);
}



var getUrl = function () {
    var str = decodeURI(location.search);
    var obj = {};
    if (str) {
        str = str.replace("?", '');
        var arr = str.split("&");
        arr.forEach(function (item, index) {
            var newArr = item.split("=");
            obj[newArr[0]] = newArr[1];
        })
    }
    return obj;
};
/*获得keyword*/
function getKeyword() {
    var url =  getUrl();
    if (url["keyword"]==="undefined"){
        return undefined;
    }
    return url["keyword"];
}

/*获得需要查询的日期*/
function getSearchDate() {
    var url =  getUrl();
    var year = url["year"];
    var month = url["month"];
    var day = url["day"];
    if (year&&month&&day){
        return new Date(year,month-1,day);
    }
    return new Date();
}

/*带T字符串转日期类型*/
function strToDate(str){
    var times = str.split("T");
    var dates = times[0].split("-");
    var timePoint = times[1].split(":");
    var date = new Date();
    date.setFullYear(parseInt(dates[0]));
    date.setMonth(parseInt(dates[1])-1);
    date.setDate(parseInt(dates[2]));
    date.setHours(parseInt(timePoint[0]));
    date.setMinutes(parseInt(timePoint[1]));
    return date;
}
/*不带T字符串转带T字符串*/
function StrToTStr(str){
    return str.replace("T"," ");
}

/*星期转换*/
function trans(num){
    switch (num) {
        case 0: return "周日";
        case 1: return "周一";
        case 2: return "周二";
        case 3: return "周三";
        case 4: return "周四";
        case 5: return "周五";
        case 6: return "周六";
    }
}



