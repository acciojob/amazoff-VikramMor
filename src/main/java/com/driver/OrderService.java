package com.driver;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public void addOrderService(@RequestBody Order order){
        orderRepository.addOrderToDB(order);
    }

    public void addPartnerService(String partnerId) {
        orderRepository.addPartnerToDB(partnerId);
    }

    public void addOrderPartnerPairService(String orderId, String partnerId) {
        orderRepository.addOrderPartnerPairToDB(orderId,partnerId);
    }

    public Order getOrderByIdService(String orderId) {
        return orderRepository.getOrderByIdFromDB(orderId);
    }

    public DeliveryPartner getPartnerByIdService(String partnerId) {
        return orderRepository.getPartnerByIdFromDB(partnerId);
    }

    public Integer getOrderCountByPartnerIdService(String partnerId) {
        return orderRepository.getOrderCountByPartnerIdFromDB(partnerId);
    }

    public List<String> getOrdersByPartnerIdService(String partnerId) {
        return orderRepository.getOrdersByPartnerIdFromDB(partnerId);
    }

    public List<String> getAllOrdersService() {
        return orderRepository.getAllOrdersFromDB();
    }

    public Integer getCountOfUnassignedOrdersService() {
        return orderRepository.getCountOfUnassignedOrdersFromDB();
    }

    public Integer getOrdersLeftAfterGivenTimeService(String time, String partnerId) {
        return orderRepository.getOrdersLeftAfterGivenTimeFromDB(time,partnerId);
    }

    public String getLastDeliveryTimeByPartnerIdService(String partnerId) {
        return orderRepository.getLastDeliveryTimeByPartnerIdFromDB(partnerId);
    }

    public void deletePartnerByIdService(String partnerId) {
        orderRepository.deletePartnerByIdFromDB(partnerId);
    }

    public void deleteOrderByIdService(String orderId) {
        orderRepository.deleteOrderByIdFromDB(orderId);
    }
}
