package com.forsythe.com.forsythe.reviewservice.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogServiceTask implements JavaDelegate {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogServiceTask.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOGGER.info(String.valueOf("Completed process: " + execution.getProcessInstanceId()));
    }
}
