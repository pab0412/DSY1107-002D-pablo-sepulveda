package com.example.rabbitmq_tutorials.model;

import com.example.rabbitmq_tutorials.config.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    /**
     * Consumidor principal: Recibe órdenes de la cola principal.
     * Simula fallos aleatorios para demostrar el paso a la DLX.
     */
    @RabbitListener(queues = RabbitMQConfig.ORDERS_QUEUE)
    public void processOrder(
            String message,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            System.out.println("[PROCESADOR] Recibida orden: " + message);

            // Simulación de fallo (50% probabilidad)
            if (Math.random() < 0.5) {
                throw new RuntimeException("Error simulado al procesar: " + message);
            }

            System.out.println("[ÉXITO] Orden procesada correctamente: " + message);
            
            // Confirmación manual (Ack)
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
            try {
                // Reject/Nack enviando directamente a DLX (requeue=false)
                channel.basicNack(deliveryTag, false, false);
                System.out.println("[→ DLX] Mensaje enviado a Dead Letter Exchange");
            } catch (Exception nackException) {
                nackException.printStackTrace();
            }
        }
    }

    /**
     * Consumidor de DLQ: Monitorea mensajes rechazados/fallidos.
     */
    @RabbitListener(queues = RabbitMQConfig.DLQ_QUEUE)
    public void processDLQ(String message) {
        System.out.println(" [DLQ] MENSAJE EN CUARENTENA: " + message);
        System.out.println(" → Revisar logs del procesador para diagnóstico del fallo.");
    }
}