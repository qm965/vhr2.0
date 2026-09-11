# Shared local infrastructure

This Compose project provides the local MySQL, Redis, and RabbitMQ services used by vhr2.0 and other projects.

## Start

```bash
cd dev-infra
cp .env.example .env
docker compose up -d
```

The default host endpoints are:

- MySQL: `localhost:33061`
- Redis: `localhost:6379`
- RabbitMQ: `localhost:5672`
- RabbitMQ management UI: `http://localhost:15672`

The Compose file intentionally keeps the existing `vhr2_*` volume names so the current local data is reused after the move. Do not run `docker compose down -v` unless deleting that data is intentional.

Each project should use its own MySQL database and credentials, Redis key prefix or ACL, and RabbitMQ vhost/user. The default values are for local development only.
