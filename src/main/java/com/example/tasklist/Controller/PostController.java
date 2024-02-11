package com.example.tasklist.Controller;

import com.example.tasklist.TaskListDao;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// 新規投稿をするためのクラス。

@Controller
public class PostController {

  public static final String TASK_ERROR = "ERROR : 1〜20文字以内で入力してください";
  public static final String DEADLINE_ERROR = "ERROR : 期限を設定してください";

  //  タスクを表すTaskItemレコードとそれを格納するtaskItemsフィールド
  public record TaskItem(String id, String task, LocalDate deadline, boolean done) {

  }

  private List<ListController.TaskItem> taskItems = new ArrayList<>();

  private TaskListDao dao;

  @Autowired
  PostController(TaskListDao dao) {
    this.dao = dao;
  }

  //    キーのtaskListがtaskItemsをHTMLに渡すためのキー
//    ${taskList}の部分がtaskItemsの中身であるListに置き換える
  @GetMapping("/post")
  String listItems(Model model) {
    List<ListController.TaskItem> taskItems = dao.findAll();
    model.addAttribute("taskList", taskItems);
    return "post";
  }

  // タスクを追加するためのメソッド
  // task及びdeadlilneが空の場合または指定の文字数の場合、errorを返す。
  @GetMapping("/add")
  String addItem(@RequestParam("task") String task,
      @RequestParam("deadline") String deadlineString, Model model) {

    LocalDate deadline = LocalDate.parse(deadlineString);

    // タスクが1~20字以内で記述がなく、期日の記述もない時にはエラーが出る。
    if ((deadline == null || deadlineString.isEmpty()) && (task.length() < 1
        || task.length() > 20)) {
      model.addAttribute("taskError", TASK_ERROR);
      model.addAttribute("deadlineError", DEADLINE_ERROR);
    }
    // タスクにが1~20字以内で記述がないとエラーが出る。
    if (task.length() < 1 || task.length() > 20) {
      model.addAttribute("taskError", TASK_ERROR);
    }
    // 期日の記述がないとエラーが出る。
    if (deadline == null || deadlineString.isEmpty()) {
      model.addAttribute("deadlineError", DEADLINE_ERROR);
    }
    // エラーメッセージがある場合に/listページを表示する。
    if (model.containsAttribute("taskError") || model.containsAttribute("deadlineError")) {
      return "forward:/post";
    }
    String id = UUID.randomUUID().toString().substring(0, 8);
    ListController.TaskItem item = new ListController.TaskItem(id, task, deadline, false);
    dao.add(item);
    return "redirect:/list";
  }

}
