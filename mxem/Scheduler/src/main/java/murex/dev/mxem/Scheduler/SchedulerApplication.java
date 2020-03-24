package murex.dev.mxem.Scheduler;

import lombok.extern.slf4j.Slf4j;
import murex.dev.mxem.Scheduler.model.Events;
import murex.dev.mxem.Scheduler.model.Request;
import murex.dev.mxem.Scheduler.model.Status;
import murex.dev.mxem.Scheduler.repository.RequestRepository;
import murex.dev.mxem.Scheduler.repository.StatusRepository;
import murex.dev.mxem.Scheduler.service.PipelineService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;
import java.util.Scanner;

import static java.lang.Thread.sleep;

@EnableAutoConfiguration
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@Slf4j
@EnableMongoRepositories(basePackages = "murex.dev.mxem.Scheduler")
public class SchedulerApplication {



	public static void main(String[] args) {
		SpringApplication.run(SchedulerApplication.class, args);
	}

}
