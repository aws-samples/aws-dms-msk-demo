// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.samples.msks3feeder.controller;

import com.amazonaws.samples.msks3feeder.Application;
import com.amazonaws.samples.msks3feeder.entity.Constants;
import com.amazonaws.samples.msks3feeder.entity.Order;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
@ComponentScan
public class DMSMessageController {

    Logger logger = LoggerFactory.getLogger(Application.class);

    @Autowired
    S3Controller s3Controller;

    @KafkaListener(topics = "${kafka.topic}", groupId = "")
    public void listen(List<String> list) {

        List<Order> orders = new ArrayList<>();
        for(int i=0;i< list.size(); i++) {
            System.out.println(" Message: " + i + "----" + list.get(i));

            orders.add(transformToOrder(list.get(i)));
        }

        s3Controller.sendOrderListAsJson(orders);
    }

    public Order transformToOrder(String dmsMessage) {
        try {


            JSONObject jsonObject = new JSONObject(dmsMessage);

            Order order = new Order();
            JSONObject data = jsonObject.getJSONObject("data");
            if(data	!= null) {
                order.setOrderId(data.getInt("orderId"));
                order.setAmount(data.getDouble("amount"));
                order.setSource(data.getString("source"));
                order.setDate(data.getString("date"));
                order.setState(data.getString("state"));
            }
            JSONObject metadata = jsonObject.getJSONObject("metadata");
            if(metadata != null){
                String operation = metadata.getString("operation");
                if(operation.equalsIgnoreCase( "insert"))
                {
                    order.setOperation(Constants.Operations.INSERT);
                }else if (operation.equalsIgnoreCase("update"))
                {
                    order.setOperation(Constants.Operations.UPDATE);
                }else if (operation.equalsIgnoreCase("delete"))
                {
                    order.setOperation(Constants.Operations.DELETE);
                }else {
                    order.setOperation(Constants.Operations.INSERT);
                }
            }
            return order;
        }
        catch (Exception e){
            logger.error("Unable to parse order. sending dummy.." , e);
        }
        return null;
    }
}
