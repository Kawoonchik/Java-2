package com.java.lingvo.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class CardAsyncService {

    // 1. Метод типу "Запустив і забув" (Fire-and-forget)
    @Async
    public void sendCardReviewNotification(Long cardId) {
        try {
            Thread.sleep(1000);

            // Спеціально кидаємо помилку, щоб перевірити AsyncUncaughtExceptionHandler
            if (cardId < 0) {
                throw new IllegalArgumentException("ID картки не може бути від'ємним!");
            }

            System.out.println("Сповіщення для картки " + cardId + " надіслано. Потік: " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // 2. Метод, що повертає результат (Future 1)
    @Async
    public CompletableFuture<String> generateAudioPronunciation(String word) {
        try {
            Thread.sleep(1500);
            System.out.println("Аудіо згенеровано. Потік: " + Thread.currentThread().getName());
            return CompletableFuture.completedFuture("http://audio.server/" + word + ".mp3");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(e);
        }
    }

    // 3. Метод, що повертає результат (Future 2)
    @Async
    public CompletableFuture<String> fetchUsageExamples(String word) {
        try {
            Thread.sleep(1500);

            if ("error".equals(word)) { // Імітація збою під час виконання
                throw new RuntimeException("Зовнішній API словника недоступний");
            }

            System.out.println("Приклади знайдено. Потік: " + Thread.currentThread().getName());
            return CompletableFuture.completedFuture("Приклад речення зі словом '" + word + "'");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(e);
        }
    }
}