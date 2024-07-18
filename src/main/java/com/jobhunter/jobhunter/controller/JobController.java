package com.jobhunter.jobhunter.controller;

import com.jobhunter.jobhunter.entity.Job;
import com.jobhunter.jobhunter.service.JobService;
import com.turkraft.springfilter.boot.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/job")
public class JobController {

    private final JobService jobService;

    @PostMapping("/save")
    public ResponseEntity<?> saveJob(@RequestBody Job job) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.save(job));
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllJobs(@Filter Specification<Job> spec, Pageable pageable) {
        return ResponseEntity.ok().body(jobService.findAll(spec, pageable));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateJob(@RequestBody Job job) {
        return ResponseEntity.ok().body(jobService.update(job));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok().body(jobService.findById(id));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteJobById(@PathVariable Long id) {
        jobService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
