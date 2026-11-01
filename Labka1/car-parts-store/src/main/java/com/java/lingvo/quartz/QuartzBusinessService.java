package com.java.lingvo.quartz;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuartzBusinessService {

    @Transactional
    public void cleanupOldData() {
        System.out.println("==> [СЕРВІС] Видалення старих або неактивних карток з бази...");

    }

    @Transactional
    public void sendReminder(String messageType) {
        System.out.println("==> [СЕРВІС] Формування нагадування. Тип: " + messageType);


        if ("ERROR_TEST".equals(messageType)) {
            throw new RuntimeException("Симуляція падіння зовнішнього API розсилки!");
        }
    }
}