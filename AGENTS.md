# Engineering workflow

## Starting work

- Start each task from the current `dev` branch.
- Create a descriptive task branch and a separate Git worktree before modifying files. Do not develop directly on `dev` or reuse an unrelated task worktree.
- Inspect the worktree status before editing. Preserve any pre-existing user changes and stop for guidance if they overlap the task.

## Test-driven development

- Use red–green–refactor for behavioral changes: first add a focused test that fails for the reported bug or requested behavior, then implement the smallest fix, then refactor only with tests green.
- Treat every bug fix as a regression-test requirement. Cover the happy path, boundary conditions, invalid or denied input, and the interaction points relevant to the changed behavior.
- Prefer integration tests when a feature crosses command, event, persistence, configuration, or localization boundaries; use unit tests for isolated logic.

## Verification and review

- Run the focused tests while iterating, then run the relevant module suite (or explain precisely why it cannot run).
- Use the repository's supported build-output redirection when a worktree's generated output is unavailable or unwritable.
- Before handoff, run formatting/static checks available to the project, inspect `git diff --check`, and review the final diff for correctness, scope, naming, error handling, and missing tests.
- Report the branch, commit(s), tests run, and any verification limitation. Do not claim exhaustive coverage when meaningful edge cases remain untested.
