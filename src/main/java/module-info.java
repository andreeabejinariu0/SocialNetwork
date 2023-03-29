module com.example.labul4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.labul4 to javafx.fxml;
    exports com.example.labul4;
    exports com.example.labul4.controller;
    exports com.example.labul4.domain;
    exports com.example.labul4.domain.validators;
    exports com.example.labul4.repository;
    exports com.example.labul4.service;
    exports com.example.labul4.utils.observer;
    exports com.example.labul4.utils;
}