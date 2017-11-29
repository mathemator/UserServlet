/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.agoppe;

import ru.agoppe.data.Extra;
import ru.agoppe.data.RequestType;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Любовь
 */
@XmlRootElement(name = "request")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceRequest {

    @XmlElement(name = "request-type", required = true)
    private RequestType requestType = RequestType.GET_BALANCE;

    @XmlElement(name = "extra", required = true)
    private ArrayList<Extra> extra;

    public String getLogin() {

        for (Extra e : extra) {
            if (e.getName().equals("login")) {
                return e.getValue();
            }
        }
        return null;
    }

    public String getPassword() {
        for (Extra e : extra) {
            if (e.getName().equals("password")) {
                return e.getValue();
            }
        }
        return null;
    }

    /**
     * @return the requestType
     */
    public RequestType getRequestType() {
        return requestType;
    }

    /**
     * @param requestType the requestType to set
     */
    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    /**
     * @return the extra
     */
    public ArrayList<Extra> getExtra() {
        return extra;
    }

    /**
     * @param extra the extra to set
     */
    public void setExtra(ArrayList<Extra> extra) {
        this.extra = extra;
    }

}
