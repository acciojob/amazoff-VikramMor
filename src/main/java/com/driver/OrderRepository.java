package com.driver;


import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {

    private HashMap<String, Order> orderHashMap = new HashMap<>();
    private HashMap<String, DeliveryPartner> partnerHashMap = new HashMap<>();
    private HashMap<String, List<String>> orderPartnerPairHashMap = new HashMap<>();
    private HashMap < String, Boolean> orderAssignedHashMap;
    private HashMap < String, String> orderDeliveryPartnerMap;

    public void addOrderToDB(@RequestBody Order order){
        orderHashMap.put(order.getId(), order);
    }

    public void addPartnerToDB(String partnerId){
        DeliveryPartner deliveryPartner =new DeliveryPartner(partnerId);
        partnerHashMap.put(deliveryPartner.getId(),deliveryPartner);
    }

    public void addOrderPartnerPairToDB(String orderId, String partnerId){
        if(orderHashMap.containsKey(orderId) && partnerHashMap.containsKey(partnerId)){
            List<String> orders = new ArrayList<>();
            if(orderPartnerPairHashMap.containsKey(partnerId))
                orders = orderPartnerPairHashMap.get(partnerId);
            orders.add(orderId);
            orderAssignedHashMap.put(orderId,false);
            orderDeliveryPartnerMap.put(orderId,partnerId);
            DeliveryPartner deliveryPartner = partnerHashMap.get(partnerId);
            deliveryPartner.setNumberOfOrders(orders.size());
            orderPartnerPairHashMap.put(partnerId,orders);
        }
    }

    public Order getOrderByIdFromDB(String orderId){
        Order order = null;
        if(orderHashMap.containsKey(orderId))
            order = orderHashMap.get(orderId);
        return order;
    }

    public DeliveryPartner getPartnerByIdFromDB(String partnerId){
        DeliveryPartner deliveryPartner = null;
        if(partnerHashMap.containsKey(partnerId))
            deliveryPartner = partnerHashMap.get(partnerId);
        return deliveryPartner;
    }

    public Integer getOrderCountByPartnerIdFromDB(String partnerId){
        Integer result = null;
        if(partnerHashMap.containsKey(partnerId)) {
            DeliveryPartner partner = partnerHashMap.get(partnerId);
            result = partner.getNumberOfOrders();
        }
        return result;
    }

    public List<String> getOrdersByPartnerIdFromDB(String partnerId){
        List<String> orders = null;
        if(orderPartnerPairHashMap.containsKey(partnerId))
            orders = orderPartnerPairHashMap.get(partnerId);
        return orders;
    }

    public List<String> getAllOrdersFromDB(){
        return new ArrayList<>(orderHashMap.keySet());
    }

    public int getCountOfUnassignedOrdersFromDB(){
        return orderHashMap.size()-orderAssignedHashMap.size();
    }

    public Integer getOrdersLeftAfterGivenTimeFromDB(String time, String partnerId){
        Integer count  = null;
        int timeT = (Integer.parseInt(time.substring(0,1))*60 + Integer.parseInt(time.substring(3,4)));
        List<String> orderIds = new ArrayList<>();
        if(orderPartnerPairHashMap.containsKey(partnerId)) {
            orderIds = orderPartnerPairHashMap.get(partnerId);
            int leftCount = orderIds.size();
            for (String orderId : orderIds) {
                Order order = orderHashMap.get(orderId);
                if(timeT >= order.getDeliveryTime()){
                    orderAssignedHashMap.put(order.getId(),true);
                    leftCount--;
                }
            }
            count  = leftCount;
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerIdFromDB(String partnerId){
        String lastDeliveryTime = "";
        List<String> orderIds = new ArrayList<>();
        if(orderPartnerPairHashMap.containsKey(partnerId)) {
            orderIds = orderPartnerPairHashMap.get(partnerId);
            int time = 0;
            for (String orderId : orderIds) {
                if (orderAssignedHashMap.get(orderId)) {
                    Order order = orderHashMap.get(orderId);
                    if (time < order.getDeliveryTime()) {
                        time = order.getDeliveryTime();
                    }
                }
            }
            lastDeliveryTime = intToStringTime(time);
        }
        return lastDeliveryTime;
    }

    public String intToStringTime(int time){
        int min = time%60;
        int hour = time/60;
        String result = Integer.toString(hour) + ":" + Integer.toString(min);
        return result;
    }

    public void deletePartnerByIdFromDB(String partnerId){
        if(partnerHashMap.containsKey(partnerId)) {
            if (orderPartnerPairHashMap.containsKey(partnerId)) {
                List<String> orders = new ArrayList<>();
                orders = orderPartnerPairHashMap.get(partnerId);
                for (String orderId : orders) {
                    if (orderAssignedHashMap.containsKey(orderId)) {
                        orderAssignedHashMap.remove(orderId);
                    }
                }
                orderPartnerPairHashMap.remove(partnerId);
            }
            orderPartnerPairHashMap.remove(partnerId);
        }
    }

    public void deleteOrderByIdFromDB(String orderId){
        if(orderHashMap.containsKey(orderId)){
            if(orderAssignedHashMap.containsKey(orderId)){
                List<String> orders = new ArrayList<>();
                String partnerId = orderDeliveryPartnerMap.get(orderId);
                orders = orderPartnerPairHashMap.get(partnerId);
                orders.remove(orderId);
                orderPartnerPairHashMap.put(partnerId,orders);
                orderDeliveryPartnerMap.remove(orderId);
                orderAssignedHashMap.remove(orderId);
            }
            orderHashMap.remove(orderId);
        }
    }
}
