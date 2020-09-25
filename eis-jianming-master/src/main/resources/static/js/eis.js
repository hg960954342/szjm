$(function () {

$.extend({
    eis:{}
})
    /**
     * 获取配置的Agv特殊点位
     */
    $.extend($.eis,{
        getAgvJSON:function(){
            var agvJSON= $.ajax({url:"agv.json",async:false}).responseText;
            var json=JSON.parse(agvJSON);
            return json;
        },

    });



});