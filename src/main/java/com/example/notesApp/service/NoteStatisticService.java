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
            if (!word.isEmpty()){ // сли слово есть счетчик по умолчанию =1, далее на каждом совпадении +1
                wordCount.put(word, wordCount.getOrDefault(word, 0L) + 1);
            }
        }

        List<Map.Entry<String, Long>> entries = new ArrayList<>(wordCount.entrySet());

        entries.sort((a, b) -> b.getValue().compareTo(a.getValue())); // сортировка по убыванию

        // Шаг 3: Создаем отсортированную Map
        Map<String, Long> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Long> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;

//        return Arrays.stream(text.toLowerCase().split("\\s+"))
//                .filter(word -> !word.isEmpty())
//                .collect(Collectors.groupingBy(
//                        Function.identity(),
//                        Collectors.counting()
//                ))
//                .entrySet().stream()
//                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        Map.Entry::getValue,
//                        (e1, e2) -> e1,
//                        LinkedHashMap::new
//                ));
    }
}