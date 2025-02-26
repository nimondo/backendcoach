import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getCoachExperts } from 'app/entities/coach-expert/coach-expert.reducer';
import { createEntity, getEntity, reset, updateEntity } from './diplome.reducer';

export const DiplomeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const coachExperts = useAppSelector(state => state.coachExpert.entities);
  const diplomeEntity = useAppSelector(state => state.diplome.entity);
  const loading = useAppSelector(state => state.diplome.loading);
  const updating = useAppSelector(state => state.diplome.updating);
  const updateSuccess = useAppSelector(state => state.diplome.updateSuccess);

  const handleClose = () => {
    navigate('/diplome');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

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
    if (values.nbAnneesEtudePostBac !== undefined && typeof values.nbAnneesEtudePostBac !== 'number') {
      values.nbAnneesEtudePostBac = Number(values.nbAnneesEtudePostBac);
    }

    const entity = {
      ...diplomeEntity,
      ...values,
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
          ...diplomeEntity,
          coachExperts: diplomeEntity?.coachExperts?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coachingApp.diplome.home.createOrEditLabel" data-cy="DiplomeCreateUpdateHeading">
            Créer ou éditer un Diplome
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="diplome-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Libelle"
                id="diplome-libelle"
                name="libelle"
                data-cy="libelle"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField
                label="Nb Annees Etude Post Bac"
                id="diplome-nbAnneesEtudePostBac"
                name="nbAnneesEtudePostBac"
                data-cy="nbAnneesEtudePostBac"
                type="text"
              />
              <ValidatedField
                label="Coach Expert"
                id="diplome-coachExpert"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/diplome" replace color="info">
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

export default DiplomeUpdate;
