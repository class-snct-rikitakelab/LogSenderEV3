/// <reference path="./reference.ts"/>
var glaph = [];
//glaph[0] = new logGlaph("g1");
//glaph[getGlaphNumberFromName("g1")].updateGlaph(0.0,1);
//createSocket();
//glaphTest();
/*var linksample = document.getElementById("sample");
linksample.addEventListener("click", function() {
   glaphTest();
});*/
var linkconnect = document.getElementById("connect");
linkconnect.addEventListener("click", function () {
    createSocket();
});
var linkdisconnect = document.getElementById("disconnect");
linkdisconnect.addEventListener("click", function () {
    destroySocket();
});
var upload = document.getElementById("upload");
upload.addEventListener("click", function () {
    uploadFile();
});
/* xlengthset = document.getElementById("xlenset");
xlengthset.addEventListener("click", function() {
   document.length.x_length.value;
});
var xlengthset = document.getElementById("xlenset");
xlengthset.addEventListener("click", function() {
});*/
/*while (true) {
  updateGlaph();
}*/
//glaphTest();
function glaphTest() {
    parseData("{\"name\":\"value1\",\"type\":\"Number\",\"value\":10,\"time\":1}{\"name\":\"value1\",\"type\":\"Num");
    parseData("ber\",\"value\":40,\"time\":2}{\"name\":\"value1\",\"type\":\"Number\",\"value\":30,\"time\":3}");
    parseData("{\"name\":\"value2\",\"type\":\"Number\",\"value\":20,\"time\":1}{\"name\":\"value2\",\"type\":\"Num");
    parseData("ber\",\"value\":10,\"time\":2}{\"name\":\"value2\",\"type\":\"Number\",\"value\":0,\"time\":3}");
    parseData("{\"name\":\"value3\",\"type\":\"Number\",\"value\":20,\"time\":1}{\"name\":\"value3\",\"type\":\"Num");
    parseData("ber\",\"value\":10,\"time\":2}{\"name\":\"value3\",\"type\":\"Number\",\"value\":0,\"time\":3}");
    //makeDownload();
}
