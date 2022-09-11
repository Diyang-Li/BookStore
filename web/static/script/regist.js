function $(id){
    return document.getElementById(id);
}

function preRegist(){
    var unameReg = /[0-9a-zA-Z]{6,16}/;
    var unameTxt = $("unameTxt");
    var uname = unameTxt.value;
    var unameSpan = $("unameSpan");
    if (!unameReg.test(uname)){
        unameSpan.style.visibility = "visible";
        return false;
    }else {
        //
        unameSpan.style.visibility = "hidden";
    }

    var pwdReg = /[\w]{8,}/;
    var pwd = $("pwdTxt").value;
    var pwdSpan = $("pwdSpan");
    if (!pwdReg.test(pwd)){
        pwdSpan.style.visibility = "visible";
        return false;
    }else {
        pwdSpan.style.visibility="hidden";
    }

    var pwdConfirm = $("pwdConfirm").value;
    var pwdConfirmSpan = $("pwdConfirmSpan");
    if (pwdConfirm!=pwd){
        pwdConfirmSpan.style.visibility = "visible";
        return false;
    }else {
        pwdConfirmSpan.style.visibility = "hidden";
    }

    var emailReg = /^[a-zA-Z0-9_\.-]+@([a-zA-Z0-9-]+[\.]{1})+[a-zA-Z]+$/;
    var email = $("emailTxt").value;
    var emailSpan = $("emailSpan");
    if (!emailReg.test(email)){
        emailSpan.style.visibility = "visible";
        return false;
    }else {
        emailSpan.style.visibility = "hidden";
    }
    return true;
}
//原生ajax
//如果想要发送异步请求，我们需要一个关键的对象 XMLHttpRequest


var xmlHttpRequest ;

function createXMLHttpRequest(){
    if(window.XMLHttpRequest){
        //符合DOM2标准的浏览器 ，xmlHttpRequest的创建方式
        xmlHttpRequest = new XMLHttpRequest() ;
    }else if(window.ActiveXObject){//IE浏览器
        try{
            xmlHttpRequest = new ActiveXObject("Microsoft.XMLHTTP");
        }catch (e) {
            xmlHttpRequest = new ActiveXObject("Msxml2.XMLHTTP")
        }
    }
}

function ckUname(uname){
    createXMLHttpRequest();
    var url = "user.do?operate=ckUname&uname="+uname ;
    xmlHttpRequest.open("GET",url,true);
    //设置回调函数
    xmlHttpRequest.onreadystatechange = ckUnameCB ;
    //发送请求
    xmlHttpRequest.send();
}

function ckUnameCB(){
    if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200){
        //xmlHttpRequest.responseText 表示 服务器端响应给我的文本内容
        //alert(xmlHttpRequest.responseText);
        var responseText = xmlHttpRequest.responseText ;
        // {'uname':'1'}
        //alert(responseText);
        if(responseText=="{'uname':'1'}"){
            alert("Username has already been registered！");
        }else{
            alert("Username can be registered!");
        }
    }
}