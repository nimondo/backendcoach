import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm, isNumber } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPaiements } from 'app/entities/paiement/paiement.reducer';
import { TypeFacture } from 'app/shared/model/enumerations/type-facture.model';
import { createEntity, getEntity, updateEntity } from './facture.reducer';

export const FactureUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const paiements = useAppSelector(state => state.paiement.entities);
  const factureEntity = useAppSelector(state => state.facture.entity);
  const loading = useAppSelector(state => state.facture.loading);
  const updating = useAppSelector(state => state.facture.updating);
  const updateSuccess = useAppSelector(state => state.facture.updateSuccess);
  const typeFactureValues = Object.keys(TypeFacture);

  const handleClose = () => {
    navigate('/facture');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getPaiements({}));
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
    values.dateComptableFacture = convertDateTimeToServer(values.dateComptableFacture);
    if (values.montant !== undefined && typeof values.montant !== 'number') {
      values.montant = Number(values.montant);
    }
    if (values.tva !== undefined && typeof values.tva !== 'number') {
      values.tva = Number(values.tva);
    }

    const entity = {
      ...factureEntity,
      ...values,
      paiement: paiements.find(it => it.id.toString() === values.paiement?.toString()),
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
          dateComptableFacture: displayDefaultDateTime(),
        }
      : {
          typeFacture: 'FactureStandard',
          ...factureEntity,
          dateComptableFacture: convertDateTimeFromServer(factureEntity.dateComptableFacture),
          paiement: factureEntity?.paiement?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coachingApp.facture.home.createOrEditLabel" data-cy="FactureCreateUpdateHeading">
            Créer ou éditer un Facture
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="facture-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Type Facture" id="facture-typeFacture" name="typeFacture" data-cy="typeFacture" type="select">
                {typeFactureValues.map(typeFacture => (
                  <option value={typeFacture} key={typeFacture}>
                    {typeFacture}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label="Date Comptable Facture"
                id="facture-dateComptableFacture"
                name="dateComptableFacture"
                data-cy="dateComptableFacture"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField
                label="Montant"
                id="facture-montant"
                name="montant"
                data-cy="montant"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                  validate: v => isNumber(v) || 'Ce champ doit être un nombre.',
                }}
              />
              <ValidatedField
                label="Tva"
                id="facture-tva"
                name="tva"
                data-cy="tva"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                  validate: v => isNumber(v) || 'Ce champ doit être un nombre.',
                }}
              />
              <ValidatedField id="facture-paiement" name="paiement" data-cy="paiement" label="Paiement" type="select">
                <option value="" key="0" />
                {paiements
                  ? paiements.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.horodatage}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/facture" replace color="info">
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

export default FactureUpdate;
