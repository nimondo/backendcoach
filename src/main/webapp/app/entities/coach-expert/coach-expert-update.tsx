import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedBlobField, ValidatedField, ValidatedForm, isNumber } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getSpecialiteExpertises } from 'app/entities/specialite-expertise/specialite-expertise.reducer';
import { getEntities as getDiplomes } from 'app/entities/diplome/diplome.reducer';
import { GenrePersonne } from 'app/shared/model/enumerations/genre-personne.model';
import { CanalSeance } from 'app/shared/model/enumerations/canal-seance.model';
import { createEntity, getEntity, updateEntity } from './coach-expert.reducer';

export const CoachExpertUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const specialiteExpertises = useAppSelector(state => state.specialiteExpertise.entities);
  const diplomes = useAppSelector(state => state.diplome.entities);
  const coachExpertEntity = useAppSelector(state => state.coachExpert.entity);
  const loading = useAppSelector(state => state.coachExpert.loading);
  const updating = useAppSelector(state => state.coachExpert.updating);
  const updateSuccess = useAppSelector(state => state.coachExpert.updateSuccess);
  const genrePersonneValues = Object.keys(GenrePersonne);
  const canalSeanceValues = Object.keys(CanalSeance);

  const handleClose = () => {
    navigate('/coach-expert');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getSpecialiteExpertises({}));
    dispatch(getDiplomes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.dateNaissance = convertDateTimeToServer(values.dateNaissance);
    if (values.codePostal !== undefined && typeof values.codePostal !== 'number') {
      values.codePostal = Number(values.codePostal);
    }
    if (values.tarifActuel !== undefined && typeof values.tarifActuel !== 'number') {
      values.tarifActuel = Number(values.tarifActuel);
    }

    const entity = {
      ...coachExpertEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
      specialiteExpertises: mapIdList(values.specialiteExpertises),
      diplomes: mapIdList(values.diplomes),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          dateNaissance: displayDefaultDateTime(),
        }
      : {
          civilite: 'Madame',
          formatProposeSeance: 'AdressePhysique',
          ...coachExpertEntity,
          dateNaissance: convertDateTimeFromServer(coachExpertEntity.dateNaissance),
          user: coachExpertEntity?.user?.id,
          specialiteExpertises: coachExpertEntity?.specialiteExpertises?.map(e => e.id.toString()),
          diplomes: coachExpertEntity?.diplomes?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coachingApp.coachExpert.home.createOrEditLabel" data-cy="CoachExpertCreateUpdateHeading">
            Créer ou éditer un Coach Expert
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="coach-expert-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Civilite" id="coach-expert-civilite" name="civilite" data-cy="civilite" type="select">
                {genrePersonneValues.map(genrePersonne => (
                  <option value={genrePersonne} key={genrePersonne}>
                    {genrePersonne}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label="Nom"
                id="coach-expert-nom"
                name="nom"
                data-cy="nom"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField
                label="Prenom"
                id="coach-expert-prenom"
                name="prenom"
                data-cy="prenom"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField
                label="Date Naissance"
                id="coach-expert-dateNaissance"
                name="dateNaissance"
                data-cy="dateNaissance"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField
                label="Adresse Email"
                id="coach-expert-adresseEmail"
                name="adresseEmail"
                data-cy="adresseEmail"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField
                label="Numero Telephone"
                id="coach-expert-numeroTelephone"
                name="numeroTelephone"
                data-cy="numeroTelephone"
                type="text"
              />
              <ValidatedField
                label="Ville"
                id="coach-expert-ville"
                name="ville"
                data-cy="ville"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField
                label="Code Postal"
                id="coach-expert-codePostal"
                name="codePostal"
                data-cy="codePostal"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                  validate: v => isNumber(v) || 'Ce champ doit être un nombre.',
                }}
              />
              <ValidatedField
                label="Numero Et Nom Voie"
                id="coach-expert-numeroEtNomVoie"
                name="numeroEtNomVoie"
                data-cy="numeroEtNomVoie"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField
                label="Tarif Actuel"
                id="coach-expert-tarifActuel"
                name="tarifActuel"
                data-cy="tarifActuel"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                  validate: v => isNumber(v) || 'Ce champ doit être un nombre.',
                }}
              />
              <ValidatedField
                label="Format Propose Seance"
                id="coach-expert-formatProposeSeance"
                name="formatProposeSeance"
                data-cy="formatProposeSeance"
                type="select"
              >
                {canalSeanceValues.map(canalSeance => (
                  <option value={canalSeance} key={canalSeance}>
                    {canalSeance}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedBlobField label="Photo" id="coach-expert-photo" name="photo" data-cy="photo" isImage accept="image/*" />
              <ValidatedField
                label="Url Photo Profil"
                id="coach-expert-urlPhotoProfil"
                name="urlPhotoProfil"
                data-cy="urlPhotoProfil"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField label="Bio" id="coach-expert-bio" name="bio" data-cy="bio" type="text" />
              <ValidatedField id="coach-expert-user" name="user" data-cy="user" label="User" type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label="Specialite Expertise"
                id="coach-expert-specialiteExpertise"
                data-cy="specialiteExpertise"
                type="select"
                multiple
                name="specialiteExpertises"
              >
                <option value="" key="0" />
                {specialiteExpertises
                  ? specialiteExpertises.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField label="Diplome" id="coach-expert-diplome" data-cy="diplome" type="select" multiple name="diplomes">
                <option value="" key="0" />
                {diplomes
                  ? diplomes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/coach-expert" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Retour</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Sauvegarder
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CoachExpertUpdate;
