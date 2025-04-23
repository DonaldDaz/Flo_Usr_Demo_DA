# README

Guida all'avvio della applicazione.
1. Build dell'applicazione Spring: ./gradlew clean build
2. Build del Container Docker: docker compose build
3. Start up dei Container Docker: docker compose up

## PostgreSQL in Docker

Questa sezione spiega come configurare e avviare un database PostgreSQL in un container Docker utilizzando `docker-compose`, con l'esecuzione automatica di script di inizializzazione.

---

### Struttura del progetto

```bash
project-root/
├── docker-compose.yml
└── db/
    └── init/
        ├── 01-create-tables.sql
        └── 02-insert-data.sql
```

- **docker-compose.yml**: definisce il servizio PostgreSQL e i volumi.
- **db/init/**: contiene gli script SQL eseguiti automaticamente alla prima inizializzazione.

---

### Contenuto di `docker-compose.yml`

```yaml
version: '3.8'

services:
  db:
    image: postgres:15
    container_name: my_postgres
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: admin
      POSTGRES_DB: postgres
    ports:
      - "5433:5432"
    volumes:
      - db_data:/var/lib/postgresql/data
      - ./db/init:/docker-entrypoint-initdb.d

volumes:
  db_data:
```

**Spiegazioni**:
- **POSTGRES_USER**: utente superuser.
- **POSTGRES_PASSWORD**: password per l'utente.
- **POSTGRES_DB**: database di default.
- **"5433:5432"**: mappatura porta host (5433) → container (5432).

---

### Comandi principali

1. **Avvio iniziale (popolazione tramite script)**
   ```bash
   docker-compose up -d
   ```
    - Il volume `db_data` viene creato e popolato.
    - Gli script in `db/init/` vengono eseguiti in ordine alfabetico.

2. **Verifica dei log**
   ```bash
   docker logs my_postgres
   ```
    - Controlla che gli script siano stati eseguiti correttamente.

3. **Accesso al database**
    - **psql**:
      ```bash
      psql -h localhost -p 5433 -U postgres -d postgres
      ```
    - **DBeaver** o altri client:
        - Host: `localhost`
        - Porta: `5433`
        - Database: `postgres`
        - User: `postgres`
        - Password: `admin`

4. **Ripristino / Reset dei dati**

   Se desideri rieseguire gli script di inizializzazione (cancella volumi e ricrea):
   ```bash
   docker-compose down -v
   docker-compose up -d
   ```
---

## Spring Boot application

Per eseguire in locale l'applicazione è necessario disporre di Java SDK 17 come JAVA_HOME corrente

```bash
# esempio su Linux/macOS
export JAVA_HOME=/opt/jdk-17
export PATH=$JAVA_HOME/bin:$PATH

# verifica
java -version
# deve stampare 17.x
./gradlew bootRun
```
O in alternativa usare tool come **jenv** per cambiare dinamicamente e facilmente la versione di Java attualmente in uso _local_

Successivamente potrà essere eseguito il comando di run

```bash
./gradlew bootRun
```

### Alternativa jar file

È possibile in alternativa creare il jar della applicazione attraverso il _meccanismo di build gradle_

```bash
./gradlew clean build
```
E successivamente eseguire il jat direttamente tramite comando

```bash
java -jar build/libs/Flo_Usr_Demo-0.0.1-SNAPSHOT.jar
```

## Testing

Al fine di compiere testing delle funzionalità realizzate, vengono proposte due alternative:

1. Esecuzione di Integration testing tramite le classi di testing presenti in _src/test/resources_

2. Import in postman della _Postman Collection_ "postman/UserController_Postman_Collection.json" e conseguente esecuzione dei test postman dopo aver avviato l'app
