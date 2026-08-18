CREATE TABLE IF NOT EXISTS public.image_assets (
    id BIGSERIAL PRIMARY KEY,
    key VARCHAR(255),
    url VARCHAR(2048),
    section VARCHAR(120),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
