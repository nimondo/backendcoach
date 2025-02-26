import React, { useEffect } from 'react';
import { ValidatedField, ValidatedForm, isEmail } from 'react-jhipster';
import { Alert, Button, Col, Row } from 'reactstrap';
import { toast } from 'react-toastify';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { handlePasswordResetInit, reset } from '../password-reset.reducer';

export const PasswordResetInit = () => {
  const dispatch = useAppDispatch();

  useEffect(
    () => () => {
      dispatch(reset());
    },
    [],
  );

  const handleValidSubmit = ({ email }) => {
    dispatch(handlePasswordResetInit(email));
  };

  const successMessage = useAppSelector(state => state.passwordReset.successMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
    }
  }, [successMessage]);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1>Réinitialiser son mot de passe</h1>
          <Alert color="warning">
            <p>Veuillez renseigner l&apos;adresse email utilisée pour vous enregistrer</p>
          </Alert>
          <ValidatedForm onSubmit={handleValidSubmit}>
            <ValidatedField
              name="email"
              label="Email"
              placeholder="Votre email"
              type="email"
              validate={{
                required: { value: true, message: 'Votre email est requis.' },
                minLength: { value: 5, message: 'Votre email doit comporter au moins 5 caractères.' },
                maxLength: { value: 254, message: 'Votre email ne doit pas comporter plus de 50 caractères.' },
                validate: v => isEmail(v) || "Votre email n'est pas valide.",
              }}
              data-cy="emailResetPassword"
            />
            <Button color="primary" type="submit" data-cy="submit">
              Réinitialiser le mot de passe
            </Button>
          </ValidatedForm>
        </Col>
      </Row>
    </div>
  );
};

export default PasswordResetInit;
