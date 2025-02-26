import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, byteSize, openFile } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './coach-expert.reducer';

export const CoachExpertDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const coachExpertEntity = useAppSelector(state => state.coachExpert.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="coachExpertDetailsHeading">Coach Expert</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{coachExpertEntity.id}</dd>
          <dt>
            <span id="civilite">Civilite</span>
          </dt>
          <dd>{coachExpertEntity.civilite}</dd>
          <dt>
            <span id="nom">Nom</span>
          </dt>
          <dd>{coachExpertEntity.nom}</dd>
          <dt>
            <span id="prenom">Prenom</span>
          </dt>
          <dd>{coachExpertEntity.prenom}</dd>
          <dt>
            <span id="dateNaissance">Date Naissance</span>
          </dt>
          <dd>
            {coachExpertEntity.dateNaissance ? (
              <TextFormat value={coachExpertEntity.dateNaissance} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="adresseEmail">Adresse Email</span>
          </dt>
          <dd>{coachExpertEntity.adresseEmail}</dd>
          <dt>
            <span id="numeroTelephone">Numero Telephone</span>
          </dt>
          <dd>{coachExpertEntity.numeroTelephone}</dd>
          <dt>
            <span id="ville">Ville</span>
          </dt>
          <dd>{coachExpertEntity.ville}</dd>
          <dt>
            <span id="codePostal">Code Postal</span>
          </dt>
          <dd>{coachExpertEntity.codePostal}</dd>
          <dt>
            <span id="numeroEtNomVoie">Numero Et Nom Voie</span>
          </dt>
          <dd>{coachExpertEntity.numeroEtNomVoie}</dd>
          <dt>
            <span id="tarifActuel">Tarif Actuel</span>
          </dt>
          <dd>{coachExpertEntity.tarifActuel}</dd>
          <dt>
            <span id="formatProposeSeance">Format Propose Seance</span>
          </dt>
          <dd>{coachExpertEntity.formatProposeSeance}</dd>
          <dt>
            <span id="photo">Photo</span>
          </dt>
          <dd>
            {coachExpertEntity.photo ? (
              <div>
                {coachExpertEntity.photoContentType ? (
                  <a onClick={openFile(coachExpertEntity.photoContentType, coachExpertEntity.photo)}>
                    <img
                      src={`data:${coachExpertEntity.photoContentType};base64,${coachExpertEntity.photo}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {coachExpertEntity.photoContentType}, {byteSize(coachExpertEntity.photo)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="urlPhotoProfil">Url Photo Profil</span>
          </dt>
          <dd>{coachExpertEntity.urlPhotoProfil}</dd>
          <dt>
            <span id="bio">Bio</span>
          </dt>
          <dd>{coachExpertEntity.bio}</dd>
          <dt>User</dt>
          <dd>{coachExpertEntity.user ? coachExpertEntity.user.id : ''}</dd>
          <dt>Specialite Expertise</dt>
          <dd>
            {coachExpertEntity.specialiteExpertises
              ? coachExpertEntity.specialiteExpertises.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {coachExpertEntity.specialiteExpertises && i === coachExpertEntity.specialiteExpertises.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>Diplome</dt>
          <dd>
            {coachExpertEntity.diplomes
              ? coachExpertEntity.diplomes.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {coachExpertEntity.diplomes && i === coachExpertEntity.diplomes.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/coach-expert" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/coach-expert/${coachExpertEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CoachExpertDetail;
