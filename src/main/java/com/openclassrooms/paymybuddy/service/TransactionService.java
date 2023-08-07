package com.openclassrooms.paymybuddy.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.openclassrooms.paymybuddy.model.ComptePMB;
import com.openclassrooms.paymybuddy.model.EtatFacturation;
import com.openclassrooms.paymybuddy.model.StatusTransaction;
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
	public Transaction utilisateur1SendMoneyToUtilisateur2(BigDecimal amount, Utilisateur utilisateur1,
			Utilisateur utilisateur2) {
		logger.info("perform utilisateur1SendMoneyToUtilisateur2");
		
		ComptePMB compte1 = getComptePMB(utilisateur1);
		ComptePMB compte2 = getComptePMB(utilisateur2);
		BigDecimal montantCommission = calculCommission(0.5f, amount);
		Transaction transaction = saveTransaction(amount, montantCommission, compte1, compte2);
		
		compte1.setMontant(compte1.getMontant().subtract(amount));
		comptePMBRepository.save(compte1);
		logger.info("compte de " + utilisateur1.getNom() + " debite");		
		compte2.setMontant(compte2.getMontant().add(amount).subtract(montantCommission));
		comptePMBRepository.save(compte2);
		logger.info("compte de " + utilisateur2.getNom() + " credite");
		
		return transaction;
		

	}
	
	/**
	 * génère et sauvegarde l'écriture de la transaction pour les transferts d'argent
	 * 
	 * @param amount de la transaction
	 * @param compte1 emetteur
	 * @param compte2 destinataire
	 * @return Transaction sauvegardée
	 */
	public Transaction saveTransaction(BigDecimal amount, BigDecimal montantCommission, ComptePMB compte1,
			ComptePMB compte2) {
		logger.info("enregistrement de la transaction");
		Date date = new Date();
		Transaction transaction = new Transaction();
		transaction.setEmetteur(compte1);
		transaction.setDestinataire(compte2);
		transaction.setSomme(amount);
		transaction.setCommission(0.5f);
		transaction.setMontantCommission(montantCommission);
		transaction.setHorodatage(date);
		transaction.setStatus(StatusTransaction.VALIDE);
		transaction.setEtatFacturation(EtatFacturation.A_EDITER);
		
		transactionRepository.save(transaction);
		
		return transaction;
		
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
	
	/**
	 * Récupere la liste des transactions de l'utilisateur connecté et met en page les résultats
	 * 
	 * @param pageNo n° de la page de résultat en cours
	 * @param pageSize nombre de résultat par page
	 * @param sortField le champs utilisé pour ordonner les résultats
	 * @param sortDirection
	 * @return la liste des transactions de l'utilisateur connecté formatée pour etre affiché par page
	 */
	public Page<Transaction> findPaginatedTransactions(int pageNo, int pageSize, String sortField, String sortDirection) {
		logger.info("pagine les transactions");
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    Utilisateur utilisateur = ((Utilisateur) authentication.getPrincipal());
		
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(pageNo -1, pageSize, sort);
		List<Transaction> result = getAllTransactionsFromUtilisateur(utilisateur);
		Page<Transaction> paginatedResult = convertListToPage(result, pageable);
		return paginatedResult;
		
	}
	
	/**
	 * Retourne toutes les transactions liées à l'utilisateur
	 * Qu'il soit emetteur ou destinataire
	 * 
	 * @param utilisateur
	 * @return Liste de transactions liées à l'utilisateur
	 */
	public List<Transaction> getAllTransactionsFromUtilisateur(Utilisateur utilisateur) {
		logger.info("Appel de getAllTransactionsFromUtilisateur");
		List<Transaction> result = transactionRepository.findByDestinataireComptePMBId(utilisateur.getUtilisateurId());
		result.addAll(transactionRepository.findByEmetteurComptePMBId(utilisateur.getUtilisateurId()));
				
		return result;
	}
	
	/**
	 * Converti une liste de transaction en Page de transaction
	 * Pour un meilleur affichage en front
	 * 
	 * @param transactions la liste a convertir en page
	 * @param pageable
	 * @return 
	 */
	public Page<Transaction> convertListToPage(List<Transaction> transactions, Pageable pageable) {
	    logger.info("appel de convertListToPage");
		int start = (int) pageable.getOffset();
	    int end = Math.min((start + pageable.getPageSize()), transactions.size());
	    return new PageImpl<>(transactions.subList(start, end), pageable, transactions.size());
	}
	
	/**
	 * Calcul la commission a prélever sur le transfert entre les utilisateurs
	 * @param commission partie numérique, du taux de commission, a diviser par 100 pour les calculs 
	 * @param montant de la transaction
	 * @return montant de la commission a prélever sur le destinataire du transfert
	 */
	public BigDecimal calculCommission(float commission, BigDecimal montant) {
		logger.info("appel de calculCommission");
		BigDecimal commissionPercent = BigDecimal.valueOf(commission);
        BigDecimal result = montant.multiply(commissionPercent).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return result;
		
	}
	
	/**
	 * Calcul la commission a prélever
	 * 
	 * @param transaction
	 * @return montant de la commission a prélever sur le destinataire du transfert
	 * @see #calculCommission(float, BigDecimal)
	 */
	public BigDecimal calculCommission(Transaction transaction) {
		return calculCommission(transaction.getCommission(), transaction.getSomme());
	}
	
	

}
