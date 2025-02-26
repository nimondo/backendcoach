import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './disponibilite.reducer';

export const DisponibiliteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const disponibiliteEntity = useAppSelector(state => state.disponibilite.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="disponibiliteDetailsHeading">Disponibilite</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{disponibiliteEntity.id}</dd>
          <dt>
            <span id="heureDebutCreneauxDisponibilite">Heure Debut Creneaux Disponibilite</span>
          </dt>
          <dd>
            {disponibiliteEntity.heureDebutCreneauxDisponibilite ? (
              <TextFormat value={disponibiliteEntity.heureDebutCreneauxDisponibilite} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="heureFinCreneauxDisponibilite">Heure Fin Creneaux Disponibilite</span>
          </dt>
          <dd>
            {disponibiliteEntity.heureFinCreneauxDisponibilite ? (
              <TextFormat value={disponibiliteEntity.heureFinCreneauxDisponibilite} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="canalSeance">Canal Seance</span>
          </dt>
          <dd>{disponibiliteEntity.canalSeance}</dd>
          <dt>Coach Expert</dt>
          <dd>{disponibiliteEntity.coachExpert ? disponibiliteEntity.coachExpert.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/disponibilite" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/disponibilite/${disponibiliteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default DisponibiliteDetail;
