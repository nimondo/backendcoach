import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, byteSize, openFile } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './client.reducer';

export const ClientDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const clientEntity = useAppSelector(state => state.client.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="clientDetailsHeading">Client</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{clientEntity.id}</dd>
          <dt>
            <span id="genre">Genre</span>
          </dt>
          <dd>{clientEntity.genre}</dd>
          <dt>
            <span id="nom">Nom</span>
          </dt>
          <dd>{clientEntity.nom}</dd>
          <dt>
            <span id="prenom">Prenom</span>
          </dt>
          <dd>{clientEntity.prenom}</dd>
          <dt>
            <span id="dateNaissance">Date Naissance</span>
          </dt>
          <dd>
            {clientEntity.dateNaissance ? <TextFormat value={clientEntity.dateNaissance} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="adresseEmail">Adresse Email</span>
          </dt>
          <dd>{clientEntity.adresseEmail}</dd>
          <dt>
            <span id="numeroTelephone">Numero Telephone</span>
          </dt>
          <dd>{clientEntity.numeroTelephone}</dd>
          <dt>
            <span id="ville">Ville</span>
          </dt>
          <dd>{clientEntity.ville}</dd>
          <dt>
            <span id="codePostal">Code Postal</span>
          </dt>
          <dd>{clientEntity.codePostal}</dd>
          <dt>
            <span id="numeroEtNomVoie">Numero Et Nom Voie</span>
          </dt>
          <dd>{clientEntity.numeroEtNomVoie}</dd>
          <dt>
            <span id="preferenceCanalSeance">Preference Canal Seance</span>
          </dt>
          <dd>{clientEntity.preferenceCanalSeance}</dd>
          <dt>
            <span id="photo">Photo</span>
          </dt>
          <dd>
            {clientEntity.photo ? (
              <div>
                {clientEntity.photoContentType ? (
                  <a onClick={openFile(clientEntity.photoContentType, clientEntity.photo)}>
                    <img src={`data:${clientEntity.photoContentType};base64,${clientEntity.photo}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {clientEntity.photoContentType}, {byteSize(clientEntity.photo)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="urlPhotoProfil">Url Photo Profil</span>
          </dt>
          <dd>{clientEntity.urlPhotoProfil}</dd>
          <dt>User</dt>
          <dd>{clientEntity.user ? clientEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/client" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/client/${clientEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ClientDetail;
