-- Bases + compte de service applicatif (pas root)
-- Exécuté par Docker au 1er démarrage MySQL, ou via scripts/setup-mysql.ps1

CREATE DATABASE IF NOT EXISTS medilabo_patient
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS medilabo_gateway
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Compte de service : utilisé uniquement par les microservices (jamais root en app)
CREATE USER IF NOT EXISTS 'medilabo'@'%' IDENTIFIED BY 'medilabo';
CREATE USER IF NOT EXISTS 'medilabo'@'localhost' IDENTIFIED BY 'medilabo';

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX, REFERENCES
    ON medilabo_patient.* TO 'medilabo'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX, REFERENCES
    ON medilabo_gateway.* TO 'medilabo'@'%';

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX, REFERENCES
    ON medilabo_patient.* TO 'medilabo'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX, REFERENCES
    ON medilabo_gateway.* TO 'medilabo'@'localhost';

FLUSH PRIVILEGES;
