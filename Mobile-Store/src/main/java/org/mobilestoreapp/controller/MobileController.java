package org.mobilestoreapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mobilestoreapp.exception.ResourceNotFoundException;
import org.mobilestoreapp.model.Mobile;
import org.mobilestoreapp.service.MobileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/mobiles")
public class MobileController {

    private static final Logger logger = LoggerFactory.getLogger(MobileController.class);
    private final MobileService mobileService;

    // POST: Add multiple mobiles
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<List<Mobile>> addMobiles(@RequestBody @Valid List<Mobile> mobiles) {
        logger.info("Received request to add {} mobiles", mobiles.size());
        return ResponseEntity.status(HttpStatus.CREATED).body(mobileService.addMobiles(mobiles));
    }

    // GET: Fetch all mobiles with pagination
    @GetMapping
    public ResponseEntity<Page<Mobile>> getAllMobiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {

        Sort sort = direction.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(mobileService.getAllMobiles(pageable));
    }

    // GET: Fetch a single mobile by ID
    @GetMapping("/{id}")
    public ResponseEntity<Mobile> getMobileById(@PathVariable Long id) {
        return ResponseEntity.ok(mobileService.getMobileById(id)); //  Throws ResourceNotFoundException if not found
    }

    // DELETE: Remove a mobile by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMobile(@PathVariable Long id) {
        mobileService.deleteMobile(id);
        return ResponseEntity.noContent().build(); //  204 No Content
    }
}