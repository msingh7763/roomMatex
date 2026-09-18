package com.RoomMateX.scheduler;

import com.RoomMateX.service.MLService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MLTrainer {

    private final MLService mlService;

    @Scheduled(cron="0 0 * * * *") // hourly
    public void retrain(){
        mlService.retrain();
    }
}

