package com.kafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST que envía mensajes a Kafka
 * Demuestra dos conceptos:
 *  - Sin Key: Round Robin (distribuye entre particiones 0, 1, 2...)
 *  - Con Key: Sticky Partitioning (todos a la misma partición)
 */
@RestController
@RequestMapping("/api/kafka")
public class KafkaController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Endpoint para enviar mensajes a Kafka
     * 
     * Ejemplos de uso:
     * SIN KEY (Round Robin):
     * GET http://localhost:8888/api/kafka/send?message=Mensaje1
     * 
     * CON KEY (Misma partición):
     * GET http://localhost:8888/api/kafka/send?message=Mensaje1&key=orden-555
     */
    @GetMapping("/send")
    public String sendMessage(
            @RequestParam String message,
            @RequestParam(required = false) String key) {
            
        String topicName = "tutorial-particiones";

        if (key != null && !key.isEmpty()) {
            // Con Key: Kafka calcula hash(key) para determinar la partición
            kafkaTemplate.send(topicName, key, message);
            return "Mensaje enviado al topic '" + topicName + "' con KEY: " + key + " | MENSAJE: " + message;
        } else {
            // Sin Key: Distribución automática (Round Robin)
            kafkaTemplate.send(topicName, message);
            return "Mensaje enviado al topic '" + topicName + "' (sin key - distribución automática) | MENSAJE: " + message;
        }
    }

    /**
     * Endpoint de verificación
     */
    @GetMapping("/health")
    public String health() {
        return "Aplicación de Kafka Tutorial está corriendo en puerto 8888";
    }
}