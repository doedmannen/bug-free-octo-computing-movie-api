package com.movienights.api.controllers;

import com.movienights.api.entities.Log;
import com.movienights.api.repos.LogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/log")
public class LogController {
    @Autowired
    LogRepo logRepo;

    @GetMapping()
    ResponseEntity<List<Log>> getLog() {
        List<Log> log = logRepo.findAll();
        if (log == null) {
            return ResponseEntity.notFound().build();
        }
        List<Log> logs = logRepo.findAll();
        if (logs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(logRepo.findAll());
    }

}
