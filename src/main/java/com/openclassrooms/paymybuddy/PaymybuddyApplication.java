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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.openclassrooms.paymybuddy.model.Utilisateur;
import com.openclassrooms.paymybuddy.service.UtilisateurService;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class PaymybuddyApplication implements CommandLineRunner {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private UtilisateurService utilisateurService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private ApplicationContext applicationContext;

	public static void main(String[] args) {
		SpringApplication.run(PaymybuddyApplication.class, args);
	}

	
	/**
	 * Injection du fichier SQL pour pré rempli la base de donnees avec quelques valeurs a des fins de tests
	 * 
	 * @throws SQLException
	 * @throws IOException
	 */
	@PostConstruct
	public void initializeDatabase() throws SQLException, IOException {
		Resource resource = applicationContext.getResource("classpath:AlimentationDB.sql");
		try (InputStream inputStream = resource.getInputStream(); Connection connection = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(connection, new InputStreamResource(inputStream));
		}
		System.out.println("Les requêtes SQL ont été exécutées avec succès !");
		
		Iterable<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();
		
		//Cryptage en base des mots de base en clair dans le SQL
		for (Utilisateur utilisateur : utilisateurs) {
			utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
			utilisateurService.updateUtilisateur(utilisateur);
		}
	}

	@Override
	public void run(String... args) throws Exception {

	}

}
