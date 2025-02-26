import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './diplome.reducer';

export const DiplomeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const diplomeEntity = useAppSelector(state => state.diplome.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="diplomeDetailsHeading">Diplome</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{diplomeEntity.id}</dd>
          <dt>
            <span id="libelle">Libelle</span>
          </dt>
          <dd>{diplomeEntity.libelle}</dd>
          <dt>
            <span id="nbAnneesEtudePostBac">Nb Annees Etude Post Bac</span>
          </dt>
          <dd>{diplomeEntity.nbAnneesEtudePostBac}</dd>
          <dt>Coach Expert</dt>
          <dd>
            {diplomeEntity.coachExperts
              ? diplomeEntity.coachExperts.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {diplomeEntity.coachExperts && i === diplomeEntity.coachExperts.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/diplome" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/diplome/${diplomeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default DiplomeDetail;
