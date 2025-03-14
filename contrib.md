Pour intégrer Google Sign-In dans votre application React avec TypeScript, vous devez suivre plusieurs étapes. Voici un guide détaillé pour vous aider à implémenter cette fonctionnalité :

1. Créer un projet Google Cloud et configurer les identifiants OAuth

   Accédez à la Console Google Cloud.

   Créez un nouveau projet ou sélectionnez un projet existant.

   Allez dans "API et services" > "Identifiants".

   Cliquez sur "Créer des identifiants" et choisissez "ID client OAuth".

   Configurez l'écran de consentement OAuth en fournissant les informations nécessaires.

   Ajoutez l'URI de redirection (par exemple, http://localhost:3000 pour le développement local).

   Notez l'ID client généré, vous en aurez besoin pour configurer le bouton de connexion Google.

1. Modification du modèle User

Ajout d'un champ pour stocker l'identifiant unique de l'utilisateur Google (googleId) et d'un champ pour indiquer le fournisseur d'authentification (provider).

2. Ajout d'une méthode pour gérer la connexion Google dans AuthenticateController
