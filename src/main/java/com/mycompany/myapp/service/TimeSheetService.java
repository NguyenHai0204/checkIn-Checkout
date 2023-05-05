package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TimeSheet;
import com.mycompany.myapp.repository.TimeSheetRepository;
import com.mycompany.myapp.service.dto.TimeSheetDTO;
import com.mycompany.myapp.service.mapper.TimeSheetMapper;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import io.undertow.util.BadRequestException;
import java.time.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TimeSheet}.
 */
@Service
@Transactional
public class TimeSheetService {

    private final Logger log = LoggerFactory.getLogger(TimeSheetService.class);

    private final TimeSheetRepository timeSheetRepository;

    private final TimeSheetMapper timeSheetMapper;

    public TimeSheetService(TimeSheetRepository timeSheetRepository, TimeSheetMapper timeSheetMapper) {
        this.timeSheetRepository = timeSheetRepository;
        this.timeSheetMapper = timeSheetMapper;
    }

    /**
     * Save a timeSheet.
     *
     * @param timeSheetDTO the entity to save.
     * @return the persisted entity.
     */
    public TimeSheetDTO save(TimeSheetDTO timeSheetDTO) {
        log.debug("Request to save TimeSheet : {}", timeSheetDTO);
        TimeSheet timeSheet = timeSheetMapper.toEntity(timeSheetDTO);
        timeSheet = timeSheetRepository.save(timeSheet);
        return timeSheetMapper.toDto(timeSheet);
    }

    /**
     * Update a timeSheet.
     *
     * @param timeSheetDTO the entity to save.
     * @return the persisted entity.
     */
    public TimeSheetDTO update(TimeSheetDTO timeSheetDTO) {
        log.debug("Request to update TimeSheet : {}", timeSheetDTO);
        TimeSheet timeSheet = timeSheetMapper.toEntity(timeSheetDTO);
        timeSheet = timeSheetRepository.save(timeSheet);
        return timeSheetMapper.toDto(timeSheet);
    }

    /**
     * Partially update a timeSheet.
     *
     * @param timeSheetDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TimeSheetDTO> partialUpdate(TimeSheetDTO timeSheetDTO) {
        log.debug("Request to partially update TimeSheet : {}", timeSheetDTO);

        return timeSheetRepository
            .findById(timeSheetDTO.getId())
            .map(existingTimeSheet -> {
                timeSheetMapper.partialUpdate(existingTimeSheet, timeSheetDTO);

                return existingTimeSheet;
            })
            .map(timeSheetRepository::save)
            .map(timeSheetMapper::toDto);
    }

    /**
     * Get all the timeSheets.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TimeSheetDTO> findAll() {
        log.debug("Request to get all TimeSheets");
        return timeSheetRepository.findAll().stream().map(timeSheetMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one timeSheet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TimeSheetDTO> findOne(Long id) {
        log.debug("Request to get TimeSheet : {}", id);
        return timeSheetRepository.findById(id).map(timeSheetMapper::toDto);
    }

    /**
     * Get one timeSheet by date.
     *
     * @param date the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TimeSheetDTO> findByDate(LocalDate date) {
        log.debug("Request to get TimeSheet : {}", date);
        return timeSheetRepository.findByDate(date).map(timeSheetMapper::toDto);
    }

    /**
     * Delete the timeSheet by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TimeSheet : {}", id);
        timeSheetRepository.deleteById(id);
    }

    /**
     * Update a timeSheet.
     *
     * @param timeSheetDTO the entity to save Checkin.
     * @return the persisted entity.
     */
    public TimeSheetDTO checkin(TimeSheetDTO timeSheetDTO) throws BadRequestException {
        log.debug("Request to checkin TimeSheet : {}", timeSheetDTO);

        //Get User
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        //End Get User

        //Get Date And Time Checkin
        Instant checkinTime = Instant.now();
        LocalDate checkinDate = LocalDate.now();
        //Emd Get Date And Time Checkin

        //Check duplicate CheckinDateTime
        Optional<TimeSheetDTO> checkDup = findByDate(checkinDate);
        if (checkDup.isPresent()) {
            throw new BadRequestException("Can't check in twice");
        }
        //End Check duplicate CheckinDateTime

        timeSheetDTO.setDate(checkinDate);
        timeSheetDTO.setCheckIn(checkinTime);
        timeSheetDTO.setUser(username);

        TimeSheet timeSheet = timeSheetMapper.toEntity(timeSheetDTO);
        timeSheet = timeSheetRepository.save(timeSheet);
        return timeSheetMapper.toDto(timeSheet);
    }

    /**
     * Update a timeSheet.
     *
     * @param timeSheetDTO the entity to save Checkin.
     * @return the persisted entity.
     */
    public TimeSheetDTO checkOut(TimeSheetDTO timeSheetDTO) throws BadRequestException {
        log.debug("Request to checkin TimeSheet : {}", timeSheetDTO);

        //Get User
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        //End Get User

        //Get Date And Time Checkin
        Instant checkoutTime = Instant.now();
        LocalDate checkinDate = LocalDate.now();
        //Emd Get Date And Time Checkin

        //Check duplicate CheckinDateTime
        Optional<TimeSheetDTO> checkFoundCheckin = findByDate(checkinDate);
        if (checkFoundCheckin.isPresent()) {
            checkFoundCheckin.get().setCheckOut(checkoutTime);
            checkFoundCheckin.get().setUser(username);
        } else {
            throw new BadRequestException("We Need Checkin Before!");
            //End Check duplicate CheckinDateTime
        }
        TimeSheet timeSheet = timeSheetMapper.toEntity(checkFoundCheckin.get());
        timeSheet = timeSheetRepository.save(timeSheet);
        return timeSheetMapper.toDto(timeSheet);
    }

    private String caculatorOverTime(Instant checkin, Instant checkout) {
        LocalDateTime localDateTime = LocalDateTime.now().withHour(17).withMinute(00).withSecond(00).withNano(0);

        Instant breakTime = localDateTime.atZone(ZoneId.systemDefault()).toInstant();

        Duration duration = Duration.between(breakTime, checkout);
        long seconds = Math.abs(duration.getSeconds());

        String formattedTime = String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);

        return formattedTime;
    }
}
