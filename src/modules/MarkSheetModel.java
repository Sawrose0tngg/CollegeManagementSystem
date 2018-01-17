package modules;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Sawrose Tamang
 */
public class MarkSheetModel {

    private final SimpleStringProperty sn;
    private final SimpleStringProperty name;
    private final SimpleStringProperty prTotal;
    private final SimpleStringProperty prObtain;
    private final SimpleStringProperty thTotal;
    private final SimpleStringProperty thObtain;
    private final SimpleStringProperty gpa;
    private final SimpleStringProperty grade;
    private final SimpleStringProperty total;
    private final SimpleStringProperty remark;

    public MarkSheetModel(String sno, String na, String pT, String pO, String tT, String tO, String gp, String gr, String tot, String rem) {

        this.sn = new SimpleStringProperty(sno);
        this.name = new SimpleStringProperty(na);
        this.prTotal = new SimpleStringProperty(pT);
        this.prObtain = new SimpleStringProperty(pO);
        this.thTotal = new SimpleStringProperty(tT);
        this.thObtain = new SimpleStringProperty(tO);
        this.gpa = new SimpleStringProperty(gp);
        this.grade = new SimpleStringProperty(gr);
        this.total = new SimpleStringProperty(tot);
        this.remark = new SimpleStringProperty(rem);
    }

    public String getSn() {
        return sn.get();
    }

    public void setSn(String sno) {
        this.sn.set(sno);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String na) {
        this.name.set(na);
    }

    public String getPrTotal() {
        return prTotal.get();
    }

    public void setPrTotal(String pT) {
        this.prTotal.set(pT);
    }

    public String getPrObtain() {
        return prObtain.get();
    }

    public void setPrObtain(String pO) {
        this.prObtain.set(pO);
    }

    public String getThTotal() {
        return thTotal.get();
    }

    public void setThTotal(String tT) {
        this.thTotal.set(tT);
    }

    public String getThObtain() {
        return thObtain.get();
    }

    public void setThObtain(String tO) {
        this.thObtain.set(tO);
    }

    public String getGpa() {
        return gpa.get();
    }

    public void setGpa(String gp) {
        this.gpa.set(gp);
    }

    public String getGrade() {
        return grade.get();
    }

    public void setGrade(String gr) {
        this.grade.set(gr);
    }

    public String getTotal() {
        return total.get();
    }

    public void setTotal(String tot) {
        this.total.set(tot);
    }

    public String getRemark() {
        return remark.get();
    }

    public void setRemark(String rem) {
        this.remark.set(rem);
    }

}
