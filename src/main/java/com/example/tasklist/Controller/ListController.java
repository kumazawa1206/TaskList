package com.example.tasklist.Controller;


import com.example.tasklist.TaskListDao;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

//　投稿を削除・更新するためのクラス

@Controller
public class ListController {

  public static final String TASK_ERROR = "ERROR : 1〜20文字以内で入力してください";
  public static final String DEADLINE_ERROR = "ERROR : 期限を設定してください";

  //  タスクを表すTaskItemレコードとそれを格納するtaskItemsフィールド
  public record TaskItem(String id, String task, LocalDate deadline, boolean done) {

  }

  private List<TaskItem> taskItems = new ArrayList<>();

  private TaskListDao dao;

  @Autowired
  ListController(TaskListDao dao) {
    this.dao = dao;
  }

  @GetMapping("/list")
  String listItems(Model model) {
    List<ListController.TaskItem> taskItems = dao.findAll();
    model.addAttribute("taskList", taskItems);
    return "list";
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
      @RequestParam("deadline") String deadlineString,
      @RequestParam("done") boolean done,
      Model model) {
    String list = "list";
    LocalDate deadline = LocalDate.parse(deadlineString);

    //タスクリストと期日が適切に入力されていなければエラーを出す。
    if ((deadlineString == null || deadlineString.isEmpty()) && (task.length() < 1
        || task.length() > 20)) {
      model.addAttribute("updateDeadlineError", DEADLINE_ERROR);
      model.addAttribute("updateTaskError", TASK_ERROR);
    } else if (task.length() < 1 || task.length() > 20) {
      model.addAttribute("updateTaskError", TASK_ERROR);
    } else if (deadlineString == null || deadlineString.isEmpty()) {
      model.addAttribute("updateDeadlineError", DEADLINE_ERROR);
    } else {
      list = null; // homeをnullに設定してリダイレクトの必要があるかどうかを示す。
    }
    // homeがnullでない場合は、homeページに戻り、タスクリストを更新する。
    if (list != null) {
      List<TaskItem> taskItems = dao.findAll();
      model.addAttribute("taskList", taskItems);
      return "list";
    }
    TaskItem taskItem = new TaskItem(id, task, deadline, done);
    dao.update(taskItem);
    return "redirect:/list";
  }
}
