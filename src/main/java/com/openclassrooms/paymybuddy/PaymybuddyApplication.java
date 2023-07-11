package com.openclassrooms.paymybuddy;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.InputStreamResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UtilisateurService;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class PaymybuddyApplication implements CommandLineRunner {
	
	@Autowired
    private DataSource dataSource;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private UtilisateurService utilisateurService;

    @Autowired
    private ApplicationContext applicationContext;


	public static void main(String[] args) {
		SpringApplication.run(PaymybuddyApplication.class, args);
	}
	
	@PostConstruct
	public void initializeDatabase() throws SQLException, IOException {
	    Resource resource = applicationContext.getResource("classpath:AlimentationDB.sql");
	    try (InputStream inputStream = resource.getInputStream();
	         Connection connection = dataSource.getConnection()) {
	        ScriptUtils.executeSqlScript(connection, new InputStreamResource(inputStream));
	    }
	    System.out.println("Les requêtes SQL ont été exécutées avec succès !");
	}
	
	@Override
	public void run(String... args) throws Exception {		
		
	}


}
