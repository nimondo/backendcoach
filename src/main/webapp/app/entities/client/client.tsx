import React, { useEffect, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, byteSize, getPaginationState, openFile } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, reset } from './client.reducer';

export const Client = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const clientList = useAppSelector(state => state.client.entities);
  const loading = useAppSelector(state => state.client.loading);
  const links = useAppSelector(state => state.client.links);
  const updateSuccess = useAppSelector(state => state.client.updateSuccess);

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
      <h2 id="client-heading" data-cy="ClientHeading">
        Clients
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Actualiser la liste
          </Button>
          <Link to="/client/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Créer un nouveau Client
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={clientList ? clientList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {clientList && clientList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('genre')}>
                    Genre <FontAwesomeIcon icon={getSortIconByFieldName('genre')} />
                  </th>
                  <th className="hand" onClick={sort('nom')}>
                    Nom <FontAwesomeIcon icon={getSortIconByFieldName('nom')} />
                  </th>
                  <th className="hand" onClick={sort('prenom')}>
                    Prenom <FontAwesomeIcon icon={getSortIconByFieldName('prenom')} />
                  </th>
                  <th className="hand" onClick={sort('dateNaissance')}>
                    Date Naissance <FontAwesomeIcon icon={getSortIconByFieldName('dateNaissance')} />
                  </th>
                  <th className="hand" onClick={sort('adresseEmail')}>
                    Adresse Email <FontAwesomeIcon icon={getSortIconByFieldName('adresseEmail')} />
                  </th>
                  <th className="hand" onClick={sort('numeroTelephone')}>
                    Numero Telephone <FontAwesomeIcon icon={getSortIconByFieldName('numeroTelephone')} />
                  </th>
                  <th className="hand" onClick={sort('ville')}>
                    Ville <FontAwesomeIcon icon={getSortIconByFieldName('ville')} />
                  </th>
                  <th className="hand" onClick={sort('codePostal')}>
                    Code Postal <FontAwesomeIcon icon={getSortIconByFieldName('codePostal')} />
                  </th>
                  <th className="hand" onClick={sort('numeroEtNomVoie')}>
                    Numero Et Nom Voie <FontAwesomeIcon icon={getSortIconByFieldName('numeroEtNomVoie')} />
                  </th>
                  <th className="hand" onClick={sort('preferenceCanalSeance')}>
                    Preference Canal Seance <FontAwesomeIcon icon={getSortIconByFieldName('preferenceCanalSeance')} />
                  </th>
                  <th className="hand" onClick={sort('photo')}>
                    Photo <FontAwesomeIcon icon={getSortIconByFieldName('photo')} />
                  </th>
                  <th className="hand" onClick={sort('urlPhotoProfil')}>
                    Url Photo Profil <FontAwesomeIcon icon={getSortIconByFieldName('urlPhotoProfil')} />
                  </th>
                  <th>
                    User <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {clientList.map((client, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/client/${client.id}`} color="link" size="sm">
                        {client.id}
                      </Button>
                    </td>
                    <td>{client.genre}</td>
                    <td>{client.nom}</td>
                    <td>{client.prenom}</td>
                    <td>
                      {client.dateNaissance ? <TextFormat type="date" value={client.dateNaissance} format={APP_DATE_FORMAT} /> : null}
                    </td>
                    <td>{client.adresseEmail}</td>
                    <td>{client.numeroTelephone}</td>
                    <td>{client.ville}</td>
                    <td>{client.codePostal}</td>
                    <td>{client.numeroEtNomVoie}</td>
                    <td>{client.preferenceCanalSeance}</td>
                    <td>
                      {client.photo ? (
                        <div>
                          {client.photoContentType ? (
                            <a onClick={openFile(client.photoContentType, client.photo)}>
                              <img src={`data:${client.photoContentType};base64,${client.photo}`} style={{ maxHeight: '30px' }} />
                              &nbsp;
                            </a>
                          ) : null}
                          <span>
                            {client.photoContentType}, {byteSize(client.photo)}
                          </span>
                        </div>
                      ) : null}
                    </td>
                    <td>{client.urlPhotoProfil}</td>
                    <td>{client.user ? client.user.id : ''}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/client/${client.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Voir</span>
                        </Button>
                        <Button tag={Link} to={`/client/${client.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/client/${client.id}/delete`)}
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
            !loading && <div className="alert alert-warning">Aucun Client trouvé</div>
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default Client;
