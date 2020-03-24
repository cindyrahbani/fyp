package murex.dev.mxem.Environments.service;


import murex.dev.mxem.Environments.model.Request;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {
    @Autowired
    private AmqpTemplate myRabbitTemplate;

    @Value("${javainuse.rabbitmq.exchange}")
    private String exchange;

    @Value("${javainuse.rabbitmq.routingkey}")
    private String routingkey;

    public void send(Request request) {
        myRabbitTemplate.convertAndSend(exchange, routingkey, request);
        System.out.println("Send msg = " + request);

    }
}
