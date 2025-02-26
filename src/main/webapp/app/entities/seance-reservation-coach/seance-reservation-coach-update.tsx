import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getFactures } from 'app/entities/facture/facture.reducer';
import { getEntities as getCoachExperts } from 'app/entities/coach-expert/coach-expert.reducer';
import { getEntities as getClients } from 'app/entities/client/client.reducer';
import { getEntities as getOffreCoaches } from 'app/entities/offre-coach/offre-coach.reducer';
import { CanalSeance } from 'app/shared/model/enumerations/canal-seance.model';
import { TypeSeance } from 'app/shared/model/enumerations/type-seance.model';
import { StatutSeance } from 'app/shared/model/enumerations/statut-seance.model';
import { createEntity, getEntity, reset, updateEntity } from './seance-reservation-coach.reducer';

export const SeanceReservationCoachUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const factures = useAppSelector(state => state.facture.entities);
  const coachExperts = useAppSelector(state => state.coachExpert.entities);
  const clients = useAppSelector(state => state.client.entities);
  const offreCoaches = useAppSelector(state => state.offreCoach.entities);
  const seanceReservationCoachEntity = useAppSelector(state => state.seanceReservationCoach.entity);
  const loading = useAppSelector(state => state.seanceReservationCoach.loading);
  const updating = useAppSelector(state => state.seanceReservationCoach.updating);
  const updateSuccess = useAppSelector(state => state.seanceReservationCoach.updateSuccess);
  const canalSeanceValues = Object.keys(CanalSeance);
  const typeSeanceValues = Object.keys(TypeSeance);
  const statutSeanceValues = Object.keys(StatutSeance);

  const handleClose = () => {
    navigate('/seance-reservation-coach');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getFactures({}));
    dispatch(getCoachExperts({}));
    dispatch(getClients({}));
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
    values.heureDebutCreneauReserve = convertDateTimeToServer(values.heureDebutCreneauReserve);
    values.heureFinCreneauReserve = convertDateTimeToServer(values.heureFinCreneauReserve);

    const entity = {
      ...seanceReservationCoachEntity,
      ...values,
      facture: factures.find(it => it.id.toString() === values.facture?.toString()),
      coachExpert: coachExperts.find(it => it.id.toString() === values.coachExpert?.toString()),
      client: clients.find(it => it.id.toString() === values.client?.toString()),
      offre: offreCoaches.find(it => it.id.toString() === values.offre?.toString()),
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
          heureDebutCreneauReserve: displayDefaultDateTime(),
          heureFinCreneauReserve: displayDefaultDateTime(),
        }
      : {
          canalSeance: 'AdressePhysique',
          typeSeance: 'Individuelle',
          statutRealisation: 'ReservationFaite',
          ...seanceReservationCoachEntity,
          heureDebutCreneauReserve: convertDateTimeFromServer(seanceReservationCoachEntity.heureDebutCreneauReserve),
          heureFinCreneauReserve: convertDateTimeFromServer(seanceReservationCoachEntity.heureFinCreneauReserve),
          facture: seanceReservationCoachEntity?.facture?.id,
          coachExpert: seanceReservationCoachEntity?.coachExpert?.id,
          client: seanceReservationCoachEntity?.client?.id,
          offre: seanceReservationCoachEntity?.offre?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coachingApp.seanceReservationCoach.home.createOrEditLabel" data-cy="SeanceReservationCoachCreateUpdateHeading">
            Créer ou éditer un Seance Reservation Coach
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
                <ValidatedField name="id" required readOnly id="seance-reservation-coach-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Heure Debut Creneau Reserve"
                id="seance-reservation-coach-heureDebutCreneauReserve"
                name="heureDebutCreneauReserve"
                data-cy="heureDebutCreneauReserve"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField
                label="Heure Fin Creneau Reserve"
                id="seance-reservation-coach-heureFinCreneauReserve"
                name="heureFinCreneauReserve"
                data-cy="heureFinCreneauReserve"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField
                label="Canal Seance"
                id="seance-reservation-coach-canalSeance"
                name="canalSeance"
                data-cy="canalSeance"
                type="select"
              >
                {canalSeanceValues.map(canalSeance => (
                  <option value={canalSeance} key={canalSeance}>
                    {canalSeance}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label="Type Seance"
                id="seance-reservation-coach-typeSeance"
                name="typeSeance"
                data-cy="typeSeance"
                type="select"
              >
                {typeSeanceValues.map(typeSeance => (
                  <option value={typeSeance} key={typeSeance}>
                    {typeSeance}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label="Statut Realisation"
                id="seance-reservation-coach-statutRealisation"
                name="statutRealisation"
                data-cy="statutRealisation"
                type="select"
              >
                {statutSeanceValues.map(statutSeance => (
                  <option value={statutSeance} key={statutSeance}>
                    {statutSeance}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="seance-reservation-coach-facture" name="facture" data-cy="facture" label="Facture" type="select">
                <option value="" key="0" />
                {factures
                  ? factures.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.dateComptableFacture}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="seance-reservation-coach-coachExpert"
                name="coachExpert"
                data-cy="coachExpert"
                label="Coach Expert"
                type="select"
              >
                <option value="" key="0" />
                {coachExperts
                  ? coachExperts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nom}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="seance-reservation-coach-client" name="client" data-cy="client" label="Client" type="select">
                <option value="" key="0" />
                {clients
                  ? clients.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nom}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="seance-reservation-coach-offre" name="offre" data-cy="offre" label="Offre" type="select">
                <option value="" key="0" />
                {offreCoaches
                  ? offreCoaches.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.synthese}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/seance-reservation-coach" replace color="info">
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

export default SeanceReservationCoachUpdate;
