# solgas-cms-api

API Spring Boot del CMS Solgas (productos, imágenes R2, revalidación de las webs Next.js).

## Desarrollo local

1. Copia credenciales locales (no se suben a git):

   ```bash
   cp src/main/resources/application.example.properties src/main/resources/application-local.properties
   ```

2. Activa el perfil local al arrancar:

   ```bash
   SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run
   ```

   O exporta `SPRING_PROFILES_ACTIVE=local` en tu IDE.

3. El archivo `application-local.properties` está en `.gitignore`.

## Deploy en Railway

Railway detecta el `Dockerfile` automáticamente.

### Variables de entorno (Railway → Service → Variables)

| Variable | Descripción |
|----------|-------------|
| `SPRING_DATASOURCE_URL` | JDBC Supabase, ej. `jdbc:postgresql://...pooler.supabase.com:5432/postgres` |
| `SPRING_DATASOURCE_USERNAME` | Usuario Postgres |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña Postgres |
| `R2_ACCESS_KEY` | Cloudflare R2 access key |
| `R2_SECRET_KEY` | Cloudflare R2 secret |
| `R2_ENDPOINT` | `https://<account>.r2.cloudflarestorage.com` |
| `R2_BUCKET` | `solgas-cms-images` |
| `R2_PUBLIC_URL` | URL pública del bucket R2 |
| `APP_JWT_SECRET` | Secret largo para firmar JWT del CMS |
| `APP_CORS_ALLOWED_ORIGINS` | Origen del panel CMS, ej. `https://cms.tudominio.com` |
| `REVALIDATE_SECRET` | Mismo valor que en Vercel (`solgas-web` y `solgas-web-surquillo`) |
| `REVALIDATE_WEBHOOK_SOLGASENLIMA` | `https://solgasenlima.pe/api/revalidate` |
| `REVALIDATE_WEBHOOK_SURQUILLO` | `https://solgasenviogratis.com/api/revalidate` |

`PORT` lo asigna Railway; no hace falta configurarlo.

### Conectar GitHub

1. Push este repo a `github.com/ttiziu/solgas-cms-api`.
2. En Railway: **New Project → Deploy from GitHub repo → solgas-cms-api**.
3. Añade las variables de entorno.
4. Genera dominio público (Settings → Networking → Generate Domain).
5. Esa URL es el `CMS_API_URL` en Vercel y `API_URL` en el CMS.

### CORS

Incluye en `APP_CORS_ALLOWED_ORIGINS` la URL del panel CMS (y `http://localhost:3000` solo en local).
