package com.deadlyfarts.mcsr.hc100timer;

import com.redlimerl.speedrunigt.timer.category.RunCategory;
import net.fabricmc.api.ClientModInitializer;

public class HC100 implements ClientModInitializer {
    public static final RunCategory HC_100_Category = new RunCategory("hc_100", "https://www.youtube.com/playlist?list=PL8O_UbX34o28vaMD06OEA6ZmVoaCTCI8Z", "100 Hardcore");

    @Override
    public void onInitializeClient() {

    }
}
