/// <reference path="./reference.ts"/>

var glaph: logGlaph[] = [];// 

// index.htmlで定義したidの画像・ボタンがクリックされた時それぞれ関数を呼び出す

var linkconnect = document.getElementById("connect"); // idからhtmlの要素を探す
linkconnect.addEventListener("click", function () { // 要素をクリックした時呼び出される
  createSocket(); // この中で関数を呼び出す
});

var linkdisconnect = document.getElementById("disconnect");
linkdisconnect.addEventListener("click", function () {
  destroySocket();
});

var upload = document.getElementById("upload");
upload.addEventListener("click", function () {
  uploadFile();
});

var upload = document.getElementById("send");
upload.addEventListener("click", function () {
  sendText();
});

// LogViewer側用のテストコード
function glaphTest() {
  parseData("{\"name\":\"value1\",\"type\":\"Number\",\"value\":10,\"time\":1}{\"name\":\"value1\",\"type\":\"Num");
  parseData("ber\",\"value\":40,\"time\":2}{\"name\":\"value1\",\"type\":\"Number\",\"value\":30,\"time\":3}");
  parseData("{\"name\":\"value2\",\"type\":\"Number\",\"value\":20,\"time\":1}{\"name\":\"value2\",\"type\":\"Num");
  parseData("ber\",\"value\":10,\"time\":2}{\"name\":\"value2\",\"type\":\"Number\",\"value\":0,\"time\":3}");
  parseData("{\"name\":\"value3\",\"type\":\"Number\",\"value\":20,\"time\":1}{\"name\":\"value3\",\"type\":\"Num");
  parseData("ber\",\"value\":10,\"time\":2}{\"name\":\"value3\",\"type\":\"Number\",\"value\":0,\"time\":3}");
  //makeDownload();
}
//glaphTest(); 
