/// <reference path="./reference.ts"/>
var clientSocketId;
// LogViewerとの接続を確立する
function createSocket() {
    chrome.sockets.tcp.create({}, function (createInfo) {
        clientSocketId = createInfo.socketId;
        chrome.sockets.tcp.connect(clientSocketId, "10.0.1.1", 7360, function (resultCode) {
            if (resultCode < 0) {
                console.log("Error: socket connect failure");
            }
        });
    });
}
// LogSenderからデータが送信された時に呼ばれる 変数infoが送られたデータ
chrome.sockets.tcp.onReceive.addListener(function (info) {
    if (info.socketId === clientSocketId) {
        var requestText = ab2str(info.data);
        parseData(requestText);
    }
});
// 何かエラーが起きた時に呼ばれる LogSenderとの通信が切断された時などにも呼ばれる
chrome.sockets.tcp.onReceiveError.addListener(function (info) {
    console.log("Error: ", info);
    makeDownload();
    updateGlaph();
});
// 通信を切断する
function destroySocket() {
    chrome.sockets.tcp.disconnect(clientSocketId);
    chrome.sockets.tcp.close(clientSocketId);
    makeDownload();
    updateGlaph();
}
/**
 * ArrayBufferを文字列に変換する(ASCIIコード専用)
 * @param arrayBuffer
 * @returns {string}
 */
function ab2str(arrayBuffer) {
    var typedArray = new Uint8Array(arrayBuffer);
    var text = "";
    for (var i = 0; i < typedArray.length; i++) {
        text += String.fromCharCode(typedArray[i]);
    }
    return text;
}
