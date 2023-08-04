package com.openclassrooms.paymybuddy.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.paymybuddy.model.ComptePMB;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.Utilisateur;
import com.openclassrooms.paymybuddy.repository.ComptePMBRepository;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private ComptePMBRepository comptePMBRepository;

	@Autowired
	private UtilisateurService utilisateurService;

	private static Logger logger = LoggerFactory.getLogger(TransactionService.class);

	/**
	 * Retourne toutes les transactions enregistrées pour des tests en phase de
	 * developpement
	 * 
	 * @return toutes les transactions enregistrées dans la base
	 */
	public Iterable<Transaction> getAllTransactions() {
		logger.info("Recuperation de toutes les transactions de la base");
		return transactionRepository.findAll();
	}

	/**
	 * Recupere le compte associé à l'utilisateur
	 * 
	 * @param utilisateur
	 * @return le compte associé à l'utilisateur
	 */
	public ComptePMB getComptePMB(Utilisateur utilisateur) {
		logger.info("Recuperation du compte de l'utilisateur " + utilisateur.getNom());
		return comptePMBRepository.findByUtilisateurEmail(utilisateur.getEmail());

	}

	/**
	 * Recupere le compte associé à l'utilisateur
	 * 
	 * @param utilisateur
	 * @return le compte associé à l'utilisateur
	 */
	public ComptePMB getComptePMB(String email) {
		logger.info("Recuperation du compte de l email ");
		return comptePMBRepository.findByUtilisateurEmail(email);

	}

	/**
	 * débite le montant du compte PMB de l'utilisateur
	 * 
	 * @param amount      montant à débiter
	 * @param utilisateur à débiter
	 * @return true si l'opération a fonctionné
	 */
	public boolean debitToBankAccount(BigDecimal amount, Utilisateur utilisateur) {
		logger.info("debit bank account from " + utilisateur.getNom() + " of euros : " + amount);

		if (controleAmountBeforeDebit(amount, utilisateur)) {
			ComptePMB compte = getComptePMB(utilisateur);
			compte.setMontant(compte.getMontant().subtract(amount));
			comptePMBRepository.save(compte);
			return true;

		} else {
			logger.error("not enough money on the account to perform the operation");
			return false;
		}

	}

	/**
	 * crédite le montant sur le compte PMB de l'utilisateur
	 * 
	 * @param amount      montant a créditer
	 * @param utilisateur à créditer
	 * @return true
	 */
	public boolean creditFromBankAccount(BigDecimal amount, Utilisateur utilisateur) {
		logger.info("credit bank account from " + utilisateur.getNom() + " of euros : " + amount);
		ComptePMB compte = getComptePMB(utilisateur);
		compte.setMontant(amount.add(compte.getMontant()));
		comptePMBRepository.save(compte);
		return true;

	}

	/**
	 * Vérifie que l'utilisateur des fonds dispose bien des fonds à lui débiter
	 * 
	 * @param amount      a débiter
	 * @param utilisateur à vérifier
	 * @return true si l'utilisateur dispose bien des fonds
	 */
	public boolean controleAmountBeforeDebit(BigDecimal amount, Utilisateur utilisateur) {
		logger.info("check account amount");
		ComptePMB compte = getComptePMB(utilisateur);
		if (compte.getMontant().compareTo(amount) < 0) {
			logger.error("not enough money");
			return false;
		} else {
			return true;
		}

	}

	/**
	 * transfert le montant du compte de l'utilisateur 1 vers l'utilisateur2
	 * 
	 * @param amount
	 * @param utilisateur1 envoie les fonds
	 * @param utilisateur2 recoie les fonds
	 * @return true si l'opération a fonctionné
	 */
	public void utilisateur1SendMoneyToUtilisateur2(BigDecimal amount, Utilisateur utilisateur1,
			Utilisateur utilisateur2) {
		logger.info("perform utilisateur1SendMoneyToUtilisateur2");

		ComptePMB compte1 = getComptePMB(utilisateur1);
		compte1.setMontant(compte1.getMontant().subtract(amount));
		comptePMBRepository.save(compte1);
		logger.info("compte de " + utilisateur1.getNom() + " debite");
		ComptePMB compte2 = getComptePMB(utilisateur2);
		compte2.setMontant(compte2.getMontant().add(amount));
		comptePMBRepository.save(compte2);
		logger.info("compte de " + utilisateur2.getNom() + " credite");

	}

	/**
	 * Récupère les infos du controller pour générer le transfert d'argent dans le bon sens
	 * 
	 * @param amount montant a transférer
	 * @param action sens du transfert d'argent
	 * @param email du 2e utilisateur
	 * @param utilisateur connecté
	 * return int code qui précise le cas rencontré pour le controller
	 */
	public int moneyTransfertService(BigDecimal amount, String action, String email, Utilisateur utilisateur) {
		logger.info("perform moneyTransfertService");
		Utilisateur utilisateur2 = utilisateurService.getUtilisateurByEmail(email);
		//inchangé si l'on demande trop d'argent a notre contact
		int result=0;

		if (action.equals("envoyer")) {
			if (controleAmountBeforeDebit(amount, utilisateur)) {				
				utilisateur1SendMoneyToUtilisateur2(amount, utilisateur, utilisateur2);
				result = 1;
			} else {
				result = 4;
			}
											
		} else if (action.equals("recevoir")) {
			if (controleAmountBeforeDebit(amount, utilisateur2)) {
				utilisateur1SendMoneyToUtilisateur2(amount, utilisateur2, utilisateur);
				result = 2;
			}
		} else {
			logger.error("action non prise en charge");
			result = 3;
		}
		
		
		return result;
		
	}

}
