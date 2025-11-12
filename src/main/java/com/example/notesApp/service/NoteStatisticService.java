package com.example.notesApp.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class NoteStatisticService {

    public Map<String, Long> calculateWordStatistics(String text) {
        if (text == null || text.isEmpty()) {
            return new HashMap<>();
        }

        Map<String, Long> wordCount = new HashMap<>();

        String[] words = text.split("\\s+");
        for (String word : words){
            if (!word.isEmpty()){ // eсли слово есть счетчик по умолчанию =1, далее на каждом совпадении +1
                wordCount.put(word, wordCount.getOrDefault(word, 0L) + 1);
            }
        }

        List<Map.Entry<String, Long>> entries = new ArrayList<>(wordCount.entrySet());

        entries.sort((a, b) -> b.getValue().compareTo(a.getValue())); // сортировка по убыванию

        Map<String, Long> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Long> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;

    }
}