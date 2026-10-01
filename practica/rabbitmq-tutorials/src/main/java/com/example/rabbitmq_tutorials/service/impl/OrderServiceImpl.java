package com.example.rabbitmq_tutorials.service.impl;

import com.example.rabbitmq_tutorials.config.RabbitMQConfig;
import com.example.rabbitmq_tutorials.model.dto.OrderRequest;
import com.example.rabbitmq_tutorials.service.OrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public Map<String, String> sendOrder(OrderRequest request) {
        String orderId = (request != null && request.getOrderId() != null && !request.getOrderId().isBlank()) 
                ? request.getOrderId() 
                : "ORD-" + System.currentTimeMillis();

        String customerName = (request != null && request.getCustomerName() != null && !request.getCustomerName().isBlank()) 
                ? request.getCustomerName() 
                : "Unknown";

        String message = String.format(
            "Orden ID: %s | Cliente: %s | Hora: %s",
            orderId,
            customerName,
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        );

        // Publicar mensaje en el Exchange de RabbitMQ
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.ORDERS_EXCHANGE,
            RabbitMQConfig.ORDERS_ROUTING_KEY,
            message
        );

        Map<String, String> response = new HashMap<>();
        response.put("status", "Orden enviada");
        response.put("orderId", orderId);
        response.put("message", message);

        return response;
    }
}