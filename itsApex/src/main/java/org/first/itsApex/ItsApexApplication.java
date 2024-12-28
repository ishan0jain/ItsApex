package org.first.itsApex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class ItsApexApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItsApexApplication.class, args);
	}

}
