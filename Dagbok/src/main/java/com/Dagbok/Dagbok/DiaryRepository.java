package com.Dagbok.Dagbok;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface DiaryRepository extends CrudRepository<Diary, Integer> {

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Query("SELECT d FROM Diary d WHERE d.date = :date")
    List<Diary> findWithDates(@Param("date") String date);

}
