import React, { useEffect, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, reset } from './avis-client.reducer';

export const AvisClient = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const avisClientList = useAppSelector(state => state.avisClient.entities);
  const loading = useAppSelector(state => state.avisClient.loading);
  const links = useAppSelector(state => state.avisClient.links);
  const updateSuccess = useAppSelector(state => state.avisClient.updateSuccess);

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
      <h2 id="avis-client-heading" data-cy="AvisClientHeading">
        AvisClients
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Actualiser la liste
          </Button>
          <Link to="/avis-client/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Créer un nouveau Avis Client
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={avisClientList ? avisClientList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {avisClientList && avisClientList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('dateAvis')}>
                    Date Avis <FontAwesomeIcon icon={getSortIconByFieldName('dateAvis')} />
                  </th>
                  <th className="hand" onClick={sort('note')}>
                    Note <FontAwesomeIcon icon={getSortIconByFieldName('note')} />
                  </th>
                  <th className="hand" onClick={sort('descriptionAvis')}>
                    Description Avis <FontAwesomeIcon icon={getSortIconByFieldName('descriptionAvis')} />
                  </th>
                  <th>
                    Client <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    Coach Expert <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {avisClientList.map((avisClient, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/avis-client/${avisClient.id}`} color="link" size="sm">
                        {avisClient.id}
                      </Button>
                    </td>
                    <td>{avisClient.dateAvis ? <TextFormat type="date" value={avisClient.dateAvis} format={APP_DATE_FORMAT} /> : null}</td>
                    <td>{avisClient.note}</td>
                    <td>{avisClient.descriptionAvis}</td>
                    <td>{avisClient.client ? <Link to={`/client/${avisClient.client.id}`}>{avisClient.client.nom}</Link> : ''}</td>
                    <td>
                      {avisClient.coachExpert ? (
                        <Link to={`/coach-expert/${avisClient.coachExpert.id}`}>{avisClient.coachExpert.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/avis-client/${avisClient.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Voir</span>
                        </Button>
                        <Button tag={Link} to={`/avis-client/${avisClient.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/avis-client/${avisClient.id}/delete`)}
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
            !loading && <div className="alert alert-warning">Aucun Avis Client trouvé</div>
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default AvisClient;
