/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.agoppe.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 *
 * @author Любовь
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlEnum(String.class)
public enum RequestType {
    @XmlEnumValue("GET-BALANCE")
    GET_BALANCE("GET-BALANCE"),
    @XmlEnumValue("CREATE-AGT")
    CREATE_AGT("CREATE-AGT");

    RequestType(String string) {
        this.id = string;
    }
    private final String id;

    public String getValue() {
        return id;
    }
}
