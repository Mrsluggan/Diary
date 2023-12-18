package com.Dagbok.Dagbok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DiaryController {

    @Autowired
    private DiaryRepositry diaryRepositry;

    @GetMapping
    public String getIndex(Model model) {
        model.addAttribute("posts", diaryRepositry.findAll());
        return "index";
    }

    @GetMapping("/new-item")
    public String addNew() {
        Diary diary = new Diary();
        diary.setTitle("obama");
        diary.setText("Obama be balling");
        diaryRepositry.save(diary);
        System.out.println(diaryRepositry.findAll());

        return "redirect:/";
    }

}
