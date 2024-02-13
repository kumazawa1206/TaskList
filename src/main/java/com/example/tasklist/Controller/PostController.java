package com.example.tasklist.Controller;

import com.example.tasklist.TaskListDao;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

  //タスクの文字数に対するエラーメッセージ
  public static final String TASK_ERROR = "ERROR : 1〜20文字以内で入力してください";
  //期限の設定に対するエラーメッセージ
  public static final String DEADLINE_ERROR = "ERROR : 期限を設定してください";

  //タスク情報を保持するレコード
  public record TaskItem(String id, String task, LocalDate deadline, boolean done) {

  }

  //タスク一覧を格納するリスト
  private final List<ListController.TaskItem> taskItems = new ArrayList<>();

  private final TaskListDao dao;

  @Autowired
  PostController(TaskListDao dao) {
    this.dao = dao;
  }

  //投稿ページを表示するメソッド
  @GetMapping("/post")
  String listItems(Model model) {
    List<ListController.TaskItem> taskItems = dao.findAll();
    model.addAttribute("taskList", taskItems);
    return "post";
  }

  // タスクを追加するメソッド
  @GetMapping("/add")
  String addItem(@RequestParam("task") String task,
      @RequestParam("deadline") String deadlineString, Model model) {

    try {
      LocalDate deadline = null;
      if (!deadlineString.isEmpty()) {
        deadline = LocalDate.parse(deadlineString);
      }

      validateTaskAndDeadline(task, deadlineString, model);
      // エラーメッセージがある場合に/postページを表示する。
      if (model.containsAttribute("taskError") || model.containsAttribute("deadlineError")) {
        return "forward:/post";
      }

      //新しいタスクのを作成してリストに追加し、listページを返す。
      String id = UUID.randomUUID().toString().substring(0, 8);
      ListController.TaskItem item = new ListController.TaskItem(id, task, deadline, false);
      dao.add(item);
      return "redirect:/list";
    } catch (DateTimeParseException e) {
      //期限の形式が正しくない場合はエラーメッセージを設定してpostページにリダイレクトする
      model.addAttribute("deadlineERROR", "ERROR : 期限の形式が正しくありません");
      return "forward:/poat";
    }
  }


  /**
   * 入力されたタスクや期限が適切でない場合はエラーメッセージを表示する。
   *
   * @param task           タスク
   * @param deadlineString 期日
   * @param model          エラーメッセージ
   */
  private static void validateTaskAndDeadline(String task, String deadlineString, Model model) {
    // 入力されたタスクや期限が適切でない場合はエラーメッセージを設定する
    if (deadlineString.isEmpty() && (task.isEmpty() || task.length() > 20)) {
      model.addAttribute("taskError", TASK_ERROR);
      model.addAttribute("deadlineError", DEADLINE_ERROR);
    }
    // タスクにが1~20字以内で記述がないとエラーが出る。
    if (task.isEmpty() || task.length() > 20) {
      model.addAttribute("taskError", TASK_ERROR);
    }
    // 期日の記述がないとエラーが出る。
    if (deadlineString.isEmpty()) {
      model.addAttribute("deadlineError", DEADLINE_ERROR);
    }
  }

}
