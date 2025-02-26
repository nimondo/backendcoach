import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedBlobField, ValidatedField, ValidatedForm, isNumber } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { GenrePersonne } from 'app/shared/model/enumerations/genre-personne.model';
import { CanalSeance } from 'app/shared/model/enumerations/canal-seance.model';
import { createEntity, getEntity, updateEntity } from './client.reducer';

export const ClientUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const clientEntity = useAppSelector(state => state.client.entity);
  const loading = useAppSelector(state => state.client.loading);
  const updating = useAppSelector(state => state.client.updating);
  const updateSuccess = useAppSelector(state => state.client.updateSuccess);
  const genrePersonneValues = Object.keys(GenrePersonne);
  const canalSeanceValues = Object.keys(CanalSeance);

  const handleClose = () => {
    navigate('/client');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
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

    const entity = {
      ...clientEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
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
          genre: 'Madame',
          preferenceCanalSeance: 'AdressePhysique',
          ...clientEntity,
          dateNaissance: convertDateTimeFromServer(clientEntity.dateNaissance),
          user: clientEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coachingApp.client.home.createOrEditLabel" data-cy="ClientCreateUpdateHeading">
            Créer ou éditer un Client
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="client-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Genre" id="client-genre" name="genre" data-cy="genre" type="select">
                {genrePersonneValues.map(genrePersonne => (
                  <option value={genrePersonne} key={genrePersonne}>
                    {genrePersonne}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label="Nom"
                id="client-nom"
                name="nom"
                data-cy="nom"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField
                label="Prenom"
                id="client-prenom"
                name="prenom"
                data-cy="prenom"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField
                label="Date Naissance"
                id="client-dateNaissance"
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
                id="client-adresseEmail"
                name="adresseEmail"
                data-cy="adresseEmail"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField
                label="Numero Telephone"
                id="client-numeroTelephone"
                name="numeroTelephone"
                data-cy="numeroTelephone"
                type="text"
              />
              <ValidatedField
                label="Ville"
                id="client-ville"
                name="ville"
                data-cy="ville"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField
                label="Code Postal"
                id="client-codePostal"
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
                id="client-numeroEtNomVoie"
                name="numeroEtNomVoie"
                data-cy="numeroEtNomVoie"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField
                label="Preference Canal Seance"
                id="client-preferenceCanalSeance"
                name="preferenceCanalSeance"
                data-cy="preferenceCanalSeance"
                type="select"
              >
                {canalSeanceValues.map(canalSeance => (
                  <option value={canalSeance} key={canalSeance}>
                    {canalSeance}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedBlobField label="Photo" id="client-photo" name="photo" data-cy="photo" isImage accept="image/*" />
              <ValidatedField
                label="Url Photo Profil"
                id="client-urlPhotoProfil"
                name="urlPhotoProfil"
                data-cy="urlPhotoProfil"
                type="text"
              />
              <ValidatedField id="client-user" name="user" data-cy="user" label="User" type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/client" replace color="info">
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

export default ClientUpdate;
