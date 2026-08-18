INSERT INTO public.users (username, password_hash, role, enabled)
SELECT 'jherry.visalot',
       '$2a$10$KZM09l0FI4NCACKBRyKL7ez3jeWBbAfz32EOsQmjhE0w.aVyv19C6',
       'ADMIN',
       TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM public.users WHERE username = 'jherry.visalot'
);
