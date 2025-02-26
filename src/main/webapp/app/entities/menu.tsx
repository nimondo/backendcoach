import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/client">
        Client
      </MenuItem>
      <MenuItem icon="asterisk" to="/theme-expertise">
        Theme Expertise
      </MenuItem>
      <MenuItem icon="asterisk" to="/diplome">
        Diplome
      </MenuItem>
      <MenuItem icon="asterisk" to="/specialite-expertise">
        Specialite Expertise
      </MenuItem>
      <MenuItem icon="asterisk" to="/coach-expert">
        Coach Expert
      </MenuItem>
      <MenuItem icon="asterisk" to="/seance-reservation-coach">
        Seance Reservation Coach
      </MenuItem>
      <MenuItem icon="asterisk" to="/facture">
        Facture
      </MenuItem>
      <MenuItem icon="asterisk" to="/paiement">
        Paiement
      </MenuItem>
      <MenuItem icon="asterisk" to="/avis-client">
        Avis Client
      </MenuItem>
      <MenuItem icon="asterisk" to="/disponibilite">
        Disponibilite
      </MenuItem>
      <MenuItem icon="asterisk" to="/offre-coach">
        Offre Coach
      </MenuItem>
      <MenuItem icon="asterisk" to="/offre-coach-media">
        Offre Coach Media
      </MenuItem>
      <MenuItem icon="asterisk" to="/consent">
        Consent
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
