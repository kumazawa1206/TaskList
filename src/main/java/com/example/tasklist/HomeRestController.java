package com.example.tasklist;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeRestController {

  //  HomeRestControllerにタスク情報を保持するための入れ物
  record TaskItem(String id, String task, String deadline, boolean done) {

  }

  private List<TaskItem> taskItems = new ArrayList<>();


  // タスクを追加するエンドポイント
  @GetMapping("/restadd")
  String addItem(@RequestParam("task") String task,
      @RequestParam("deadline") String deadline) {
    String id = UUID.randomUUID().toString().substring(0, 8);
    TaskItem item = new TaskItem(id, task, deadline, false);
    taskItems.add(item);
    return "タスクを追加しました。";
  }

  // タスクを一覧表示するエンドポイント
  @GetMapping("/restlist")
  String listItems() {
    String result = taskItems.stream()
        .map(TaskItem::toString)
        .collect(Collectors.joining(", "));
    return result;
  }
}
