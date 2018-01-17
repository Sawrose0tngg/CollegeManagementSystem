package modules;

import javafx.beans.property.SimpleStringProperty;

public class IssueModel {

    private final SimpleStringProperty sno;
    private final SimpleStringProperty mid;
    private final SimpleStringProperty bid;
    private final SimpleStringProperty mname;
    private final SimpleStringProperty bname;
    private final SimpleStringProperty course;
    private final SimpleStringProperty semester;
    private SimpleStringProperty date;

    public IssueModel(String sn, String mi, String bi, String mn, String bn, String c, String s, String a) {
        this.sno = new SimpleStringProperty(sn);
        this.mid = new SimpleStringProperty(mi);
        this.bid = new SimpleStringProperty(bi);
        this.mname = new SimpleStringProperty(mn);
        this.bname = new SimpleStringProperty(bn);
        this.course = new SimpleStringProperty(c);
        this.semester = new SimpleStringProperty(s);
        this.date = new SimpleStringProperty(a);
        
    }

    public String getSno() {
        return sno.get();
    }

    public void setSno(String sn) {
        this.sno.set(sn);
    }

   

    public String getCourse() {
        return course.get();
    }

    public void setCourse(String c) {
        this.course.set(c);
    }

    public String getSemester() {
        return semester.get();
    }

    public void setSemester(String s) {
        this.semester.set(s);
    }

    public String getMid() {
        return mid.get();
    }

    public void setMid(String mi) {
        this.mid.set(mi);
    }

    public String getBid() {
        return bid.get();
    }

    public void setBid(String bi) {
        this.bid.set(bi);
    }

    public String getMname() {
        return mname.get();
    }

    public void setMname(String mn) {
        this.mname.set(mn);
    }

    public String getBname() {
        return bname.get();
    }

    public void setBname(String bn) {
        this.bname.set(bn);
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String a) {
        this.date.set(a);
    }

}
