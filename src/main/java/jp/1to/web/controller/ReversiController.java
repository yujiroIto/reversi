package jp._1to.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReversiController {

    @GetMapping("/")
    public String getIndex() {
        return "ReversiblePiece";
    }
}
