// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package com.amazonaws.samples.service;

import com.amazonaws.samples.entity.Constants;
import com.amazonaws.samples.entity.Order;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;


@Service
@EnableAutoConfiguration
public class OrderService {


	Logger logger = LoggerFactory.getLogger(OrderService.class);

	private Random random = new Random();
	
	@Autowired
    SimpMessagingTemplate template;
	
	@KafkaListener(topics="${kafka.topic}")
	public void orders(@Payload String orderMessage) {
		//orderMessage is the message sent by DMS to MSK. It should be a json message.
		Order order = transformToOrder(orderMessage);
		//now we can send this message to our topic for web application to consume.
		template.convertAndSend("/topic/orders", order);
		
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

			return getDummyOrder();
		}
	}


	public Order getDummyOrder(){
		Order order = new Order();
		order.setDate(LocalDateTime.now().toString());
		order.setOrderId(random.nextInt());
		order.setAmount( Double.valueOf(random.nextInt(4000)));
		order.setSource(arrSources[ new Random().nextInt(3)]);
		order.setState(arrStates[ new Random().nextInt(5)]);
		order.setOperation(Constants.Operations.INSERT);
		return order;
	}

	// data for dummy order generation.
	String[] arrStates = new String[]{ "California", "Texas", "Florida", "New York", "Ohio"};
	String[] arrSources = new String[]{"Andriod", "iOS", "Windows", "MacOS"};

}
