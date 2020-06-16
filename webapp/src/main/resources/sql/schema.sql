CREATE TABLE IF NOT EXISTS species (
id BIGSERIAL PRIMARY KEY,
es_AR VARCHAR(255),
en_US VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS breeds (
id BIGSERIAL PRIMARY KEY,
speciesId INTEGER REFERENCES species(id),
es_AR VARCHAR(255),
en_US VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS users (
id BIGSERIAL PRIMARY KEY,
username VARCHAR(255) NOT NULL UNIQUE,
password VARCHAR(255) NOT NULL,
mail VARCHAR(255) NOT NULL UNIQUE,
status INTEGER NOT NULL,
locale CHAR(7),
interestsDate TIMESTAMP DEFAULT NOW(),
requestsDate TIMESTAMP DEFAULT NOW()
);
CREATE TABLE IF NOT EXISTS admins (
id BIGINT REFERENCES users(id) PRIMARY KEY
);
CREATE TABLE IF NOT EXISTS reviews (
id BIGSERIAL PRIMARY KEY,
ownerId BIGINT NOT NULL REFERENCES users(id),
targetId BIGINT NOT NULL REFERENCES users(id),
score INTEGER  NOT NULL CHECK (score > 0 AND score < 6),
description TEXT,
creationDate DATE DEFAULT CURRENT_DATE,
status INTEGER NOT NULL,
CONSTRAINT owner_target_diff CHECK (ownerId <> targetId),
CONSTRAINT owner_target_unique UNIQUE (ownerId, targetId)
);
CREATE TABLE IF NOT EXISTS provinces(
id BIGSERIAL PRIMARY KEY,
name VARCHAR(256) NOT NULL UNIQUE,
latitude FLOAT(8),
longitude FLOAT(8)
);
CREATE TABLE IF NOT EXISTS departments(
id BIGSERIAL PRIMARY KEY,
name VARCHAR(256) NOT NULL,
latitude FLOAT(8),
longitude FLOAT(8),
province VARCHAR(256) REFERENCES provinces(name)
);
CREATE TABLE IF NOT EXISTS pets (
id BIGSERIAL PRIMARY KEY,
petName VARCHAR(255),
species INTEGER REFERENCES species(id),
breed INTEGER REFERENCES breeds(id),
department INTEGER REFERENCES departments(id),
province INTEGER REFERENCES provinces(id),
vaccinated BOOLEAN NOT NULL,
gender VARCHAR(255) NOT NULL,
description TEXT,
birthDate DATE,
uploadDate DATE DEFAULT CURRENT_DATE,
price INTEGER,
ownerId BIGINT NOT NULL REFERENCES users(id),
status INTEGER NOT NULL
);
CREATE TABLE IF NOT EXISTS images (
id BIGSERIAL PRIMARY KEY,
img BYTEA,
petId INTEGER REFERENCES pets(id)
);
CREATE TABLE IF NOT EXISTS requests (
id BIGSERIAL primary key,
ownerId INTEGER references users(id),
petId INTEGER references pets(id),
status INTEGER,
creationDate DATE DEFAULT CURRENT_DATE,
updateDate TIMESTAMP DEFAULT NOW(),
CONSTRAINT norepeats UNIQUE(ownerId, petId)
);
CREATE TABLE IF NOT EXISTS tokens (
id SERIAL PRIMARY KEY,
token UUID,
userId BIGINT REFERENCES users(id),
expirationDate DATE
);
CREATE TABLE IF NOT EXISTS questions (
id BIGSERIAL PRIMARY KEY,
content VARCHAR(255) NOT NULL,
ownerId BIGINT NOT NULL REFERENCES users(id),
targetId BIGINT NOT NULL REFERENCES users(id),
petId BIGINT NOT NULL REFERENCES pets(id),
creationDate DATE DEFAULT CURRENT_DATE,
status INTEGER NOT NULL
);
CREATE TABLE IF NOT EXISTS answers (
id BIGSERIAL PRIMARY KEY,
questionId BIGINT NOT NULL REFERENCES questions(id),
content VARCHAR(255) NOT NULL,
ownerId BIGINT NOT NULL REFERENCES users(id),
targetId BIGINT NOT NULL REFERENCES users(id),
petId BIGINT NOT NULL REFERENCES pets(id),
creationDate DATE DEFAULT CURRENT_DATE,
status INTEGER NOT NULL
);