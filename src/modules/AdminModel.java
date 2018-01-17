/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Sawrose Tamang
 */
public class AdminModel {
    
    private final SimpleStringProperty sno;
    private final SimpleStringProperty name;
    private final SimpleStringProperty email;
    private final SimpleStringProperty type;
    private final SimpleStringProperty mobile;
    private final SimpleStringProperty gender;
    
    public AdminModel(String sn, String na, String em, String ty, String mo, String ge) {
        this.sno = new SimpleStringProperty(sn);
        this.name = new SimpleStringProperty(na);
        this.email = new SimpleStringProperty(em);
        this.type = new SimpleStringProperty(ty);
        this.mobile = new SimpleStringProperty(mo);
        this.gender = new SimpleStringProperty(ge);        
    }
    
    public String getSno() {
        return sno.get();
    }
    
    public void setSno(String sno) {
        this.sno.set(sno);
    }
    
    public String getName() {
        return name.get();
    }
    
    public void setName(String name) {
        this.name.set(name);
    }
    
    public String getEmail() {
        return email.get();
    }
    
    public void setEmail(String email) {
        this.email.set(email);
    }
    
    public String getType() {
        return type.get();
    }
    
    public void setType(String type) {
        this.type.set(type);
    }
    
    public String getMobile() {
        return mobile.get();
    }
    
    public void setMobile(String mobile) {
        this.mobile.set(mobile);
    }
    
    public String getGender() {
        return gender.get();
    }
    
    public void setGender(String gender) {
        this.gender.set(gender);
    }
    
}
