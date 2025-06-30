package com.deadlyfarts.mcsr.hc100timer;

import com.redlimerl.speedrunigt.api.SpeedRunIGTApi;
import com.redlimerl.speedrunigt.timer.category.RunCategory;

public class HC100Impl implements SpeedRunIGTApi {
    @Override
    public RunCategory registerCategory() {
        return HC100.HC_100_Category;
    }
}