function sayhi(str){
	if(str==""){
		document.getElementById("hello").innerHTML = "";
	}else{
		document.getElementById("hello").innerHTML = "Hi, " + str;
	}
}
function saywelcome(str){
	if(str==""){
		document.getElementById("hello").innerHTML = "";
	}else{
		document.getElementById("hello").innerHTML = "Welcome, " + str;
	}
}