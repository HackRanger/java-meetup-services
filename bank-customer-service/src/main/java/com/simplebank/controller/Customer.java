package com.simplebank.controller;

import com.simplebank.service.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;


@RestController
@RequestMapping("customer")
@Api(value="Customer controller", description="",tags = {"Customer"})
public class Customer {
	   private final KafkaProducer producer;

	    @Autowired
	    Customer(KafkaProducer producer) {
	        this.producer = producer;
	    }
		
		@GetMapping
		public String getMessage(){
			
			return "Hello World!";

		}
		
		@PostMapping
		public void sendMessage(@RequestBody String msg) {
			this.producer.sendMessage("mytopic",msg);
		}

}
