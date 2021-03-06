<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>数据报表</title>
    <link rel="stylesheet" href="${ctx}/static/data_display/css/echarts.css">
    <link rel="stylesheet" href="${ctx}/static/data_display/css/jquery-ui.css">
</head>
<body>
<div class="selectHeader" id="selectHeader">
    <ul>
        <li>
            <select name="" id="carType">
                <option value="业务类型">业务类型</option>
                <option value="滴滴车分期">滴滴车分期</option>
                <option value="车贷">车贷</option>
            </select>
        </li>
        <li>
            <select name="" id="carCompany">
                <option value="公司名称">公司名称</option>
                <option value="滴滴车分期">滴滴车分期</option>
                <option value="车贷">车贷</option>
            </select>
        </li>
        <li>
            <select name="" id="carTime">
                <option value="时间窗">时间窗</option>
                <option value="周">周</option>
                <option value="年">年</option>
                <option value="日">日</option>
            </select>
        </li>
        <li class="info_header_date">
           <label>选择时间: <input type="text" id="datepicker1" ng-model="date1"><i></i> 至 <input type="text" id="datepicker2" ng-model="date1"><i></i></label>
        </li>
    </ul>
</div>
<div class="container">
    <div id="canvas1" class="canvas"  ></div>
    <div id="canvas2" class="canvas"  ></div>
    <div id="canvas3" class="canvas" ></div>
    <div id="canvas4" class="canvas" ></div>
    <div id="canvas5" class="canvas" ></div>
    <div id="canvas6" class="canvas" ></div>
</div>
<script src="${ctx}/static/data_display/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/static/data_display/js/echarts.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/static/data_display/js/jquery-ui.js"></script>
<script>
	

        $( "#datepicker1").datepicker();
        $( "#datepicker2").datepicker();
        function art(id,bgColor,fontColor,graColorStart,graColorEnd,y1,y2,yName,data,obj) {
            var dom = document.getElementById(id);
            var myChart = echarts.init(dom);
            option = null;
            var num=id.substr(id.length-1,1);
            option = {
                backgroundColor: '#132531',
                title: {
                    text: '数据客户走势',
                    x:'center',
                    y:20,
                    padding:[5,20],
                    backgroundColor: bgColor,
                    textAlign: null,
                    textStyle:{
                        fontSize:14,
                        color:fontColor,
                        fontWeight:400
                    }
                },

                tooltip: {
                    trigger: 'axis',
                    formatter:'<table class="gridTable">'+
                    '<tr>'+
                    '<td>时间点</td>'+
                    '<td>业务类型</td>'+
                    '<td>公司名称</td>'+
                    '<td>放款金额</td>'+
                    '<td>放款总量</td>'+
                    '<td>逾期人数</td>'+
                    '</tr>'+
                    '<tr>'+
                    '<td rowspan="4">'+obj.time+'</td>'+
                    '<td rowspan="2">'+obj.type1+'</td>'+
                    '<td>'+obj.company1_1+'</td>'+
                    '<td>2</td>'+
                    '<td>1</td>'+
                    '<td>3</td>'+

                    '</tr>'+
                    '<tr>'+
                    '<td >'+obj.company1_2+'</td>'+
                    '<td>3</td>'+
                    '<td>2</td>'+
                    '<td>1</td>'+

                    '</tr>'+
                    '<tr>'+
                    '<td rowspan="2">'+obj.type2+'</td>'+
                    '<td>'+obj.company2_1+'</td>'+
                    '<td>2</td>'+
                    '<td>1</td>'+
                    '<td>3</td>'+
                    '</tr>'+
                    '<tr>'+
                    '<td>'+obj.company2_2+'</td>'+
                    '<td>2</td>'+
                    '<td>1</td>'+
                    '<td>3</td>'+
                    '</tr>'+
                    '</table>', //这里是对提示的悬浮框进行自定义
                    borderColor:'#B6D0FC',
                    borderWidth:1
                },
//             legend: {  //多条折线的时候使用
//                 data:['邮件营销','联盟广告','视频广告','直接访问','搜索引擎']
//             },
                grid: { //表格的偏移量
                    left: '5%',
                    right: '4%',
                    bottom: '10%',//距离底部有10%的距离，
                    containLabel: true
                },
                toolbox: {  //右上方的小图标工具
                    feature: {
                        dataView: {readOnly: false},//文本形式
                        magicType: {type: ['line', 'bar']}, //折线柱状图切换
                        restore: {},//刷新
                        saveAsImage: {} //下载
                    },
                    iconStyle:{    //设置icon样式
                        normal:{
                            //color:'white',
                            borderWidth:1,
                            borderColor:'white'

                        },
                        emphasis:{
                            borderWidth:1,
                            borderColor:bgColor
                        }
                    }
                },
                xAxis: {
                    type: 'category',
                    splitLine:{ //显示网格
                        show: true,
                        lineStyle: {
                            color:['#1E3749'],
                            width:1,
                            type:'solid'
                        }
                    },
                    name:'日期/周',   //x轴的名字
                    nameLocation:'middle', //x轴的名字的竖向放置
                    nameTextStyle:{   //x轴的名字的样式属性
                        color: bgColor,
                        fontFamily: 'verdana',
                        fontSize: 14

                    },
                    nameGap:30,
                    boundaryGap: false,
                    data: ['5周','6周','7周','8周','9周','10周','11周','12周'],
                    axisLabel : {   //设置坐标刻度
                        interval: 'auto',
                        textStyle: {
                            color: bgColor,
                            fontFamily: 'verdana',
                            fontSize: 12
                        }
                    }
                },
                yAxis: {
                    type: 'value',
                    splitLine:{ //显示网格
                        show: true,
                        lineStyle: {
                            color:['#1E3749'],
                            width:1,
                            type:'solid'
                        }
                    },
                    show: true,    //显示纵轴false-不显示，true-显示
                    name:yName,   //y轴的名字
                    nameLocation:'middle', //y轴的名字的竖向放置
                    nameTextStyle:{   //y轴的名字的样式属性
                        color: bgColor,
                        fontFamily: 'verdana',
                        fontSize: 14
                    },
                    nameGap:30,   //y轴坐标与标题之间的距离属性
                    boundaryGap: [y1,y2],   //坐标轴的刻度差值
                    position: 'left',
                    axisLabel : {   //设置坐标刻度
                    interval: 'auto',
                        textStyle: {
                            color: bgColor,
                            fontFamily: 'verdana',
                            fontSize: 12
                        }
                    },
                },
                series: [ //这里代表几种数据
                    {
                        name:'客户数据',
                        type:'line',
                        stack: '总量',
                        data:data,
                        symbol:'circle',  //设置折点变成实心点
                        symbolSize: 6,   //设置折点的大小
                        itemStyle : {   //设置线条的颜色
                            normal : {
                                color:bgColor,
                                lineStyle:{
                                    color:bgColor
                                }
                            }
                        },
                        areaStyle: {
                            normal: {
                                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                    offset: 0,
                                    color: graColorStart
                                }, {
                                    offset: 1,
                                    color: graColorEnd
                                }])
                            }
                        }
                    }
                ],
                color:['dark']
            };

            if (option && typeof option === "object") {
                myChart.setOption(option, true);
            }
        }
        var obj={
            time:"2017-05-03",
            type1:"滴滴车",
            type2:"爱享融",
            company1_1:"太原滴滴",
            company1_2:"大连滴滴",
            company2_1:"太原轮动",
            company2_2:"成都轮动"
        }

		
</script>
	<script type="text/javascript">
	setTimeout("artIinit()",200);
	function artIinit(){
        art("canvas1","#AD2FE8","#EAF1FE","rgba(242,66,112,0.12)",'#152533',0,0,"还 款 金 额 （ 万 元 ）",[1, 3,2, 1,3,5,2,1,5,7],obj);
        art("canvas2","#70A3F9","#EAF1FE","rgba(112,163,249,0.12)",'#132632',0,0,"还 款 总 量",[1,4,44,-5,200,100,6,1,5,7,1,5,15,63],obj);
        art("canvas3","#F24270","#EAF1FE","rgba(242,66,112,0.12)",'#192939',0,0,"逾 期 天 数",[5, 13, 12, 6,9,4,88,25,5,42],obj);
        art("canvas4","#FECA38","black","rgba(242,66,112,0.12)",'#122837',0,0,"增 长 比 率",[1, 3, 2, 1,3,5,2,1,5,7],obj);
        art("canvas5","#80C75B","black","rgba(128,199,91,0.12)",'#122837',0,0,"增 长 比 率",[1, 3, 2, 1,3,5,2,1,5,7],obj);
        art("canvas6","#44CFD8","black","rgba(68,207,216,0.12)",'#122837',0,0,"逾 期 占 比",[1, 3, 2, 1,3,5,2,1,5,7],obj) ;
	}
	
	</script>
</body>
