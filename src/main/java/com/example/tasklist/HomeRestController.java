package com.example.tasklist;


import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeRestController {

  @RequestMapping(value = "resthello")
  String hello() {
    return """
        Hello.
        It Works!!
        現在の時刻は %sです。
        """.formatted(LocalDateTime.now());
  }

}
