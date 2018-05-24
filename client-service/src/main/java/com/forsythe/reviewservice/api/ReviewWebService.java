package com.forsythe.reviewservice.api;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import org.flowable.engine.IdentityService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class ReviewWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewWebService.class);
    private static final String CLIENT_REVIEW_DEPLOYMENT_KEY = "client-update-review-process";
    private static final String DEFAULT_PROCESS_USER = "admin";
    private static final String ENTITY_STATUS_FIELD = "status";

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private Vertx vertx;

    @RequestMapping("/task/complete/{processId}")
    public ResponseEntity<Map> completeCurrentTask(@PathVariable String processId) {
        Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        taskService.complete(task.getId());
        return new ResponseEntity<>(Collections.singletonMap("processId", task.getId()), HttpStatus.OK);
    }

    @RequestMapping(value = "/signal/send/{signal}", method = RequestMethod.POST)
    public void sendSignalEvent(@PathVariable String signal, @RequestBody Map<String, Object> variables) {
        processEngine.getRuntimeService().signalEventReceived(signal, variables);
    }

    @RequestMapping(value = "/signal/send/{processId}/{signal}", method = RequestMethod.POST)
    public void sendProcessSignalEvent(@PathVariable String processId, @PathVariable String signal,
                                       @RequestBody Map<String, Object> variables) {
        List<Execution> executions = processEngine.getRuntimeService().createExecutionQuery()
                .processInstanceId(processId).signalEventSubscriptionName(signal).list();
        if (executions.isEmpty()) {
            LOGGER.warn("No active signal subscription registered for process {}", processId);
        } else {
            executions.forEach(execution -> {
                processEngine.getRuntimeService().signalEventReceived(signal, execution.getId(), variables);
                LOGGER.warn("Signal {} sent to execution {}", signal, processId);
            });
        }
    }

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void subscribe() {
        vertx.eventBus().consumer("CLIENT_REVIEW_SERVICE").handler(event -> {
            identityService.setAuthenticatedUserId(DEFAULT_PROCESS_USER);

            Map<String, Object> variables = (Map<String, Object>) Json.decodeValue((String) event.body(), Map.class);
            variables.put(ENTITY_STATUS_FIELD, "pending");

            ProcessInstance processInstance = processEngine.getRuntimeService()
                    .startProcessInstanceByKey(CLIENT_REVIEW_DEPLOYMENT_KEY, variables);

            LOGGER.info("Process Instance {}", processInstance.getProcessInstanceId());
        });
    }
}
