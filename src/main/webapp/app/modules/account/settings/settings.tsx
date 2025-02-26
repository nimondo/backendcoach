import React, { useEffect } from 'react';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm, isEmail } from 'react-jhipster';
import { toast } from 'react-toastify';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import { reset, saveAccountSettings } from './settings.reducer';

export const SettingsPage = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const successMessage = useAppSelector(state => state.settings.successMessage);

  useEffect(() => {
    dispatch(getSession());
    return () => {
      dispatch(reset());
    };
  }, []);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
    }
  }, [successMessage]);

  const handleValidSubmit = values => {
    dispatch(
      saveAccountSettings({
        ...account,
        ...values,
      }),
    );
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="settings-title">
            Profil de l&apos;utilisateur [<strong>{account.login}</strong>]
          </h2>
          <ValidatedForm id="settings-form" onSubmit={handleValidSubmit} defaultValues={account}>
            <ValidatedField
              name="firstName"
              label="Prénom"
              id="firstName"
              placeholder="Votre prénom"
              validate={{
                required: { value: true, message: 'Votre prénom est requis.' },
                minLength: { value: 1, message: 'Votre prénom doit comporter au moins un caractère.' },
                maxLength: { value: 50, message: 'Votre prénom ne doit pas comporter plus de 50 caractères.' },
              }}
              data-cy="firstname"
            />
            <ValidatedField
              name="lastName"
              label="Nom"
              id="lastName"
              placeholder="Votre nom"
              validate={{
                required: { value: true, message: 'Votre nom est requis.' },
                minLength: { value: 1, message: 'Votre nom doit comporter au moins un caractère.' },
                maxLength: { value: 50, message: 'Votre nom ne doit pas comporter plus de 50 caractères.' },
              }}
              data-cy="lastname"
            />
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
              data-cy="email"
            />
            <Button color="primary" type="submit" data-cy="submit">
              Sauvegarder
            </Button>
          </ValidatedForm>
        </Col>
      </Row>
    </div>
  );
};

export default SettingsPage;
