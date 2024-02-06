package com.example.tasklist.Controller;


import com.example.tasklist.TaskListDao;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

  public static final String TASK_ERROR = "ERROR : 1〜20文字以内で入力してください";
  public static final String DEADLINE_ERROR = "ERROR : 期限を設定してください";

  //  タスクを表すTaskItemレコードとそれを格納するtaskItemsフィールド
  public record TaskItem(String id, String task, String deadline, boolean done) {

  }

  private List<TaskItem> taskItems = new ArrayList<>();

  private TaskListDao dao;

  @Autowired
  HomeController(TaskListDao dao) {
    this.dao = dao;
  }

  //  キーのtaskListがtaskItemsをHTMLに渡すためのキー
  //  ${taskList}の部分がtaskItemsの中身であるListに置き換える
  @GetMapping("/list")
  String listItems(Model model) {
    List<TaskItem> taskItems = dao.findAll();
    model.addAttribute("taskList", taskItems);
    return "post";
  }

  // タスクを追加するためのメソッド
  // task及びdeadlilneが空の場合または指定の文字数の場合、errorを返す。
  @GetMapping("/add")
  String addItem(@RequestParam("task") String task,
      @RequestParam("deadline") String deadline, Model model) {

    // タスクが1~20字以内で記述がなく、期日の記述もない時にはエラーが出る。
    if ((deadline == null || deadline.isEmpty()) && (task.length() < 1 || task.length() > 20)) {
      model.addAttribute("taskError", TASK_ERROR);
      model.addAttribute("deadlineError", DEADLINE_ERROR);
    }
    // タスクにが1~20字以内で記述がないとエラーが出る。
    if (task.length() < 1 || task.length() > 20) {
      model.addAttribute("taskError", TASK_ERROR);
    }
    // 期日の記述がないとエラーが出る。
    if (deadline == null || deadline.isEmpty()) {
      model.addAttribute("deadlineError", DEADLINE_ERROR);
    }
    // エラーメッセージがある場合に/listページを表示する。
    if (model.containsAttribute("taskError") || model.containsAttribute("deadlineError")) {
      return "forward:/list";
    }
    String id = UUID.randomUUID().toString().substring(0, 8);
    TaskItem item = new TaskItem(id, task, deadline, false);
    dao.add(item);
    return "redirect:/list";
  }


  //  タスクを削除するためのメソッド
  // idを引数にする。
  @GetMapping("/delete")
  String deleteItem(@RequestParam("id") String id) {
    dao.delete(id);
    return "redirect:/list";
  }

  // タスクを更新するためのメソッド
  // task及びdeadlilneが空の場合または指定の文字数の場合、errorを返す。
  @PostMapping("/update")
  String updateItem(@RequestParam("id") String id,
      @RequestParam("task") String task,
      @RequestParam("deadline") String deadline,
      @RequestParam("done") boolean done,
      Model model) {
    String home = "home";
    //タスクリストと期日が適切に入力されていなければエラーを出す。
    if ((deadline == null || deadline.isEmpty()) && (task.length() < 1 || task.length() > 20)) {
      model.addAttribute("updateDeadlineError", DEADLINE_ERROR);
      model.addAttribute("updateTaskError", TASK_ERROR);
    } else if (task.length() < 1 || task.length() > 20) {
      model.addAttribute("updateTaskError", TASK_ERROR);
    } else if (deadline == null || deadline.isEmpty()) {
      model.addAttribute("updateDeadlineError", DEADLINE_ERROR);
    } else {
      home = null; // homeをnullに設定してリダイレクトの必要があるかどうかを示す。
    }
    // homeがnullでない場合は、homeページに戻り、タスクリストを更新する。
    if (home != null) {
      List<TaskItem> taskItems = dao.findAll();
      model.addAttribute("taskList", taskItems);
      return "home";
    }
    TaskItem taskItem = new TaskItem(id, task, deadline, done);
    dao.update(taskItem);
    return "redirect:/list";
  }
}
