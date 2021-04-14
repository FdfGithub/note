/*更新查询日期*/
function updateDate(date) {
    $("#dateTime").text((date.getMonth() + 1) + "月" + date.getDate() + "日 " + trans(date.getDay()));
    $("iframe").attr({src: "note.html?year=" + date.getFullYear() + "&month=" + (date.getMonth() + 1) + "&day=" + date.getDate()+"&keyword="+getUrl()["keyword"]});
}


$(function () {

    $.ajax({
        url: "user/isLogin/",
        datatype: "json",
        type: "get",
        success: function (data) {
            if (!data) {
                location.href = "login.html";
            }
        }
    });

    var date = getSearchDate();
    updateDate(date);

    $("#prev").click(function () {
        date.setDate(date.getDate() - 1);
        updateDate(date);
        return false;
    });

    $("#next").click(function () {
        date.setDate(date.getDate() + 1);
        updateDate(date);
        return false;
    });


    $("#search").click(function () {
        location.href = "search.html";
    });


    $("#adBtn").click(function () {
        location.href = "addNote.html";
    });
});