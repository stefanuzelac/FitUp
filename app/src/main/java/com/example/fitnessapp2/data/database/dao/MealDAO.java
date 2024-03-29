package com.example.fitnessapp2.data.database.dao;

import com.example.fitnessapp2.data.model.MealLog;

import java.util.List;

public interface MealDAO {
    boolean insertMealLog(MealLog log);
    List<MealLog> getMealLogsByUserId(int userId);
    List<MealLog> getMealLogsByUserIdAndDate(int userId, String date);
    boolean updateMealLog(MealLog log);
    void deleteMealLog(int id);
}