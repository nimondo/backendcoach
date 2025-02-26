import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './paiement.reducer';

export const PaiementDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const paiementEntity = useAppSelector(state => state.paiement.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paiementDetailsHeading">Paiement</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{paiementEntity.id}</dd>
          <dt>
            <span id="horodatage">Horodatage</span>
          </dt>
          <dd>
            {paiementEntity.horodatage ? <TextFormat value={paiementEntity.horodatage} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="moyenPaiement">Moyen Paiement</span>
          </dt>
          <dd>{paiementEntity.moyenPaiement}</dd>
          <dt>
            <span id="statutPaiement">Statut Paiement</span>
          </dt>
          <dd>{paiementEntity.statutPaiement}</dd>
          <dt>
            <span id="idStripe">Id Stripe</span>
          </dt>
          <dd>{paiementEntity.idStripe}</dd>
        </dl>
        <Button tag={Link} to="/paiement" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/paiement/${paiementEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PaiementDetail;
