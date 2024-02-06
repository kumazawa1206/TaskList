# 標準的なタスク管理アプリに機能を追加したもの
データベース：MySQL<br>
言語：Java<br>
フレームワーク：SpringBoot<br>

## 機能要件
・タスクを登録する\n
・期日を設定できる\n
・タスクを一覧として確認できる\n
・タスクを削除できる\n
・タスクを更新できる\n

## 非機能要件
・タスク投稿時に空の登録ができないように文字数を制限（１〜２０字）\n
・タスク更新時に空の登録ができないように文字数を制限（１〜２０字）\n
・タスク投稿時、期日が空の状態のままでは登録できない\n
・タスク更新時、期日が空の状態のままでは登録できない\n
・タスク一覧の期日が古いものから順に表示される\n
・トップページ、タスク投稿ページ、タスク一覧ページそれぞれに移動できる \n
