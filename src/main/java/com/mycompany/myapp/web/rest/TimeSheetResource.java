package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.TimeSheetRepository;
import com.mycompany.myapp.service.TimeSheetService;
import com.mycompany.myapp.service.dto.ResponseEntityDTO;
import com.mycompany.myapp.service.dto.TimeSheetDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import io.undertow.util.BadRequestException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.TimeSheet}.
 */
@RestController
@RequestMapping("/api")
public class TimeSheetResource {

    private final Logger log = LoggerFactory.getLogger(TimeSheetResource.class);

    private static final String ENTITY_NAME = "timeSheet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TimeSheetService timeSheetService;

    private final TimeSheetRepository timeSheetRepository;

    public TimeSheetResource(TimeSheetService timeSheetService, TimeSheetRepository timeSheetRepository) {
        this.timeSheetService = timeSheetService;
        this.timeSheetRepository = timeSheetRepository;
    }

    /**
     * {@code POST  /time-sheets} : Create a new timeSheet.
     *
     * @param timeSheetDTO the timeSheetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new timeSheetDTO, or with status {@code 400 (Bad Request)} if the timeSheet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/time-sheets")
    public ResponseEntity<TimeSheetDTO> createTimeSheet(@RequestBody TimeSheetDTO timeSheetDTO) throws URISyntaxException {
        log.debug("REST request to save TimeSheet : {}", timeSheetDTO);
        if (timeSheetDTO.getId() != null) {
            throw new BadRequestAlertException("A new timeSheet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TimeSheetDTO result = timeSheetService.save(timeSheetDTO);
        return ResponseEntity
            .created(new URI("/api/time-sheets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /time-sheets/:id} : Updates an existing timeSheet.
     *
     * @param id the id of the timeSheetDTO to save.
     * @param timeSheetDTO the timeSheetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timeSheetDTO,
     * or with status {@code 400 (Bad Request)} if the timeSheetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the timeSheetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/time-sheets/{id}")
    public ResponseEntity<TimeSheetDTO> updateTimeSheet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TimeSheetDTO timeSheetDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TimeSheet : {}, {}", id, timeSheetDTO);
        if (timeSheetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timeSheetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timeSheetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TimeSheetDTO result = timeSheetService.update(timeSheetDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, timeSheetDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /time-sheets/:id} : Partial updates given fields of an existing timeSheet, field will ignore if it is null
     *
     * @param id the id of the timeSheetDTO to save.
     * @param timeSheetDTO the timeSheetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timeSheetDTO,
     * or with status {@code 400 (Bad Request)} if the timeSheetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the timeSheetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the timeSheetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/time-sheets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TimeSheetDTO> partialUpdateTimeSheet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TimeSheetDTO timeSheetDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TimeSheet partially : {}, {}", id, timeSheetDTO);
        if (timeSheetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timeSheetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timeSheetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TimeSheetDTO> result = timeSheetService.partialUpdate(timeSheetDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, timeSheetDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /time-sheets} : get all the timeSheets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of timeSheets in body.
     */
    @GetMapping("/time-sheets")
    public List<TimeSheetDTO> getAllTimeSheets() {
        log.debug("REST request to get all TimeSheets");
        return timeSheetService.findAll();
    }

    /**
     * {@code GET  /time-sheets/:id} : get the "id" timeSheet.
     *
     * @param id the id of the timeSheetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the timeSheetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/time-sheets/{id}")
    public ResponseEntity<TimeSheetDTO> getTimeSheet(@PathVariable Long id) {
        log.debug("REST request to get TimeSheet : {}", id);
        Optional<TimeSheetDTO> timeSheetDTO = timeSheetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(timeSheetDTO);
    }

    /**
     * {@code DELETE  /time-sheets/:id} : delete the "id" timeSheet.
     *
     * @param id the id of the timeSheetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/time-sheets/{id}")
    public ResponseEntity<Void> deleteTimeSheet(@PathVariable Long id) {
        log.debug("REST request to delete TimeSheet : {}", id);
        timeSheetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    //    @PostMapping("/time-sheets/checkin")
    //    public ResponseEntityDTO<TimeSheetDTO> checkin(@RequestBody TimeSheetDTO timeSheetDTO) throws URISyntaxException {
    //        log.debug("REST request to save TimeSheet : {}", timeSheetDTO);
    //        if (timeSheetDTO.getId() != null) {
    //            throw new BadRequestAlertException("A new timeSheet cannot already have an ID", ENTITY_NAME, "idexists");
    //        }
    //
    //        ResponseEntityDTO<TimeSheetDTO> responseDTO;
    //        TimeSheetDTO result = null;
    //
    //        try {
    //            result = timeSheetService.checkin(timeSheetDTO);
    //        } catch (BadRequestException e) {
    //                return responseDTO = new ResponseEntityDTO<>(result,null,e.getMessage(),400);
    //        }
    //        return responseDTO = new ResponseEntityDTO<>(result,"Success",null,200);
    //    }

    @PostMapping("/time-sheets/checkin")
    public ResponseEntity<TimeSheetDTO> checkin(@RequestBody TimeSheetDTO timeSheetDTO) throws URISyntaxException, BadRequestException {
        log.debug("REST request to checkin TimeSheet : {}", timeSheetDTO);
        if (timeSheetDTO.getId() != null) {
            throw new BadRequestAlertException("A new timeSheet cannot already have an ID", ENTITY_NAME, "idexists");
        }

        ResponseEntityDTO<TimeSheetDTO> responseDTO;
        TimeSheetDTO result = null;

        try {
            result = timeSheetService.checkin(timeSheetDTO);
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
        return ResponseEntity
            .created(new URI("/api/time-sheets/checkin" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/time-sheets/checkout")
    public ResponseEntity<TimeSheetDTO> checkout(@RequestBody TimeSheetDTO timeSheetDTO) throws URISyntaxException, BadRequestException {
        log.debug("REST request to checkout TimeSheet : {}", timeSheetDTO);
        if (timeSheetDTO.getId() != null) {
            throw new BadRequestAlertException("A new timeSheet cannot already have an ID", ENTITY_NAME, "idexists");
        }

        ResponseEntityDTO<TimeSheetDTO> responseDTO;
        TimeSheetDTO result = null;

        try {
            result = timeSheetService.checkOut(timeSheetDTO);
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
        return ResponseEntity
            .created(new URI("/api/time-sheets/checkin" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
