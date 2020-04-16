package com.simplebank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.simplebank.dto.FundTransferDto;
import com.simplebank.service.KafkaProducer;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("fundtransfer")
@Api(value = "Fundtransfer controller", description = "This is fundtransfer service endpoint", tags = { "fundtransfer" })
public class FundTransfer {
	@Autowired
	private KafkaProducer producer;

	@Value("${topic.transaction}")
	private String transactionTopic;

	@PostMapping("/{transferId}")
	public void createFundTransferInstruction(@PathVariable("transferId") String transferId,
			@RequestBody FundTransferDto fundTransferDto) {
		this.producer.sendMessage(transactionTopic, transferId, fundTransferDto.toString());
	}

}
