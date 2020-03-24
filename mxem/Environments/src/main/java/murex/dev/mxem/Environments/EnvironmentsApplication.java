package murex.dev.mxem.Environments;

import murex.dev.mxem.Environments.model.Environment;
import murex.dev.mxem.Environments.repository.EnvironmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import java.util.Arrays;

@EnableAutoConfiguration
@SpringBootApplication
@EnableDiscoveryClient
@EnableMongoRepositories(basePackages = "murex.dev.mxem.Environments")
public class EnvironmentsApplication{

	public static void main(String[] args) {
		SpringApplication.run(EnvironmentsApplication.class, args);
	}
}
