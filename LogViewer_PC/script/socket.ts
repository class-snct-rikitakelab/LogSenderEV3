/// <reference path="./reference.ts"/>

var clientSocketId: number;

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

chrome.sockets.tcp.onReceive.addListener(function (info) {
  if (info.socketId === clientSocketId) {
    var requestText: string = ab2str(info.data);
    parseData(requestText);
  }
});

chrome.sockets.tcp.onReceiveError.addListener(function (info) {
  console.log("Error: ", info);
  makeDownload();
  updateGlaph();
});

function destroySocket() {
  chrome.sockets.tcp.disconnect(clientSocketId);
  chrome.sockets.tcp.close(clientSocketId);
  makeDownload();
  updateGlaph();
}

/**
 * 文字列をArrayBufferに変換する(ASCIIコード専用)
 *
 * @param text
 * @returns {ArrayBuffer}
 */
function str2ab(text) {
  var typedArray = new Uint8Array(text.length);

  for (var i = 0; i < typedArray.length; i++) {
    typedArray[i] = text.charCodeAt(i);
  }

  return typedArray.buffer;
}

/**
 * ArrayBufferを文字列に変換する(ASCIIコード専用)
 *
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

