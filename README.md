# AeroportSpring

API REST de gestion aéroportuaire : aéroports, terminaux, vols, avions, compagnies, passagers et personnels.

Projet Spring Boot 4.1 / Java 17, avec JPA (Hibernate), H2 en mémoire pour le développement et PostgreSQL via Docker Compose.

## Démarrage rapide

### Avec Docker (base PostgreSQL persistante)

```bash
docker compose up --build
```

Le `compose.yaml` lance PostgreSQL 17 puis l'application avec le profil `docker` (`application-docker.properties`). Les données sont conservées dans le volume `db-data` et le schéma est mis à jour sans être effacé (`ddl-auto=update`). PostgreSQL est exposé sur le port 5432 pour s'y connecter depuis IntelliJ ou DBeaver.

### Tests

```bash
./mvnw test
```

## Documentation de l'API

| Ressource | URL |
|---|---|
| Interface Scalar | http://localhost:8080/docs |
| Document OpenAPI (JSON) | http://localhost:8080/v3/api-docs |

## Modèle de données

- **Aeroport** — nom, pays, adresse, UTC ; possède des terminaux et du personnel affecté.
- **Terminal** — rattaché à un aéroport, point de départ d'un vol.
- **Vol** — compagnie, avion, terminal de départ, aéroport de destination, dates, prix, durée, passagers et personnels.
- **Avion** — modèle (`Boeing747`, `Boeing777`, `A380`, `A340`), capacité, compagnie, indicateur `enVol`.
- **Compagnie** — nom ; regroupe des avions et des vols.
- **Passager** — nom, prénom, passeport, bagage (`SOUTE`, `CABINE`, `SAC`).
- **Personnel** — nom, prénom, profession (`PILOTE`, `COPILOTE`, `STEWART`, `EMPLOYEPOLYVALENT`).

## Endpoints

Chaque ressource expose le CRUD classique : `GET /{Ressource}`, `GET /{Ressource}/{id}`, `POST /{Ressource}`, `PUT /{Ressource}/{id}`, `DELETE /{Ressource}/{id}`, sur `/Aeroport`, `/Terminal`, `/Vol`, `/Avion`, `/Compagnie`, `/Passager` et `/Personnel`.

S'y ajoutent les routes de mise en relation :

### `/Aeroport`
- `GET|POST|DELETE /{id}/terminals[/{terminalId}]` — consulter, rattacher ou détacher un terminal
- `GET|POST|DELETE /{id}/personnels[/{personnelId}]` — consulter, affecter ou retirer un membre du personnel

### `/Vol`
- `GET|POST|DELETE /{id}/passagers[/{passagerId}]` — liste, ajout, retrait d'un passager
- `GET|POST|DELETE /{id}/personnels[/{personnelId}]` — équipage du vol
- `PUT|DELETE /{id}/terminal[/{terminalId}]` — terminal de départ
- `PUT|DELETE /{id}/avion[/{avionId}]` — avion affecté
- `PUT /{id}/compagnie/{compagnieId}` — compagnie opératrice
- `PUT /{id}/destination/{aeroportId}` — aéroport de destination

### `/Avion`
- `PATCH /{id}/compagnie/{compagnieId}` — rattacher à une compagnie
- `PATCH /{id}/enVol` — mettre à jour le statut en vol

### `/Compagnie`
- `GET /{id}/avions` — flotte de la compagnie
- `GET /{id}/vols` — vols opérés

### `/Passager`
- `PATCH /{id}/passeport` — mettre à jour le passeport
- `PATCH /{id}/bagage` — changer le type de bagage
- `GET /{id}/vols` — vols du passager

### `/Personnel`
- `PATCH /{id}/profession` — changer la profession
- `GET /{id}/vols` — vols affectés

## Structure du projet

```
src/main/java/com/example/aeroportspring/
├── config/      OpenApiConfig (métadonnées OpenAPI), WebConfig (redirection /docs)
├── controller/  Endpoints REST, un par ressource
├── service/     Logique métier et règles de rattachement
├── model/       Entités JPA et énumérations
└── repository/  Interfaces Spring Data JPA
```
