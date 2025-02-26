import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './avis-client.reducer';

export const AvisClientDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const avisClientEntity = useAppSelector(state => state.avisClient.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="avisClientDetailsHeading">Avis Client</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{avisClientEntity.id}</dd>
          <dt>
            <span id="dateAvis">Date Avis</span>
          </dt>
          <dd>
            {avisClientEntity.dateAvis ? <TextFormat value={avisClientEntity.dateAvis} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="note">Note</span>
          </dt>
          <dd>{avisClientEntity.note}</dd>
          <dt>
            <span id="descriptionAvis">Description Avis</span>
          </dt>
          <dd>{avisClientEntity.descriptionAvis}</dd>
          <dt>Client</dt>
          <dd>{avisClientEntity.client ? avisClientEntity.client.nom : ''}</dd>
          <dt>Coach Expert</dt>
          <dd>{avisClientEntity.coachExpert ? avisClientEntity.coachExpert.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/avis-client" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/avis-client/${avisClientEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default AvisClientDetail;
