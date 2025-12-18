# API Food Payments

**Funcionalidades principais**:
# API Food Payments

Este repositório contém o serviço de pagamentos do projeto POS Tech G57 — uma API Spring Boot responsável por criação e gerenciamento de pagamentos (QRCode Pix), além de receber notificações via webhook.

## Recursos criados / presentes neste projeto

- Serviço REST com endpoints de pagamento e webhook.
- Integração com serviço de pedidos via `OrdersClient`.
- Suporte a documentação OpenAPI/Swagger (`springdoc`).
- Configurações para containerização com `Docker` e orquestração básica com manifests em `k8s/prd/`.

## 📂 Estrutura do Repositório

```
.
├── Dockerfile
├── docker-compose.yml
├── HELP.md
├── mvnw
├── mvnw.cmd
├── pom.xml
├── k8s/
│   └── prd/
│       ├── configmap.yaml
│       ├── deployment.yaml
│       ├── hpa.yaml
	├── ingress.yaml
	├── k8s-service.yaml
	└── namespace.yaml
├── src/
│   ├── main/
│   │   ├── java/br/com/five/seven/food    # código fonte
│   │   └── resources/application.yml
└── README.md
```

## 📋 Pré-requisitos

- `Java 21`
- `Maven` (ou use o wrapper `./mvnw` / `mvnw.cmd`)
- `Docker` (para build/execução em contêiner)
- `kubectl` (para aplicar os manifests Kubernetes, opcional)

## 🛠️ Como executar localmente

### 1) Clonar o repositório

```powershell
git clone https://github.com/rachelkozlowsky/pos-tech-g57-app-payments.git
cd pos-tech-g57-app-payments
```

### 2) Build com Maven

```powershell
./mvnw clean package -DskipTests
```

### 3) Executar o jar

```powershell
java -jar target/api-payments.jar
```

### 4) Executar com Docker

```powershell
docker build -t api-food-payments:local .
docker run -p 8080:8080 --env-file ./.env --name api-payments api-food-payments:local
```

### 5) Executar com `docker-compose` (inclui MongoDB para dev)

```powershell
docker-compose up --build
```

Para parar/limpar:

```powershell
docker-compose down -v
```

## 🔧 Variáveis de ambiente importantes

As principais variáveis lidas em `src/main/resources/application.yml`:

- `SERVER_PORT` (default `8080`)
- `SPRING_DATA_MONGODB_URI` (string de conexão com MongoDB)
- `PAYMENT_INTEGRATION_HOST` (ex: `https://api.mercadopago.com`)
- `ORDERS_CLIENT_HOST` (host do serviço de pedidos usado por `OrdersClient`)
- `JWT_SECRET` (segredo JWT)
- `JWT_TOKEN_PIX_APPLICATION_PAYMENT` (token para integração com Mercadopago)

Recomenda-se criar um arquivo `.env` local com essas variáveis para desenvolvimento.

## 🚀 Endpoints principais

- `GET /health` — health check
- `GET /v1/payments/{payment_id}` — buscar pagamento por id (role `ADMIN`)
- `POST /v1/payments` — criar pagamento (QRCode Pix). Body: `PaymentRequest` (veja classes em `src/main/java/.../rest/request`).
- `GET /v1/payments` — listar pagamentos (paginação) (role `ADMIN`)
- `GET /v1/payments/options` — listar opções de pagamento
- `DELETE /v1/payments/{payment_order_id}` — deletar pagamento (role `ADMIN`)
- `POST /v1/webhook/payment` — webhook de notificação (body: `NotificationRequest`)

API docs: `/swagger-ui.html`

## ☸️ Deploy em Kubernetes (produção)

Manifests prontos em `k8s/prd/`.

Exemplo de aplicação dos manifests:

```powershell
kubectl apply -f k8s/prd/namespace.yaml
kubectl apply -f k8s/prd/configmap.yaml
kubectl apply -f k8s/prd/deployment.yaml
kubectl apply -f k8s/prd/k8s-service.yaml
kubectl apply -f k8s/prd/ingress.yaml
```

Observações:

- A `ConfigMap` definida em `k8s/prd/configmap.yaml` inclui `PAYMENT_INTEGRATION_HOST`.
- A imagem usada no `deployment.yaml` é `postechf57/pos-tech-g57-app-payments:0.0.1` — atualize para a tag correta antes do deploy.
- Liveness/readiness probes apontam para `/health`.

## ✅ Testes

```powershell
./mvnw test
```

## 🔁 Fluxo de CI/CD

Este repositório inclui `mvnw` para builds reprodutíveis. Se desejar, podemos adicionar workflows do GitHub Actions para build, testes e publicação de imagem Docker.

## 🧹 Remoção de recursos locais

- Parar containers: `docker-compose down -v`
- Remover imagem local (opcional): `docker rmi api-food-payments:local`

## 🔒 Segurança

- Nunca commit credenciais ou segredos.
- Use `Secret` no Kubernetes e variáveis de ambiente no CI para dados sensíveis.

## 🤝 Contribuição

1. Fork
2. `git checkout -b feature/MinhaFeature`
3. Commit e push
4. Abra um Pull Request

## 📄 Licença

Licença: verifique o arquivo `LICENSE` (se presente) no repositório.

---


