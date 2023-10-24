package com.example.fitnessapp2;

public class Exercise {
    private String name, type, muscle, equipment, difficulty, instructions;

    public Exercise(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
    public String getMuscle() {
        return muscle;
    }
    public String getEquipment() {
        return equipment;
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
}