-- Las fotos de productos viven en el CMS (R2); ya no usamos rutas estáticas en solgas-web.
UPDATE public.store_products
SET fallback_image_url = ''
WHERE site_slug = 'solgasenlima'
  AND fallback_image_url LIKE '/assets/images/%';
