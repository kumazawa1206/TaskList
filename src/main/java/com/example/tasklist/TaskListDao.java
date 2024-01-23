package com.example.tasklist;

import com.example.tasklist.HomeController.TaskItem;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

@Service
public class TaskListDao {

  //変更できないようにfinalで宣言
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  TaskListDao(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  //引数に渡されたTaskItemオブジェクト情報を
  //データベースのtasklistテーブルに登録する
  public void add(TaskItem taskItem) {
    SqlParameterSource param = new BeanPropertySqlParameterSource(taskItem);
    SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("tasklist");
    insert.execute(param);
  }

  //tasklistテーブルから現在登録されているタスク情報を
  //全て取得してListオブジェクトに格納して返す
  public List<TaskItem> findAll() {
    String query = "SELECT * FROM tasklist";

    List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
    List<TaskItem> taskItems = result.stream()
        .map((Map<String, Object> row) -> new TaskItem(
            row.get("id").toString(),
            row.get("task").toString(),
            row.get("deadline").toString(),
            (Boolean) row.get("done")))
        .toList();

    return taskItems;
  }
}