/**
 * Created by Administrator on 2017/11/17 0017.
 */
(function($) {
    $.fn.dragDiv = function(divWrap) {

        return this.each(function() {
            var $divMove = $(this);//鼠标可拖拽区域
            var cssBorder = $divMove.css("border");

            var paddingLeft = parseInt($divMove.css("padding-left").split("px")[0]);
            var paddingRight = parseInt($divMove.css("padding-right").split("px")[0]);
            var paddingTop = parseInt($divMove.css("padding-top").split("px")[0]);
            var paddingBottom = parseInt($divMove.css("padding-bottom").split("px")[0]);
            var marginLeft = parseInt($divMove.css("margin-left").split("px")[0]);
            var marginRight = parseInt($divMove.css("margin-right").split("px")[0]);
            var marginTop = parseInt($divMove.css("margin-top").split("px")[0]);
            var marginBottom = parseInt($divMove.css("margin-bottom").split("px")[0]);
            var borderLeft = parseInt($divMove.css("border-left-width").split("px")[0]);
            var borderRight = parseInt($divMove.css("border-right-width").split("px")[0]);
            var borderTop = parseInt($divMove.css("border-top-width").split("px")[0]);
            var borderBottom = parseInt($divMove.css("border-bottom-width").split("px")[0]);
            var moveTop = parseInt($divMove.css("top").split("px")[0]);
            var moveLeft = parseInt($divMove.css("left").split("px")[0]);
            var height = parseInt($divMove.css("height").split("px")[0])+paddingTop+paddingBottom+marginTop+marginBottom+borderTop+borderBottom;
            var width  = parseInt($divMove.css("width").split("px")[0]) +paddingLeft+paddingRight+marginLeft+marginRight+borderLeft+borderRight;

            if(!$divMove.hasClass("move-wrap")){
                $divMove.css({"border":"4px dashed #639BF6"});
            }

            var $divWrap = divWrap ? $divMove.parents(divWrap) : $divMove;//整个移动区域
            var mX = 0, mY = 0;//定义鼠标X轴Y轴
            var dX = 0, dY = 0;//定义div左、上位置
            var isDown = false;//mousedown标记
            if(document.attachEvent) {//ie的事件监听，拖拽div时禁止选中内容，firefox与chrome已在css中设置过-moz-user-select: none; -webkit-user-select: none;
                $divMove[0].attachEvent('onselectstart', function() {
                    return false;
                });
            }
            $divMove.mousedown(function(event) {
                var event = event || window.event;
                mX = event.clientX;
                mY = event.clientY;
                dX = $divWrap.offset().left;
                dY = $divWrap.offset().top;
                isDown = true;//鼠标拖拽启动
            });
            $(document).mousemove(function(event) {
                var event = event || window.event;
                var x = event.clientX;//鼠标滑动时的X轴
                var y = event.clientY;//鼠标滑动时的Y轴
                if(isDown) {
                    //$divWrap.css({"left": x - mX + dX-moveLeft, "top": y - mY + dY-moveTop});//div动态位置赋值
                    var moveX = 0;
                    var moveY = 0;
                    if(dX==moveLeft){moveX=dX;}else{moveX=moveLeft}
                    if(dY==moveTop){moveY=dY;}else{moveY=moveTop}
                    $divWrap.css({"left": x - mX + moveX, "top": y - mY + moveY});//div动态位置赋值
                    //console.log("fleft:"+ (dX +"-"+moveLeft)+" |  "+"ftop:" + (dY +"-"+moveTop))
                }
            });
            /*  $(document).mouseover(function(event) {
             var event = event || window.event;
             var x = event.clientX;//鼠标滑动时的X轴
             var y = event.clientY;//鼠标滑动时的Y轴
             dX = $divWrap.offset().left;
             dY = $divWrap.offset().top;
             if(dX-x>=$divMove.css("width").split("px")[0]){
             $("body").css("cursor","e-resize")
             }
             });*/
            $divMove.mouseup(function() {
                isDown = false;//鼠标拖拽结束
            });


            $divMove.mouseleave(function(){
                $("body").mousedown(function(event) {
                    //event.which=0 没按
                    if(event.which==1){//鼠标左键
                        $divMove.css("border",cssBorder);
                        $divMove.unbind("mousedown");
                        $("body").unbind("mousedown");
                    }else if(event.which==2){//鼠标中键

                    }else if(event.which==3){//鼠标右键

                    }
                })

                /*$divMove.closest('body').click(function(){
                 $divMove.css("border",cssBorder);
                 $divMove.unbind("mousedown");
                 })*/
            })



        });
    };
})(jQuery);
//
$(document).ready(function() {

    /*
     $(".oper-div").dblclick(function(){
     if($(this).css('border')!='4px dashed rgb(99, 155, 246)'){
     $(this).dragDiv();//拖拽整个div

     $(this).children().each(function(index,element){
     $(element).addClass("div-wrap");
     $(element).dragDiv(".div-wrap");//拖拽div头部
     });
     }
     })
     $(".div-wrap").click(function(){
     $(".oper-div").removeClass("div-wrap");
     });
     */

    $(".oper-move").dblclick(function(){
        if($(this).css('border')!='4px dashed rgb(99, 155, 246)'){
            $(this).dragDiv();//拖拽整个div
        }
    })
    $(".move-wrap").dblclick(function(){
        if($(this).css('border')!='4px dashed rgb(99, 155, 246)'){
            $(this).dragDiv(".move-wrap");//拖拽div头部
        }
    })


});
