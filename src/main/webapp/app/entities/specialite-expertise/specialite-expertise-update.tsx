import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getDiplomes } from 'app/entities/diplome/diplome.reducer';
import { getEntities as getThemeExpertises } from 'app/entities/theme-expertise/theme-expertise.reducer';
import { getEntities as getCoachExperts } from 'app/entities/coach-expert/coach-expert.reducer';
import { createEntity, getEntity, reset, updateEntity } from './specialite-expertise.reducer';

export const SpecialiteExpertiseUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const diplomes = useAppSelector(state => state.diplome.entities);
  const themeExpertises = useAppSelector(state => state.themeExpertise.entities);
  const coachExperts = useAppSelector(state => state.coachExpert.entities);
  const specialiteExpertiseEntity = useAppSelector(state => state.specialiteExpertise.entity);
  const loading = useAppSelector(state => state.specialiteExpertise.loading);
  const updating = useAppSelector(state => state.specialiteExpertise.updating);
  const updateSuccess = useAppSelector(state => state.specialiteExpertise.updateSuccess);

  const handleClose = () => {
    navigate('/specialite-expertise');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getDiplomes({}));
    dispatch(getThemeExpertises({}));
    dispatch(getCoachExperts({}));
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
    if (values.tarifMoyenHeure !== undefined && typeof values.tarifMoyenHeure !== 'number') {
      values.tarifMoyenHeure = Number(values.tarifMoyenHeure);
    }

    const entity = {
      ...specialiteExpertiseEntity,
      ...values,
      diplome: diplomes.find(it => it.id.toString() === values.diplome?.toString()),
      themeExpertise: themeExpertises.find(it => it.id.toString() === values.themeExpertise?.toString()),
      coachExperts: mapIdList(values.coachExperts),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...specialiteExpertiseEntity,
          diplome: specialiteExpertiseEntity?.diplome?.id,
          themeExpertise: specialiteExpertiseEntity?.themeExpertise?.id,
          coachExperts: specialiteExpertiseEntity?.coachExperts?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coachingApp.specialiteExpertise.home.createOrEditLabel" data-cy="SpecialiteExpertiseCreateUpdateHeading">
            Créer ou éditer un Specialite Expertise
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="specialite-expertise-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Specialite"
                id="specialite-expertise-specialite"
                name="specialite"
                data-cy="specialite"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField
                label="Specialite Description"
                id="specialite-expertise-specialiteDescription"
                name="specialiteDescription"
                data-cy="specialiteDescription"
                type="text"
              />
              <ValidatedField
                label="Tarif Moyen Heure"
                id="specialite-expertise-tarifMoyenHeure"
                name="tarifMoyenHeure"
                data-cy="tarifMoyenHeure"
                type="text"
              />
              <ValidatedField label="Duree Tarif" id="specialite-expertise-dureeTarif" name="dureeTarif" data-cy="dureeTarif" type="text" />
              <ValidatedField
                label="Diplome Requis"
                id="specialite-expertise-diplomeRequis"
                name="diplomeRequis"
                data-cy="diplomeRequis"
                check
                type="checkbox"
              />
              <ValidatedField
                label="Url Photo"
                id="specialite-expertise-urlPhoto"
                name="urlPhoto"
                data-cy="urlPhoto"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField id="specialite-expertise-diplome" name="diplome" data-cy="diplome" label="Diplome" type="select">
                <option value="" key="0" />
                {diplomes
                  ? diplomes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.libelle}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="specialite-expertise-themeExpertise"
                name="themeExpertise"
                data-cy="themeExpertise"
                label="Theme Expertise"
                type="select"
              >
                <option value="" key="0" />
                {themeExpertises
                  ? themeExpertises.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.libelleExpertise}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label="Coach Expert"
                id="specialite-expertise-coachExpert"
                data-cy="coachExpert"
                type="select"
                multiple
                name="coachExperts"
              >
                <option value="" key="0" />
                {coachExperts
                  ? coachExperts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/specialite-expertise" replace color="info">
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

export default SpecialiteExpertiseUpdate;
