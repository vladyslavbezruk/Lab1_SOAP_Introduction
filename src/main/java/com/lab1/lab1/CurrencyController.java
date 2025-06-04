package com.lab1.lab1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController // Позначає цей клас як контролер для обробки HTTP запитів
@RequestMapping("/api/currency") // Вказує базовий шлях для всіх ендпоінтів цього контролера
public class CurrencyController {

    @GetMapping("/convert") // Обробляє GET-запити за шляхом /api/currency/convert
    public ResponseEntity<Map<String, Object>> convertCurrency(
            @RequestParam String from, // Вхідний параметр "з якої валюти"
            @RequestParam String to,   // Вхідний параметр "в яку валюту"
            @RequestParam double amount // Вхідний параметр "сума для конвертації"
    ) {

        double rate = 0; // Змінна для зберігання курсу обміну

        // Визначаємо курс обміну залежно від валют
        if (Objects.equals(from, "UAH") && Objects.equals(to, "USD")) {
            rate = 41.43; // Курс гривня -> долар
        } else if (Objects.equals(from, "USD") && Objects.equals(to, "UAH")) {
            rate = 1 / 41.43; // Курс долар -> гривня
        } else if (Objects.equals(from, "USD") && Objects.equals(to, "EUR")) {
            rate = 0.88; // Курс долар -> євро
        } else if (Objects.equals(from, "EUR") && Objects.equals(to, "USD")) {
            rate = 1 / 0.88; // Курс євро -> долар
        } else {
            // Якщо валюти не знайдені, повертаємо відповідь із помилкою
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Невідомі валюти");
            errorResponse.put("details", String.format("Конвертація з %s до %s не підтримується.", from, to));
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // Розраховуємо конвертовану суму
        double convertedAmount = amount / rate;

        // Формуємо відповідь у вигляді JSON
        Map<String, Object> response = new HashMap<>();
        response.put("from", from); // Валюта джерела
        response.put("to", to); // Цільова валюта
        response.put("amount", amount); // Початкова сума
        response.put("convertedAmount", convertedAmount); // Сума після конвертації

        // Повертаємо успішну відповідь із розрахованими даними
        return ResponseEntity.ok(response);
    }
}