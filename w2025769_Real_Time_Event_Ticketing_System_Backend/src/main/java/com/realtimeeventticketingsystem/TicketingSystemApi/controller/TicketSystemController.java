package com.realtimeeventticketingsystem.TicketingSystemApi.controller;

import com.realtimeeventticketingsystem.TicketingSystemApi.logger.LoggerService;
import com.realtimeeventticketingsystem.TicketingSystemApi.service.TicketSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ticket-system")
public class TicketSystemController {

    @Autowired
    private TicketSystemService ticketSystemService;

    // Endpoint to start the system
    @PostMapping("/system/start")
    public boolean startSystem() {
        return ticketSystemService.startSystem();
    }

    // Endpoint to stop the system
    @PostMapping("/system/stop")
    public Boolean stopSystem() {
        return ticketSystemService.stopSystem();
    }

    // Retrieve the last 100 logs stored in memory
    @GetMapping("/recent")
    public List<String> getRecentLogs() {
        return LoggerService.getRecentLogs();  // Retrieve and return the last 100 logs
    }

    // Endpoint to load the configuration
    @GetMapping("/config/get")
    public String getConfiguration() {
        return ticketSystemService.getConfiguration();
    }

    // Endpoint to set a new configuration
    @PostMapping("/config/set")
    public boolean setConfiguration(@RequestBody String configData) {
        return ticketSystemService.setConfiguration(configData);
    }

}
