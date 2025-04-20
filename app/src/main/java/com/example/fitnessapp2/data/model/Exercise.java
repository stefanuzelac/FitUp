package com.example.fitnessapp2.data.model;

public class Exercise {
    private String name;
    private String muscle;
    private String equipment;
    private String difficulty;
    private String instructions;
    private String mediaPath;

    public Exercise(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getMuscle() {
        return muscle;
    }
    public void setMuscle(String muscle) {
        this.muscle = muscle;
    }

    public String getEquipment() {
        return equipment;
    }
    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getInstructions() {
        return instructions;
    }
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getMediaPath() {
        return mediaPath;
    }
    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }
}
