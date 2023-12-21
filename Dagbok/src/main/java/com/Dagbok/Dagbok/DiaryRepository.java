package com.Dagbok.Dagbok;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

public interface DiaryRepository extends CrudRepository<Diary, Integer> {

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Query("SELECT d FROM Diary d ORDER BY d.date")
    List<Diary> sortByDates();


    @Query("SELECT d FROM Diary d WHERE d.date = :date")
    List<Diary> findWithDates(@Param("date") String date);

    @Query("SELECT d FROM Diary d WHERE d.id = :id")
    List<Diary> findWithId(@Param("id") Integer id);

    @Query("SELECT d FROM Diary d WHERE d.date BETWEEN :startDate AND :endDate ")
    List<Diary> findBetweenDates(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Transactional
    @Modifying
    @Query("UPDATE Diary d SET d.title = :newTitle, d.text = :newText WHERE d.id = :id")
    void updateWithId(@Param("newTitle") String newTitle, @Param("newText") String newText, @Param("id") int id);

}
