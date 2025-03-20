package org.mobilestoreapp.service;

import org.mobilestoreapp.exception.ResourceNotFoundException;
import org.mobilestoreapp.model.Mobile;
import org.mobilestoreapp.repository.MobileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MobileService {

    private static final Logger logger = LoggerFactory.getLogger(MobileService.class);


    private final MobileRepository mobileRepository;

    public MobileService(MobileRepository mobileRepository) {
        this.mobileRepository = mobileRepository;
    }

    // Add multiple mobiles
    public List<Mobile> addMobiles(List<Mobile> mobiles) {
        logger.info("Saving {} mobiles to the database", mobiles.size());
        return mobileRepository.saveAll(mobiles);
    }

    // Get all mobiles with pagination
    public Page<Mobile> getAllMobiles(Pageable pageable) {
        logger.info("Fetching mobiles with pagination: Page={}, Size={}", pageable.getPageNumber(), pageable.getPageSize());
        return mobileRepository.findAll(pageable);
    }

    // Get a mobile by ID
    public Mobile getMobileById(Long id) {
        logger.info("Fetching mobile with ID: {}", id);
        return mobileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mobile not found with ID: " + id));
    }

    // Delete a mobile by ID
    @Transactional
    public void deleteMobile(Long id) {
        if (!mobileRepository.existsById(id)) {
            logger.error("Mobile with ID {} not found, cannot delete", id);
            throw new ResourceNotFoundException("Mobile not found with ID: " + id);
        }
        logger.info("Deleting mobile with ID: {}", id);
        mobileRepository.deleteById(id);
    }
}