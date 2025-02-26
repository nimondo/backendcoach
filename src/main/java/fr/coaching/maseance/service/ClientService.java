package fr.coaching.maseance.service;

import fr.coaching.maseance.domain.Client;
import fr.coaching.maseance.repository.ClientRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.coaching.maseance.domain.Client}.
 */
@Service
@Transactional
public class ClientService {

    private static final Logger LOG = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Save a client.
     *
     * @param client the entity to save.
     * @return the persisted entity.
     */
    public Client save(Client client) {
        LOG.debug("Request to save Client : {}", client);
        return clientRepository.save(client);
    }

    /**
     * Update a client.
     *
     * @param client the entity to save.
     * @return the persisted entity.
     */
    public Client update(Client client) {
        LOG.debug("Request to update Client : {}", client);
        return clientRepository.save(client);
    }

    /**
     * Partially update a client.
     *
     * @param client the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Client> partialUpdate(Client client) {
        LOG.debug("Request to partially update Client : {}", client);

        return clientRepository
            .findById(client.getId())
            .map(existingClient -> {
                if (client.getGenre() != null) {
                    existingClient.setGenre(client.getGenre());
                }
                if (client.getNom() != null) {
                    existingClient.setNom(client.getNom());
                }
                if (client.getPrenom() != null) {
                    existingClient.setPrenom(client.getPrenom());
                }
                if (client.getDateNaissance() != null) {
                    existingClient.setDateNaissance(client.getDateNaissance());
                }
                if (client.getAdresseEmail() != null) {
                    existingClient.setAdresseEmail(client.getAdresseEmail());
                }
                if (client.getNumeroTelephone() != null) {
                    existingClient.setNumeroTelephone(client.getNumeroTelephone());
                }
                if (client.getVille() != null) {
                    existingClient.setVille(client.getVille());
                }
                if (client.getCodePostal() != null) {
                    existingClient.setCodePostal(client.getCodePostal());
                }
                if (client.getNumeroEtNomVoie() != null) {
                    existingClient.setNumeroEtNomVoie(client.getNumeroEtNomVoie());
                }
                if (client.getPreferenceCanalSeance() != null) {
                    existingClient.setPreferenceCanalSeance(client.getPreferenceCanalSeance());
                }
                if (client.getPhoto() != null) {
                    existingClient.setPhoto(client.getPhoto());
                }
                if (client.getPhotoContentType() != null) {
                    existingClient.setPhotoContentType(client.getPhotoContentType());
                }
                if (client.getUrlPhotoProfil() != null) {
                    existingClient.setUrlPhotoProfil(client.getUrlPhotoProfil());
                }

                return existingClient;
            })
            .map(clientRepository::save);
    }

    /**
     * Get one client by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Client> findOne(Long id) {
        LOG.debug("Request to get Client : {}", id);
        return clientRepository.findById(id);
    }

    /**
     * Delete the client by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Client : {}", id);
        clientRepository.deleteById(id);
    }
}
