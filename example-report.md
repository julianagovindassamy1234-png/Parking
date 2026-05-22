# Example Filled Report

This is a worked example of what a good test report looks like for the EV Charging System. Use it as a reference for tone, level of detail, and concreteness.

The example below corresponds to fictional feature **FEAT-CHG-01: Start and stop a charging session**.

---

# Test Report — FEAT-CHG-01

| Field | Value |
|---|---|
| **Project** | EV Charging System |
| **Feature ID** | FEAT-CHG-01 |
| **Test type** | e2e |
| **Executed by** | Quality Engineer (Claude) |
| **Executed on** | 2026-05-22 14:30 |
| **Build / commit** | a1b2c3d |
| **Overall result** | ⚠️ PARTIAL |
| **Pass rate** | 4/5 steps passed |
| **Linked scenario** | `test/e2e/FEAT-CHG-01_scenario.md` |

## Summary
Tested the happy path for starting and stopping a charging session against a local backend. Authentication, station lookup, session start, and session stop all worked. The final balance verification failed — the driver was charged 1.05 EUR for a session that should have cost 1.00 EUR. Suspected rounding bug in the billing service.

## Preconditions verified
- [x] Backend running on localhost:8080
- [x] Test driver `test.driver@evcs.local` exists with 10.00 EUR balance
- [x] Station STN-001 in AVAILABLE state
- [x] Connector C1 healthy

## Step-by-step results

| # | Action | Expected | Actual | Result |
|---|---|---|---|---|
| 1 | POST `/api/auth/login` with test creds | 200 OK, JWT | 200 OK, JWT received | ✅ |
| 2 | GET `/api/stations/STN-001` | status = AVAILABLE | status = AVAILABLE | ✅ |
| 3 | POST `/api/sessions/start` with `{stationId: "STN-001", connectorId: "C1"}` | 201, sessionId, station → OCCUPIED | 201, sessionId = `sess_42`, station → OCCUPIED | ✅ |
| 4 | Wait 5s, POST `/api/sessions/sess_42/stop` | 200, energy ≈ 2.86 kWh, cost = 1.00 EUR | 200, energy = 2.857 kWh, cost = 1.05 EUR | ❌ |
| 5 | GET `/api/drivers/me/balance` | Balance = 9.00 EUR | Balance = 8.95 EUR | ❌ (cascading from step 4) |

## Failures and bugs raised

- **Step 4 — Session cost incorrect**
  - **Severity:** High
  - **Bug ID:** BUG-2026-05-22-001
  - **Filed at:** `reports/bugs/open.md`
  - **Suspected cause:** billing service multiplies before rounding; should round per-kWh first or use BigDecimal end-to-end.
  - **AC violated:** *"The driver is charged at the published rate per kWh, rounded to two decimal places."*

## Environment notes
Backend was running on commit `a1b2c3d` with in-memory H2 database. Time was wall-clock; the 5-second wait used `Thread.sleep` equivalent via `curl --max-time`.

## Decision
⚠️ **CONDITIONAL** — The core charging flow works end-to-end. The billing rounding bug is high severity but does not block demo; it must be fixed before the regression suite is re-run and before any merge to `main`.
