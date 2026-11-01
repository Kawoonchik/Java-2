package com.java.lingvo.aop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aop-demo")
public class AopDemoController {

    private final AopDemoService aopDemoService;

    public AopDemoController(AopDemoService aopDemoService) {
        this.aopDemoService = aopDemoService;
    }

    @GetMapping("/run")
    public String runDemo() {
        System.out.println("=== ПОЧАТОК ДЕМОНСТРАЦІЇ AOP ===");

        // 1 ЗОВНІШНІЙ
        System.out.println("Зовнішній виклик з контролера: " + aopDemoService.getDirtyString());

        // 2. ВНУТРІШНІЙ
        aopDemoService.selfInvocationDemo();

        System.out.println("=== КІНЕЦЬ ДЕМОНСТРАЦІЇ ===");
        return "Демонстрація виконана! Подивіться в консоль IntelliJ IDEA.";
    }
}