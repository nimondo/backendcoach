import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './consent.reducer';

export const ConsentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const consentEntity = useAppSelector(state => state.consent.entity);
  const loading = useAppSelector(state => state.consent.loading);
  const updating = useAppSelector(state => state.consent.updating);
  const updateSuccess = useAppSelector(state => state.consent.updateSuccess);

  const handleClose = () => {
    navigate('/consent');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
      ...consentEntity,
      ...values,
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
          ...consentEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coachingApp.consent.home.createOrEditLabel" data-cy="ConsentCreateUpdateHeading">
            Créer ou éditer un Consent
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="consent-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Email"
                id="consent-email"
                name="email"
                data-cy="email"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField label="Necessary" id="consent-necessary" name="necessary" data-cy="necessary" check type="checkbox" />
              <ValidatedField label="Analytics" id="consent-analytics" name="analytics" data-cy="analytics" check type="checkbox" />
              <ValidatedField label="Marketing" id="consent-marketing" name="marketing" data-cy="marketing" check type="checkbox" />
              <ValidatedField label="Preferences" id="consent-preferences" name="preferences" data-cy="preferences" check type="checkbox" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/consent" replace color="info">
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

export default ConsentUpdate;
