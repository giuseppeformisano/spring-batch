package com.gf.springbatch.controllers;

import com.gf.springbatch.services.JobService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    JobService jobService;

    @GetMapping("/startJob")
    public ResponseEntity<?> startJob(
//            @RequestParam("dataRif") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataRif,
            @RequestParam("errorStep2") Boolean errorStep2
    ) {
        try {
            jobService.runJob("job", errorStep2);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
