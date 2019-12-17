package com.movienights.api.controllers;

import com.movienights.api.entities.Log;
import com.movienights.api.repos.LogRepo;
import com.movienights.api.services.LogServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/log")
public class LogController {
    @Autowired
    LogRepo logRepo;

    @Autowired
    LogServices logServices;

    @GetMapping()
    ResponseEntity<List<Log>> getLog(@RequestParam("page") int page) {
        Pageable ppp = PageRequest.of(page, 50, Sort.Direction.DESC, "when");
        Page logsPage = logRepo.findAll(ppp);
        var logs = logsPage.toList();
        if (logs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(logs);
    }



}
