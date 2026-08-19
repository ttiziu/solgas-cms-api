-- Las fotos de productos viven en el CMS (R2); ya no usamos rutas estáticas en solgas-web-surquillo.
UPDATE public.store_products
SET fallback_image_url = ''
WHERE site_slug = 'surquillo'
  AND fallback_image_url LIKE '/images/productos/%';
