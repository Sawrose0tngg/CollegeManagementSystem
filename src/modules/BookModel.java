package modules;

import javafx.beans.property.SimpleStringProperty;

public class BookModel {

    private final SimpleStringProperty sno;
    private final SimpleStringProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty course;
    private final SimpleStringProperty semester;
    private final SimpleStringProperty author;
    private final SimpleStringProperty publisher;
    private final SimpleStringProperty number;

    public BookModel(String sn, String id, String name, String course, String semester, String author, String publisher, String number) {
        this.sno = new SimpleStringProperty(sn);
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.course = new SimpleStringProperty(course);
        this.semester = new SimpleStringProperty(semester);
        this.author = new SimpleStringProperty(author);
        this.publisher = new SimpleStringProperty(publisher);
        this.number = new SimpleStringProperty(number);
    }
    public BookModel(String sn, String id, String name, String course, String semester, String author, String publisher) {
        this.sno = new SimpleStringProperty(sn);
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.course = new SimpleStringProperty(course);
        this.semester = new SimpleStringProperty(semester);
        this.author = new SimpleStringProperty(author);
        this.publisher = new SimpleStringProperty(publisher);
        this.number = null;
    }

    public String getSno() {
        return sno.get();
    }

    public void setSno(String sno) {
        this.sno.set(sno);
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

    public void setName(String name) {
        this.name.set(name);
    }

    public String getCourse() {
        return course.get();
    }

    public void setCourse(String course) {
        this.course.set(course);
    }

    public String getSemester() {
        return semester.get();
    }

    public void setSemester(String semester) {
        this.semester.set(semester);
    }

    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public String getPublisher() {
        return publisher.get();
    }

    public void setPublisher(String publisher) {
        this.publisher.set(publisher);
    }

    public String getNumber() {
        return number.get();
    }

    public void setNumber(String number) {
        this.number.set(number);
    }
}
