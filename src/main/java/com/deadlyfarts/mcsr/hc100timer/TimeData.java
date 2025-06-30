package com.deadlyfarts.mcsr.hc100timer;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;

import com.google.gson.Gson;

public class TimeData {
        public ArrayList<HC100Run> current_streak_runs = new ArrayList<>();

        public void loadCurrentStreak(Path path) {
            current_streak_runs.clear();
            try {
                String json = new String(java.nio.file.Files.readAllBytes(path), StandardCharsets.UTF_8);
                HC100Run[] loadedRuns = new Gson().fromJson(json, HC100Run[].class);
                if (loadedRuns != null) {
                    Collections.addAll(current_streak_runs, loadedRuns);
                }

            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }

        public void saveCompletedRun(Path currentStreakPath, HC100Run run, Path allRunsPath) {
            current_streak_runs.add(run);
            try {
                String json = new Gson().toJson(current_streak_runs);
                java.nio.file.Files.write(currentStreakPath, json.getBytes(StandardCharsets.UTF_8));
                // save time nicely formatted in all runs file
                // hh:mm:ss format
                String formattedTime = String.format("%02d:%02d:%02d.%03d",
                        run.igt / 3600000,
                        (run.igt % 3600000) / 60000,
                        (run.igt % 60000) / 1000,
                        run.igt % 1000);
                String runEntry = String.format("Run Nr. %d: %s\n", current_streak_runs.size(), formattedTime);
                java.nio.file.Files.write(allRunsPath, runEntry.getBytes(StandardCharsets.UTF_8),
                        java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }

        public void saveNoneCompletedRun(Path all, HC100Run run) {
            try {
                String formattedTime = String.format("%02d:%02d:%02d.%03d",
                        run.igt / 3600000,
                        (run.igt % 3600000) / 60000,
                        (run.igt % 60000) / 1000,
                        run.igt % 1000);
                String runEntry = String.format("STREAK ENDED! Died after %d runs and %s\n", current_streak_runs.size() + 1, formattedTime);
                java.nio.file.Files.write(all, runEntry.getBytes(StandardCharsets.UTF_8),
                        java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }

        public void deleteCurrentStreak(Path currentStreakPath) {
            current_streak_runs.clear();
            try {
                java.nio.file.Files.write(currentStreakPath, "[]".getBytes(StandardCharsets.UTF_8));
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }

        public void updateOverlay(Path overlayPath) {
            // Update the overlay file with the current streak and the formatted average time
            long averageTime = (long) current_streak_runs.stream()
                    .mapToLong(r -> r.igt)
                    .average()
                    .orElse(0);

            // the average time is in Milliseconds, format it to hh:mm:ss or mm:ss

String formattedAverageTime = averageTime >= 3600000
        ? String.format("%02d:%02d:%02d",
            averageTime / 3600000,
            (averageTime % 3600000) / 60000,
            (averageTime % 60000) / 1000)
        : String.format("%02d:%02d",
            (averageTime % 3600000) / 60000,
            (averageTime % 60000) / 1000);

            int currentStreak = current_streak_runs.size();

            String overlayContent = String.format("%d\n%s", currentStreak, formattedAverageTime);
            try {
                java.nio.file.Files.write(overlayPath, overlayContent.getBytes(StandardCharsets.UTF_8));
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }

    public static class HC100Run {
        public long startTimeSeconds;
        public long startTimeNanos;
        public long endTimeSeconds;
        public long endTimeNanos;
        public long igt;

        public HC100Run(Instant startTime, Instant completedTime, long igt){
            startTimeSeconds = startTime.getEpochSecond();
            startTimeNanos = startTime.getNano();
            endTimeSeconds = completedTime.getEpochSecond();
            endTimeNanos = completedTime.getNano();
            this.igt = igt;
        }
    }
}


