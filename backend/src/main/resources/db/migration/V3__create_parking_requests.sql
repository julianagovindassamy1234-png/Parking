CREATE TABLE parking_requests (
    id                    UUID      PRIMARY KEY DEFAULT gen_random_uuid(),
    requester_id          UUID      NOT NULL REFERENCES users(id),
    spot_id               UUID      NOT NULL REFERENCES parking_spots(id),
    status                VARCHAR(20) NOT NULL DEFAULT 'PENDING'
                              CHECK (status IN ('PENDING','APPROVED','REJECTED','AUTO_APPROVED','CANCELLED')),
    requested_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    responded_at          TIMESTAMP,
    responded_by          UUID      REFERENCES users(id),
    auto_approve_deadline TIMESTAMP NOT NULL,
    notes                 TEXT,
    created_at            TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at            TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_requests_status        ON parking_requests(status);
CREATE INDEX idx_requests_requester     ON parking_requests(requester_id);
CREATE INDEX idx_requests_spot          ON parking_requests(spot_id);
CREATE INDEX idx_requests_auto_deadline ON parking_requests(auto_approve_deadline)
    WHERE status = 'PENDING';
