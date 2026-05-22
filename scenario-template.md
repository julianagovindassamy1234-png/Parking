# Test Scenario Template

Use this when writing a scenario **before** execution (i.e. the outer-red phase, or when no scenario yet exists for a feature). For executed-and-reported results, use `report-template.md` instead.

Save as: `test/<test-type>/<feature-id>_scenario.md`

---

## Scenario: <feature-id> — <short title>

**Feature ID:** <feature-id>
**Test type:** <integration | e2e | regression>
**Priority:** <P0 | P1 | P2>
**Author:** Quality Engineer
**Date authored:** <YYYY-MM-DD>
**Linked user story:** <link or ID from project/features/<feature-id>.md>

### Objective
One sentence: what behaviour are we verifying? Phrase it as user value, not implementation detail.
*Example: "A driver can start a charging session at an available station and is billed for the energy consumed."*

### Preconditions
List the system state required before the test can run. Be specific.
- The backend is running on `http://localhost:8080`.
- A driver account exists with email `test.driver@evcs.local`.
- Station `STN-001` is in `AVAILABLE` state.
- Driver has at least 10 EUR in account balance.

### Test data
Concrete values used in the test. Anyone reading this should be able to copy-paste.
| Field | Value |
|---|---|
| Driver email | test.driver@evcs.local |
| Station ID | STN-001 |
| Connector ID | C1 |
| Expected rate | 0.35 EUR/kWh |

### Steps
Numbered, atomic actions. Each step has one expected result.

| # | Action | Expected result |
|---|---|---|
| 1 | POST `/api/auth/login` with the test driver credentials | 200 OK, JWT returned |
| 2 | GET `/api/stations/STN-001` | 200 OK, status = "AVAILABLE" |
| 3 | POST `/api/sessions/start` with `{stationId: "STN-001", connectorId: "C1"}` | 201 Created, sessionId returned, station status flips to "OCCUPIED" |
| 4 | Wait 5 seconds, then POST `/api/sessions/<sessionId>/stop` | 200 OK, energy ≥ 0 kWh, cost computed |
| 5 | GET `/api/drivers/me/balance` | Balance reduced by the session cost |

### Expected overall result
The session completes successfully, the driver is charged the correct amount, and the station returns to `AVAILABLE`.

### Out of scope for this scenario
List anything intentionally NOT tested here so reviewers don't think it's a gap.
- Payment method failure handling (covered in FEAT-PAY-02)
- Concurrent session conflicts (covered in FEAT-STN-05)
