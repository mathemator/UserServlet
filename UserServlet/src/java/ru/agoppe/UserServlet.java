package ru.agoppe;

import ru.agoppe.data.Extra;
import ru.agoppe.database.DatabaseHandler;
import static ru.agoppe.data.RequestType.CREATE_AGT;
import static ru.agoppe.data.RequestType.GET_BALANCE;
import ru.agoppe.exceptions.UserAlreadyExistsException;
import ru.agoppe.exceptions.UserNotExistException;
import ru.agoppe.exceptions.WrongPasswordException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

public class UserServlet extends HttpServlet {

    JAXBContext requestContext;
    JAXBContext responseContext;
    Unmarshaller requestUnmarshaller;
    Marshaller responseMarshaller;

    @Override
    public void init() throws ServletException {
        super.init();

        try {
            Class.forName("org.postgresql.Driver");
            requestContext = JAXBContext.newInstance(ServiceRequest.class);
            responseContext = JAXBContext.newInstance(ServiceResponse.class);
            requestUnmarshaller = requestContext.createUnmarshaller();
            responseMarshaller = responseContext.createMarshaller();

            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            URL schemaRequestURL = UserServlet.class.getResource("schemaRequest.xsd");
            Schema schemaRequest = schemaFactory.newSchema(schemaRequestURL);
            requestUnmarshaller.setSchema(schemaRequest);
            requestUnmarshaller.setEventHandler(companyErrorHandler);

            URL schemaResponseURL = UserServlet.class.getResource("schemaResponse.xsd");
            Schema schemaResponse = schemaFactory.newSchema(schemaResponseURL);
            responseMarshaller.setSchema(schemaResponse);
            responseMarshaller.setEventHandler(companyErrorHandler);

        } catch (JAXBException | SAXException | ClassNotFoundException ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        // Set response content type
        response.setContentType("text/html");

        // Actual logic goes here.
        PrintWriter out = response.getWriter();
        out.println("Hurray !!\n Servlet is Working!");
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        ServiceResponse serviceResponse = new ServiceResponse();
        try {
            ServiceRequest serviceRequest = (ServiceRequest) requestUnmarshaller.unmarshal(request.getInputStream());

            String code = "";
            double balance = 0.0;

            switch (serviceRequest.getRequestType()) {
                case CREATE_AGT: {
                    try {
                        DatabaseHandler.createAgent(serviceRequest);
                        code = "0";
                    } catch (UserAlreadyExistsException ex) {
                        Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                        code = "1";
                    } catch (SQLException ex) {
                        Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                        code = "2";
                    }
                }
                break;
                case GET_BALANCE: {
                    try {
                        balance = DatabaseHandler.getBalance(serviceRequest);
                        code = "0";
                    } catch (WrongPasswordException ex) {
                        Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                        code = "4";
                    } catch (UserNotExistException ex) {
                        Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                        code = "3";
                    } catch (SQLException ex) {
                        Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                        code = "2";
                    }
                }
                break;
                default:
                    throw new AssertionError(serviceRequest.getRequestType().name());
            }

            if (serviceRequest.getRequestType().equals(GET_BALANCE) && code.equals("0")) {
                serviceResponse.setExtra(new Extra("balance", String.valueOf(balance)));
            }
            serviceResponse.setCode(code);
            responseMarshaller.marshal(serviceResponse, response.getWriter());

        } catch (JAXBException ex) {
            try {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                serviceResponse.setCode("2");
                responseMarshaller.marshal(serviceResponse, response.getWriter());
            } catch (JAXBException ex1) {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex1);
                throw new ServletException(ex1);
            }
        }

    }

    @Override
    public void destroy() {
        // resource release
        super.destroy();
    }

    private static ValidationEventHandler companyErrorHandler = new ValidationEventHandler() {
        @Override
        public boolean handleEvent(ValidationEvent event) {
            System.err.println(String.format("ERROR: %s (%d, %d) Severity: %s", event.getMessage(),
                    event.getLocator().getLineNumber(),
                    event.getLocator().getColumnNumber(),
                    event.getSeverity()));
            return false;
        }
    };
}
