/// <reference path="./reference.ts"/>
// ダウンロードの画像を表示し、ファイルを作る
function makeDownload() {
    var downloadLink = document.getElementById("download");
    if (downloadLink.hasChildNodes() == false) {
        var downloadFile = "";
        if (glaph.length != 0)
            downloadFile += "[";
        for (var i = 0; i < glaph.length; ++i) {
            var mgdata = glaph[i].getmgdata();
            console.log(mgdata);
            downloadFile += JSON.stringify(mgdata);
            if (i + 1 < glaph.length) {
                downloadFile += ",";
            }
        }
        if (glaph.length != 0)
            downloadFile += "]";
        var a = document.createElement("a");
        a.download = "log.json";
        a.href = URL.createObjectURL(new Blob([downloadFile], { type: "text.plain" }));
        a.dataset.downloadurl = ["text/plain", a.download, a.href].join(":");
        downloadLink.appendChild(a);
        var image = new Image();
        image.src = "../assets/download.svg";
        a.appendChild(image);
    }
}
// 選択されたファイルをグラフ表示する
function uploadFile() {
    var uploadFile = document.getElementById("upload-file");
    var file = uploadFile.files[0];
    var uploadData;
    var reader = new FileReader();
    reader.readAsText(file);
    reader.onload = function () {
        var mgdata = JSON.parse(reader.result);
        //mgdata = Encoding.codeToString(mgdata);
        console.log(mgdata, mgdata.length);
        for (var i = 0; i < mgdata.length; ++i) {
            glaph[i] = new logGlaph(mgdata[i].title);
            glaph[i].setmgdata(mgdata[i]);
            glaph[i].setdata(mgdata[i].data);
        }
        updateGlaph();
    };
}
