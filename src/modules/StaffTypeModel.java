package modules;

import javafx.beans.property.SimpleStringProperty;

public class StaffTypeModel {

    private SimpleStringProperty sno;
    private SimpleStringProperty type;
    private SimpleStringProperty shift;
    private SimpleStringProperty time;

    public StaffTypeModel(String sn, String type, String shift, String time) {
        this.sno = new SimpleStringProperty(sn);
        this.type = new SimpleStringProperty(type);
        this.shift = new SimpleStringProperty(shift);
        this.time = new SimpleStringProperty(time);
    }

    public String getSno() {
        return sno.get();
    }

    public void setSno(String sn) {
        this.sno.set(sn);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getShift() {
        return shift.get();
    }

    public void setShift(String shift) {
        this.shift.set(shift);
    }

    public String getTime() {
        return time.get();
    }

    public void setTime(String time) {
        this.time.set(time);
    }

}
