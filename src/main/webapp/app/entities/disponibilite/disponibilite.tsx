import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './disponibilite.reducer';

export const Disponibilite = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const disponibiliteList = useAppSelector(state => state.disponibilite.entities);
  const loading = useAppSelector(state => state.disponibilite.loading);

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
      <h2 id="disponibilite-heading" data-cy="DisponibiliteHeading">
        Disponibilites
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Actualiser la liste
          </Button>
          <Link to="/disponibilite/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Créer un nouveau Disponibilite
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {disponibiliteList && disponibiliteList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('heureDebutCreneauxDisponibilite')}>
                  Heure Debut Creneaux Disponibilite <FontAwesomeIcon icon={getSortIconByFieldName('heureDebutCreneauxDisponibilite')} />
                </th>
                <th className="hand" onClick={sort('heureFinCreneauxDisponibilite')}>
                  Heure Fin Creneaux Disponibilite <FontAwesomeIcon icon={getSortIconByFieldName('heureFinCreneauxDisponibilite')} />
                </th>
                <th className="hand" onClick={sort('canalSeance')}>
                  Canal Seance <FontAwesomeIcon icon={getSortIconByFieldName('canalSeance')} />
                </th>
                <th>
                  Coach Expert <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {disponibiliteList.map((disponibilite, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/disponibilite/${disponibilite.id}`} color="link" size="sm">
                      {disponibilite.id}
                    </Button>
                  </td>
                  <td>
                    {disponibilite.heureDebutCreneauxDisponibilite ? (
                      <TextFormat type="date" value={disponibilite.heureDebutCreneauxDisponibilite} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {disponibilite.heureFinCreneauxDisponibilite ? (
                      <TextFormat type="date" value={disponibilite.heureFinCreneauxDisponibilite} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{disponibilite.canalSeance}</td>
                  <td>
                    {disponibilite.coachExpert ? (
                      <Link to={`/coach-expert/${disponibilite.coachExpert.id}`}>{disponibilite.coachExpert.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/disponibilite/${disponibilite.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Voir</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/disponibilite/${disponibilite.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/disponibilite/${disponibilite.id}/delete`)}
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
          !loading && <div className="alert alert-warning">Aucun Disponibilite trouvé</div>
        )}
      </div>
    </div>
  );
};

export default Disponibilite;
