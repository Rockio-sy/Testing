package org.academo.academo.London.service;

import org.academo.academo.Exception.AlreadyExistsException;
import org.academo.academo.Exception.DatabaseServiceException;
import org.academo.academo.Exception.InvalidDataException;
import org.academo.academo.Exception.ResourceNotFoundException;
import org.academo.academo.builders.GradeBuilder;
import org.academo.academo.dto.GradeDTO;
import org.academo.academo.extension.TestWatcherExtension;
import org.academo.academo.model.Grade;
import org.academo.academo.repository.GradeRepository;
import org.academo.academo.service.impl.GradeServiceImpl;
import org.academo.academo.util.Converter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
@DisplayName("Grade Service Implementation Tests")
@Tag("unit")
@TestMethodOrder(MethodOrderer.Random.class)
class GradeServiceTest {

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private Converter converter;

    @InjectMocks
    private GradeServiceImpl gradeService;

    private GradeDTO testGradeDTO;
    private Grade testGrade;
    private final UUID submissionTestId = UUID.randomUUID();
    private final UUID gradeTestId = UUID.randomUUID();


    @BeforeAll
    static void warmup() {
        // 1) SLF4J/Logback bootstrap
        LoggerFactory.getLogger(GradeServiceTest.class).info("warmup");

        // 2) Mockito/ByteBuddy bootstrap
        mock(Object.class);
        try {
            Class.forName("org.academo.academo.Exception.CustomException");
            Class.forName("org.academo.academo.Exception.ResourceNotFoundException");
            Class.forName("org.academo.academo.Exception.AlreadyExistsException");
            Class.forName("org.academo.academo.Exception.InvalidDataException");
            Class.forName("org.academo.academo.Exception.DatabaseServiceException");
        } catch (ClassNotFoundException ignored) {}

        // 4) (Optional) If your Converter uses Jackson/Validation internally, pre-touch them:
        // new com.fasterxml.jackson.databind.ObjectMapper();
        // jakarta.validation.Validation.buildDefaultValidatorFactory().close();
    }
    @BeforeEach
    void setUp() {
        var fixture = GradeBuilder.aGrade()
                .withId(gradeTestId)
                .withSubmissionId(submissionTestId)
                .withValue(90)
                .withFeedback("Good work");

        testGradeDTO = fixture.asDto();
        testGrade = fixture.asEntity();
        org.mockito.Mockito.mockingDetails(gradeRepository).isMock();
        org.mockito.Mockito.mockingDetails(converter).isMock();
    }

    @Test
    @DisplayName("Create Grade - Success")
    void createGrade_Success() {
        when(converter.DTOtoGrade(any())).thenReturn(testGrade);
        doNothing().when(gradeRepository).save(any());

        gradeService.createGrade(testGradeDTO);

        verify(converter).DTOtoGrade(testGradeDTO);
        verify(gradeRepository).save(testGrade);
    }

    @Test
    @DisplayName("Create Grade - Throws AlreadyExistsException for duplicate grade")
    void createGrade_DuplicateKeyException() {
        when(converter.DTOtoGrade(any())).thenReturn(testGrade);
        doThrow(DuplicateKeyException.class).when(gradeRepository).save(any());

        assertThrows(AlreadyExistsException.class,
                () -> gradeService.createGrade(testGradeDTO));
    }

    @Test
    @DisplayName("Create Grade - Throws InvalidDataException for invalid grade data")
    void createGrade_DataIntegrityViolationException() {
        when(converter.DTOtoGrade(any())).thenReturn(testGrade);
        doThrow(DataIntegrityViolationException.class).when(gradeRepository).save(any());

        assertThrows(InvalidDataException.class,
                () -> gradeService.createGrade(testGradeDTO));
    }

    @Test
    @DisplayName("Create Grade - Throws DatabaseServiceException for database errors")
    void createGrade_DataAccessException() {
        when(converter.DTOtoGrade(any())).thenReturn(testGrade);
        doThrow(new DataAccessException("DB Error") {
        }).when(gradeRepository).save(any());

        assertThrows(DatabaseServiceException.class,
                () -> gradeService.createGrade(testGradeDTO));
    }

    @Test
    @DisplayName("Get By Submission ID - Returns grade when found")
    void getBySubmissionId_GradeFound() {
        UUID submissionTestId = UUID.randomUUID();
        when(gradeRepository.getBySubmissionId(any(UUID.class))).thenReturn(Optional.of(testGrade));
        when(converter.gradeToDTO(any())).thenReturn(testGradeDTO);

        GradeDTO result = gradeService.getBySubmissionId(submissionTestId);

        assertEquals(testGradeDTO, result);
        verify(gradeRepository).getBySubmissionId(submissionTestId);
        verify(converter).gradeToDTO(testGrade);
    }

    @Test
    @DisplayName("Get By Submission ID - Throws ResourceNotFoundException when not found")
    void getBySubmissionId_GradeNotFound() {
        UUID submissionTestId = UUID.randomUUID();
        when(gradeRepository.getBySubmissionId(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> gradeService.getBySubmissionId(submissionTestId));
    }


    @Test
    @DisplayName("Get By Submission ID - Throws DatabaseServiceException on database error")
    void getBySubmissionId_DataAccessException() {
        UUID submissionTestId = UUID.randomUUID();
        when(gradeRepository.getBySubmissionId(any(UUID.class)))
                .thenThrow(new DataAccessException("DB Error") {
                });

        assertThrows(DatabaseServiceException.class,
                () -> gradeService.getBySubmissionId(submissionTestId));
    }

    @Test
    @DisplayName("Get by submission id -- Success")
    void getBySubmissionId__Success() {
        when(gradeRepository.getBySubmissionId(any()))
                .thenReturn(Optional.of(testGrade));

        when(converter.gradeToDTO(testGrade))
                .thenReturn(testGradeDTO);

        GradeDTO resp = gradeService.getBySubmissionId(submissionTestId);
        assertEquals(resp, testGradeDTO);
    }
// Remove it

    @Test
    void _diag_costOfNotFoundException() {
        long t0 = System.nanoTime();
        try {

            throw new ResourceNotFoundException("x");
        } catch (ResourceNotFoundException e) {

        }
        long ms = (System.nanoTime() - t0) / 1_000_000;
        System.out.println("Construct+throw ResourceNotFoundException took ~" + ms + " ms");
    }

}