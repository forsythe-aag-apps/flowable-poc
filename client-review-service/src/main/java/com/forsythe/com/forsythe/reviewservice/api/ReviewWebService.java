package com.forsythe.com.forsythe.reviewservice.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/")
public class ReviewWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewWebService.class);

    private static final String CLIENT_REVIEW_DEPLOYMENT_KEY = "client-review-process";

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    protected HistoryService historyService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @RequestMapping("/process/start/{clientId}")
    public ResponseEntity<Map> startReview(@PathVariable String clientId) {
        identityService.setAuthenticatedUserId("admin");
        ProcessInstance instance = processEngine.getRuntimeService()
                .startProcessInstanceByKey("payment-client-review-process", Collections.singletonMap("client", clientId));
        return new ResponseEntity<>(Collections.singletonMap("processId", instance.getId()), HttpStatus.OK);
    }

    @RequestMapping("/task/complete/{processId}")
    public ResponseEntity<Map> completeCurrentTask(@PathVariable String processId) {
        Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        taskService.complete(task.getId());
        return new ResponseEntity<>(Collections.singletonMap("processId", task.getId()), HttpStatus.OK);
    }

    @RequestMapping(value = "/invoke/sendEvent/{event}", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void logEvent(@PathVariable String event, @RequestBody Map body) throws JsonProcessingException {
        LOGGER.info("Event sent: " + event);
        LOGGER.info(new ObjectMapper().writeValueAsString(body));
    }
}
