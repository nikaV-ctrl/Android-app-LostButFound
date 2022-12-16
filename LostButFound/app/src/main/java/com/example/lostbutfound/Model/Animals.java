package com.example.lostbutfound.Model;

public class Animals {

    public String id, title, imageUrl, city, circumstances,
            description, breed, lastLocation,
            status, reward, currentTime, user, phone;
    public Integer year, month, day;

    public Animals(){

    }

    public Animals(String id, String title, String imageUrl, String city,
                   String circumstances, String description,
                   String breed, String lastLocation, String status,
                   String reward, String currentTime, String user,
                   String phone, Integer year, Integer month, Integer day) {

        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.city = city;
        this.circumstances = circumstances;
        this.description = description;
        this.breed = breed;
        this.lastLocation = lastLocation;
        this.status = status;
        this.reward = reward;
        this.currentTime = currentTime;
        this.user = user;
        this.phone = phone;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCircumstances() {
        return circumstances;
    }

    public void setCircumstances(String circumstances) {
        this.circumstances = circumstances;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }
}
