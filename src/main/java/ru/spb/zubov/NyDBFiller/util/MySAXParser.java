package ru.spb.zubov.NyDBFiller.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.spb.zubov.NyDBFiller.domain.NYCalendar;

import java.util.ArrayList;
import java.util.List;

public class MySAXParser extends DefaultHandler {

    String caseId = "";
    String calendarId = "";
    String appearanceId = "";
    boolean parseTime = false;

    List<NYCalendar> newData = new ArrayList<>();
    List<NYCalendar> updateCaseData = new ArrayList<>();

    public List<NYCalendar> getNewData() {
        return newData;
    }

    public List<NYCalendar> getUpdateCaseData() {
        return updateCaseData;
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
        if (qName.equalsIgnoreCase("Case")) {
            caseId = atts.getValue(0);
        } else if (qName.equalsIgnoreCase("Calendar")) {
            calendarId = atts.getValue(0);
        } else if (qName.equalsIgnoreCase("Appearance")) {
            parseTime = true;
            appearanceId = atts.getValue(0);
        } else if (qName.equalsIgnoreCase("IncludedAppearanceReference")) {
            parseTime = true;
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) {
        if (qName.equalsIgnoreCase("Case")) {
            caseId = "";
        } else if (qName.equalsIgnoreCase("Calendar")) {
            calendarId = "";
        } else if (qName.equalsIgnoreCase("Appearance")) {
            appearanceId = "";
        } else if (qName.equalsIgnoreCase("IncludedAppearanceReference")) {
            parseTime = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (parseTime && !caseId.isEmpty() && !appearanceId.isEmpty()) {
            NYCalendar nyCalendar = new NYCalendar();
            nyCalendar.setCaseId(caseId);
            nyCalendar.setAppearanceId(appearanceId);
            updateCaseData.add(nyCalendar);
            parseTime = false;
        }
        if (parseTime && !calendarId.isEmpty()) {
            newData.add(new NYCalendar(calendarId, new String(ch, start, length)));
        }
    }
}
