package ru.spb.zubov.NyDBFiller.domain;

import javax.persistence.*;

@Entity
@IdClass(NYCalendarPK.class)
@Table(schema = "...", name = "...")
public class NYCalendar {

    public NYCalendar() {
    }

    public NYCalendar(String calendarId, String appearanceId) {
        this.calendarId = calendarId;
        this.appearanceId = appearanceId;
    }

    @Column(name = "CALENDAR_ID", length = 50, nullable = false)
    @Basic(fetch = FetchType.EAGER)
    @Id
    private String calendarId;

    @Column(name = "APPEARANCE_ID", length = 60, nullable = false)
    @Basic(fetch = FetchType.EAGER)
    @Id
    private String appearanceId;

    @Column(name = "CASE_ID", length = 50)
    @Basic(fetch = FetchType.EAGER)
    private String caseId;

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public String getAppearanceId() {
        return appearanceId;
    }

    public void setAppearanceId(String appearanceId) {
        this.appearanceId = appearanceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NYCalendar that = (NYCalendar) o;

        if (!caseId.equals(that.caseId)) return false;
        if (!calendarId.equals(that.calendarId)) return false;
        return appearanceId.equals(that.appearanceId);
    }

    @Override
    public int hashCode() {
        int result = caseId.hashCode();
        result = 31 * result + calendarId.hashCode();
        result = 31 * result + appearanceId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "NYCalendar{" +
                "caseId='" + caseId + '\'' +
                ", calendarId='" + calendarId + '\'' +
                ", appearanceId='" + appearanceId + '\'' +
                '}';
    }
}
