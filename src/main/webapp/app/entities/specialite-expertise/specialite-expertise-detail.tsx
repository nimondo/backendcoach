import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './specialite-expertise.reducer';

export const SpecialiteExpertiseDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const specialiteExpertiseEntity = useAppSelector(state => state.specialiteExpertise.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="specialiteExpertiseDetailsHeading">Specialite Expertise</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{specialiteExpertiseEntity.id}</dd>
          <dt>
            <span id="specialite">Specialite</span>
          </dt>
          <dd>{specialiteExpertiseEntity.specialite}</dd>
          <dt>
            <span id="specialiteDescription">Specialite Description</span>
          </dt>
          <dd>{specialiteExpertiseEntity.specialiteDescription}</dd>
          <dt>
            <span id="tarifMoyenHeure">Tarif Moyen Heure</span>
          </dt>
          <dd>{specialiteExpertiseEntity.tarifMoyenHeure}</dd>
          <dt>
            <span id="dureeTarif">Duree Tarif</span>
          </dt>
          <dd>{specialiteExpertiseEntity.dureeTarif}</dd>
          <dt>
            <span id="diplomeRequis">Diplome Requis</span>
          </dt>
          <dd>{specialiteExpertiseEntity.diplomeRequis ? 'true' : 'false'}</dd>
          <dt>
            <span id="urlPhoto">Url Photo</span>
          </dt>
          <dd>{specialiteExpertiseEntity.urlPhoto}</dd>
          <dt>Diplome</dt>
          <dd>{specialiteExpertiseEntity.diplome ? specialiteExpertiseEntity.diplome.libelle : ''}</dd>
          <dt>Theme Expertise</dt>
          <dd>{specialiteExpertiseEntity.themeExpertise ? specialiteExpertiseEntity.themeExpertise.libelleExpertise : ''}</dd>
          <dt>Coach Expert</dt>
          <dd>
            {specialiteExpertiseEntity.coachExperts
              ? specialiteExpertiseEntity.coachExperts.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {specialiteExpertiseEntity.coachExperts && i === specialiteExpertiseEntity.coachExperts.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/specialite-expertise" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/specialite-expertise/${specialiteExpertiseEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SpecialiteExpertiseDetail;
