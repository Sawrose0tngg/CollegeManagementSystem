package modules;

import javafx.beans.property.SimpleStringProperty;

public class CourseModel {

    private final SimpleStringProperty course;
    private final SimpleStringProperty duration;
    private final SimpleStringProperty semester;
    private final SimpleStringProperty sno;
    private final SimpleStringProperty fullMark;
    private final SimpleStringProperty passMark;
    private final SimpleStringProperty theoryMark;
    private final SimpleStringProperty practicalMark;

    public CourseModel(String cou, String dur, String sem) {
        this.course = new SimpleStringProperty(cou);
        this.duration = new SimpleStringProperty(dur);
        this.semester = new SimpleStringProperty(sem);
        this.sno = null;
        this.fullMark = null;
        this.passMark = null;
        this.theoryMark = null;
        this.practicalMark = null;
    }

    public CourseModel(String cou, String dur, String sem, String sn) {
        this.course = new SimpleStringProperty(cou);
        this.duration = new SimpleStringProperty(dur);
        this.semester = new SimpleStringProperty(sem);
        this.sno = new SimpleStringProperty(sn);
        this.passMark = null;
        this.fullMark = null;
        this.theoryMark = null;
        this.practicalMark = null;
    }
    
    public CourseModel(String cou, String dur, String sem, String sn, String ful, String pas) {
        this.course = new SimpleStringProperty(cou);
        this.duration = new SimpleStringProperty(dur);
        this.semester = new SimpleStringProperty(sem);
        this.sno = new SimpleStringProperty(sn);
        this.fullMark = new SimpleStringProperty(ful);
        this.passMark = new SimpleStringProperty(pas);
        this.theoryMark = null;
        this.practicalMark = null;
    }
    
    public CourseModel(String cou, String dur, String sem, String sn, String ful, String pas, String theo, String prac) {
        this.course = new SimpleStringProperty(cou);
        this.duration = new SimpleStringProperty(dur);
        this.semester = new SimpleStringProperty(sem);
        this.sno = new SimpleStringProperty(sn);
        this.fullMark = new SimpleStringProperty(ful);
        this.passMark = new SimpleStringProperty(pas);
        this.theoryMark = new SimpleStringProperty(theo);
        this.practicalMark = new SimpleStringProperty(prac);
    }

    public String getCourse() {
        return course.get();
    }

    public void setCourse(String cou) {
        this.course.set(cou);
    }

    public String getDuration() {
        return duration.get();
    }

    public void setDuration(String dur) {
        this.duration.set(dur);
    }

    public String getSemester() {
        return semester.get();
    }

    public void setSemester(String sem) {
        this.semester.set(sem);
    }

    public String getSno() {
        return sno.get();
    }

    public void setSno(String sn) {
        this.sno.set(sn);
    }

    public String getFullMark() {
        return fullMark.get();
    }

    public void setFullMark(String ful) {
        this.fullMark.set(ful);
    }

    public String getPassMark() {
        return passMark.get();
    }

    public void setPassMark(String pas) {
        this.passMark.set(pas);
    }

    public String getTheoryMark() {
        return theoryMark.get();
    }

    public void setTheoryMark(String theo) {
        this.theoryMark.set(theo);
    }

    public String getPracticalMark() {
        return practicalMark.get();
    }

    public void setPracticalMark(String prac) {
        this.practicalMark.set(prac);
    }

}
