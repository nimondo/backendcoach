import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm, isNumber } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getSpecialiteExpertises } from 'app/entities/specialite-expertise/specialite-expertise.reducer';
import { getEntities as getCoachExperts } from 'app/entities/coach-expert/coach-expert.reducer';
import { CanalSeance } from 'app/shared/model/enumerations/canal-seance.model';
import { TypeSeance } from 'app/shared/model/enumerations/type-seance.model';
import { createEntity, getEntity, updateEntity } from './offre-coach.reducer';

export const OffreCoachUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const specialiteExpertises = useAppSelector(state => state.specialiteExpertise.entities);
  const coachExperts = useAppSelector(state => state.coachExpert.entities);
  const offreCoachEntity = useAppSelector(state => state.offreCoach.entity);
  const loading = useAppSelector(state => state.offreCoach.loading);
  const updating = useAppSelector(state => state.offreCoach.updating);
  const updateSuccess = useAppSelector(state => state.offreCoach.updateSuccess);
  const canalSeanceValues = Object.keys(CanalSeance);
  const typeSeanceValues = Object.keys(TypeSeance);

  const handleClose = () => {
    navigate('/offre-coach');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getSpecialiteExpertises({}));
    dispatch(getCoachExperts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.tarif !== undefined && typeof values.tarif !== 'number') {
      values.tarif = Number(values.tarif);
    }
    if (values.duree !== undefined && typeof values.duree !== 'number') {
      values.duree = Number(values.duree);
    }

    const entity = {
      ...offreCoachEntity,
      ...values,
      specialite: specialiteExpertises.find(it => it.id.toString() === values.specialite?.toString()),
      coachExpert: coachExperts.find(it => it.id.toString() === values.coachExpert?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          canalSeance: 'AdressePhysique',
          typeSeance: 'Individuelle',
          ...offreCoachEntity,
          specialite: offreCoachEntity?.specialite?.id,
          coachExpert: offreCoachEntity?.coachExpert?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coachingApp.offreCoach.home.createOrEditLabel" data-cy="OffreCoachCreateUpdateHeading">
            Créer ou éditer un Offre Coach
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="offre-coach-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Canal Seance" id="offre-coach-canalSeance" name="canalSeance" data-cy="canalSeance" type="select">
                {canalSeanceValues.map(canalSeance => (
                  <option value={canalSeance} key={canalSeance}>
                    {canalSeance}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Type Seance" id="offre-coach-typeSeance" name="typeSeance" data-cy="typeSeance" type="select">
                {typeSeanceValues.map(typeSeance => (
                  <option value={typeSeance} key={typeSeance}>
                    {typeSeance}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Synthese" id="offre-coach-synthese" name="synthese" data-cy="synthese" type="text" />
              <ValidatedField
                label="Description Detaillee"
                id="offre-coach-descriptionDetaillee"
                name="descriptionDetaillee"
                data-cy="descriptionDetaillee"
                type="text"
              />
              <ValidatedField
                label="Tarif"
                id="offre-coach-tarif"
                name="tarif"
                data-cy="tarif"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                  validate: v => isNumber(v) || 'Ce champ doit être un nombre.',
                }}
              />
              <ValidatedField
                label="Duree"
                id="offre-coach-duree"
                name="duree"
                data-cy="duree"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                  validate: v => isNumber(v) || 'Ce champ doit être un nombre.',
                }}
              />
              <ValidatedField
                label="Description Diplome"
                id="offre-coach-descriptionDiplome"
                name="descriptionDiplome"
                data-cy="descriptionDiplome"
                type="text"
              />
              <ValidatedField id="offre-coach-specialite" name="specialite" data-cy="specialite" label="Specialite" type="select">
                <option value="" key="0" />
                {specialiteExpertises
                  ? specialiteExpertises.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="offre-coach-coachExpert" name="coachExpert" data-cy="coachExpert" label="Coach Expert" type="select">
                <option value="" key="0" />
                {coachExperts
                  ? coachExperts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/offre-coach" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Retour</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Sauvegarder
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default OffreCoachUpdate;
