import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { deleteEntity, getEntity } from './seance-reservation-coach.reducer';

export const SeanceReservationCoachDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const seanceReservationCoachEntity = useAppSelector(state => state.seanceReservationCoach.entity);
  const updateSuccess = useAppSelector(state => state.seanceReservationCoach.updateSuccess);

  const handleClose = () => {
    navigate('/seance-reservation-coach');
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(seanceReservationCoachEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="seanceReservationCoachDeleteDialogHeading">
        Confirmation de suppression
      </ModalHeader>
      <ModalBody id="coachingApp.seanceReservationCoach.delete.question">
        Êtes-vous certain de vouloir supprimer le Seance Reservation Coach {seanceReservationCoachEntity.id} ?
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Annuler
        </Button>
        <Button id="jhi-confirm-delete-seanceReservationCoach" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Supprimer
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default SeanceReservationCoachDeleteDialog;
