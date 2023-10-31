package com.example.fitnessapp2;

import java.util.HashMap;
import java.util.Map;

public class ExerciseVideoUtility {

    private static final Map<String, String> exerciseToVideoMap;

    static {
        exerciseToVideoMap = new HashMap<>();
        exerciseToVideoMap.put("barbell_curl", "barbell_curl");
        exerciseToVideoMap.put("barbell_deadlift", "barbell_deadlift");
        exerciseToVideoMap.put("barbell_deficit_deadlift", "barbell_deficit_deadlift");
        exerciseToVideoMap.put("barbell_glute_bridge", "barbell_glute_bridge");
        exerciseToVideoMap.put("barbell_hip_thrust", "barbell_hip_thrust");
        exerciseToVideoMap.put("cable_v_bar_push_down", "cable_v_bar_push_down");
        exerciseToVideoMap.put("close_grip_bench_press", "close_grip_bench_press");
        exerciseToVideoMap.put("close_grip_pull_down", "close_grip_pull_down");
        exerciseToVideoMap.put("concentration_curl", "concentration_curl");
        exerciseToVideoMap.put("deadlift_with_bands", "deadlift_with_bands");
        exerciseToVideoMap.put("dumbbell_bench_press", "dumbbell_bench_press");
        exerciseToVideoMap.put("dumbbell_floor_press", "dumbbell_floor_press");
        exerciseToVideoMap.put("dumbbell_flyes", "dumbbell_flyes");
        exerciseToVideoMap.put("glute_bridge", "glute_bridge");
        exerciseToVideoMap.put("hammer_curls", "hammer_curls");
        exerciseToVideoMap.put("incline_hammer_curls", "incline_hammer_curls");
        exerciseToVideoMap.put("one_arm_dumbbell_row", "one_arm_dumbbell_row");
        exerciseToVideoMap.put("pullups", "pullups");
        exerciseToVideoMap.put("pushups", "pushups");
        exerciseToVideoMap.put("reverse_grip_bent_over_row", "reverse_grip_bent_over_row");
        exerciseToVideoMap.put("romanian_deadlift_with_dumbbells", "romanian_deadlift_with_dumbbells");
        exerciseToVideoMap.put("single_leg_cable_hip_extension", "single_leg_cable_hip_extension");
        exerciseToVideoMap.put("sumo_deadlift", "sumo_deadlift");
        exerciseToVideoMap.put("triceps_dip", "triceps_dip");
    }

    public static String getVideoFileNameForExercise(String exerciseName) {
        String formattedExerciseName = formatExerciseName(exerciseName);
        if (exerciseToVideoMap.containsKey(formattedExerciseName)) {
            return exerciseToVideoMap.get(formattedExerciseName);
        } else {
            return null;
        }
    }

    private static String formatExerciseName(String exerciseName) {
        return exerciseName.trim().toLowerCase().replace(" ", "_").replace("-", "_").replace(".", "");
    }

    public static String toTitleCase(String stringOfName) {
        if (stringOfName == null || stringOfName.isEmpty()) {
            return stringOfName;
        }
        StringBuilder sb = new StringBuilder(stringOfName.length());
        boolean capitalizeNext = true;
        for (char c : stringOfName.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
                sb.append(c);
            } else if (capitalizeNext) {
                sb.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }

}
