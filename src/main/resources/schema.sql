--タスク情報格納用のテーブル
--タスク情報は[id][タスク内容][期日][完了済か未完了]
CREATE TABLE IF NOT EXISTS tasklist (
    id VARCHAR(8) PRIMARY KEY,
    task VARCHAR(256),
    deadline DATE,
    done BOOLEAN
);