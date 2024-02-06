package com.example.tasklist.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TopController {

  // トップページ
  @RequestMapping("/")
  String hello(Model model) {
    model.addAttribute("message", "計画的にタスク管理しましょう！！");
    return "top"; //top.htmlを返す。
  }

}
