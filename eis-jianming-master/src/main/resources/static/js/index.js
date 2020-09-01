function deletestore() {
    if ($("#deletestore").val() == "") {
        jAlert("托盘号必须填写！","警告");
        return;
    }
    jConfirm("删除库存危险！请确认？","确认窗口",function(result){
        if (result) {
            $.post("api/v1/master/view/deletestore", {containerNo: $("#deletestore").val()}, function (result) {
                    $("#inputResult").val(result);
                }
            );
        };})
};
function inPut3() {
    if ($("#inPut3inPut4Task").val() == "") {
        jAlert("托盘号必须填写！","警告");
        return;
    }
    $.post("api/v1/master/view/inPut3", {containerCode: $("#inPut3inPut4Task").val()}, function (result) {
        if (!result.result) {
            $("#inputResult").val(result);
        }
    });
}
function inPut4Task() {
    if ($("#inPut3inPut4Task").val() == "") {
        jAlert("托盘号必须填写！","警告");
        return;
    }
    $.post("api/v1/master/view/inPut4Task", {containerCode: $("#inPut3inPut4Task").val()}, function (result) {
            if (!result.result) {
                $("#inputResult").val(result);
            }
        }
    );
}
function inPut4Empty() {
    if($("#inputResult4").val()==""){
        jAlert("托盘号必须填写！","警告");
        return;
    }
    $.post("api/v1/master/view/inPut4Empty",{containerCode:$("#inputResult4").val()},function(result){
        if(!result.result){$("#inputResult").val(result);}
    });

};
function btnClick(layer,isLock) {
    jConfirm("危险系数高，确定操作吗？","确认窗口",function(result){
        if (result) {
            $.post("api/v1/master/view/sxStoreLock", {
                layer:layer,
                isLock:isLock
            }, function (result) {
                if (!result.result) {
                    $("#inputResult").val(result);
                }
            });
        }});
};
function updateInBound() {
    if ($("#deletestore").val() == "") {
        jAlert("托盘号必须填写！","警告");
        return;
    }
    jConfirm("更新库存危险！请确认？","确认窗口",function(result){
        if (result) {
            $.post("api/v1/master/view/update", {id: $("#deletestore").val()}, function (result) {
                    $("#inputResult").val(result);
                }
            );


        }});
};