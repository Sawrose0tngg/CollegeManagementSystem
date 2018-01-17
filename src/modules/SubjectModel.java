package modules;

import javafx.beans.property.SimpleStringProperty;

public class SubjectModel {

    private final SimpleStringProperty subject1;
    private final SimpleStringProperty subject2;
    private final SimpleStringProperty subject3;
    private final SimpleStringProperty subject4;
    private final SimpleStringProperty subject5;
    private final SimpleStringProperty subject6;

    public SubjectModel(String sub1, String sub2, String sub3, String sub4, String sub5, String sub6) {
        this.subject1 = new SimpleStringProperty(sub1);
        this.subject2 = new SimpleStringProperty(sub2);
        this.subject3 = new SimpleStringProperty(sub3);
        this.subject4 = new SimpleStringProperty(sub4);
        this.subject5 = new SimpleStringProperty(sub5);
        this.subject6 = new SimpleStringProperty(sub6);
    }
    public SubjectModel(String sub1, String sub2, String sub3, String sub4) {
        this.subject1 = new SimpleStringProperty(sub1);
        this.subject2 = new SimpleStringProperty(sub2);
        this.subject3 = new SimpleStringProperty(sub3);
        this.subject4 = new SimpleStringProperty(sub4);
        this.subject5 = null;
        this.subject6 = null;
    }

    public String getSubject1() {
        return subject1.get();
    }

    public void setSubject1(String sub1) {
        this.subject1.set(sub1);
    }

    public String getSubject2() {
        return subject2.get();
    }

    public void setSubject2(String sub2) {
        this.subject2.set(sub2);
    }

    public String getSubject3() {
        return subject3.get();
    }

    public void setSubject3(String sub3) {
        this.subject3.set(sub3);
    }

    public String getSubject4() {
        return subject4.get();
    }

    public void setSubject4(String sub4) {
        this.subject4.set(sub4);
    }

    public String getSubject5() {
        return subject5.get();
    }

    public void setSubject5(String sub5) {
        this.subject5.set(sub5);
    }

    public String getSubject6() {
        return subject6.get();
    }

    public void setSubject6(String sub6) {
        this.subject6.set(sub6);
    }

}
