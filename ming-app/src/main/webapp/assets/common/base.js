/**
 * Created by Administrator on 2017/11/17 0017.
 */



$(document).ready(function(){
    var $div = $(".oper-move");
    var state = 0;

    //移除事件
    $div.click(function(){
        var index0 = $(this).index();
        $("#divMove").remove();
        state=0;
        $div.each(function(index,element){
            if(index==index0){
                $(element).unbind("mousedown");
            }
        });
    });

    $div.dblclick(function(){
        var index0 = $(this).index()
        $div.each(function(index,element){
            if(index==index0){
                if(state==0){
                    $(element).before('<div id="topDiv"></div>');
                    var offset_x = $(element)[0].offsetLeft;//x坐标
                    var offset_y = $(element)[0].offsetTop-1;//y坐标

                    var paddingLeft = parseInt($(element).css("padding-left").split("px")[0]);
                    var paddingRight = parseInt($(element).css("padding-right").split("px")[0]);
                    var paddingTop = parseInt($(element).css("padding-top").split("px")[0]);
                    var paddingBottom = parseInt($(element).css("padding-bottom").split("px")[0]);
                    var marginLeft = parseInt($(element).css("margin-left").split("px")[0]);
                    var marginRight = parseInt($(element).css("margin-right").split("px")[0]);
                    var marginTop = parseInt($(element).css("margin-top").split("px")[0]);
                    var marginBottom = parseInt($(element).css("margin-bottom").split("px")[0]);
                    var borderLeft = parseInt($(element).css("border-left-width").split("px")[0]);
                    var borderRight = parseInt($(element).css("border-right-width").split("px")[0]);
                    var borderTop = parseInt($(element).css("border-top-width").split("px")[0]);
                    var borderBottom = parseInt($(element).css("border-bottom-width").split("px")[0]);
                    var height = parseInt($(element).css("height").split("px")[0])+paddingTop+paddingBottom+marginTop+marginBottom+borderTop+borderBottom;
                    var width  = parseInt($(element).css("width").split("px")[0]) +paddingLeft+paddingRight+marginLeft+marginRight+borderLeft+borderRight;
                    var warpStr = '<div id="divMove" style="cursor:move;height:'+height+'px;width:'+width+'px;border:1px solid #000000;position:absolute;left:'+offset_x+'px;top:'+offset_y+'px;" ></div>';

                    $("#topDiv").wrap(warpStr);
                    $("#topDiv").before('<div id="rRightDown"> </div>');
                    $("#topDiv").before('<div id="rLeftDown"> </div>');
                    $("#topDiv").before('<div id="rRightUp"> </div>');
                    $("#topDiv").before('<div id="rLeftUp"> </div>');
                    $("#topDiv").before('<div id="rRight"> </div>');
                    $("#topDiv").before('<div id="rLeft"> </div>');
                    $("#topDiv").before('<div id="rUp"> </div>');
                    $("#topDiv").before('<div id="rDown"> </div>');
                    state=1;
                    //$(this).css("border","3px dashed #666");
                    $(element).css("z-index",999);
                }


                /* 绑定鼠标左键按住事件 */
                var $box = $(element).bind("mousedown",function(event){
                    var $p = $(this).parent();
                    var $pp = $p[0];
                    var offset = $p.offset();
                    $pp.posix = {'x': event.pageX - offset.left, 'y': event.pageY - offset.top};
                    $.extend(document, {'move': true, 'move_target':$pp });

                    /* 获取需要拖动节点的坐标 */
                    var offset_x = $(element)[0].offsetLeft;//x坐标
                    var offset_y = $(element)[0].offsetTop;//y坐标
                    //offset_x=$("#ss").css("left").split("px")[0];
                    //offset_y=$("#ss").css("top").split("px")[0];
                    /* 获取当前鼠标的坐标 */
                    var mouse_x = event.pageX;
                    var mouse_y = event.pageY;
                    /* 绑定拖动事件 */
                    /* 由于拖动时，可能鼠标会移出元素，所以应该使用全局（document）元素 */
                    $(document).bind("mousemove",function(ev){
                        /* 计算鼠标移动了的位置 */
                        var _x = ev.pageX - mouse_x;
                        var _y = ev.pageY - mouse_y;
                        /* 设置移动后的元素坐标 */
                        var now_x = (offset_x + _x ) + "px";
                        var now_y = (offset_y + _y ) + "px";
                        var now_y1 = (offset_y + _y -1 ) + "px";
                        /* 改变目标元素的位置 */

                        $(element).css({
                            top:(offset_y + _y )-(parseInt($(element).css("margin-top").split("px")[0]))+"px",
                            left:(offset_x + _x)-(parseInt($(element).css("margin-left").split("px")[0]))+"px"
                        });
                        $("#divMove").css({
                            top:now_y1,
                            left:now_x
                        });



                        $(element).css({cursor: "default"});
                        var offset = $(element).offset(), resize=true;
                        var x = event.clientX, y = event.clientY, t = offset.top, l = offset.left, w = $(this).width(), h = $(this).height(), ww = $('.main_tabletop').height(), b = 10;
                        if(x<(l+b) && y > (t+ww) && y < (t+h-b)){
                            $(this).css({cursor: "w-resize"});
                            $(this).unbind("mousedown").bind({"mousedown":function(e){
                                var $p = $(this);
                                var posix = {
                                    'w': $p.width(),
                                    'h': $p.height(),
                                    'x': e.pageX,
                                    'y': e.pageY
                                };

                                $.extend(document, {'move': true, 'call_down': function(e) {
                                    $p.css({
                                        'width': Math.max(30,  posix.x-e.pageX + posix.w),
                                        'left': Math.max(30,  e.pageX)
                                    });
                                }});
                            }});
                        }else if(x<(l+w) && x>(l+w-b) &&  y > (t+ww) && y < (t+h-b)){
                            $(this).css({cursor: "e-resize"});
                            $(this).unbind("mousedown").bind({"mousedown":function(e){
                                var $p = $(this);
                                var posix = {
                                    'w': $p.width(),
                                    'x': e.pageX
                                };
                                resizeBox($p, posix, e);
                            }});
                        }else if(y<(t+h) && y>(t+h-b) && x>(l+b) && x<(l+w-b)){
                            $(this).css({cursor: "s-resize"});
                            $(this).unbind("mousedown").bind({"mousedown":function(e){
                                var $p = $(this);
                                var posix = {
                                    'h': $p.height(),
                                    'y': e.pageY
                                };
                                resizeBox($p, posix, e);
                            }
                            });
                        }else if(x<(l+b) && y>(t+h-b) && y<(t+h)){
                            $(this).css({cursor: "sw-resize"});
                            $(this).unbind("mousedown").bind({"mousedown":function(e){
                                var $p = $(this);
                                var posix = {
                                    'w': $p.width(),
                                    'h': $p.height(),
                                    'x': e.pageX,
                                    'y': e.pageY
                                };

                                $.extend(document, {'move': true, 'call_down': function(e) {
                                    $p.css({
                                        'width': Math.max(30,  posix.x-e.pageX + posix.w),
                                        'height': Math.max(30, e.pageY - posix.y + posix.h),
                                        'left': Math.max(30,  e.pageX)
                                    });
                                }});
                            }});
                        }else if(y<(t+h) && y>(t+h-b) && x<(l+w) && x>(l+w-b)){
                            $(this).css({cursor: "se-resize"});
                            $(this).unbind("mousedown").bind({"mousedown":function(e){
                                var $p = $(this);
                                var posix = {
                                    'w': $p.width(),
                                    'h': $p.height(),
                                    'x': e.pageX,
                                    'y': e.pageY
                                };
                                $.extend(document, {'move': true, 'call_down': function(e) {
                                    $p.css({
                                        'width': Math.max(30, e.pageX - posix.x + posix.w),
                                        'height': Math.max(30, e.pageY - posix.y + posix.h)
                                    });
                                }});
                            }
                            });
                        }else if(y<(t+ww) && x>l && x<(l+w)){
                            $(this).css({cursor: "move"});
                            $(this).unbind("mousedown");
                        }
                    });


                });

                function resizeBox($p,posix,e){
                    $.extend(document, {'move': true, 'call_down': function(e) {
                        $p.css({
                            'width': Math.max(30, e.pageX - posix.x + posix.w),
                            'height': Math.max(30, e.pageY - posix.y + posix.h)
                        });
                    }});
                }

                /* 当鼠标左键松开，解除事件绑定 */
                $(document).bind("mouseup",function(){
                    $(this).unbind("mousemove");
                });

            }
        });

    });








});