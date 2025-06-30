package com.deadlyfarts.mcsr.hc100timer;

import com.redlimerl.speedrunigt.timer.InGameTimer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.commons.lang3.SystemUtils;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;

public class HC100Timer implements ClientModInitializer {
    public static Path currentStreakPath;
    public static Path allRunsPath;
    public static Path overlayPath;
    public static KeyBinding resetBinding;
    public static TimeData timeData;

    public static void onComplete(InGameTimer timer) throws IOException {
        Instant startTime = Instant.now().minusMillis(timer.getRealTimeAttack());
        timeData.saveCompletedRun(currentStreakPath,
                new TimeData.HC100Run(startTime, Instant.now(), timer.getInGameTime()),
                allRunsPath);
        timeData.updateOverlay(overlayPath);
    }

    public static void onDeath(InGameTimer timer) throws IOException {
        Instant startTime = Instant.now().minusMillis(timer.getRealTimeAttack());
        timeData.deleteCurrentStreak(currentStreakPath);
        timeData.updateOverlay(overlayPath);
        timeData.saveNoneCompletedRun(allRunsPath, new TimeData.HC100Run(startTime, Instant.now(), timer.getInGameTime()));
    }


    @Override
    public void onInitializeClient() {
        System.out.println("INITIALIZE HC100 TIMER");
        currentStreakPath = Paths.get(SystemUtils.getUserHome().getPath().concat("/speedrunigt/HC100/current_streak.json"));
        allRunsPath = Paths.get(SystemUtils.getUserHome().getPath().concat("/speedrunigt/HC100/all_runs.txt"));
        overlayPath = Paths.get(SystemUtils.getUserHome().getPath().concat("/speedrunigt/HC100/overlay.txt"));

        try {
            if (!Files.exists(currentStreakPath)) {
                Files.createDirectories(currentStreakPath.getParent());
                Files.write(currentStreakPath, "[]".getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
            }
            if (!Files.exists(allRunsPath)) {
                Files.createDirectories(allRunsPath.getParent());
                Files.write(allRunsPath, "".getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
            }
            if (!Files.exists(overlayPath)) {
                Files.createDirectories(overlayPath.getParent());
                Files.write(overlayPath, "0\n0:00".getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        timeData = new TimeData();
        timeData.loadCurrentStreak(currentStreakPath);
        timeData.updateOverlay(overlayPath);

        resetBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "hc100-timer.reset",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_P,
                "hc100-timer.category"));
    }
}
