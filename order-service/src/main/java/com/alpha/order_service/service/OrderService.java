package com.alpha.order_service.service;

import com.alpha.order_service.client.InventoryClient;
import com.alpha.order_service.dto.OrderRequest;
import com.alpha.order_service.event.OrderPlacedEvent;
import com.alpha.order_service.model.Order;
import com.alpha.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void placeOrder(OrderRequest orderRequest) {
        var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
        if (isProductInStock) {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.price());
            order.setQuantity(orderRequest.quantity());
            order.setSkuCode(orderRequest.skuCode());

            orderRepository.save(order);

            OrderPlacedEvent orderPlacedEvent = OrderPlacedEvent.newBuilder()
                    .setOrderNumber(order.getOrderNumber())
                    .setEmail(orderRequest.userDetails().email())
                    .setFirstName(orderRequest.userDetails().firstName() != null ? orderRequest.userDetails().firstName() : "")
                    .setLastName(orderRequest.userDetails().lastName() != null ? orderRequest.userDetails().lastName() : "")
                    .build();
            log.info("start - sending OrderPlacedEvent {} to kafka topic order-placed", orderPlacedEvent);
            kafkaTemplate.send("order-placed", orderPlacedEvent);
            log.info("end - sending OrderPlacedEvent {} to kafka topic order-placed", orderPlacedEvent);
        }else {
            throw new RuntimeException("Product with SKU code " + orderRequest.skuCode() + " is not in stock");
        }

    }
}
