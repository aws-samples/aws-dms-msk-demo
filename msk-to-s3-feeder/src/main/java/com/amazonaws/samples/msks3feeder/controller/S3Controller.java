// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.samples.msks3feeder.controller;

import com.amazonaws.samples.msks3feeder.entity.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.List;
import java.util.UUID;


@ComponentScan
@Controller
public class S3Controller {

    ObjectMapper mapper = new ObjectMapper();
    Logger logger = LoggerFactory.getLogger(S3Controller.class);

    // Create the S3Client object
    @Value("aws.s3.region")
    Region region;

    @Value("${aws.s3.bucketName}")
    String bucketName;

    S3Client s3Client = S3Client.builder()
                            .region(region)
                            .build();

    public S3Controller(){ }

    public void sendOrderListAsJson(List<Order> orders){

        if(StringUtils.isEmpty( bucketName))
        {
            logger.error("The bucket name is empty. Please assign valid bucket name.");
            return;
        }

        StringBuilder builder = new StringBuilder();

        if(orders!= null && orders.size() >0) {
            //Build json message
            for (Order order : orders) {
                try {
                    builder.append(mapper.writeValueAsString(order));
                } catch (JsonProcessingException e) {
                    logger.error(e.getMessage(), e);

                }
            }
            String key = UUID.randomUUID().toString() + "-" +System.currentTimeMillis()  +  ".json";
            //write to s3 here
            s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(key)
                    .build(), RequestBody.fromString(builder.toString()));
            logger.info("written object to s3 with key:" + key + " with orders count:" + orders.size());
        }
    }
}
