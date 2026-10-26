# TCPRI Team Workspace

A TCPRI-branded internal team portal built with Java 21, Spring Boot, Thymeleaf, Spring Security, Spring Data JPA, H2, Flyway, and SMTP email. The starter is designed for a team of 30+ members and is intentionally scoped to TCPRI.

## Included

- Responsive TCPRI-branded dashboard and sign-in / registration pages
- Self-registration with pending status until an administrator approves the account
- Secure login using Spring Security and BCrypt password hashing
- ADMIN, MANAGER, and MEMBER roles
- Admin screen for approving/rejecting registration requests and changing roles
- Task assignment, due dates, and status updates
- Email notifications to the assigned member and configured administrator when a task is assigned
- Social content drafts and internal approval / change-request workflow
- H2 schema migrations through Flyway
- CSRF protection enabled by Spring Security defaults

## Important scope note

This is a functional starter project, not a guarantee of error-free production software. It has not been compiled or integration-tested in this environment because Maven is not installed here. Before using it with real TCPRI data, build and test it locally and complete the production hardening checklist below. Automatic publishing to Instagram, Facebook, LinkedIn, or other networks, media uploads, live analytics, email retries, and password reset are not implemented; these require additional integrations and infrastructure.

## Prerequisites

- JDK 21
- Maven 3.6.3 or later (Maven 3.9.x recommended)
- H2 (embedded; included with the application)

Verify installation:

```powershell
java -version
mvn -version
```

## 1. Configure the application

For local development, the application uses an embedded H2 database stored under `data/tcpri-workspace`. No database server or database credentials are required. Do not commit passwords or SMTP credentials to Git.

Required for the initial administrator:

- `BOOTSTRAP_ADMIN_EMAIL` — administrator email
- `BOOTSTRAP_ADMIN_PASSWORD` — a strong unique password (12+ characters recommended)

Database variables are optional for local H2 use. To connect another JDBC-compatible database, override `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`.
- `BOOTSTRAP_ADMIN_NAME=TCPRI Administrator` (optional)

The initial administrator is created only when the configured email does not already exist. If no bootstrap email/password is configured, no administrator is created automatically.

## 2. Configure email notifications (optional but recommended)

Task assignment emails are sent to the assigned team member and, if configured, the administrator. Set these environment variables using your approved SMTP provider:

- `MAIL_HOST` — SMTP hostname
- `MAIL_PORT=587` — typical STARTTLS port
- `MAIL_USERNAME` — SMTP username
- `MAIL_PASSWORD` — SMTP credential or provider-approved app password
- `MAIL_FROM` — verified sender email address
- `ADMIN_NOTIFICATION_EMAIL` — administrator email for assignment confirmations
- `APP_BASE_URL=http://localhost:8080` — use your deployed HTTPS URL after deployment

For Gmail, use a Google App Password if your account is eligible and App Passwords are enabled. For a workplace account, use TCPRI's approved mail provider. Never put credentials directly in Java source or commit them to a repository. Without `MAIL_FROM`, assignment emails are skipped and a warning is logged. SMTP errors are logged and do not roll back the task.

## 3. Start the application on Windows PowerShell

Open PowerShell in this project directory and set the values for your environment. Example:

```powershell
$env:BOOTSTRAP_ADMIN_EMAIL="admin@your-domain.example"
$env:BOOTSTRAP_ADMIN_PASSWORD="replace-with-a-strong-unique-password"
$env:BOOTSTRAP_ADMIN_NAME="TCPRI Administrator"
# Optional SMTP settings:
$env:MAIL_HOST="smtp.your-provider.example"
$env:MAIL_PORT="587"
$env:MAIL_USERNAME="your-smtp-username"
$env:MAIL_PASSWORD="your-smtp-password"
$env:MAIL_FROM="verified-sender@your-domain.example"
$env:ADMIN_NOTIFICATION_EMAIL="admin@your-domain.example"
$env:APP_BASE_URL="http://localhost:8080"
mvn spring-boot:run
```

Then open `http://localhost:8080` in a browser. Sign in using the bootstrap administrator credentials. Do not use the example `.example` email literally; substitute a real address you control.

Alternative package command:

```powershell
mvn clean package
java -jar target/tcpri-team-workspace-0.0.1-SNAPSHOT.jar
```

## 4. First-use workflow

1. The configured administrator signs in and opens **Administration**.
2. Team members request accounts at `/register`.
3. The administrator approves registrations and assigns appropriate roles.
4. An administrator or manager assigns a task to an approved member.
5. TCPRI sends task assignment email notifications if SMTP is configured.
6. Members update task status and create content drafts.
7. Members submit drafts for review; managers/admins approve or return them for changes.

## Production checklist before TCPRI rollout

- Run `mvn clean verify` on a supported Java/Maven installation and fix any local environment issues.
- Add automated unit, integration, and browser tests for registration, role checks, task access, and post approval.
- Add audit logs and safeguards against removing or demoting the final administrator.
- Add email verification, password reset, login throttling, and account lockout controls.
- Deploy behind HTTPS; store secrets in a secret manager; restrict database network access and configure backups/restore tests.
- Configure monitoring, error reporting, and durable email retries/outbox processing.
- Review privacy, retention, and access policies with TCPRI before entering real team data.
- For automatic publishing and analytics, register approved developer apps with each social network, implement OAuth/token refresh, encrypt tokens, handle permissions and rate limits, and test with approved accounts.

The starter is TCPRI-branded but does not itself establish official TCPRI ownership, identity, authorization, or approval to deploy. Confirm branding and deployment permissions with the organization before production use.
