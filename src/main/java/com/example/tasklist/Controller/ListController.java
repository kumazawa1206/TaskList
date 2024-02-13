package com.example.tasklist.Controller;


import com.example.tasklist.TaskListDao;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

//　タスクの追加、削除、更新を行うためのコントローラークラス

@Controller
public class ListController {

  //タスクの文字数に対するエラーメッセージ
  public static final String TASK_ERROR = "ERROR : 1〜20文字以内で入力してください";
  //期限の設定に対するエラーメッセージ
  public static final String DEADLINE_ERROR = "ERROR : 期限を設定してください";

  //タスク情報を保持するレコード
  public record TaskItem(String id, String task, LocalDate deadline, boolean done) {

  }

  private final List<TaskItem> taskItems = new ArrayList<>();

  private final TaskListDao dao;

  @Autowired
  ListController(TaskListDao dao) {
    this.dao = dao;
  }

  //タスクリストの一覧を表示するメソッド
  @GetMapping("/list")
  String listItems(Model model) {
    List<ListController.TaskItem> taskItems = dao.findAll();
    model.addAttribute("taskList", taskItems);
    return "list";
  }

  //  タスクを削除するメソッド
  @GetMapping("/delete")
  String deleteItem(@RequestParam("id") String id) {
    dao.delete(id);
    return "redirect:/list";
  }

  // タスクを更新するメソッド
  @PostMapping("/update")
  String updateItem(@RequestParam("id") String id,
      @RequestParam("task") String task,
      @RequestParam("deadline") String deadlineString,
      @RequestParam("done") boolean done,
      Model model) {
    String list = "list"; //リダイレクト先のページ

    try {
      LocalDate deadline = null;
      if (!deadlineString.isEmpty()) {
        deadline = LocalDate.parse(deadlineString);
      }

      list = setValidate(task, deadlineString, model, list);

      if (list != null) {
        List<TaskItem> taskItems = dao.findAll();
        model.addAttribute("taskList", taskItems);
        return "list";
      }

      //更新対象のタスクを更新してリダイレクトする。
      TaskItem taskItem = new TaskItem(id, task, deadline, done);
      dao.update(taskItem);
      return "redirect:/list";
    } catch (DateTimeParseException e) {
      //
      model.addAttribute("deadlineERROR", "ERROR : 期限の形式が正しくありません");
      return "forward:/list";
    }
  }


  /**
   * タスクと期日が適切に入力されていない場合にエラーメッセージを表示する。
   *
   * @param task           タスク
   * @param deadlineString 期日
   * @param model          エラーメッセージ
   * @param list           listページ
   * @return list.htmlを返す
   */
  private static String setValidate(String task, String deadlineString, Model model, String list) {
    if (deadlineString.isEmpty() && (task.isEmpty() || task.length() > 20)) {
      model.addAttribute("updateDeadlineError", DEADLINE_ERROR);
      model.addAttribute("updateTaskError", TASK_ERROR);
    } else if (task.isEmpty() || task.length() > 20) {
      model.addAttribute("updateTaskError", TASK_ERROR);
    } else if (deadlineString.isEmpty()) {
      model.addAttribute("updateDeadlineError", DEADLINE_ERROR);
    } else {
      list = null; // リダイレクト先をnullに設定してリダイレクトの必要があるかどうかを示す。
    }
    return list;// リダイレクト先がnullでない場合は、listページに戻り、更新を反映する。
  }
}
