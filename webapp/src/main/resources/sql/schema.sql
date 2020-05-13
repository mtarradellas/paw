CREATE TABLE IF NOT EXISTS species (
id SERIAL PRIMARY KEY,
es_AR VARCHAR(255),
en_US VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS breeds (
id SERIAL PRIMARY KEY,
speciesId INTEGER REFERENCES species(id),
es_AR VARCHAR(255),
en_US VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS user_status (
id INTEGER primary key,
en_US VARCHAR(255),
es_AR VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS users (
id SERIAL PRIMARY KEY,
username VARCHAR(255) NOT NULL UNIQUE,
password VARCHAR(255) NOT NULL,
mail VARCHAR(255) NOT NULL UNIQUE,
status INTEGER references user_status(id)
);
CREATE TABLE IF NOT EXISTS pet_status (
id INTEGER primary key,
en_US VARCHAR(255),
es_AR VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS pets (
id SERIAL PRIMARY KEY,
petName VARCHAR(255),
species INTEGER REFERENCES species(id),
breed INTEGER REFERENCES breeds(id),
location TEXT,
vaccinated BOOLEAN NOT NULL,
gender VARCHAR(255) NOT NULL,
description TEXT,
birthDate DATE,
uploadDate DATE NOT NULL DEFAULT CURRENT_DATE,
price INTEGER,
ownerId INTEGER REFERENCES users(id),
status INTEGER REFERENCES pet_status(id)
);
CREATE TABLE IF NOT EXISTS images (
id SERIAL PRIMARY KEY,
img BYTEA,
petId INTEGER REFERENCES pets(id)
);
CREATE TABLE IF NOT EXISTS request_status (
id INTEGER primary key,
en_US VARCHAR(255),
es_AR VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS requests (
id SERIAL primary key,
ownerId INTEGER references users(id),
petId INTEGER references pets(id),
status INTEGER references request_status(id),
creationDate DATE DEFAULT CURRENT_DATE,
CONSTRAINT norepeats UNIQUE(ownerId,petId)
);