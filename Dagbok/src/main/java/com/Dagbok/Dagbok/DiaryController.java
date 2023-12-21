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

    // Skapar formatering och instans av localdate
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
    LocalDate localDate = LocalDate.now();

    @Autowired
    private DiaryRepository diaryRepository;

    // ger sidan tillgång till databasen samt retunerar templete "index"
    @GetMapping
    public String getIndex(Model model) {
        model.addAttribute("posts", diaryRepository.sortByDates());
        return "index";
    }

    // Lyssnar efter två parametrar, och sedan skapar ett nytt inlägg, därefter
    // skickar dem till databasen
    @PostMapping("/new-item")
    public String addNew(@RequestParam("title") String title, @RequestParam("text") String text) {
        Diary diary = new Diary();
        diary.setTitle(title);
        diary.setText(text);
        diary.setDate(localDate.format(dtf));
        diaryRepository.save(diary);

        return "redirect:/";
    }

    // Lyssnar efter en parameter, sedan skickar det till metoden som sedan tar bort
    // inlägget med samma id
    @GetMapping("/delete")
    public String delete(@RequestParam Integer id) {
        diaryRepository.deleteById(id);
        System.out.println(id);
        return "redirect:/";

    }

    // Skickar användaren till rätt inlägg, som sedan kan redigeras
    @GetMapping("/change")
    public String change(@RequestParam Integer id) {
        System.out.println(id);
        return "redirect:/" + id;

    }

    // Lyssnar efter ett id, och sedan skickar två parametrar vars data ska ändra
    // inlägget som matchar id:et
    @PostMapping("/edit")
    public String edit(@RequestParam Integer id, @RequestParam String newTitle, @RequestParam String newText) {
        System.out.println("Id: " + id);
        System.out.println("New Title: " + newTitle);
        System.out.println("New Text: " + newText);
        diaryRepository.updateWithId(newTitle, newText, id);
        return "redirect:/" + id;
    }

    // Tar fram enskild inlägg med hjälp av findwithID
    @GetMapping("/{id}")
    public String changeDiary(@PathVariable Integer id, Model model) {

        model.addAttribute("postById", diaryRepository.findWithId(id));
        return "form";
    }

    // Lyssnar efter vilket datum den ska kolla, skickar det vidare för sedan visa
    // det på sidan
    @PostMapping("/date")
    public String getDate(@RequestParam("date") LocalDate date, Model model) {
        String formattedDate = date.format(dtf);
        model.addAttribute("posts", diaryRepository.findWithDates(formattedDate));
        System.out.println(formattedDate);
        System.out.println(diaryRepository.findWithDates(formattedDate));
        return "index";
    }

    // Tar in två datum, skickar det vidare till databasen för sedan få tillbaka
    // inläggen
    @PostMapping("betweenDate")
    public String getBetweenDates(@RequestParam(value = "firstDate", required = false) LocalDate firstDate, @RequestParam(value = "endDate", required = false) LocalDate endDate, Model model) {

        if (firstDate == null && endDate != null) {
            model.addAttribute("posts", diaryRepository.findWithDates(endDate.format(dtf)));

        } else if (firstDate != null && endDate == null) {
            model.addAttribute("posts", diaryRepository.findWithDates(firstDate.format(dtf)));

        } else if (firstDate != null && endDate != null) {
            model.addAttribute("posts", diaryRepository.findBetweenDates(firstDate.format(dtf), endDate.format(dtf)));
        } else {
            System.out.println("tomt");
        }

        return "index";
    }

    @PostMapping("search")
    public String search(@RequestParam("search") String search, Model model) {
        model.addAttribute("posts", diaryRepository.findWithSearch(search));
        System.out.println(search);
        return "index";
    }
}
