import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './theme-expertise.reducer';

export const ThemeExpertiseDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const themeExpertiseEntity = useAppSelector(state => state.themeExpertise.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="themeExpertiseDetailsHeading">Theme Expertise</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{themeExpertiseEntity.id}</dd>
          <dt>
            <span id="libelleExpertise">Libelle Expertise</span>
          </dt>
          <dd>{themeExpertiseEntity.libelleExpertise}</dd>
          <dt>
            <span id="urlPhoto">Url Photo</span>
          </dt>
          <dd>{themeExpertiseEntity.urlPhoto}</dd>
        </dl>
        <Button tag={Link} to="/theme-expertise" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/theme-expertise/${themeExpertiseEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ThemeExpertiseDetail;
