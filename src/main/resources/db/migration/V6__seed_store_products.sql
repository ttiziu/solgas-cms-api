INSERT INTO public.store_products (site_slug, product_key, name, description, whatsapp_message, fallback_image_url, sort_order) VALUES
    ('solgasenlima', 'balon-10kg', 'Balón Solgas 10kg', 'Ideal para hogares pequeños y medianos. Fácil de transportar y seguro.', 'Hola, me interesa el Balón Solgas 10kg. ¿Podrían darme más información?', '/assets/images/balon10kg..webp', 1),
    ('solgasenlima', 'balon-10kg-contenido', 'Balón Solgas 10kg + Contenido', 'Balón de 10kg con contenido adicional. Perfecto para mayor duración.', 'Hola, me interesa el Balón Solgas 10kg + Contenido. ¿Podrían darme más información?', '/assets/images/balonmascontenido.webp', 2),
    ('solgasenlima', 'balon-45kg', 'Balón Solgas 45kg', 'Perfecto para negocios, restaurantes o familias grandes. Máxima duración.', 'Hola, me interesa el Balón Solgas 45kg. ¿Podrían darme más información?', '/assets/images/balonde45kg.webp', 3),
    ('solgasenlima', 'balon-45kg-contenido', 'Balón Solgas 45kg + Contenido', 'Balón de 45kg con contenido adicional. Ideal para uso intensivo.', 'Hola, me interesa el Balón Solgas 45kg + Contenido. ¿Podrían darme más información?', '/assets/images/balonde45mascontenido.webp', 4),
    ('solgasenlima', 'kit-regulador-premium', 'Kit Regulador Premium', 'Incluye regulador y manguera de alta seguridad para tu instalación.', 'Hola, me interesa el Kit Regulador Premium. ¿Podrían darme más información?', '/assets/images/kitreguladorpremium.webp', 5),
    ('solgasenlima', 'kit-completo', 'Kit Completo', 'Balón de 10kg + contenido + kit de válvula. Todo lo necesario para tu hogar.', 'Hola, me interesa el Kit Completo (Balón 10kg + contenido + kit de válvula). ¿Podrían darme más información?', '/assets/images/kitcompleto.webp', 6)
ON CONFLICT (site_slug, product_key) DO NOTHING;

INSERT INTO public.store_products (site_slug, product_key, name, description, whatsapp_message, fallback_image_url, sort_order) VALUES
    ('surquillo', 'balon-solgas-10kg', 'Balón Solgas 10 kg', 'Ideal para familias y cocinas de uso diario.', NULL, '/images/productos/balon-solgas-10kg.webp', 1),
    ('surquillo', 'balon-solgas-45kg', 'Balón Solgas 45 kg', 'Para negocios, restaurantes y bodegas.', NULL, '/images/productos/balon-solgas-45kg.webp', 2),
    ('surquillo', 'balon-masgas-10kg', 'Balón + Más Gas 10 kg', 'Balón de 10 kg con más contenido de gas.', NULL, '/images/productos/balon-masgas-10kg.webp', 3),
    ('surquillo', 'kit-valvula-premium', 'Kit Válvula Premium', 'Regulador y manguera de seguridad certificada.', NULL, '/images/productos/kitvalvulva-premium.webp', 4),
    ('surquillo', 'balon-kit-valvula-10kg', 'Balón + Kit Válvula 10 kg', 'Pack completo: balón 10 kg con kit de válvula incluido.', NULL, '/images/productos/balon-maskitvalulva-10kg..webp', 5)
ON CONFLICT (site_slug, product_key) DO NOTHING;
