package org.mobilestoreapp.controller;

import org.mobilestoreapp.model.Mobile;
import org.mobilestoreapp.service.MobileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("v1/api/mobiles")
public class MobileController {

    private static final Logger logger = LoggerFactory.getLogger(MobileController.class);

   @Autowired
    private MobileService mobileService;


    // POST: Add multiple mobiles
    @PostMapping
    public ResponseEntity<List<Mobile>> addMobiles(@RequestBody List<Mobile> mobiles) {
        try {
            logger.info("Received request to add {} mobiles", mobiles.size());
            List<Mobile> savedMobiles = mobileService.addMobiles(mobiles);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMobiles);
        } catch (Exception e) {
            logger.error("Error saving mobile: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET: Fetch all mobiles with pagination
    @GetMapping
    public ResponseEntity<Page<Mobile>> getAllMobiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            logger.info("Fetching mobiles: Page={}, Size={}", page, size);
            Pageable pageable = PageRequest.of(page, size);
            Page<Mobile> mobiles = mobileService.getAllMobiles(pageable);
            return ResponseEntity.ok(mobiles);
        } catch (Exception e) {
            logger.error("Error fetching mobiles: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // GET: Fetch a single mobile by ID
    @GetMapping("/{id}")
    public ResponseEntity<Mobile> getMobileById(@PathVariable Long id) {
        try {
            logger.info("Fetching mobile with ID: {}", id);
            Optional<Mobile> mobile = mobileService.getMobileById(id);
            return mobile.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error fetching mobile: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // DELETE: Remove a mobile by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMobile(@PathVariable Long id) {
        try {
            logger.info("Deleting mobile with ID: {}", id);
            mobileService.deleteMobile(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting mobile: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}