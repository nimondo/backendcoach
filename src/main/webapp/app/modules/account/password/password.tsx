import React, { useEffect, useState } from 'react';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { Button, Col, Row } from 'reactstrap';
import { toast } from 'react-toastify';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import PasswordStrengthBar from 'app/shared/layout/password/password-strength-bar';
import { reset, savePassword } from './password.reducer';

export const PasswordPage = () => {
  const [password, setPassword] = useState('');
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(reset());
    dispatch(getSession());
    return () => {
      dispatch(reset());
    };
  }, []);

  const handleValidSubmit = ({ currentPassword, newPassword }) => {
    dispatch(savePassword({ currentPassword, newPassword }));
  };

  const updatePassword = event => setPassword(event.target.value);

  const account = useAppSelector(state => state.authentication.account);
  const successMessage = useAppSelector(state => state.password.successMessage);
  const errorMessage = useAppSelector(state => state.password.errorMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
    } else if (errorMessage) {
      toast.error(errorMessage);
    }
    dispatch(reset());
  }, [successMessage, errorMessage]);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="password-title">
            Changer le mot de passe pour [<strong>{account.login}</strong>]
          </h2>
          <ValidatedForm id="password-form" onSubmit={handleValidSubmit}>
            <ValidatedField
              name="currentPassword"
              label="Mot de passe actuel"
              placeholder="Mot de passe actuel"
              type="password"
              validate={{
                required: { value: true, message: 'Votre mot de passe est requis.' },
              }}
              data-cy="currentPassword"
            />
            <ValidatedField
              name="newPassword"
              label="Nouveau mot de passe"
              placeholder="Nouveau mot de passe"
              type="password"
              validate={{
                required: { value: true, message: 'Votre mot de passe est requis.' },
                minLength: { value: 4, message: 'Votre mot de passe doit comporter au moins 4 caractères.' },
                maxLength: { value: 50, message: 'Votre mot de passe ne doit pas comporter plus de 50 caractères.' },
              }}
              onChange={updatePassword}
              data-cy="newPassword"
            />
            <PasswordStrengthBar password={password} />
            <ValidatedField
              name="confirmPassword"
              label="Confirmation du nouveau mot de passe"
              placeholder="Confirmation du nouveau mot de passe"
              type="password"
              validate={{
                required: { value: true, message: 'Votre confirmation du mot de passe est requise.' },
                minLength: { value: 4, message: 'Votre confirmation du mot de passe doit comporter au moins 4 caractères.' },
                maxLength: { value: 50, message: 'Votre confirmation du mot de passe ne doit pas comporter plus de 50 caractères.' },
                validate: v => v === password || 'Le nouveau mot de passe et sa confirmation ne sont pas égaux !',
              }}
              data-cy="confirmPassword"
            />
            <Button color="success" type="submit" data-cy="submit">
              Sauvegarder
            </Button>
          </ValidatedForm>
        </Col>
      </Row>
    </div>
  );
};

export default PasswordPage;
