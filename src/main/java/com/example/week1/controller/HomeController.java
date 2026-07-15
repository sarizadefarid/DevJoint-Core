package com.example.week1.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String home() {
        return """
                <html>
                <head>
                    <title>Week1</title>
                </head>
                <body style="font-family: Arial, sans-serif; padding: 32px;">
                    <h1>Layihe isleyir</h1>
                    <p>Bu sehife localhost:8081 uzerinde acilir.</p>
                    <p>Yoxlama <a href="/salam">/salam</a> endpoint-ini de sinaya bilersen.</p>
                </body>
                </html>
                """;
    }

    @GetMapping("/salam") 
    public String salam() {
        return "Bəli! Axır ki, server kodlarimizi oxudu!";
    }
}