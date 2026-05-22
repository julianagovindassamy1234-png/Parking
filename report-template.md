# Test Report Template (Markdown)

Use this template for every executed test run. Fill every field — no placeholders left behind.

Save as: `test/<test-type>/<feature-id>_<test-type>_<YYYY-MM-DD>_<HHMM>.md`
And mirror to: `reports/<test-type>/<feature-id>_<test-type>_<YYYY-MM-DD>_<HHMM>.md`

---

# Test Report — <feature-id>

| Field | Value |
|---|---|
| **Project** | EV Charging System |
| **Feature ID** | <feature-id> |
| **Test type** | <integration \| e2e \| regression> |
| **Executed by** | Quality Engineer (Claude) |
| **Executed on** | <YYYY-MM-DD HH:MM> |
| **Build / commit** | <git SHA or "n/a"> |
| **Overall result** | ✅ PASS \| ❌ FAIL \| ⚠️ PARTIAL |
| **Pass rate** | <X>/<Y> steps passed |
| **Linked scenario** | `test/<test-type>/<feature-id>_scenario.md` |

## Summary
One paragraph: what was tested, what the outcome was, and the headline finding (e.g. "All steps passed", or "Step 4 failed because the session cost was computed incorrectly").

## Preconditions verified
- [x] Backend running
- [x] Test driver account exists
- [ ] Station STN-001 was in AVAILABLE state (was actually OCCUPIED — see step 2)

## Step-by-step results

| # | Action | Expected | Actual | Result |
|---|---|---|---|---|
| 1 | POST `/api/auth/login` | 200, JWT | 200, JWT received | ✅ |
| 2 | GET `/api/stations/STN-001` | status = AVAILABLE | status = OCCUPIED | ❌ |
| 3 | POST `/api/sessions/start` | 201 Created | 409 Conflict | ❌ |
| 4 | … | … | … | … |

## Failures and bugs raised
For each failed step:
- **Step 2 — Station state mismatch**
  - **Severity:** High
  - **Bug ID:** BUG-2026-05-22-001 (filed in `reports/bugs/open.md`)
  - **Reproduction:** described in bug note
  - **Suspected cause:** station state cleanup not running between tests

If no failures: write "None."

## Environment notes
Anything unusual about the run: data resets, mock services in use, timing oddities. Future readers will thank you.

## Decision
- ✅ **Ship it** — all P0 steps passed, no blocking bugs.
- ❌ **Hold** — N blocking bugs raised, see above.
- ⚠️ **Conditional** — P0 passed but P1 issues exist; ship with notes.

---

*This report was also saved to the Claude Project at `reports/<test-type>/` for BA triage.*
