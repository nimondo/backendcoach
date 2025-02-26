import React, { useEffect, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, reset } from './offre-coach.reducer';

export const OffreCoach = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const offreCoachList = useAppSelector(state => state.offreCoach.entities);
  const loading = useAppSelector(state => state.offreCoach.loading);
  const links = useAppSelector(state => state.offreCoach.links);
  const updateSuccess = useAppSelector(state => state.offreCoach.updateSuccess);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const resetAll = () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    resetAll();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="offre-coach-heading" data-cy="OffreCoachHeading">
        OffreCoaches
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Actualiser la liste
          </Button>
          <Link to="/offre-coach/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Créer un nouveau Offre Coach
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={offreCoachList ? offreCoachList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {offreCoachList && offreCoachList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('canalSeance')}>
                    Canal Seance <FontAwesomeIcon icon={getSortIconByFieldName('canalSeance')} />
                  </th>
                  <th className="hand" onClick={sort('typeSeance')}>
                    Type Seance <FontAwesomeIcon icon={getSortIconByFieldName('typeSeance')} />
                  </th>
                  <th className="hand" onClick={sort('synthese')}>
                    Synthese <FontAwesomeIcon icon={getSortIconByFieldName('synthese')} />
                  </th>
                  <th className="hand" onClick={sort('descriptionDetaillee')}>
                    Description Detaillee <FontAwesomeIcon icon={getSortIconByFieldName('descriptionDetaillee')} />
                  </th>
                  <th className="hand" onClick={sort('tarif')}>
                    Tarif <FontAwesomeIcon icon={getSortIconByFieldName('tarif')} />
                  </th>
                  <th className="hand" onClick={sort('duree')}>
                    Duree <FontAwesomeIcon icon={getSortIconByFieldName('duree')} />
                  </th>
                  <th className="hand" onClick={sort('descriptionDiplome')}>
                    Description Diplome <FontAwesomeIcon icon={getSortIconByFieldName('descriptionDiplome')} />
                  </th>
                  <th>
                    Specialite <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    Coach Expert <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {offreCoachList.map((offreCoach, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/offre-coach/${offreCoach.id}`} color="link" size="sm">
                        {offreCoach.id}
                      </Button>
                    </td>
                    <td>{offreCoach.canalSeance}</td>
                    <td>{offreCoach.typeSeance}</td>
                    <td>{offreCoach.synthese}</td>
                    <td>{offreCoach.descriptionDetaillee}</td>
                    <td>{offreCoach.tarif}</td>
                    <td>{offreCoach.duree}</td>
                    <td>{offreCoach.descriptionDiplome}</td>
                    <td>
                      {offreCoach.specialite ? (
                        <Link to={`/specialite-expertise/${offreCoach.specialite.id}`}>{offreCoach.specialite.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>
                      {offreCoach.coachExpert ? (
                        <Link to={`/coach-expert/${offreCoach.coachExpert.id}`}>{offreCoach.coachExpert.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/offre-coach/${offreCoach.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Voir</span>
                        </Button>
                        <Button tag={Link} to={`/offre-coach/${offreCoach.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/offre-coach/${offreCoach.id}/delete`)}
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
            !loading && <div className="alert alert-warning">Aucun Offre Coach trouvé</div>
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default OffreCoach;
