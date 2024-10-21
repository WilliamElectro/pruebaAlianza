package com.wbohorquez.pruebaAlianza.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author WI-LL
 */
@Getter
@Setter
public class Client {

    private String sharedKey;
    private String businessId;
    private String email;
    private String phone;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateAdded;

    // Getters y Setters
//    public String getSharedKey() {
//        return sharedKey;
//    }
//
//    public void setSharedKey(String sharedKey) {
//        this.sharedKey = sharedKey;
//    }
//
//    public String getBusinessId() {
//        return businessId;
//    }
//
//    public void setBusinessId(String businessId) {
//        this.businessId = businessId;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Date getDateAdded() {
//        return dateAdded;
//    }
//
//    public void setDateAdded(Date dateAdded) {
//        this.dateAdded = dateAdded;
//    }
}
