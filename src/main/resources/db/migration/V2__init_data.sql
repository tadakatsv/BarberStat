-- 1. Insert 100 Barbers
INSERT INTO barbers (first_name, last_name, phone, birth_date, status, barber_role, salary_percent, notes)
SELECT
    'BarberFirst_' || i,
    'BarberLast_' || i,
    '+1555000' || LPAD(i::text, 4, '0'),
    (CURRENT_DATE - ((20 + (i % 30)) * INTERVAL '1 year'))::date,
    CASE WHEN i % 5 = 0 THEN 'INACTIVE' ELSE 'ACTIVE' END,
    CASE WHEN i % 3 = 0 THEN 'SENIOR' ELSE 'JUNIOR' END,
    40 + (i % 20),
    'Auto-generated barber profile ' || i
FROM generate_series(1, 100) AS i;

-- 2. Insert 100 Clients
INSERT INTO clients (first_name, last_name, phone, birth_date, status, last_visit_date, notes)
SELECT
    'ClientFirst_' || i,
    'ClientLast_' || i,
    '+1444000' || LPAD(i::text, 4, '0'),
    (CURRENT_DATE - ((15 + (i % 40)) * INTERVAL '1 year'))::date,
    CASE WHEN i % 10 = 0 THEN 'INACTIVE' ELSE 'ACTIVE' END,
    (CURRENT_DATE - ((i % 100) * INTERVAL '1 day'))::date,
    'Auto-generated client profile ' || i
FROM generate_series(1, 100) AS i;

-- 3. Insert 100 Offers
INSERT INTO offers (name)
SELECT
    'Offer_' || i || '_' ||
    CASE i % 4
        WHEN 0 THEN 'Haircut'
        WHEN 1 THEN 'Beard Trim'
        WHEN 2 THEN 'Styling'
        ELSE 'Coloring'
        END
FROM generate_series(1, 100) AS i;

-- 4. Insert 100 Barber Offerings
-- We join the generated barbers and offers to ensure valid foreign keys and unique pairs
INSERT INTO barber_offerings (barber_id, offer_id, price, custom_time)
SELECT
    b.id,
    o.id,
    (15.00 + (b.id % 50))::decimal(10,2),
    30 + (o.id % 60)
FROM barbers b
         JOIN offers o ON b.id = o.id;

-- 5. Insert 100 Visits
-- Joining clients, barbers, and offers to guarantee referential integrity
INSERT INTO visits (client_id, barber_id, offer_id, visit_time, actual_price, actual_percent_barber, status, duration, notes)
SELECT
    c.id,
    b.id,
    o.id,
    CURRENT_TIMESTAMP - ((c.id % 30) * INTERVAL '1 day'),
    25.00 + (b.id % 20),
    50,
    CASE
        WHEN c.id % 5 = 0 THEN 'CANCELLED'
        WHEN c.id % 2 = 0 THEN 'COMPLETED'
        ELSE 'PLANNED'
        END,
    45,
    'Auto-generated visit record ' || c.id
FROM clients c
         JOIN barbers b ON c.id = b.id
         JOIN offers o ON c.id = o.id;

-- 6. Insert 100 Salaries
INSERT INTO salaries (barber_id, period_start, period_end, total_sum, status)
SELECT
    b.id,
    (CURRENT_DATE - ((b.id % 12 + 1) * INTERVAL '1 month'))::date,
    (CURRENT_DATE - ((b.id % 12) * INTERVAL '1 month'))::date,
    1000.00 + (b.id * 10),
    CASE WHEN b.id % 4 = 0 THEN 'PENDING' ELSE 'PAID' END
FROM barbers b;