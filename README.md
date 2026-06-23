# MedAgenda 🩺

**MedAgenda** é um sistema corporativo B2B (SaaS) projetado para orquestrar o fluxo completo de uma clínica médica. Construído com uma arquitetura de microsserviços, o projeto resolve desafios reais do domínio de saúde, combinando regras rígidas de faturamento com a flexibilidade necessária para prontuários clínicos.

O ecossistema garante forte isolamento de responsabilidades através de **RBAC (Role-Based Access Control)**, processa fluxos operacionais em um banco relacional robusto e utiliza o poder do **JSONB no PostgreSQL** para garantir flexibilidade em formulários de anamnese. Para garantir alta disponibilidade e não bloquear a interface do usuário, a geração de documentos médicos (Receituários e Atestados em PDF) é processada de forma 100% assíncrona utilizando **RabbitMQ**.

**Principais Tecnologias:** Java 21, Spring Boot 4, PostgreSQL, RabbitMQ, JWT Security, API Gateway e Docker.