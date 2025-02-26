import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';

import { Alert, Col, Row } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row>
      <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col>
      <Col md="9">
        <h1 className="display-4">Bienvenue, Java Hipster !</h1>
        <p className="lead">Ceci est votre page d&apos;accueil</p>
        {account?.login ? (
          <div>
            <Alert color="success">Vous êtes connecté en tant que &quot;{account.login}&quot;.</Alert>
          </div>
        ) : (
          <div>
            <Alert color="warning">
              Si vous voulez vous
              <span>&nbsp;</span>
              <Link to="/login" className="alert-link">
                connecter
              </Link>
              , vous pouvez utiliser les comptes par défaut : <br /> - Administrateur (nom d&apos;utilisateur=&quot;admin&quot; et mot de
              passe =&quot;admin&quot;) <br /> - Utilisateur (nom d&apos;utilisateur=&quot;user&quot; et mot de passe =&quot;user&quot;).
            </Alert>

            <Alert color="warning">
              Vous n&apos;avez pas encore de compte ?&nbsp;
              <Link to="/account/register" className="alert-link">
                Créer un compte
              </Link>
            </Alert>
          </div>
        )}
        <p>Si vous avez des questions à propos de JHipster :</p>

        <ul>
          <li>
            <a href="https://www.jhipster.tech/" target="_blank" rel="noopener noreferrer">
              Page d&apos;accueil de JHipster
            </a>
          </li>
          <li>
            <a href="https://stackoverflow.com/tags/jhipster/info" target="_blank" rel="noopener noreferrer">
              JHipster sur Stack Overflow
            </a>
          </li>
          <li>
            <a href="https://github.com/jhipster/generator-jhipster/issues?state=open" target="_blank" rel="noopener noreferrer">
              JHipster bug tracker
            </a>
          </li>
          <li>
            <a href="https://gitter.im/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">
              JHipster public chat room
            </a>
          </li>
          <li>
            <a href="https://twitter.com/jhipster" target="_blank" rel="noopener noreferrer">
              Suivez @jhipster sur Twitter
            </a>
          </li>
        </ul>

        <p>
          Si vous aimez JHipster, donnez nous une étoile sur{' '}
          <a href="https://github.com/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">
            GitHub
          </a>
          !
        </p>
      </Col>
    </Row>
  );
};

export default Home;
