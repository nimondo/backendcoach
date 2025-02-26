package fr.coaching.maseance.service;

import fr.coaching.maseance.domain.Consent;
import fr.coaching.maseance.repository.ConsentRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.coaching.maseance.domain.Consent}.
 */
@Service
@Transactional
public class ConsentService {

    private static final Logger LOG = LoggerFactory.getLogger(ConsentService.class);

    private final ConsentRepository consentRepository;

    public ConsentService(ConsentRepository consentRepository) {
        this.consentRepository = consentRepository;
    }

    /**
     * Save a consent.
     *
     * @param consent the entity to save.
     * @return the persisted entity.
     */
    public Consent save(Consent consent) {
        LOG.debug("Request to save Consent : {}", consent);
        return consentRepository.save(consent);
    }

    /**
     * Update a consent.
     *
     * @param consent the entity to save.
     * @return the persisted entity.
     */
    public Consent update(Consent consent) {
        LOG.debug("Request to update Consent : {}", consent);
        return consentRepository.save(consent);
    }

    /**
     * Partially update a consent.
     *
     * @param consent the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Consent> partialUpdate(Consent consent) {
        LOG.debug("Request to partially update Consent : {}", consent);

        return consentRepository
            .findById(consent.getId())
            .map(existingConsent -> {
                if (consent.getEmail() != null) {
                    existingConsent.setEmail(consent.getEmail());
                }
                if (consent.getNecessary() != null) {
                    existingConsent.setNecessary(consent.getNecessary());
                }
                if (consent.getAnalytics() != null) {
                    existingConsent.setAnalytics(consent.getAnalytics());
                }
                if (consent.getMarketing() != null) {
                    existingConsent.setMarketing(consent.getMarketing());
                }
                if (consent.getPreferences() != null) {
                    existingConsent.setPreferences(consent.getPreferences());
                }

                return existingConsent;
            })
            .map(consentRepository::save);
    }

    /**
     * Get all the consents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Consent> findAll() {
        LOG.debug("Request to get all Consents");
        return consentRepository.findAll();
    }

    /**
     * Get one consent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Consent> findOne(Long id) {
        LOG.debug("Request to get Consent : {}", id);
        return consentRepository.findById(id);
    }

    /**
     * Delete the consent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Consent : {}", id);
        consentRepository.deleteById(id);
    }
}
