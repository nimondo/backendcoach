package fr.coaching.maseance.service;

import fr.coaching.maseance.domain.CoachExpert;
import fr.coaching.maseance.repository.CoachExpertRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.coaching.maseance.domain.CoachExpert}.
 */
@Service
@Transactional
public class CoachExpertService {

    private static final Logger LOG = LoggerFactory.getLogger(CoachExpertService.class);

    private final CoachExpertRepository coachExpertRepository;

    public CoachExpertService(CoachExpertRepository coachExpertRepository) {
        this.coachExpertRepository = coachExpertRepository;
    }

    /**
     * Save a coachExpert.
     *
     * @param coachExpert the entity to save.
     * @return the persisted entity.
     */
    public CoachExpert save(CoachExpert coachExpert) {
        LOG.debug("Request to save CoachExpert : {}", coachExpert);
        return coachExpertRepository.save(coachExpert);
    }

    /**
     * Update a coachExpert.
     *
     * @param coachExpert the entity to save.
     * @return the persisted entity.
     */
    public CoachExpert update(CoachExpert coachExpert) {
        LOG.debug("Request to update CoachExpert : {}", coachExpert);
        return coachExpertRepository.save(coachExpert);
    }

    /**
     * Partially update a coachExpert.
     *
     * @param coachExpert the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CoachExpert> partialUpdate(CoachExpert coachExpert) {
        LOG.debug("Request to partially update CoachExpert : {}", coachExpert);

        return coachExpertRepository
            .findById(coachExpert.getId())
            .map(existingCoachExpert -> {
                if (coachExpert.getCivilite() != null) {
                    existingCoachExpert.setCivilite(coachExpert.getCivilite());
                }
                if (coachExpert.getNom() != null) {
                    existingCoachExpert.setNom(coachExpert.getNom());
                }
                if (coachExpert.getPrenom() != null) {
                    existingCoachExpert.setPrenom(coachExpert.getPrenom());
                }
                if (coachExpert.getDateNaissance() != null) {
                    existingCoachExpert.setDateNaissance(coachExpert.getDateNaissance());
                }
                if (coachExpert.getAdresseEmail() != null) {
                    existingCoachExpert.setAdresseEmail(coachExpert.getAdresseEmail());
                }
                if (coachExpert.getNumeroTelephone() != null) {
                    existingCoachExpert.setNumeroTelephone(coachExpert.getNumeroTelephone());
                }
                if (coachExpert.getVille() != null) {
                    existingCoachExpert.setVille(coachExpert.getVille());
                }
                if (coachExpert.getCodePostal() != null) {
                    existingCoachExpert.setCodePostal(coachExpert.getCodePostal());
                }
                if (coachExpert.getNumeroEtNomVoie() != null) {
                    existingCoachExpert.setNumeroEtNomVoie(coachExpert.getNumeroEtNomVoie());
                }
                if (coachExpert.getTarifActuel() != null) {
                    existingCoachExpert.setTarifActuel(coachExpert.getTarifActuel());
                }
                if (coachExpert.getFormatProposeSeance() != null) {
                    existingCoachExpert.setFormatProposeSeance(coachExpert.getFormatProposeSeance());
                }
                if (coachExpert.getPhoto() != null) {
                    existingCoachExpert.setPhoto(coachExpert.getPhoto());
                }
                if (coachExpert.getPhotoContentType() != null) {
                    existingCoachExpert.setPhotoContentType(coachExpert.getPhotoContentType());
                }
                if (coachExpert.getUrlPhotoProfil() != null) {
                    existingCoachExpert.setUrlPhotoProfil(coachExpert.getUrlPhotoProfil());
                }
                if (coachExpert.getBio() != null) {
                    existingCoachExpert.setBio(coachExpert.getBio());
                }

                return existingCoachExpert;
            })
            .map(coachExpertRepository::save);
    }

    /**
     * Get all the coachExperts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CoachExpert> findAllWithEagerRelationships(Pageable pageable) {
        return coachExpertRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one coachExpert by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CoachExpert> findOne(Long id) {
        LOG.debug("Request to get CoachExpert : {}", id);
        return coachExpertRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the coachExpert by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CoachExpert : {}", id);
        coachExpertRepository.deleteById(id);
    }
}
