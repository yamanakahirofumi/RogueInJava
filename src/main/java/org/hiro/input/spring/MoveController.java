package org.hiro.input.spring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MoveController {

    @GetMapping("/left")
    public void left(){

    }
}
