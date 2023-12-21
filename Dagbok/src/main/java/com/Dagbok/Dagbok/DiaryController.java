package com.Dagbok.Dagbok;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        model.addAttribute("posts", diaryRepository.sortByDates());
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

    @GetMapping("/change")
    public String change(@RequestParam int id) {
        System.out.println(id);
        return "redirect:/" + id;

    }

    @PostMapping("/edit")
    public String edit(@RequestParam Integer id, @RequestParam String newTitle, @RequestParam String newText) {
        System.out.println("Id: " + id);
        System.out.println("New Title: " + newTitle);
        System.out.println("New Text: " + newText);
        diaryRepository.updateWithId(newTitle, newText, id);
        return "redirect:/" + id;
    }

    @PostMapping("/date")
    public String getDate(@RequestParam("date") LocalDate date, Model model) {
        String formattedDate = date.format(dtf);
        model.addAttribute("posts", diaryRepository.findWithDates(formattedDate));
        System.out.println(formattedDate);
        System.out.println(diaryRepository.findWithDates(formattedDate));
        return "index";
    }

    @GetMapping("/{id}")
    public String changeDiary(@PathVariable Integer id, Model model) {

        model.addAttribute("postById", diaryRepository.findWithId(id));
        return "form";
    }

    @PostMapping("betweenDate")
    public String getBetweenDates(@RequestParam("firstDate") LocalDate firstDate, @RequestParam("endDate") LocalDate endDate, Model model) {

        System.out.println(firstDate.format(dtf) + endDate.format(dtf));
        
        model.addAttribute("posts", diaryRepository.findBetweenDates(firstDate.format(dtf), endDate.format(dtf)));

        return "index";
    }

}
