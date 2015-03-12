package com.dh.processor;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dh.service.KnapsackService;
import com.dh.service.PlayerAccountService;

@Component
public class ComposeProcessor {
	@Resource
	private KnapsackService knapsackService;
	@Resource
	private PlayerAccountService playerAccountService;
}
