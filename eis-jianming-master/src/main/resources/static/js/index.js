function deletestore() {
    if ($("#deletestore").val() == "") {
        jAlert("托盘号必须填写！", "警告");
        return;
    }
    jConfirm("删除库存危险！请确认？", "确认窗口", function (result) {
        if (result) {
            $.post("api/v1/master/view/deletestore", {containerNo: $("#deletestore").val()}, function (result) {
                    $("#inputResult").val(JSON.stringify(result));
                }
            );
        }
        ;
    })
};
function queryPickStation(){
    $.post("api/v1/master/view/queryPickStation", {}, function (result) {
        $("#inputResult").val(JSON.stringify(result));

    });
}
function inPut3() {
    if ($("#inPut3inPut4Task").val() == "") {
        jAlert("托盘号必须填写！", "警告");
        return;
    }
    $.post("api/v1/master/view/inPut3", {containerCode: $("#inPut3inPut4Task").val()}, function (result) {
        $("#inputResult").val(JSON.stringify(result));

    });
}

function inPut4Task() {
    if ($("#inPut3inPut4Task").val() == "") {
        jAlert("托盘号必须填写！", "警告");
        return;
    }
    $.post("api/v1/master/view/inPut4Task", {containerCode: $("#inPut3inPut4Task").val()}, function (result) {
            $("#inputResult").val(JSON.stringify(result));

        }
    );
}

function inPut4Empty() {
    if ($("#inputResult4").val() == "") {
        jAlert("托盘号必须填写！", "警告");
        return;
    }
    $.post("api/v1/master/view/inPut4Empty", {containerCode: $("#inputResult4").val()}, function (result) {
        $("#inputResult").val(JSON.stringify(result));
    });

};

function btnClick(layer, isLock) {
    jConfirm("危险系数高，确定操作吗？", "确认窗口", function (result) {
        if (result) {
            $.post("api/v1/master/view/sxStoreLock", {
                layer: layer,
                isLock: isLock
            }, function (result) {
                $("#inputResult").val(JSON.stringify(result));

            });
        }
    });
};

function updateInBound() {
    if ($("#deletestore").val() == "") {
        jAlert("托盘号必须填写！", "警告");
        return;
    }
    jConfirm("更新库存危险！请确认？", "确认窗口", function (result) {
        if (result) {
            $.post("api/v1/master/view/update", {id: $("#deletestore").val()}, function (result) {
                    $("#inputResult").val(JSON.stringify(result));
                }
            );


        }
    });
};

function agvMove() {
    if ($("#startP").val() == "") {
        jAlert("起点必须填写！", "警告");
        return;
    }
    if ($("#endP").val() == "") {
        jAlert("终点必须填写！", "警告");
        return;
    }

    if (($("#startP").val().indexOf("AB") > 0 && $("#endP").val().indexOf("AB") > 0) ||
        ($("#startP").val().indexOf("XY") > 0 && $("#endP").val().indexOf("XY") > 0)) {
        jConfirm("AGV调用危险！请确认？", "确认窗口", function (result) {
            if (result) {
                $.post("api/v1/master/view/agvMove", {
                        startP: $("#startP").val(),
                        endP: $("#endP").val()
                    }, function (result) {
                        $("#inputResult").val(JSON.stringify(result));
                    }
                );


            }
        });
    } else {
        jAlert("不能跨楼层或者坐标有误！", "警告");
        return;
    }

}
    function foldInBoundMove() {
        jConfirm("4楼碟盘机入库失败才能使用！请确认？", "确认窗口", function (result) {
            if (result) {
                $.post("api/v1/master/view/foldInBoundMove", {containerNo: "999999", deviceNo: "D01"}, function (result) {
                        $("#inputResult").val(JSON.stringify(result));
                    }
                );


            }
        });
    };

