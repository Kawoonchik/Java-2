package com.java.lingvo.service;

import com.java.lingvo.configuration.AsyncConfig;
import com.java.lingvo.controller.AsyncDemoController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {AsyncConfig.class, CardAsyncService.class, AsyncDemoController.class})
class AsyncIntegrationTest {

    @Autowired
    private AsyncDemoController asyncController;

    @Autowired
    private CardAsyncService cardAsyncService;

    @Test
    void enrichCardData_ShouldExecuteInParallel_TakingLessThanThreeSeconds() {
        // GIVEN
        long startTime = System.currentTimeMillis();

        // WHEN
        CompletableFuture<String> resultFuture = asyncController.enrichCardData("apple");
        String result = resultFuture.join();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // THEN
        assertTrue(duration < 2500, "Завдання виконувалися не паралельно! Час виконання: " + duration + " мс");
        assertTrue(result.contains("Аудіо="), "Має повернутись успішний об'єднаний результат");
    }

    @Test
    void enrichCardData_ShouldHandleException_AndReturnFallbackResult() {
        // GIVEN
        // WHEN
        CompletableFuture<String> resultFuture = asyncController.enrichCardData("error");
        String result = resultFuture.join();

        // THEN
        assertTrue(result.contains("Частковий результат"), "Має повернутись fallback-повідомлення");
        assertTrue(result.contains("Зовнішній API словника недоступний"), "Має містити причину помилки");
    }

    @Test
    void asyncExecutor_ShouldHandleQueueOverflow_WithCallerRunsPolicy() {
        // GIVEN
        List<CompletableFuture<String>> futures = new ArrayList<>();

        // WHEN
        for (int i = 0; i < 10; i++) {
            futures.add(cardAsyncService.generateAudioPronunciation("word" + i));
        }

        // THEN
        assertDoesNotThrow(() -> {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        }, "Переповнення пулу не повинно призводити до збою програми");
    }
}