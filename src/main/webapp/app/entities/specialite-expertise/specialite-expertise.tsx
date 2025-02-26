import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './specialite-expertise.reducer';

export const SpecialiteExpertise = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const specialiteExpertiseList = useAppSelector(state => state.specialiteExpertise.entities);
  const loading = useAppSelector(state => state.specialiteExpertise.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="specialite-expertise-heading" data-cy="SpecialiteExpertiseHeading">
        SpecialiteExpertises
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Actualiser la liste
          </Button>
          <Link
            to="/specialite-expertise/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp; Créer un nouveau Specialite Expertise
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {specialiteExpertiseList && specialiteExpertiseList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('specialite')}>
                  Specialite <FontAwesomeIcon icon={getSortIconByFieldName('specialite')} />
                </th>
                <th className="hand" onClick={sort('specialiteDescription')}>
                  Specialite Description <FontAwesomeIcon icon={getSortIconByFieldName('specialiteDescription')} />
                </th>
                <th className="hand" onClick={sort('tarifMoyenHeure')}>
                  Tarif Moyen Heure <FontAwesomeIcon icon={getSortIconByFieldName('tarifMoyenHeure')} />
                </th>
                <th className="hand" onClick={sort('dureeTarif')}>
                  Duree Tarif <FontAwesomeIcon icon={getSortIconByFieldName('dureeTarif')} />
                </th>
                <th className="hand" onClick={sort('diplomeRequis')}>
                  Diplome Requis <FontAwesomeIcon icon={getSortIconByFieldName('diplomeRequis')} />
                </th>
                <th className="hand" onClick={sort('urlPhoto')}>
                  Url Photo <FontAwesomeIcon icon={getSortIconByFieldName('urlPhoto')} />
                </th>
                <th>
                  Diplome <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Theme Expertise <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Coach Expert <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {specialiteExpertiseList.map((specialiteExpertise, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/specialite-expertise/${specialiteExpertise.id}`} color="link" size="sm">
                      {specialiteExpertise.id}
                    </Button>
                  </td>
                  <td>{specialiteExpertise.specialite}</td>
                  <td>{specialiteExpertise.specialiteDescription}</td>
                  <td>{specialiteExpertise.tarifMoyenHeure}</td>
                  <td>{specialiteExpertise.dureeTarif}</td>
                  <td>{specialiteExpertise.diplomeRequis ? 'true' : 'false'}</td>
                  <td>{specialiteExpertise.urlPhoto}</td>
                  <td>
                    {specialiteExpertise.diplome ? (
                      <Link to={`/diplome/${specialiteExpertise.diplome.id}`}>{specialiteExpertise.diplome.libelle}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {specialiteExpertise.themeExpertise ? (
                      <Link to={`/theme-expertise/${specialiteExpertise.themeExpertise.id}`}>
                        {specialiteExpertise.themeExpertise.libelleExpertise}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {specialiteExpertise.coachExperts
                      ? specialiteExpertise.coachExperts.map((val, j) => (
                          <span key={j}>
                            <Link to={`/coach-expert/${val.id}`}>{val.id}</Link>
                            {j === specialiteExpertise.coachExperts.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/specialite-expertise/${specialiteExpertise.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Voir</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/specialite-expertise/${specialiteExpertise.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/specialite-expertise/${specialiteExpertise.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Supprimer</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">Aucun Specialite Expertise trouvé</div>
        )}
      </div>
    </div>
  );
};

export default SpecialiteExpertise;
