package com.example.tasklist;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

  //  タスクを表すTaskItemレコードとそれを格納するtaskItemsフィールド
  record TaskItem(String id, String task, String deadline, boolean done) {

  }

  private List<TaskItem> taskItems = new ArrayList<>();

  @RequestMapping(value = "/hello")
  String hello(Model model) {
    model.addAttribute("time", LocalDateTime.now());
    return "hello";
  }

  //  キーのtaskListがtaskItemsをHTMLに渡すためのキー
  //  ${taskList}の部分がtaskItemsの中身であるListに置き換える
  @GetMapping("/list")
  String listItems(Model model) {
    List<TaskItem> taskItems = dao.findAll();
    model.addAttribute("taskList", taskItems);
    return "home";
  }

  //  タスクを追加するためのメソッド
  @GetMapping("/add")
  String addItem(@RequestParam("task") String task,
      @RequestParam("deadline") String deadline, Model model) {
    if (task.length() < 1 || task.length() > 20) {
      model.addAttribute("error", "ERROR : :1〜20文字以内で入力してください");
      return "home";
    }
    String id = UUID.randomUUID().toString().substring(0, 8);
    TaskItem item = new TaskItem(id, task, deadline, false);
    dao.add(item);
    return "redirect:/list";
  }

  //  タスクを削除するためのメソッド
  @GetMapping("/delete")
  String deleteItem(@RequestParam("id") String id) {
    dao.delete(id);
    return "redirect:/list";
  }

  //  タスクを更新するためのメソッド
  @PostMapping("/update")
  String updateItem(@RequestParam("id") String id,
      @RequestParam("task") String task,
      @RequestParam("deadline") String deadline,
      @RequestParam("done") boolean done) {
    TaskItem taskItem = new TaskItem(id, task, deadline, done);
    dao.update(taskItem);
    return "redirect:/list";
  }

  private TaskListDao dao;

  @Autowired
  HomeController(TaskListDao dao) {
    this.dao = dao;
  }


}
