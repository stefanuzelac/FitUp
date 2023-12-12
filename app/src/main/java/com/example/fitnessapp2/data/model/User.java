package com.example.fitnessapp2.data.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class User {
    private int id, height;
    private double weight;
    private String name, lastName, email, password, phone, gender, dob;
    private byte[] profilePicture;

    public User(int id, String name, String lastName, String email, String password, String phone, String gender, String dob, int height, double weight, byte[] profilePicture) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
        this.height = height;
        this.weight = weight;
        this.profilePicture = profilePicture;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getDob() {
        return dob;
    }

    public int getAge() {
        if (this.dob == null || this.dob.isEmpty()) {
            return 0; // Or handle error appropriately
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date birthDate = sdf.parse(this.dob);
            Calendar birthDay = Calendar.getInstance();
            birthDay.setTime(birthDate);

            Calendar today = Calendar.getInstance();
            int age = today.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_YEAR) < birthDay.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }

            return age;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // Or handle error appropriately
        }
    }

    public String getGender() {
        return gender;
    }

    public int getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", dob='" + dob + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                // You can decide if you want to print the 'password' field
                // Also, you might not want to include the 'profilePicture' field as it is binary data
                '}';
    }
}