# 標準的なタスク管理アプリに機能を追加したもの
データベース：MySQL<br>
言語：Java<br>
フレームワーク：SpringBoot<br>

## 機能要件
・タスクを登録する<br>
・期日を設定できる<br>
・タスクを一覧として確認できる<br>
・タスクを削除できる<br>
・タスクを更新できる<br>

## 非機能要件
・タスク投稿時に空の登録ができないように文字数を制限（１〜２０字）<br>
・タスク更新時に空の登録ができないように文字数を制限（１〜２０字）<br>
・タスク投稿時、期日が空の状態のままでは登録できない<br>
・タスク更新時、期日が空の状態のままでは登録できない<br>
・タスク一覧の期日が古いものから順に表示される<br>
・トップページ、タスク投稿ページ、タスク一覧ページそれぞれに移動できる <br>
