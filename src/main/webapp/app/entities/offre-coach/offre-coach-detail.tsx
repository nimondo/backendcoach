import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './offre-coach.reducer';

export const OffreCoachDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const offreCoachEntity = useAppSelector(state => state.offreCoach.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="offreCoachDetailsHeading">Offre Coach</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{offreCoachEntity.id}</dd>
          <dt>
            <span id="canalSeance">Canal Seance</span>
          </dt>
          <dd>{offreCoachEntity.canalSeance}</dd>
          <dt>
            <span id="typeSeance">Type Seance</span>
          </dt>
          <dd>{offreCoachEntity.typeSeance}</dd>
          <dt>
            <span id="synthese">Synthese</span>
          </dt>
          <dd>{offreCoachEntity.synthese}</dd>
          <dt>
            <span id="descriptionDetaillee">Description Detaillee</span>
          </dt>
          <dd>{offreCoachEntity.descriptionDetaillee}</dd>
          <dt>
            <span id="tarif">Tarif</span>
          </dt>
          <dd>{offreCoachEntity.tarif}</dd>
          <dt>
            <span id="duree">Duree</span>
          </dt>
          <dd>{offreCoachEntity.duree}</dd>
          <dt>
            <span id="descriptionDiplome">Description Diplome</span>
          </dt>
          <dd>{offreCoachEntity.descriptionDiplome}</dd>
          <dt>Specialite</dt>
          <dd>{offreCoachEntity.specialite ? offreCoachEntity.specialite.id : ''}</dd>
          <dt>Coach Expert</dt>
          <dd>{offreCoachEntity.coachExpert ? offreCoachEntity.coachExpert.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/offre-coach" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/offre-coach/${offreCoachEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default OffreCoachDetail;
