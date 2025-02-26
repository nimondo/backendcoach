import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getCoachExperts } from 'app/entities/coach-expert/coach-expert.reducer';
import { CanalSeance } from 'app/shared/model/enumerations/canal-seance.model';
import { createEntity, getEntity, reset, updateEntity } from './disponibilite.reducer';

export const DisponibiliteUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const coachExperts = useAppSelector(state => state.coachExpert.entities);
  const disponibiliteEntity = useAppSelector(state => state.disponibilite.entity);
  const loading = useAppSelector(state => state.disponibilite.loading);
  const updating = useAppSelector(state => state.disponibilite.updating);
  const updateSuccess = useAppSelector(state => state.disponibilite.updateSuccess);
  const canalSeanceValues = Object.keys(CanalSeance);

  const handleClose = () => {
    navigate('/disponibilite');
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
    values.heureDebutCreneauxDisponibilite = convertDateTimeToServer(values.heureDebutCreneauxDisponibilite);
    values.heureFinCreneauxDisponibilite = convertDateTimeToServer(values.heureFinCreneauxDisponibilite);

    const entity = {
      ...disponibiliteEntity,
      ...values,
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
          heureDebutCreneauxDisponibilite: displayDefaultDateTime(),
          heureFinCreneauxDisponibilite: displayDefaultDateTime(),
        }
      : {
          canalSeance: 'AdressePhysique',
          ...disponibiliteEntity,
          heureDebutCreneauxDisponibilite: convertDateTimeFromServer(disponibiliteEntity.heureDebutCreneauxDisponibilite),
          heureFinCreneauxDisponibilite: convertDateTimeFromServer(disponibiliteEntity.heureFinCreneauxDisponibilite),
          coachExpert: disponibiliteEntity?.coachExpert?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coachingApp.disponibilite.home.createOrEditLabel" data-cy="DisponibiliteCreateUpdateHeading">
            Créer ou éditer un Disponibilite
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
                <ValidatedField name="id" required readOnly id="disponibilite-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Heure Debut Creneaux Disponibilite"
                id="disponibilite-heureDebutCreneauxDisponibilite"
                name="heureDebutCreneauxDisponibilite"
                data-cy="heureDebutCreneauxDisponibilite"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField
                label="Heure Fin Creneaux Disponibilite"
                id="disponibilite-heureFinCreneauxDisponibilite"
                name="heureFinCreneauxDisponibilite"
                data-cy="heureFinCreneauxDisponibilite"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField label="Canal Seance" id="disponibilite-canalSeance" name="canalSeance" data-cy="canalSeance" type="select">
                {canalSeanceValues.map(canalSeance => (
                  <option value={canalSeance} key={canalSeance}>
                    {canalSeance}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="disponibilite-coachExpert" name="coachExpert" data-cy="coachExpert" label="Coach Expert" type="select">
                <option value="" key="0" />
                {coachExperts
                  ? coachExperts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/disponibilite" replace color="info">
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

export default DisponibiliteUpdate;
