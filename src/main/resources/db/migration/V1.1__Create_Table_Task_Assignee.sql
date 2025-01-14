CREATE TABLE task_assignee
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    task_id     UUID NOT NULL REFERENCES task (id) ON DELETE CASCADE,
    user_id     UUID NOT NULL REFERENCES "user" (id) ON DELETE CASCADE,
    assigned_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);