package com.example.rabbitmq_tutorials.service;

import com.example.rabbitmq_tutorials.model.dto.OrderRequest;
import java.util.Map;

public interface OrderService {
    Map<String, String> sendOrder(OrderRequest request);
}