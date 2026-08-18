CREATE TABLE IF NOT EXISTS public.sites (
    slug VARCHAR(64) PRIMARY KEY,
    name VARCHAR(120) NOT NULL
);

INSERT INTO public.sites (slug, name) VALUES
    ('solgasenlima', 'Solgas en Lima'),
    ('surquillo', 'Solgas Surquillo')
ON CONFLICT (slug) DO NOTHING;

ALTER TABLE public.image_assets
    ADD COLUMN IF NOT EXISTS site_slug VARCHAR(64) REFERENCES public.sites (slug);

CREATE INDEX IF NOT EXISTS idx_image_assets_site_slug ON public.image_assets (site_slug);
CREATE INDEX IF NOT EXISTS idx_image_assets_site_section ON public.image_assets (site_slug, section);
