package com.openclassrooms.paymybuddy.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.openclassrooms.paymybuddy.model.Contact;
import com.openclassrooms.paymybuddy.model.Utilisateur;
import com.openclassrooms.paymybuddy.repository.ContactRepository;
import com.openclassrooms.paymybuddy.repository.UtilisateurRepository;


@Service
public class ContactService {
	
	@Autowired
	private UtilisateurRepository utilisateurRepository;
		
	@Autowired
	private ContactRepository contactRepository;
			
	private static Logger logger = LoggerFactory.getLogger(ContactService.class);
	
	/**
	 * Retourne tous les contacts de la base
	 * 
	 * @return liste de tous les contacts enregistrés
	 */
	public List<Contact> getAllContacts() {
		logger.info("perform get all contacts");
		return contactRepository.findAll();		
	}
	
	/**
	 * retourne tous les contacts de l'utilisateur spécifié
	 * 
	 * @param id de l'utilisateur
	 * @returntous les contacts de l'utilisateur spécifié
	 */
	public List<Contact> getAllContactsByUserId(int userId) {
		logger.info("perform get all contacts from user " +userId );
		int result = contactRepository.findByUtilisateurId(userId).size();
		logger.info("Nombre de contacts : "+result);
		return contactRepository.findByUtilisateurId(userId);
	}
	
	/**
	 * Vérifie que le contact est bien rattaché à l'utilisateur avant toute modification
	 * @param utilisateur
	 * @param contact
	 * @return true si le contact appartient bien a l'utilisateur
	 */
	public boolean matchUtilisateurContact(Utilisateur utilisateur, Contact contact) {
		logger.info("Verification de correspondance Utilisateur/Contact");
		if (contact.getUtilisateur().getUtilisateurId() == utilisateur.getUtilisateurId()) {
			logger.info("Correspondance contact/utilisateur trouvée");
			return true;
			
		} else {
			logger.error("Le contact ne semble pas etre associé a l'utilisateur");
			return false;
		}
		
	}
	
	/**
	 * Ajoute un contact à la liste de contact de l'utilisateur. Seuls les utilisateurs peuvent etre des contacts.
	 * 
	 * @param utilisateur qui ajoute le contact
	 * @param contact a ajouter
	 * @return le contact ajouté
	 */
	public Contact addContact(Utilisateur utilisateur, Contact contact) {
		logger.info("Ajout du contact a l'utilisateur");
		if (utilisateurRepository.findByEmail(contact.getEmail())!= null) {
			utilisateur.getContacts().add(contact);
			utilisateurRepository.save(utilisateur);
			
		} else {
			logger.error("Le contact n'est pas un utilisateur");
		}

				
		return contact;
		
	}
	
	/**
	 * Supprime le contact de la liste de contact de l'utilisateur
	 * 
	 * @param utilisateur qui supprime le contact
	 * @param contact a supprimer
	 * @return le contact supprimé
	 */
	public Contact deleteContact(Utilisateur utilisateur, Contact contact) {
		logger.info("Suppression du contact");
		if (matchUtilisateurContact(utilisateur, contact)) {		
			utilisateur.getContacts().remove(contact);
			utilisateurRepository.save(utilisateur);
			
		} else {
			logger.error("suppression du contact impossible");
		}
		return contact;
	}
	
	/**
	 * Met a jour dynamiquement le contact
	 * 
	 * @param utilisateur
	 * @param contact
	 * @return le contact a jour
	 */
	public Contact updateContact(Utilisateur utilisateur, Contact contact) {
		logger.info("Mise a jour du contact");
		if (matchUtilisateurContact(utilisateur, contact)) {
			contactRepository.save(contact);			
		}

		return contact;
	}
	
	public Page<Contact> findPaginated(int pageNo, int pageSize) {
		logger.info("pagine les contacts");
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Contact> result = contactRepository.findAll(pageable);
		logger.info("findAll de pageable contient : "+result.getTotalElements());
		return result;
	}

}
