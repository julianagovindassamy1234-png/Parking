CREATE TABLE parking_spots (
    id         UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    spot_code  VARCHAR(50)  NOT NULL UNIQUE,
    location   VARCHAR(255) NOT NULL,
    floor      INT,
    status     VARCHAR(20)  NOT NULL DEFAULT 'AVAILABLE'
                   CHECK (status IN ('AVAILABLE', 'OCCUPIED', 'RESERVED', 'MAINTENANCE')),
    spot_type  VARCHAR(20)  NOT NULL DEFAULT 'STANDARD'
                   CHECK (spot_type IN ('STANDARD', 'DISABLED', 'ELECTRIC', 'MOTORCYCLE')),
    is_active  BOOLEAN      NOT NULL DEFAULT TRUE,
    version    BIGINT       NOT NULL DEFAULT 0,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_spots_status_location ON parking_spots(status, location);
CREATE INDEX idx_spots_is_active       ON parking_spots(is_active);
