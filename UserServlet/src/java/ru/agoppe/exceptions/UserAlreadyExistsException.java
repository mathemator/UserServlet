/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.agoppe.exceptions;

/**
 *
 * @author Любовь
 */
public class UserAlreadyExistsException extends Exception {
    
    public UserAlreadyExistsException(String message) {
        super("User with such login is already exist");
    }
    
}
