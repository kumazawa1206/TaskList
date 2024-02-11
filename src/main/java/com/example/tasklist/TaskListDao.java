package com.example.tasklist;

import com.example.tasklist.Controller.ListController.TaskItem;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

// データベース操作用のクラス
@Service
public class TaskListDao {

  //変更できないようにfinalで宣言
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  TaskListDao(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  //テーブルにタスク情報を追加するメソッド。
  public void add(TaskItem taskItem) {
    SqlParameterSource param = new BeanPropertySqlParameterSource(taskItem);
    SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("tasklist");
    insert.execute(param);
  }

  //テーブルのタスク情報を全て取得するメソッド。
  //期日の古い順に表示される。
  public List<TaskItem> findAll() {
    String query = "SELECT * FROM tasklist ORDER BY deadline";

    List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
    List<TaskItem> taskItems = result.stream()
        .map((Map<String, Object> row) -> {
          LocalDate deadline = LocalDate.parse(row.get("deadline").toString());
          boolean done = (Boolean) row.get("done");
          if (!done && deadline.isEqual(LocalDate.now())) {
            LocalDateTime now = LocalDateTime.now();
            if (now.getHour() == 15 && deadline.isEqual(LocalDate.now())) {
              done = true;
              jdbcTemplate.update("UPDATE tasklist SET done = ? WHERE id = ?", done, row.get("id"));
            }
          }

          return new TaskItem(
              row.get("id").toString(),
              row.get("task").toString(),
              deadline,
              done
          );
        }).toList();

    return taskItems;
  }

  //tasklistテーブルから現在登録されているタスク情報を取得して削除するメソッド
  public int delete(String id) {
    int number = jdbcTemplate.update("DELETE FROM tasklist WHERE id = ?", id);
    return number;
  }

  //タスク情報を更新する
  public int update(TaskItem taskItem) {
    LocalDateTime now = LocalDateTime.now();
    LocalDate deadline = taskItem.deadline();
    boolean done = taskItem.done();
    if (!done && deadline.isEqual(LocalDate.now())) {
      if (now.getHour() == 15) {
        done = true;
      }
    }
    int number = jdbcTemplate.update(
        "UPDATE tasklist SET task = ? , deadline = ?, done = ? WHERE id = ?",
        taskItem.task(),
        taskItem.deadline(),
        done,
        taskItem.id());
    return number;
  }
}