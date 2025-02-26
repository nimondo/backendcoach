import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './facture.reducer';

export const FactureDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const factureEntity = useAppSelector(state => state.facture.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="factureDetailsHeading">Facture</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{factureEntity.id}</dd>
          <dt>
            <span id="typeFacture">Type Facture</span>
          </dt>
          <dd>{factureEntity.typeFacture}</dd>
          <dt>
            <span id="dateComptableFacture">Date Comptable Facture</span>
          </dt>
          <dd>
            {factureEntity.dateComptableFacture ? (
              <TextFormat value={factureEntity.dateComptableFacture} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="montant">Montant</span>
          </dt>
          <dd>{factureEntity.montant}</dd>
          <dt>
            <span id="tva">Tva</span>
          </dt>
          <dd>{factureEntity.tva}</dd>
          <dt>Paiement</dt>
          <dd>{factureEntity.paiement ? factureEntity.paiement.horodatage : ''}</dd>
        </dl>
        <Button tag={Link} to="/facture" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/facture/${factureEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default FactureDetail;
