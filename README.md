# anpikakuninProject
プロジェクト演習A1「高い回答率を目指した安否確認システムの構築」のAndroidアプリ

# 画面構成
## InquiartFragment
webclassやいちぽるへ飛べるボタンや問い合わせを行える画面

## NotificationFragment
学校からのお知らせを表示する画面

いちぽるにあるようなものを想定

## OperationInfoFragment
バスや電車といった運行情報を見ることができる画面

「くるけん」等といったサイトを自分で登録してボタンを追加可能

## ScheduleFragment
個人の現在の時間割を見ることができる画面

時間割アプリみたいに自分で登録して保存

## LoginActivity
上記4画面に行くために学籍番号、パスワードを入力してログインする画面

## PasswordActivity
パスワードを変更する画面

# コミットメッセージの書き方
今後のことも考えて日本語で書きます。

## フォーマット
```
[コミット種類] 要約

変更した理由を具体的に
```
## コミット種類
* fix：バグ修正
* hotfix：クリティカルなバグ修正
* add：新規（ファイル）機能追加
* update：機能修正（バグではない）
* change：仕様変更
* clean：整理（リファクタリング等）
* disable：無効化（コメントアウト等）
* remove：削除（ファイル）
* upgrade：バージョンアップ
* revert：変更取り消し

### 例
add:ボタン作成機能追加
