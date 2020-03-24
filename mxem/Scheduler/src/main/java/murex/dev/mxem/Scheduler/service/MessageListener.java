package murex.dev.mxem.Scheduler.service;

import murex.dev.mxem.Scheduler.model.Request;
import murex.dev.mxem.Scheduler.model.Status;
import murex.dev.mxem.Scheduler.repository.RequestRepository;
import murex.dev.mxem.Scheduler.repository.StatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageListener {

    private static final Logger log = LoggerFactory.getLogger(MessageListener.class);

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    PipelineService pipelineService;

    @Autowired
    StatusRepository statusRepository;

//    @Autowired
//    SchedulerRequestService schedulerRequestService;

    @RabbitListener(queues = "javainuse.queue")
    public void receiveMessage(final Request message) {

        log.info("Received message as generic: {}", message.toString());
        log.info("Resultat;");




        Status status = pipelineService.sendCommandRequest(message);




        statusRepository.save(status);
        message.setOperationId(status.getOperationId());
        requestRepository.save(message);


    }

}