package com.simplebank.controller;

import com.simplebank.dto.CustomerDto;
import com.simplebank.service.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("customer")
@Api(value = "Customer controller", description = "This is customer service endpoint", tags = { "Customer" })
public class Customer {
	@Autowired
	private KafkaProducer producer;

	@Value("${topic.newcustomer}")
	private String newCustomerTopic;

	@PostMapping("/{customerNumber}")
	public void createCustomer(@PathVariable("customerNumber") String customerNumber,
			@RequestBody CustomerDto customer) {
		this.producer.sendMessage(newCustomerTopic, customerNumber, customer.toString());
	}

}
