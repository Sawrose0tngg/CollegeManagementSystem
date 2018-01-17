package modules;

import javafx.beans.property.SimpleStringProperty;

public class StudentModel {

    private final SimpleStringProperty sno;
    private final SimpleStringProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty course;
    private final SimpleStringProperty semester;
    private final SimpleStringProperty email;
    private final SimpleStringProperty address;
    private final SimpleStringProperty mobile;
    private final SimpleStringProperty admission;
    private final SimpleStringProperty gender;

    public StudentModel(String sn, String id, String na, String co, String se, String em, String add, String mo, String adm, String ge) {

        this.sno = new SimpleStringProperty(sn);
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(na);
        this.course = new SimpleStringProperty(co);
        this.semester = new SimpleStringProperty(se);
        this.email = new SimpleStringProperty(em);
        this.address = new SimpleStringProperty(add);
        this.mobile = new SimpleStringProperty(mo);
        this.admission = new SimpleStringProperty(adm);
        this.gender = new SimpleStringProperty(ge);

    }

    public String getSno() {
        return sno.get();
    }

    public void setSno(String sn) {
        this.sno.set(sn);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String na) {
        this.name.set(na);
    }

    public String getCourse() {
        return course.get();
    }

    public void setCourse(String co) {
        this.course.set(co);
    }

    public String getSemester() {
        return semester.get();
    }

    public void setSemester(String se) {
        this.semester.set(se);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String em) {
        this.email.set(em);
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String add) {
        this.address.set(add);
    }

    public String getMobile() {
        return mobile.get();
    }

    public void setMobile(String mo) {
        this.mobile.set(mo);
    }

    public String getAdmission() {
        return admission.get();
    }

    public void setAdmission(String adm) {
        this.admission.set(adm);
    }

    public String getGender() {
        return gender.get();
    }

    public void setGender(String ge) {
        this.gender.set(ge);
    }

}
