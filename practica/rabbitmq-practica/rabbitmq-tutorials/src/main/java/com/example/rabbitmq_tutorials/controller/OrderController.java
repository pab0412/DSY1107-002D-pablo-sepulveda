package com.example.rabbitmq_tutorials.controller;

import com.example.rabbitmq_tutorials.model.dto.OrderRequest;
import com.example.rabbitmq_tutorials.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/send")
    public Map<String, String> sendOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.sendOrder(orderRequest);
    }

    @GetMapping("/status")
    public Map<String, String> getStatus() {
        Map<String, String> status = new HashMap<>();
        status.put("backend", "Online");
        status.put("rabbitmq", "Conectado");
        status.put("timestamp", LocalDateTime.now().toString());
        return status;
    }
}