package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TimeSheet;
import com.mycompany.myapp.repository.TimeSheetRepository;
import com.mycompany.myapp.service.dto.TimeSheetDTO;
import com.mycompany.myapp.service.mapper.TimeSheetMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TimeSheetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TimeSheetResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_CHECK_IN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CHECK_IN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CHECK_OUT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CHECK_OUT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_OVER_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OVER_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_USER = "AAAAAAAAAA";
    private static final String UPDATED_USER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/time-sheets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TimeSheetRepository timeSheetRepository;

    @Autowired
    private TimeSheetMapper timeSheetMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTimeSheetMockMvc;

    private TimeSheet timeSheet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeSheet createEntity(EntityManager em) {
        TimeSheet timeSheet = new TimeSheet()
            .date(DEFAULT_DATE)
            .checkIn(DEFAULT_CHECK_IN)
            .checkOut(DEFAULT_CHECK_OUT)
            .overTime(DEFAULT_OVER_TIME)
            .user(DEFAULT_USER);
        return timeSheet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeSheet createUpdatedEntity(EntityManager em) {
        TimeSheet timeSheet = new TimeSheet()
            .date(UPDATED_DATE)
            .checkIn(UPDATED_CHECK_IN)
            .checkOut(UPDATED_CHECK_OUT)
            .overTime(UPDATED_OVER_TIME)
            .user(UPDATED_USER);
        return timeSheet;
    }

    @BeforeEach
    public void initTest() {
        timeSheet = createEntity(em);
    }

    @Test
    @Transactional
    void createTimeSheet() throws Exception {
        int databaseSizeBeforeCreate = timeSheetRepository.findAll().size();
        // Create the TimeSheet
        TimeSheetDTO timeSheetDTO = timeSheetMapper.toDto(timeSheet);
        restTimeSheetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(timeSheetDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TimeSheet in the database
        List<TimeSheet> timeSheetList = timeSheetRepository.findAll();
        assertThat(timeSheetList).hasSize(databaseSizeBeforeCreate + 1);
        TimeSheet testTimeSheet = timeSheetList.get(timeSheetList.size() - 1);
        assertThat(testTimeSheet.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testTimeSheet.getCheckIn()).isEqualTo(DEFAULT_CHECK_IN);
        assertThat(testTimeSheet.getCheckOut()).isEqualTo(DEFAULT_CHECK_OUT);
        assertThat(testTimeSheet.getOverTime()).isEqualTo(DEFAULT_OVER_TIME);
        assertThat(testTimeSheet.getUser()).isEqualTo(DEFAULT_USER);
    }

    @Test
    @Transactional
    void createTimeSheetWithExistingId() throws Exception {
        // Create the TimeSheet with an existing ID
        timeSheet.setId(1L);
        TimeSheetDTO timeSheetDTO = timeSheetMapper.toDto(timeSheet);

        int databaseSizeBeforeCreate = timeSheetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimeSheetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(timeSheetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSheet in the database
        List<TimeSheet> timeSheetList = timeSheetRepository.findAll();
        assertThat(timeSheetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTimeSheets() throws Exception {
        // Initialize the database
        timeSheetRepository.saveAndFlush(timeSheet);

        // Get all the timeSheetList
        restTimeSheetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timeSheet.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].checkIn").value(hasItem(DEFAULT_CHECK_IN.toString())))
            .andExpect(jsonPath("$.[*].checkOut").value(hasItem(DEFAULT_CHECK_OUT.toString())))
            .andExpect(jsonPath("$.[*].overTime").value(hasItem(DEFAULT_OVER_TIME.toString())))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER)));
    }

    @Test
    @Transactional
    void getTimeSheet() throws Exception {
        // Initialize the database
        timeSheetRepository.saveAndFlush(timeSheet);

        // Get the timeSheet
        restTimeSheetMockMvc
            .perform(get(ENTITY_API_URL_ID, timeSheet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(timeSheet.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.checkIn").value(DEFAULT_CHECK_IN.toString()))
            .andExpect(jsonPath("$.checkOut").value(DEFAULT_CHECK_OUT.toString()))
            .andExpect(jsonPath("$.overTime").value(DEFAULT_OVER_TIME.toString()))
            .andExpect(jsonPath("$.user").value(DEFAULT_USER));
    }

    @Test
    @Transactional
    void getNonExistingTimeSheet() throws Exception {
        // Get the timeSheet
        restTimeSheetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTimeSheet() throws Exception {
        // Initialize the database
        timeSheetRepository.saveAndFlush(timeSheet);

        int databaseSizeBeforeUpdate = timeSheetRepository.findAll().size();

        // Update the timeSheet
        TimeSheet updatedTimeSheet = timeSheetRepository.findById(timeSheet.getId()).get();
        // Disconnect from session so that the updates on updatedTimeSheet are not directly saved in db
        em.detach(updatedTimeSheet);
        updatedTimeSheet
            .date(UPDATED_DATE)
            .checkIn(UPDATED_CHECK_IN)
            .checkOut(UPDATED_CHECK_OUT)
            .overTime(UPDATED_OVER_TIME)
            .user(UPDATED_USER);
        TimeSheetDTO timeSheetDTO = timeSheetMapper.toDto(updatedTimeSheet);

        restTimeSheetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timeSheetDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(timeSheetDTO))
            )
            .andExpect(status().isOk());

        // Validate the TimeSheet in the database
        List<TimeSheet> timeSheetList = timeSheetRepository.findAll();
        assertThat(timeSheetList).hasSize(databaseSizeBeforeUpdate);
        TimeSheet testTimeSheet = timeSheetList.get(timeSheetList.size() - 1);
        assertThat(testTimeSheet.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTimeSheet.getCheckIn()).isEqualTo(UPDATED_CHECK_IN);
        assertThat(testTimeSheet.getCheckOut()).isEqualTo(UPDATED_CHECK_OUT);
        assertThat(testTimeSheet.getOverTime()).isEqualTo(UPDATED_OVER_TIME);
        assertThat(testTimeSheet.getUser()).isEqualTo(UPDATED_USER);
    }

    @Test
    @Transactional
    void putNonExistingTimeSheet() throws Exception {
        int databaseSizeBeforeUpdate = timeSheetRepository.findAll().size();
        timeSheet.setId(count.incrementAndGet());

        // Create the TimeSheet
        TimeSheetDTO timeSheetDTO = timeSheetMapper.toDto(timeSheet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimeSheetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timeSheetDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(timeSheetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSheet in the database
        List<TimeSheet> timeSheetList = timeSheetRepository.findAll();
        assertThat(timeSheetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTimeSheet() throws Exception {
        int databaseSizeBeforeUpdate = timeSheetRepository.findAll().size();
        timeSheet.setId(count.incrementAndGet());

        // Create the TimeSheet
        TimeSheetDTO timeSheetDTO = timeSheetMapper.toDto(timeSheet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeSheetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(timeSheetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSheet in the database
        List<TimeSheet> timeSheetList = timeSheetRepository.findAll();
        assertThat(timeSheetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTimeSheet() throws Exception {
        int databaseSizeBeforeUpdate = timeSheetRepository.findAll().size();
        timeSheet.setId(count.incrementAndGet());

        // Create the TimeSheet
        TimeSheetDTO timeSheetDTO = timeSheetMapper.toDto(timeSheet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeSheetMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(timeSheetDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimeSheet in the database
        List<TimeSheet> timeSheetList = timeSheetRepository.findAll();
        assertThat(timeSheetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTimeSheetWithPatch() throws Exception {
        // Initialize the database
        timeSheetRepository.saveAndFlush(timeSheet);

        int databaseSizeBeforeUpdate = timeSheetRepository.findAll().size();

        // Update the timeSheet using partial update
        TimeSheet partialUpdatedTimeSheet = new TimeSheet();
        partialUpdatedTimeSheet.setId(timeSheet.getId());

        partialUpdatedTimeSheet.date(UPDATED_DATE).checkIn(UPDATED_CHECK_IN).overTime(UPDATED_OVER_TIME);

        restTimeSheetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimeSheet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTimeSheet))
            )
            .andExpect(status().isOk());

        // Validate the TimeSheet in the database
        List<TimeSheet> timeSheetList = timeSheetRepository.findAll();
        assertThat(timeSheetList).hasSize(databaseSizeBeforeUpdate);
        TimeSheet testTimeSheet = timeSheetList.get(timeSheetList.size() - 1);
        assertThat(testTimeSheet.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTimeSheet.getCheckIn()).isEqualTo(UPDATED_CHECK_IN);
        assertThat(testTimeSheet.getCheckOut()).isEqualTo(DEFAULT_CHECK_OUT);
        assertThat(testTimeSheet.getOverTime()).isEqualTo(UPDATED_OVER_TIME);
        assertThat(testTimeSheet.getUser()).isEqualTo(DEFAULT_USER);
    }

    @Test
    @Transactional
    void fullUpdateTimeSheetWithPatch() throws Exception {
        // Initialize the database
        timeSheetRepository.saveAndFlush(timeSheet);

        int databaseSizeBeforeUpdate = timeSheetRepository.findAll().size();

        // Update the timeSheet using partial update
        TimeSheet partialUpdatedTimeSheet = new TimeSheet();
        partialUpdatedTimeSheet.setId(timeSheet.getId());

        partialUpdatedTimeSheet
            .date(UPDATED_DATE)
            .checkIn(UPDATED_CHECK_IN)
            .checkOut(UPDATED_CHECK_OUT)
            .overTime(UPDATED_OVER_TIME)
            .user(UPDATED_USER);

        restTimeSheetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimeSheet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTimeSheet))
            )
            .andExpect(status().isOk());

        // Validate the TimeSheet in the database
        List<TimeSheet> timeSheetList = timeSheetRepository.findAll();
        assertThat(timeSheetList).hasSize(databaseSizeBeforeUpdate);
        TimeSheet testTimeSheet = timeSheetList.get(timeSheetList.size() - 1);
        assertThat(testTimeSheet.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTimeSheet.getCheckIn()).isEqualTo(UPDATED_CHECK_IN);
        assertThat(testTimeSheet.getCheckOut()).isEqualTo(UPDATED_CHECK_OUT);
        assertThat(testTimeSheet.getOverTime()).isEqualTo(UPDATED_OVER_TIME);
        assertThat(testTimeSheet.getUser()).isEqualTo(UPDATED_USER);
    }

    @Test
    @Transactional
    void patchNonExistingTimeSheet() throws Exception {
        int databaseSizeBeforeUpdate = timeSheetRepository.findAll().size();
        timeSheet.setId(count.incrementAndGet());

        // Create the TimeSheet
        TimeSheetDTO timeSheetDTO = timeSheetMapper.toDto(timeSheet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimeSheetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, timeSheetDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(timeSheetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSheet in the database
        List<TimeSheet> timeSheetList = timeSheetRepository.findAll();
        assertThat(timeSheetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTimeSheet() throws Exception {
        int databaseSizeBeforeUpdate = timeSheetRepository.findAll().size();
        timeSheet.setId(count.incrementAndGet());

        // Create the TimeSheet
        TimeSheetDTO timeSheetDTO = timeSheetMapper.toDto(timeSheet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeSheetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(timeSheetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSheet in the database
        List<TimeSheet> timeSheetList = timeSheetRepository.findAll();
        assertThat(timeSheetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTimeSheet() throws Exception {
        int databaseSizeBeforeUpdate = timeSheetRepository.findAll().size();
        timeSheet.setId(count.incrementAndGet());

        // Create the TimeSheet
        TimeSheetDTO timeSheetDTO = timeSheetMapper.toDto(timeSheet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeSheetMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(timeSheetDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimeSheet in the database
        List<TimeSheet> timeSheetList = timeSheetRepository.findAll();
        assertThat(timeSheetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTimeSheet() throws Exception {
        // Initialize the database
        timeSheetRepository.saveAndFlush(timeSheet);

        int databaseSizeBeforeDelete = timeSheetRepository.findAll().size();

        // Delete the timeSheet
        restTimeSheetMockMvc
            .perform(delete(ENTITY_API_URL_ID, timeSheet.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TimeSheet> timeSheetList = timeSheetRepository.findAll();
        assertThat(timeSheetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
