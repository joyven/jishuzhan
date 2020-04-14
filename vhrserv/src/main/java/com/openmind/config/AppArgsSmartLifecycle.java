package com.openmind.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

/**
 * AppArgsSmartLifecycle
 *
 * @author zhoujunwen
 * @date 2020-02-24
 * @time 14:51
 * @desc
 */
@Slf4j
@Component
public class AppArgsSmartLifecycle implements SmartLifecycle {

    private boolean isRunning = false;
    private final ApplicationArguments applicationArguments;

    public AppArgsSmartLifecycle(ApplicationArguments applicationArguments) {
        this.applicationArguments = applicationArguments;
    }

    @Override
    public void start() {
        log.info("--------------------AppArgsSmartLifecycle--Start()------------------------------");
        if (applicationArguments != null) {
            log.info(applicationArguments.getNonOptionArgs().spliterator().toString());
        }
        isRunning = true;
    }

    @Override
    public void stop() {
        isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        callback.run();
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }
}
