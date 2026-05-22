# Bug Note Template

Every failed test step produces one of these. Appended to `reports/bugs/open.md` in the Claude Project so the BA can triage.

Bug IDs follow the pattern `BUG-<YYYY-MM-DD>-<NNN>` where NNN is a zero-padded sequence number for that day, starting at 001.

---

## BUG-<YYYY-MM-DD>-<NNN> — <Short title>

| Field | Value |
|---|---|
| **Bug ID** | BUG-2026-05-22-001 |
| **Filed on** | 2026-05-22 14:32 |
| **Filed by** | Quality Engineer (Claude) |
| **Feature ID** | <feature-id> |
| **Source test** | `reports/<test-type>/<filename>` |
| **Severity** | Blocker \| High \| Medium \| Low |
| **Status** | Open |

### What happened
Plain-language description of the unexpected behaviour. One or two sentences.

### Expected behaviour
What the acceptance criteria say should happen. Quote or reference the AC if possible.

### Reproduction steps
1. Step one
2. Step two
3. Step three
4. Observe the bug

### Test data used
Concrete values — the BA or dev should be able to repro on their machine.

### Environment
- Backend version / commit:
- Frontend version (if relevant):
- Data state at time of test:

### Suspected cause
Optional. If the QE has a hypothesis, write it here — but mark it clearly as a guess, not a diagnosis. Devs decide root cause.

### Acceptance criterion violated
Quote the specific AC from `project/features/<feature-id>.md` that this bug breaks.

### Triage notes (BA fills this)
Left blank by the QE.
