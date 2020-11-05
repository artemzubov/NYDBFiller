package ru.spb.zubov.NyDBFiller.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.spb.zubov.NyDBFiller.domain.NYCalendar;
import ru.spb.zubov.NyDBFiller.domain.NYCalendarPK;

import java.util.List;

public interface NYCalendarRepo extends CrudRepository<NYCalendar, NYCalendarPK> {

    @Query("SELECT NYC FROM NYCalendar NYC WHERE NYC.calendarId LIKE concat('%', concat(?1, '%'))")
    List<NYCalendar> findForFun(String partOfCalendar);

    @Query("SELECT NYC FROM NYCalendar NYC WHERE NYC.calendarId LIKE concat('%', concat(:partOfCalendar, '%'))")
    List<NYCalendar> findForFun2(String partOfCalendar);

    @Query("SELECT NYC FROM NYCalendar NYC WHERE NYC.calendarId LIKE concat('%', :partOfCalendar, '%')")
    List<NYCalendar> findForFun3(String partOfCalendar);

    @Query("SELECT NYC FROM NYCalendar NYC WHERE NYC.calendarId LIKE %:partOfCalendar%")
    List<NYCalendar> findForFun4(String partOfCalendar);

    List<NYCalendar> findAllByAppearanceIdAndCaseId(String appearanceId, String caseId);

    @Override
    List<NYCalendar> findAll();
}
