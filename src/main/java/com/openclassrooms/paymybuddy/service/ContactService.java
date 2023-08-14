package com.openclassrooms.paymybuddy.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.openclassrooms.paymybuddy.model.Contact;
import com.openclassrooms.paymybuddy.model.Utilisateur;
import com.openclassrooms.paymybuddy.repository.ContactRepository;
import com.openclassrooms.paymybuddy.repository.UtilisateurRepository;


/**
 * Regroupe les services liés à la liste de contacts des utilisateurs, notamment le CRUD
 * 
 * @author Simon
 *
 */
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
		logger.info("Appel de getAllContacts");
		return contactRepository.findAll();		
	}
	
	/**
	 * retourne tous les contacts de l'utilisateur spécifié
	 * 
	 * @param id de l'utilisateur
	 * @return tous les contacts de l'utilisateur spécifié
	 */
	public List<Contact> getAllContactsByUserId(Integer userId) {
		logger.info("Appel de getAllContactsByUserId pour " +userId );
		int result = contactRepository.findByUtilisateurId(userId).size();
		logger.info("Nombre de contacts : "+result);
		return contactRepository.findByUtilisateurId(userId);
	}
	
	/**
	 * Vérifie que le contact est bien rattaché à l'utilisateur avant toute modification
	 * 
	 * @param utilisateur
	 * @param contact
	 * @return true si le contact appartient bien a l'utilisateur
	 */
	public boolean matchUtilisateurContact(Utilisateur utilisateur, Contact contact) {
		logger.info("Verification de correspondance Utilisateur/Contact");
		List<Contact> contacts = getAllContactsByUserId(utilisateur.getUtilisateurId());
		
		for (Contact contactToCheck : contacts) {
			if (contactToCheck.getContactId() == contact.getContactId()) {
				logger.info("Correspondance contact/utilisateur trouvee");
		        return true;
			}
			
		}
		logger.error("Le contact ne semble pas être associe a l'utilisateur");
        return false;
		
	}
	
	public Contact getContactByContactId(Integer contactId) {
		return contactRepository.getContactById(contactId);
	}
	
	
	/**
	 * Ajoute un contact à la liste de contact de l'utilisateur
	 * 
	 * @param utilisateur qui ajoute le contact
	 * @param contact a ajouter
	 * @return le contact ajouté 
	 */ 
	public Contact addContact(Utilisateur utilisateur, Contact contact) {
		logger.info("Ajout du contact a l'utilisateur");
		List<Contact> contacts = getAllContactsByUserId(utilisateur.getUtilisateurId());
		contacts.add(contact);
		utilisateur.setContacts(contacts);	
		utilisateurRepository.save(utilisateur);
				
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
	
	/**
	 * Supprime le contact identifié de la base
	 * 
	 * @param contactId
	 */
	public void deleteContact(Integer contactId) {
		logger.info("Suppression du contact");
		contactRepository.deleteByContactId(contactId);
	}

	/**
	 * Récupere la liste des contacts de l'utilisateur connecté et met en page les résultats
	 * 
	 * @param pageNo n° de la page de résultat en cours
	 * @param pageSize nombre de résultat par page
	 * @param sortField le champs utilisé pour ordonner les résultats
	 * @param sortDirection
	 * @return la liste des contacts de l'utilisateur connecté formatée pour etre affiché par page
	 */
	public Page<Contact> findPaginatedContacts(int pageNo, int pageSize, String sortField, String sortDirection) {
		logger.info("pagine les contacts");
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    int userId = ((Utilisateur) authentication.getPrincipal()).getUtilisateurId();
		
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(pageNo -1, pageSize, sort);
		Page<Contact> result = contactRepository.findByUtilisateurId(userId, pageable);

		logger.info("findAll de pageable contient : "+result.getTotalElements());
		return result;
	}

}
