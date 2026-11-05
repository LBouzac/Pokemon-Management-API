# Plan de la vidéo : Pokémon Management API

**Durée visée : 8 min environ**

| # | Séquence | Durée | Cumul |
|---|---|---|---|
| 1 | Introduction | 0:45 | 0:45 |
| 2 | Modèle et architecture | 1:15 | 2:00 |
| 3 | Règle métier | 1:00 | 3:00 |
| 4 | Tests Repository | 1:15 | 4:15 |
| 5 | Tests Service | 2:00 | 6:15 |
| 6 | Tests Controller | 1:00 | 7:15 |
| 7 | `./mvnw install` en direct | 0:45 | 8:00 |
| 8 | Choix et conclusion | 1:00 | 9:00 |

Le cœur de la note, ce sont les tests : ils prennent plus de 4 minutes sur 9.

## 1. Introduction (0:45)

**À l'écran :** le README, en haut de page.

- Se présenter, nommer le projet.
- Le contexte : le microservice du SI de la Ligue Pokémon.
- L'objectif : une suite de tests sur les 3 couches, isolées, en structure AAA.
- Annoncer le plan en une phrase : modèle, règle métier, tests couche par couche, build, choix.

## 2. Modèle et architecture (1:15)

**À l'écran :** le diagramme de classes du README, puis le schéma des couches.

- Les deux entités. La relation : `@ManyToOne` côté `Pokemon` (il porte la clé étrangère), `@OneToMany(mappedBy)` côté `Dresseur`.
- Le DTO, et pourquoi on ne renvoie pas l'entité (boucle infinie en JSON, modèle de persistance exposé).
- Les 3 couches, et ce qu'on isole dans chacune : on teste chaque couche en remplaçant celle du dessous par un mock, ou par une vraie base H2 pour le repository.

## 3. Règle métier (1:00)

**À l'écran :** le tableau d'exemples du README, puis `calculerNiveau` dans `DresseurService`.

- Les 4 éléments de la règle : la somme des niveaux, +20 à partir de 3 types, +10 par Pokémon de niveau ≥ 50, et 1 si le dresseur n'a aucun Pokémon.
- Dérouler un exemple de bout en bout : Feu 50, Eau 60, Électrik 10 → 120 + 20 + 20 = **160**.
- Montrer que le code se lit comme la règle (une ligne par élément) et que les seuils sont des constantes nommées.

## 4. Tests Repository (1:15)

**À l'écran :** `data.sql`, puis `DresseurRepositoryTest`.

- `@DataJpaTest` : seule la couche JPA est chargée, sur une vraie base H2.
- Le jeu de données : Sacha (3 Pokémon), Ondine (2), Novice (0).
- Le piège à expliquer : `spring.jpa.defer-datasource-initialization=true`, sinon `data.sql` s'exécute avant la création des tables.
- Montrer 2 tests : `should_load_pokemons_when_dresseur_has_pokemons` (la relation fonctionne) et `should_persist_dresseur_when_saved`.
- Dire que chaque test est annulé en fin de transaction, donc les tests ne s'influencent pas.

## 5. Tests Service (2:00)

**À l'écran :** `DresseurServiceTest`.

- Isolation totale : `MockitoExtension`, sans contexte Spring. `@Mock` sur le repository, `@InjectMocks` sur le service.
- Le helper `givenDresseurAvec(...)` et le `when(findById).thenReturn(...)` qu'il contient.
- Détailler un test en pointant les 3 blocs : `should_cumulate_all_bonuses_when_diversity_and_elite_conditions_met`.
- Montrer la logique des cas limites, testés par paires de part et d'autre du seuil :
  - 49 contre 50 pour l'élite ;
  - 2 types contre 3 pour la diversité ;
  - 0 Pokémon.
- Le cas d'erreur, avec `assertThrows`.
- Lancer la classe dans l'IDE pour montrer les 10 tests au vert.

## 6. Tests Controller (1:00)

**À l'écran :** `DresseurControllerTest`.

- `@WebMvcTest` : seule la couche web est chargée. `@MockitoBean` remplace le service.
- Le test 200 : `MockMvc` simule la requête, puis on vérifie les 3 champs avec `jsonPath`.
- Le test 404 : le service mocké lève l'exception, et on vérifie le statut.

## 7. `./mvnw install` en direct (0:45)

**À l'écran :** le terminal.

- Lancer `./mvnw clean install`.
- Pendant que ça tourne, rappeler que c'est le critère de rendu : 100 % de succès.
- Montrer le résumé : `Tests run: 20, Failures: 0, Errors: 0`, puis `BUILD SUCCESS`.

> [!tip] Le build prend du temps
> S'il dure plus d'une minute, l'enregistrer à part et couper le passage au montage. Ou bien lancer `./mvnw install` une première fois avant d'enregistrer, pour que les dépendances soient déjà téléchargées.

## 8. Choix et conclusion (1:00)

**À l'écran :** le tableau « Choix d'implémentation » du README.

- **404 plutôt que 201** : 201 veut dire *Created*. Le choix a été validé avec l'enseignant.
- **Types comparés tels quels** : le sujet ne demande pas de normalisation. Une évolution possible serait d'ignorer casse et accents.
- **`assertEquals`** : c'est l'outil cité dans le sujet.
- **`@MockitoBean`** : il remplace `@MockBean`, supprimé en Spring Boot 4 (le projet est en 4.1.2).
- Conclure en une phrase : 19 tests sur 3 couches isolées (plus le test de démarrage), la règle métier couverte jusque dans ses cas limites.