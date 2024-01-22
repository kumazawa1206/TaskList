package com.example.tasklist;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    model.addAttribute("taskList", taskItems);
    return "home";
  }

  //
//
//
  @GetMapping("/add")
  String addItem(@RequestParam("task") String task,
      @RequestParam("deadline") String deadline) {
    String id = UUID.randomUUID().toString().substring(0, 8);
    TaskItem item = new TaskItem(id, task, deadline, false);
    taskItems.add(item);

    return "redirect:/list";
  }

}
