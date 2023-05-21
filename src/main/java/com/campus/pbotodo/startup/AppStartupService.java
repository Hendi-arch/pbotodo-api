package com.campus.pbotodo.startup;

import org.springframework.boot.context.event.ApplicationReadyEvent;

public interface AppStartupService {
    
    void handleStartup(ApplicationReadyEvent event);

}
