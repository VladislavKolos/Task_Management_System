CREATE TABLE "user"
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email      VARCHAR(256) NOT NULL UNIQUE,
    password   VARCHAR(256) NOT NULL,
    role       VARCHAR(20)  NOT NULL,
    created_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "task"
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    status      VARCHAR(20)  NOT NULL CHECK (status IN ('pending', 'in_progress', 'completed')),
    priority    VARCHAR(20)  NOT NULL CHECK (priority IN ('high', 'medium', 'low')),
    author_id   UUID         NOT NULL REFERENCES "user" (id) ON DELETE CASCADE,
    assignee_id UUID         REFERENCES "user" (id) ON DELETE SET NULL,
    created_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "comment"
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    content    TEXT NOT NULL,
    task_id    UUID NOT NULL REFERENCES task (id) ON DELETE CASCADE,
    author_id  UUID NOT NULL REFERENCES "user" (id) ON DELETE CASCADE,
    created_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);
