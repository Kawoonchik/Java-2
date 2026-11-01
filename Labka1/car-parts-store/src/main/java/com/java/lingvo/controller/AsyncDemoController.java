package com.java.lingvo.controller;

import com.java.lingvo.service.CardAsyncService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/async-demo")
public class AsyncDemoController {

    private final CardAsyncService asyncService;

    public AsyncDemoController(CardAsyncService asyncService) {
        this.asyncService = asyncService;
    }

    @GetMapping("/notify")
    public String triggerNotification(@RequestParam Long id) {
        asyncService.sendCardReviewNotification(id);
        return "Запит на відправлення прийнято і обробляється у фоні.";
    }


    @GetMapping("/enrich")
    public CompletableFuture<String> enrichCardData(@RequestParam String word) {
        // 1.
        CompletableFuture<String> audioFuture = asyncService.generateAudioPronunciation(word);
        CompletableFuture<String> examplesFuture = asyncService.fetchUsageExamples(word);

        // 2.
        return audioFuture.thenCombine(examplesFuture, (audio, example) ->
                        "Успішно зібрано дані: Аудіо=[" + audio + "], Приклади=[" + example + "]"
                )
                // 3.
                .orTimeout(3, TimeUnit.SECONDS)
                // 4.
                .exceptionally(ex -> {
                    System.out.println("Виникла помилка: " + ex.getMessage());
                    return "Частковий результат (виникла помилка, але сервіс працює). Деталі: " + ex.getMessage();
                });
    }
}