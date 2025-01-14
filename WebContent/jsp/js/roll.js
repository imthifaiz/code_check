/*
旋转�?项类声明
*/
Item=function(UI){

        this.angle=0;
        this.UI=UI;
        this.update();

};
Item.ini={/*类�?��?属性(椭圆轨迹�?�数)*/

        axle_w:280,/* �?�宽 */
        axle_h:55,/* �?�高(视角高) */
        cen_x:380,/* 中心x */
        cen_y:75/* 中心y */

};
Item.prototype.update=function(){/* 按角度刷新UI */

        var J=this.UI.style,C=Item.ini,W=C.axle_w,H=C.axle_h,X=C.cen_x,Y=C.cen_y;
        var angle=this.angle/180*Math.PI; /* 角度转弧度 */
        var left=Math.cos(angle)*W+X;
        var top=Math.sin(angle)*H+Y;
        var A=this.angle>270?this.angle-360:this.angle;
        var size=360-Math.abs(90-A)*1.5;/* 设置大�? */
        this.UI.width=Math.max(size,120);/* 下�? */
        var opacity=Math.max(5,size-220);/* 设置�?明度 */
        J.filter='alpha(opacity='+opacity+')';
        J.opacity=opacity/100;
        J.left=(left-this.UI.offsetWidth/2)+'px';/* 设置�?置 */
        J.top=top+'px';
        J.zIndex=parseInt(size*100);/* 设置Z�? */

};

/* 
�?��?�管�?�器 
*/

Nav_3D={

        items:[],
        dir:1,
        index:0,
        hover:false,

        add:function(item){
                this.items.push(item);
                item.index=this.items.length-1;
                item.UI.onclick=function (){
                        var J=item.angle,M=Nav_3D;
                        if(M.uping)return;
                        if(J==90)return;
                        M.wheel_90(item);
                        M.index=item.index;
                };
                item.UI.onmouseover=function (){
                        if(item.angle==90){
                                Nav_3D.hover=true;
                                clearTimeout(Nav_3D.autoTimer);
                        };
                };
                item.UI.onmouseout=function (){
                        if(item.angle==90){
                                Nav_3D.hover=false;
                                Nav_3D.auto();
                        };
                };
                return this;
        },

        wheel_90:function(hot){/* 把目标旋转到90度 */
                if(this.uping)return;
                this.uping=true;
                var This=this;
                this.timer=setInterval(function (){
                        clearTimeout(This.autoTimer);/* 清除自动 */
                        var A=hot.angle;
                        This.dir=A<270&&A>90?-1:1;/* 确定旋转方�?� */
                        if(A==90){/* 旋转到指定�?置时结�?� */
                                clearInterval(This.timer);
                                This.uping=false;
                                This.onEnd(hot);/* 自定义事件 */
                        }
                        if(A>270)A-=360;
                        var set=Math.ceil(Math.abs((90-A)*0.1));/* 缓冲 */
                        for (var i=0;i<This.items.length;i++ ) {
                                var J=This.items[i];
                                J.angle+= (set*This.dir);/* 角度自增 */
                                J.update();
                                if(J.angle>360)J.angle-=360;
                                if(J.angle<0)J.angle +=360;
                        };
                },15);
        },

        ready:function(){/* 自动设置�?始角度 */
       
                var J=this.items,step=parseInt(360/J.length);
                for (var i=0;i<J.length;i++) {J[i].angle=i*step+90;}
                this.wheel_90(this.items[0]);/* 把第一个项目转到90度 */
                Nav_3D.prevHot=this.items[0].UI;
                Nav_3D.setHot();
        },

        setHot:function(isHot){/* 设置焦点样�? */
                if(!this.prevHot)return;
                with(this.prevHot.style){
                        cursor=isHot!==false?'default':"pointer";
                };
                return this;
        },
        
        auto:function(){/* 自动轮转 */
                this.index--;
                if(this.index<0)this.index=this.items.length-1;
                var J=this.items[this.index];
                this.setHot(false).prevHot=J.UI;
                this.setHot();
                this.wheel_90(J);
        },

        onEnd:function(hot){
                if(this.hover){
                        return setTimeout(function(){Nav_3D.onEnd();},1000);
                }
                this.autoTimer=setTimeout(function(){Nav_3D.auto();},1200);
        },
		
		userClick:function(i){
  
        		this.index = i-1;
                if(this.index<0)this.index=this.items.length-1;
                var J=this.items[this.index];
               
                this.setHot(false).prevHot=J.UI;
                this.setHot();
                this.wheel_90(J);
        }

};

