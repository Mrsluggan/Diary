package com.Dagbok.Dagbok;

import java.util.List;
import java.time.format.DateTimeFormatter;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

public interface DiaryRepository extends CrudRepository<Diary, Integer> {

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Sorterar Listan, så tidigaste inläggen kommer först

    @Query("SELECT d FROM Diary d ORDER BY d.date")
    List<Diary> sortByDates();

    // Tar in en parameter "Date" och letar i databasen med dom inläggen med samma
    // datum, sedan lägger dem i en lista
    @Query("SELECT d FROM Diary d WHERE d.date = :date")
    List<Diary> findWithDates(@Param("date") String date);

    // Tar in parameter, och sedan letar i databasen med inläggen med samma id,
    // lägger dem sedan i en lista
    @Query("SELECT d FROM Diary d WHERE d.id = :id")
    List<Diary> findWithId(@Param("id") Integer id);

    // Letar igenom databasen med två parameter, den börjar på startdate, och sedan
    // enddate
    @Query("SELECT d FROM Diary d WHERE d.date BETWEEN :startDate AND :endDate ")
    List<Diary> findBetweenDates(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT d FROM Diary d WHERE d.text LIKE %:search%")
    List<Diary> findWithSearch(@Param("search") String search);

    // Letar efter inlägget med samma id, sedan uppdateras deras värde med det som
    // följer med i parameterna
    @Transactional
    @Modifying
    @Query("UPDATE Diary d SET d.title = :newTitle, d.text = :newText WHERE d.id = :id")
    void updateWithId(@Param("newTitle") String newTitle, @Param("newText") String newText, @Param("id") int id);

}
