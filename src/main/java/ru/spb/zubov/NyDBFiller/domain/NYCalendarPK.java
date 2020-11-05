package ru.spb.zubov.NyDBFiller.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.io.Serializable;

public class NYCalendarPK implements Serializable {

    static final long serialVersionUID = 1L;

    public NYCalendarPK() {
    }

    @Column(name = "CALENDAR_ID", length = 50, nullable = false)
    @Basic(fetch = FetchType.EAGER)
    @Id
    private String calendarId;

    @Column(name = "APPEARANCE_ID", length = 50, nullable = false)
    @Basic(fetch = FetchType.EAGER)
    @Id
    private String appearanceId;

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

        NYCalendarPK that = (NYCalendarPK) o;

        if (!calendarId.equals(that.calendarId)) return false;
        return appearanceId.equals(that.appearanceId);
    }

    @Override
    public int hashCode() {
        int result = calendarId.hashCode();
        result = 31 * result + appearanceId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "NYCalendarPK{" +
                "calendarId='" + calendarId + '\'' +
                ", appearanceId='" + appearanceId + '\'' +
                '}';
    }
}
