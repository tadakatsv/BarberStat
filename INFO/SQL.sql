CREATE TABLE IF NOT EXISTS barbers (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50),
    phone VARCHAR(20) NOT NULL UNIQUE,
    birth_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    barber_role VARCHAR(20) NOT NULL,
    salary_percent INT NOT NULL DEFAULT 50 CHECK (salary_percent BETWEEN 0 AND 100),
    notes TEXT
    );

CREATE TABLE IF NOT EXISTS clients (
                            id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                            first_name VARCHAR(50) NOT NULL,
                            last_name VARCHAR(50),
                            phone VARCHAR(20) NOT NULL UNIQUE,
                            birth_date DATE,
                            status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                            notes TEXT
);

CREATE TABLE IF NOT EXISTS services (
                            id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                            name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS barber_services (
                            id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                            barber_id BIGINT NOT NULL REFERENCES barbers(id) ON DELETE CASCADE,
                            service_id BIGINT NOT NULL REFERENCES services(id) ON DELETE CASCADE,
                            price DECIMAL(10, 2) NOT NULL,
                            CONSTRAINT uk_barber_service UNIQUE (barber_id, service_id)
);

CREATE TABLE IF NOT EXISTS visits (
                            id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                            client_id BIGINT NOT NULL REFERENCES clients(id),
                            barber_id BIGINT NOT NULL REFERENCES barbers(id),
                            service_id BIGINT NOT NULL REFERENCES services(id),
                            visit_time TIMESTAMPTZ NOT NULL,
                            actual_price DECIMAL(10, 2) NOT NULL,
                            status VARCHAR(20) NOT NULL DEFAULT 'PLANNED',
                            notes TEXT
    );