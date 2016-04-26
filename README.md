# LogSenderEV3

EV3からPCにログデータを送信します。


##使い方

###準備
1. LogSender_EV3/src/ev3Viewer/LogSender.javaを、EV3のプロジェクトにコピーする
2. Google Chromeを立ち上げ、拡張機能画面で、デベロッパーモードを選択し、「パッケージ化されていない拡張機能を読み込む」を選択し、LogSender_PCを選択する


###使用時
1. EV3のプログラムでLogSenderを呼び出すコードを書く<br>
一例がLogSenderEV3/LogSender_EV3/src/testcode/Test*.javaにある
2. EV3でプログラムを実行し、connectメソッドが呼び出されるまで待つ
3. Google Chromeの拡張機能からLogViewerを起動し、左上に表示される接続ボタンを押すと、LogViewerにグラフが表示される<br>
グラフはEV3のプログラムでsendメソッドが呼び出されるごとに更新される
4. EV3のプログラムが終了する、EV3のプログラムでdisconnectメソッドが呼び出される、LogViewerで切断ボタンが押されるのいずれかが起きると、通信が終了し、LogViewerにダウンロードボタンが表示される
5. ダウンロードボタンを押すと、グラフデータがダウンロードされる

##### ファイルデータの表示
「ファイルを選択」ボタンを押し、ファイルを選択し、アップロードボタンを押すと、グラフデータが表示される


###注意点
LogViewerで指定された順番でボタンを押さないと動作しない可能性がある<br>
例:接続ボタンを押す前に切断ボタンを押す、ファイルを選択する前にアップロードボタンを押す<br>
そういった時は、LogViewerを再起動する必要がある
