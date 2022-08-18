package com.udemy.os.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.udemy.os.services.DBService;

@Configuration
@Profile("dev")
public class DevConfig {
	
	@Autowired
	private DBService dbService;
	
	@Value("${spring.jpa.hibernate.ddl-auto}") //TemplateString para buscar o valor do application-properties
	private String ddl;
	
	@Bean /*Sempre que esse objeto TestConfig for instanciado, chama esse método*/
	public boolean instanciaDB() {
		if(ddl.equals("create")) {
			this.dbService.instanciaDB();
		}
		
		return false;
	}
}
