package com.example.notesApp.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

public class NoteStatisticServiceTest {
    private NoteStatisticService noteStatisticService;

    @BeforeEach
    void setUp(){
        noteStatisticService = new NoteStatisticService();
    }

    @Test
    public void calculateWordStatistics_shouldReturnEmptyMap_whenTextIsNull(){
        Map<String, Long> result =  noteStatisticService.calculateWordStatistics(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void calculateWordStatistics_shouldReturnEmptyMap_whenTextIsEmpty(){
        Map<String, Long> result =  noteStatisticService.calculateWordStatistics("");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void calculateWordStatistics_shouldIgnoreCase(){
        String text = "Java java JAVA result oop";

        Map<String, Long> result = noteStatisticService.calculateWordStatistics(text);

        assertEquals(3L, result.get("java"));
    }

    @Test
    public void calculateWordStatistics_shouldIgnorePunctuation(){
        String text = "Java! ! java. JAVA, result, oop";

        Map<String, Long> result = noteStatisticService.calculateWordStatistics(text);

        assertEquals(1L, result.get("result"));
    }

    @Test
    void calculateWordStatistics_shouldSortByFrequencyDescending() {
        String text = "java spring spring boot boot boot";

        Map<String, Long> result = noteStatisticService.calculateWordStatistics(text);

        String firstKey = result.keySet().iterator().next();

        assertEquals("boot", firstKey);
        assertEquals(3L, result.get("boot"));
    }
}
