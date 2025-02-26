import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './consent.reducer';

export const ConsentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const consentEntity = useAppSelector(state => state.consent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="consentDetailsHeading">Consent</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{consentEntity.id}</dd>
          <dt>
            <span id="email">Email</span>
          </dt>
          <dd>{consentEntity.email}</dd>
          <dt>
            <span id="necessary">Necessary</span>
          </dt>
          <dd>{consentEntity.necessary ? 'true' : 'false'}</dd>
          <dt>
            <span id="analytics">Analytics</span>
          </dt>
          <dd>{consentEntity.analytics ? 'true' : 'false'}</dd>
          <dt>
            <span id="marketing">Marketing</span>
          </dt>
          <dd>{consentEntity.marketing ? 'true' : 'false'}</dd>
          <dt>
            <span id="preferences">Preferences</span>
          </dt>
          <dd>{consentEntity.preferences ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/consent" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/consent/${consentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ConsentDetail;
