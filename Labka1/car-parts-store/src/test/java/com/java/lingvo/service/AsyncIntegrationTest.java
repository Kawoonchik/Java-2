package com.java.lingvo.service;

import com.java.lingvo.controller.AsyncDemoController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AsyncIntegrationTest {

    @Autowired
    private AsyncDemoController asyncController;

    @Autowired
    private CardAsyncService cardAsyncService;

    @Test
    void enrichCardData_ShouldExecuteInParallel_TakingLessThanThreeSeconds() {
        // GIVEN: Метод, що викликає два асинхронні завдання (кожне по 1.5 сек)
        long startTime = System.currentTimeMillis();

        // WHEN: Викликаємо об'єднаний результат
        CompletableFuture<String> resultFuture = asyncController.enrichCardData("apple");
        String result = resultFuture.join(); // Чекаємо завершення

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // THEN: Час виконання має бути близько 1500 мс (плюс трохи на накладні витрати Spring)
        // Якщо б вони виконувалися послідовно, це зайняло б 3000 мс.
        assertTrue(duration < 2500, "Завдання виконувалися не паралельно! Час виконання: " + duration + " мс");
        assertTrue(result.contains("Аудіо="), "Має повернутись успішний об'єднаний результат");
    }

    @Test
    void enrichCardData_ShouldHandleException_AndReturnFallbackResult() {
        // GIVEN: Слово "error" викликає RuntimeException у сервісі
        // WHEN
        CompletableFuture<String> resultFuture = asyncController.enrichCardData("error");
        String result = resultFuture.join();

        // THEN: Помилка оброблена блоком .exceptionally(), програма не впала
        assertTrue(result.contains("Частковий результат"), "Має повернутись fallback-повідомлення");
        assertTrue(result.contains("Зовнішній API словника недоступний"), "Має містити причину помилки");
    }

    @Test
    void asyncExecutor_ShouldHandleQueueOverflow_WithCallerRunsPolicy() {
        // GIVEN: Наш пул має розмір 2 і чергу 2 (всього може вмістити 4 задачі).
        // Ми відправляємо 10 задач одночасно.
        List<CompletableFuture<String>> futures = new ArrayList<>();

        // WHEN: Спамимо виконавця запитами
        for (int i = 0; i < 10; i++) {
            futures.add(cardAsyncService.generateAudioPronunciation("word" + i));
        }

        // THEN: Не виникає TaskRejectedException (помилка 500).
        // Завдяки CallerRunsPolicy, "зайві" задачі просто виконуються в головному потоці.
        assertDoesNotThrow(() -> {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        }, "Переповнення пулу не повинно призводити до збою програми");
    }
}