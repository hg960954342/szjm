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

    $.extend($.eis,{
        getPointValue:function(point,json){
            for(var i in json){
                if(point==i){
                    return json[i];
                }
            }
            return point;

        },
      isEmpty:function(obj){
           if((typeof obj=='string')&&obj.constructor==String){
               obj.replace(/(^\s*)|(\s*$)/g, '');
               if (typeof obj === "undefined" || obj == null || obj.trim() == "") {
                   return true;
               } else {
                   return false;
               }
           }
           throw "输入的值不是字符串！";


      }
    })



});