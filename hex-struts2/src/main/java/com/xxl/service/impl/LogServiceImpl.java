package com.xxl.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xxl.service.ILogService;

/**
 * Log
 * @author xuxueli
 */

@Service("logService")
public class LogServiceImpl implements ILogService{
	private static transient Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

	@Override
	public void loginLog(String userId) {
		logger.info("login log, userId:{}, time:{}", new Object[]{userId, new Date()});
	}

}
