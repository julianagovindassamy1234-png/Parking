CREATE TABLE parking_assignments (
    id          UUID      PRIMARY KEY DEFAULT gen_random_uuid(),
    request_id  UUID      NOT NULL UNIQUE REFERENCES parking_requests(id),
    user_id     UUID      NOT NULL REFERENCES users(id),
    spot_id     UUID      NOT NULL REFERENCES parking_spots(id),
    assigned_at TIMESTAMP NOT NULL DEFAULT NOW(),
    released_at TIMESTAMP,
    is_active   BOOLEAN   NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_assignments_user_active ON parking_assignments(user_id) WHERE is_active = TRUE;
CREATE INDEX idx_assignments_spot        ON parking_assignments(spot_id);
