import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './offre-coach-media.reducer';

export const OffreCoachMediaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const offreCoachMediaEntity = useAppSelector(state => state.offreCoachMedia.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="offreCoachMediaDetailsHeading">Offre Coach Media</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{offreCoachMediaEntity.id}</dd>
          <dt>
            <span id="urlMedia">Url Media</span>
          </dt>
          <dd>{offreCoachMediaEntity.urlMedia}</dd>
          <dt>
            <span id="typeMedia">Type Media</span>
          </dt>
          <dd>{offreCoachMediaEntity.typeMedia}</dd>
          <dt>Offre Coach</dt>
          <dd>{offreCoachMediaEntity.offreCoach ? offreCoachMediaEntity.offreCoach.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/offre-coach-media" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/offre-coach-media/${offreCoachMediaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default OffreCoachMediaDetail;
