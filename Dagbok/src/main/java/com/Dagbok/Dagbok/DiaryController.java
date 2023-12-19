package com.Dagbok.Dagbok;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DiaryController {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
    LocalDate localDate = LocalDate.now();

    @Autowired
    private DiaryRepository diaryRepository;

    @GetMapping
    public String getIndex(Model model) {
        model.addAttribute("posts", diaryRepository.findAll());
        return "index";
    }

    @PostMapping("/new-item")
    public String addNew(@RequestParam("title") String title, @RequestParam("text") String text) {
        Diary diary = new Diary();
        diary.setTitle(title);
        diary.setText(text);
        diary.setDate(localDate.format(dtf));
        diaryRepository.save(diary);

        return "redirect:/";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam int id) {
        diaryRepository.deleteById(id);
        System.out.println(id);
        return "redirect:/";

    }

    @PostMapping("/date")
    public String getDate(@RequestParam("date") LocalDate date, Model model) {
        String formattedDate = date.format(dtf);
        model.addAttribute("posts", diaryRepository.findWithDates(formattedDate));
        System.out.println(formattedDate);
        System.out.println(diaryRepository.findWithDates(formattedDate));
        return "index";
    }
    

}
