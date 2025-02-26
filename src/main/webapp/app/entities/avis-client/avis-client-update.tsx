import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm, isNumber } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getClients } from 'app/entities/client/client.reducer';
import { getEntities as getCoachExperts } from 'app/entities/coach-expert/coach-expert.reducer';
import { createEntity, getEntity, updateEntity } from './avis-client.reducer';

export const AvisClientUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const clients = useAppSelector(state => state.client.entities);
  const coachExperts = useAppSelector(state => state.coachExpert.entities);
  const avisClientEntity = useAppSelector(state => state.avisClient.entity);
  const loading = useAppSelector(state => state.avisClient.loading);
  const updating = useAppSelector(state => state.avisClient.updating);
  const updateSuccess = useAppSelector(state => state.avisClient.updateSuccess);

  const handleClose = () => {
    navigate('/avis-client');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getClients({}));
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
    values.dateAvis = convertDateTimeToServer(values.dateAvis);
    if (values.note !== undefined && typeof values.note !== 'number') {
      values.note = Number(values.note);
    }

    const entity = {
      ...avisClientEntity,
      ...values,
      client: clients.find(it => it.id.toString() === values.client?.toString()),
      coachExpert: coachExperts.find(it => it.id.toString() === values.coachExpert?.toString()),
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
          dateAvis: displayDefaultDateTime(),
        }
      : {
          ...avisClientEntity,
          dateAvis: convertDateTimeFromServer(avisClientEntity.dateAvis),
          client: avisClientEntity?.client?.id,
          coachExpert: avisClientEntity?.coachExpert?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coachingApp.avisClient.home.createOrEditLabel" data-cy="AvisClientCreateUpdateHeading">
            Créer ou éditer un Avis Client
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="avis-client-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Date Avis"
                id="avis-client-dateAvis"
                name="dateAvis"
                data-cy="dateAvis"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField
                label="Note"
                id="avis-client-note"
                name="note"
                data-cy="note"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                  validate: v => isNumber(v) || 'Ce champ doit être un nombre.',
                }}
              />
              <ValidatedField
                label="Description Avis"
                id="avis-client-descriptionAvis"
                name="descriptionAvis"
                data-cy="descriptionAvis"
                type="text"
              />
              <ValidatedField id="avis-client-client" name="client" data-cy="client" label="Client" type="select">
                <option value="" key="0" />
                {clients
                  ? clients.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nom}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="avis-client-coachExpert" name="coachExpert" data-cy="coachExpert" label="Coach Expert" type="select">
                <option value="" key="0" />
                {coachExperts
                  ? coachExperts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/avis-client" replace color="info">
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

export default AvisClientUpdate;
