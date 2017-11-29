/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.agoppe;

import ru.agoppe.data.Extra;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Любовь
 */
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceResponse {
    
    @XmlElement(name = "result-code", required = true)
    private String code;

    @XmlElement(name = "extra", required = false)
    private Extra extra;
    
    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the extra
     */
    public Extra getExtra() {
        return extra;
    }

    /**
     * @param extra the extra to set
     */
    public void setExtra(Extra extra) {
        this.extra = extra;
    }
    
    
}
