CREATE TABLE IF NOT EXISTS public.store_products (
    id BIGSERIAL PRIMARY KEY,
    site_slug VARCHAR(64) NOT NULL REFERENCES public.sites (slug) ON DELETE CASCADE,
    product_key VARCHAR(120) NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    whatsapp_message TEXT,
    fallback_image_url VARCHAR(512) NOT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_store_products_site_key UNIQUE (site_slug, product_key)
);

CREATE INDEX IF NOT EXISTS idx_store_products_site_sort ON public.store_products (site_slug, sort_order);
