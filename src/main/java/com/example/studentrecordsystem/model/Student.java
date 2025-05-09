package com.example.studentrecordsystem.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Student {

    private final StringProperty studentId;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty email;
    private final StringProperty major;

    // Constructor for creating a new student before adding to DB
    public Student(String studentId, String firstName, String lastName, String email, String major) {
        this.studentId = new SimpleStringProperty(studentId);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.email = new SimpleStringProperty(email);
        this.major = new SimpleStringProperty(major);
    }

    // Constructor potentially used when loading from DB (can use the above too)
    public Student() {
        this(null, null, null, null, null); // Initialize with nulls
    }


    // --- Property Getters ---

    public StringProperty studentIdProperty() {
        return studentId;
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty majorProperty() {
        return major;
    }

    // --- Standard Getters ---

    public String getStudentId() {
        return studentId.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getMajor() {
        return major.get();
    }

    // --- Standard Setters ---

    public void setStudentId(String studentId) {
        this.studentId.set(studentId);
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setMajor(String major) {
        this.major.set(major);
    }

    @Override
    public String toString() { // Useful for debugging
        return "Student{" +
                "studentId=" + getStudentId() +
                ", firstName=" + getFirstName() +
                ", lastName=" + getLastName() +
                '}';
    }
}
