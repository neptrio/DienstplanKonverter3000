package sample;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.io.TimezoneAssignment;
import biweekly.property.Summary;
import biweekly.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;


public class CalenderBuilder implements ICalenderBuilder {

    private Logger logger;

    private String pathOutputDirectory;
    private int year;
    private int month;

    public CalenderBuilder(Logger logger, String pathOutputDirectory, int month, int year) {
        this.pathOutputDirectory = pathOutputDirectory;
        this.logger = logger;
        this.year = year;
        this.month = month;
    }

    public void createCalenderFile(Employee e) {

        boolean failureInCalender = false;
        File file;

        TimezoneAssignment germany = TimezoneAssignment.download(
                TimeZone.getTimeZone("Europe/Berlin"),
                true
        );

        ICalendar ical = new ICalendar();
        ical.getTimezoneInfo().setDefaultTimezone(germany);

        for (ShiftDate shift : e.getDienste()) {

            if (shift.getName().equals("-"))
                continue;

            try {
                ShiftTypeFactory shiftTypeFactory = new ShiftTypeFactory();
                ShiftType sType = shiftTypeFactory.getShift(shift.getName());

                if(sType == null)
                    continue;

                VEvent event = new VEvent();
                Summary summary = event.setSummary(shift.getName());
                summary.setLanguage("de-de");

                Calendar myCal = Calendar.getInstance();
                myCal.set(Calendar.YEAR, this.year);
                myCal.set(Calendar.MONTH, this.month);
                myCal.set(Calendar.DAY_OF_MONTH, shift.getDay());
                myCal.set(Calendar.HOUR_OF_DAY, sType.getStartHour());
                myCal.set(Calendar.MINUTE, 0);
                myCal.set(Calendar.SECOND, 0);
                Date theDate = myCal.getTime();

                event.setDateStart(theDate);

                Duration duration = new Duration.Builder().minutes(sType.getDuration()).build();
                event.setDuration(duration);
                event.setLocation(sType.getPlace());

                ical.addEvent(event);
            } catch (IllegalArgumentException ex) {
                logger.info("Fehler beim Erzeugen des Termins für: " + e.getName() + " Grund: " + ex);
                System.out.println("Fehler beim Erzeugen des Termins für: " + e.getName() + " Grund: " + ex);
                failureInCalender = true;
            }
        }

        if(failureInCalender) {
            file = new File(pathOutputDirectory + "\\" + e.getName() + "_FEHLERBEHAFTET.ics");
        }else{
            file = new File(pathOutputDirectory + "\\" + e.getName() + ".ics");
        }
        String str = Biweekly.write(ical).go();
        System.out.println(str);

        try{
            Biweekly.write(ical).go(file);
        } catch (IOException biEx) {
            logger.info("Fehler beim Erzeugen der Datei für: " + e.getName() + " Grund: " + biEx);
            System.out.println("Fehler beim Erzeugen der Datei für: " + e.getName() + " Grund: " + biEx);
        }

    }
}
