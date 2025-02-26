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

import { getEntities } from './seance-reservation-coach.reducer';

export const SeanceReservationCoach = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const seanceReservationCoachList = useAppSelector(state => state.seanceReservationCoach.entities);
  const loading = useAppSelector(state => state.seanceReservationCoach.loading);

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
      <h2 id="seance-reservation-coach-heading" data-cy="SeanceReservationCoachHeading">
        SeanceReservationCoaches
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Actualiser la liste
          </Button>
          <Link
            to="/seance-reservation-coach/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp; Créer un nouveau Seance Reservation Coach
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {seanceReservationCoachList && seanceReservationCoachList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('heureDebutCreneauReserve')}>
                  Heure Debut Creneau Reserve <FontAwesomeIcon icon={getSortIconByFieldName('heureDebutCreneauReserve')} />
                </th>
                <th className="hand" onClick={sort('heureFinCreneauReserve')}>
                  Heure Fin Creneau Reserve <FontAwesomeIcon icon={getSortIconByFieldName('heureFinCreneauReserve')} />
                </th>
                <th className="hand" onClick={sort('canalSeance')}>
                  Canal Seance <FontAwesomeIcon icon={getSortIconByFieldName('canalSeance')} />
                </th>
                <th className="hand" onClick={sort('typeSeance')}>
                  Type Seance <FontAwesomeIcon icon={getSortIconByFieldName('typeSeance')} />
                </th>
                <th className="hand" onClick={sort('statutRealisation')}>
                  Statut Realisation <FontAwesomeIcon icon={getSortIconByFieldName('statutRealisation')} />
                </th>
                <th>
                  Facture <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Coach Expert <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Client <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Offre <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {seanceReservationCoachList.map((seanceReservationCoach, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/seance-reservation-coach/${seanceReservationCoach.id}`} color="link" size="sm">
                      {seanceReservationCoach.id}
                    </Button>
                  </td>
                  <td>
                    {seanceReservationCoach.heureDebutCreneauReserve ? (
                      <TextFormat type="date" value={seanceReservationCoach.heureDebutCreneauReserve} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {seanceReservationCoach.heureFinCreneauReserve ? (
                      <TextFormat type="date" value={seanceReservationCoach.heureFinCreneauReserve} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{seanceReservationCoach.canalSeance}</td>
                  <td>{seanceReservationCoach.typeSeance}</td>
                  <td>{seanceReservationCoach.statutRealisation}</td>
                  <td>
                    {seanceReservationCoach.facture ? (
                      <Link to={`/facture/${seanceReservationCoach.facture.id}`}>
                        {seanceReservationCoach.facture.dateComptableFacture}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {seanceReservationCoach.coachExpert ? (
                      <Link to={`/coach-expert/${seanceReservationCoach.coachExpert.id}`}>{seanceReservationCoach.coachExpert.nom}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {seanceReservationCoach.client ? (
                      <Link to={`/client/${seanceReservationCoach.client.id}`}>{seanceReservationCoach.client.nom}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {seanceReservationCoach.offre ? (
                      <Link to={`/offre-coach/${seanceReservationCoach.offre.id}`}>{seanceReservationCoach.offre.synthese}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/seance-reservation-coach/${seanceReservationCoach.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Voir</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/seance-reservation-coach/${seanceReservationCoach.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/seance-reservation-coach/${seanceReservationCoach.id}/delete`)}
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
          !loading && <div className="alert alert-warning">Aucun Seance Reservation Coach trouvé</div>
        )}
      </div>
    </div>
  );
};

export default SeanceReservationCoach;
