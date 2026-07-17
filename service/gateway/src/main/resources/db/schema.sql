-- Gateway auth schema — exécuté automatiquement au démarrage (spring.sql.init)
-- Compatible H2 (MODE=MySQL) et MySQL 8
CREATE TABLE IF NOT EXISTS users (
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)  NOT NULL,
    password VARCHAR(100) NOT NULL,
    enabled  BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_users_username UNIQUE (username)
);
