package com.deadlyfarts.mcsr.hc100timer.mixins;

import com.deadlyfarts.mcsr.hc100timer.HC100;
import com.deadlyfarts.mcsr.hc100timer.HC100Timer;
import com.redlimerl.speedrunigt.timer.InGameTimer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(method = "onDeath", at = @At("TAIL"))
    public void onDeath(DamageSource source, CallbackInfo ci) throws IOException {
        InGameTimer timer = InGameTimer.getInstance();
        if (timer.getCategory() == HC100.HC_100_Category && timer.isPlaying()) {
            HC100Timer.onDeath(InGameTimer.getInstance());
        }
    }
}
