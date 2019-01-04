package com.minor.store;

import com.minor.store.config.ApplicationConfiguration;
import com.minor.store.utils.log.LogWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableConfigurationProperties({ ApplicationConfiguration.class })
public class MiPosApiApplication {

	private static LogWrapper log = LogWrapper.create(MiPosApiApplication.class);

	@Autowired
	private ApplicationConfiguration applicationConfiguration;

	public static void main(String[] args) {
		SpringApplication.run(MiPosApiApplication.class, args);
	}

	@PostConstruct
	public void initApplication() {
		log.infoMask("Config : {}", applicationConfiguration);
	}

}

