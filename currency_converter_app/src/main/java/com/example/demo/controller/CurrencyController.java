package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
public class CurrencyController {

    @GetMapping("/")
    public String showPage() {
        return "currency";
    }

    @PostMapping("/convert")
    public String convertCurrency(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam double amount,
            Model model) {

        String url = "https://api.frankfurter.app/latest"
                + "?amount=" + amount
                + "&from=" + from.trim().toUpperCase()
                + "&to=" + to.trim().toUpperCase();

        RestTemplate restTemplate = new RestTemplate();
        Map response = restTemplate.getForObject(url, Map.class);

        System.out.println("API RESPONSE: " + response);

        Map rates = (Map) response.get("rates");

        if (rates == null || !rates.containsKey(to.toUpperCase())) {
            model.addAttribute("error", "Invalid currency code");
            return "currency";
        }

        double result = ((Number) rates.get(to.toUpperCase())).doubleValue();
        double rate = result / amount;

        model.addAttribute("from", from.toUpperCase());
        model.addAttribute("to", to.toUpperCase());
        model.addAttribute("amount", amount);
        model.addAttribute("result", result);
        model.addAttribute("rate", rate);

        return "currency";
    }



}
