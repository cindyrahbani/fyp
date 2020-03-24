
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS permissions CASCADE;
DROP TABLE IF EXISTS user_role CASCADE;
DROP TABLE IF EXISTS role_perm CASCADE;


CREATE TABLE users(id serial PRIMARY KEY,  name VARCHAR(255) NOT NULL, is_deleted BOOLEAN NOT NULL, created_on DATE, created_by VARCHAR(255), modified_on DATE, modified_by VARCHAR(255));
CREATE TABLE roles(id serial PRIMARY KEY,  name VARCHAR(255) NOT NULL, is_deleted BOOLEAN NOT NULL, created_on DATE, created_by VARCHAR(255), modified_on DATE, modified_by VARCHAR(255));
CREATE TABLE permissions(id serial PRIMARY KEY,  name VARCHAR(255) NOT NULL, is_deleted BOOLEAN NOT NULL, created_on DATE, created_by VARCHAR(255), modified_on DATE, modified_by VARCHAR(255));
CREATE TABLE role_perm(role_id integer REFERENCES roles(id) ON UPDATE CASCADE ON DELETE CASCADE, permission_id integer REFERENCES permissions(id) ON UPDATE CASCADE, PRIMARY KEY(role_id,permission_id));
CREATE TABLE user_role(user_id integer REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE, role_id integer REFERENCES roles(id) ON UPDATE CASCADE, PRIMARY KEY(user_id,role_id));
