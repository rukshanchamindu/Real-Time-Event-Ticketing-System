package com.realtimeeventticketingsystem.TicketingSystemApi.controller;

import com.realtimeeventticketingsystem.TicketingSystemApi.logger.LoggerService;
import com.realtimeeventticketingsystem.TicketingSystemApi.service.TicketSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ticket-system")
@CrossOrigin(origins = "http://localhost:3000")  // Allow CORS for this controller from localhost:3000
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
    public String stopSystem() {
        return ticketSystemService.stopSystem();
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

    @GetMapping("/system/getremaingticket")
    public String getremaingTicket() {
        return ticketSystemService.getremaingTicket();
    }

}
