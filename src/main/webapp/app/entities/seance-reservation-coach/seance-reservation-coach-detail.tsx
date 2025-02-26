import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './seance-reservation-coach.reducer';

export const SeanceReservationCoachDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const seanceReservationCoachEntity = useAppSelector(state => state.seanceReservationCoach.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="seanceReservationCoachDetailsHeading">Seance Reservation Coach</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{seanceReservationCoachEntity.id}</dd>
          <dt>
            <span id="heureDebutCreneauReserve">Heure Debut Creneau Reserve</span>
          </dt>
          <dd>
            {seanceReservationCoachEntity.heureDebutCreneauReserve ? (
              <TextFormat value={seanceReservationCoachEntity.heureDebutCreneauReserve} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="heureFinCreneauReserve">Heure Fin Creneau Reserve</span>
          </dt>
          <dd>
            {seanceReservationCoachEntity.heureFinCreneauReserve ? (
              <TextFormat value={seanceReservationCoachEntity.heureFinCreneauReserve} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="canalSeance">Canal Seance</span>
          </dt>
          <dd>{seanceReservationCoachEntity.canalSeance}</dd>
          <dt>
            <span id="typeSeance">Type Seance</span>
          </dt>
          <dd>{seanceReservationCoachEntity.typeSeance}</dd>
          <dt>
            <span id="statutRealisation">Statut Realisation</span>
          </dt>
          <dd>{seanceReservationCoachEntity.statutRealisation}</dd>
          <dt>Facture</dt>
          <dd>{seanceReservationCoachEntity.facture ? seanceReservationCoachEntity.facture.dateComptableFacture : ''}</dd>
          <dt>Coach Expert</dt>
          <dd>{seanceReservationCoachEntity.coachExpert ? seanceReservationCoachEntity.coachExpert.nom : ''}</dd>
          <dt>Client</dt>
          <dd>{seanceReservationCoachEntity.client ? seanceReservationCoachEntity.client.nom : ''}</dd>
          <dt>Offre</dt>
          <dd>{seanceReservationCoachEntity.offre ? seanceReservationCoachEntity.offre.synthese : ''}</dd>
        </dl>
        <Button tag={Link} to="/seance-reservation-coach" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/seance-reservation-coach/${seanceReservationCoachEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SeanceReservationCoachDetail;
