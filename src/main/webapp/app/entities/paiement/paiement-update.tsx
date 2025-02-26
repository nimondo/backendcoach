import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { StatutPaiement } from 'app/shared/model/enumerations/statut-paiement.model';
import { createEntity, getEntity, updateEntity } from './paiement.reducer';

export const PaiementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const paiementEntity = useAppSelector(state => state.paiement.entity);
  const loading = useAppSelector(state => state.paiement.loading);
  const updating = useAppSelector(state => state.paiement.updating);
  const updateSuccess = useAppSelector(state => state.paiement.updateSuccess);
  const statutPaiementValues = Object.keys(StatutPaiement);

  const handleClose = () => {
    navigate('/paiement');
  };

  useEffect(() => {
    if (!isNew) {
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
    values.horodatage = convertDateTimeToServer(values.horodatage);

    const entity = {
      ...paiementEntity,
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
      ? {
          horodatage: displayDefaultDateTime(),
        }
      : {
          statutPaiement: 'PaiementAccepte',
          ...paiementEntity,
          horodatage: convertDateTimeFromServer(paiementEntity.horodatage),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coachingApp.paiement.home.createOrEditLabel" data-cy="PaiementCreateUpdateHeading">
            Créer ou éditer un Paiement
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="paiement-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Horodatage"
                id="paiement-horodatage"
                name="horodatage"
                data-cy="horodatage"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField label="Moyen Paiement" id="paiement-moyenPaiement" name="moyenPaiement" data-cy="moyenPaiement" type="text" />
              <ValidatedField
                label="Statut Paiement"
                id="paiement-statutPaiement"
                name="statutPaiement"
                data-cy="statutPaiement"
                type="select"
              >
                {statutPaiementValues.map(statutPaiement => (
                  <option value={statutPaiement} key={statutPaiement}>
                    {statutPaiement}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Id Stripe" id="paiement-idStripe" name="idStripe" data-cy="idStripe" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/paiement" replace color="info">
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

export default PaiementUpdate;
