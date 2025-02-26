import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getOffreCoaches } from 'app/entities/offre-coach/offre-coach.reducer';
import { TypeMedia } from 'app/shared/model/enumerations/type-media.model';
import { createEntity, getEntity, reset, updateEntity } from './offre-coach-media.reducer';

export const OffreCoachMediaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const offreCoaches = useAppSelector(state => state.offreCoach.entities);
  const offreCoachMediaEntity = useAppSelector(state => state.offreCoachMedia.entity);
  const loading = useAppSelector(state => state.offreCoachMedia.loading);
  const updating = useAppSelector(state => state.offreCoachMedia.updating);
  const updateSuccess = useAppSelector(state => state.offreCoachMedia.updateSuccess);
  const typeMediaValues = Object.keys(TypeMedia);

  const handleClose = () => {
    navigate('/offre-coach-media');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getOffreCoaches({}));
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

    const entity = {
      ...offreCoachMediaEntity,
      ...values,
      offreCoach: offreCoaches.find(it => it.id.toString() === values.offreCoach?.toString()),
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
          typeMedia: 'Photo',
          ...offreCoachMediaEntity,
          offreCoach: offreCoachMediaEntity?.offreCoach?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coachingApp.offreCoachMedia.home.createOrEditLabel" data-cy="OffreCoachMediaCreateUpdateHeading">
            Créer ou éditer un Offre Coach Media
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
                <ValidatedField name="id" required readOnly id="offre-coach-media-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Url Media" id="offre-coach-media-urlMedia" name="urlMedia" data-cy="urlMedia" type="text" />
              <ValidatedField label="Type Media" id="offre-coach-media-typeMedia" name="typeMedia" data-cy="typeMedia" type="select">
                {typeMediaValues.map(typeMedia => (
                  <option value={typeMedia} key={typeMedia}>
                    {typeMedia}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="offre-coach-media-offreCoach" name="offreCoach" data-cy="offreCoach" label="Offre Coach" type="select">
                <option value="" key="0" />
                {offreCoaches
                  ? offreCoaches.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/offre-coach-media" replace color="info">
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

export default OffreCoachMediaUpdate;
