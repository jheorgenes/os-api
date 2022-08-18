package com.udemy.os.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.udemy.os.services.DBService;

@Configuration
@Profile("test")
public class TestConfig {
	
	@Autowired
	private DBService dbService;
	
	@Bean /*Sempre que esse objeto TestConfig for instanciado, chama esse método*/
	public void instanciaDB() {
		this.dbService.instanciaDB();
	}
}
