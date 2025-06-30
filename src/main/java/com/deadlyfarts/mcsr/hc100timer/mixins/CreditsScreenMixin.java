package com.deadlyfarts.mcsr.hc100timer.mixins;

import com.deadlyfarts.mcsr.hc100timer.HC100;
import com.deadlyfarts.mcsr.hc100timer.HC100Timer;
import com.redlimerl.speedrunigt.timer.InGameTimer;
import com.redlimerl.speedrunigt.timer.TimerStatus;
import net.minecraft.client.gui.screen.CreditsScreen;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(CreditsScreen.class)
public class CreditsScreenMixin {
    @Inject(method = "init()V", at = @At("TAIL"))
    private void initMixin(CallbackInfo ci) throws IOException {
        @NotNull
        InGameTimer timer = InGameTimer.getInstance();
        if (timer.getStatus() != TimerStatus.NONE) {
            if (timer.getCategory() == HC100.HC_100_Category && timer.isPlaying()) {
                InGameTimer.complete();
                HC100Timer.onComplete(InGameTimer.getInstance());
            }
        }
    }
}
