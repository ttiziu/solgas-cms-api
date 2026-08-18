ALTER TABLE public.sites
    ADD COLUMN IF NOT EXISTS public_url VARCHAR(255);

UPDATE public.sites
SET public_url = 'https://solgasenlima.pe'
WHERE slug = 'solgasenlima';

UPDATE public.sites
SET public_url = 'https://solgasenviogratis.com'
WHERE slug = 'surquillo';
