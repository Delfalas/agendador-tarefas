# 📅 Microserviço de Agendamento de Tarefas

Este microserviço é responsável pelo **gerenciamento e agendamento de tarefas dos usuários**, fazendo parte de uma arquitetura baseada em microsserviços.

Ele permite criar, consultar, atualizar e excluir tarefas, além de controlar o status de notificações (ex: envio de e-mail).

---

## 🧩 Papel na Arquitetura

Este serviço atua como o **orquestrador de tarefas do sistema**, sendo responsável por:

* Registrar tarefas associadas a um usuário
* Definir quando uma tarefa deve acontecer
* Controlar o status de notificação
* Servir de base para o microserviço de notificações

---

## 🚀 Funcionalidades

### 📌 Tarefas

* ➕ Criação de tarefas
* 📦 Criação em lote
* 🔍 Consulta por usuário
* 📅 Consulta por período (eventos)
* ✏️ Atualização de tarefas
* 🔄 Alteração de status
* ❌ Exclusão de tarefas

---

## 🔐 Segurança

* Integração com **JWT**
* O email do usuário é extraído automaticamente do token
* Nenhum dado sensível precisa ser enviado no body

```id="authheader2"
Authorization: Bearer <token>
```

---

## 📂 Estrutura

### 📌 Service (`TarefaService`)

Responsável pela lógica de negócio:

* Associação automática da tarefa ao usuário autenticado
* Controle de datas (`dataCriacao`, `dataAlteracao`)
* Definição do status inicial como `PENDENTE`
* Operações em lote para performance

---

### 📌 Mapper (`TarefaConverter`)

* Conversão entre `DTO` e `Entity`
* Implementado com **MapStruct**
* Suporte a listas

---

### 🌐 Controller (`TarefaController`)

Responsável pelos endpoints REST:

---

## 🔗 Endpoints

## 📌 Cadastro de Tarefas

### ➕ Criar tarefa

```id="t1"
POST /tarefas
```

---

### 📦 Criar tarefas em lote

```id="t2"
POST /tarefas/lote
```

---

## 🔍 Consultas

### 📋 Buscar tarefas do usuário logado

```id="t3"
GET /tarefas
```

---

### 📅 Buscar tarefas por período

```id="t4"
GET /tarefas/eventos?dataInicial=2026-03-18T10:00:00&dataFinal=2026-03-18T18:00:00
```

> Retorna apenas tarefas com status **PENDENTE**

---

## 🔄 Atualizações

### ✏️ Atualizar tarefa

```id="t5"
PUT /tarefas?id=123
```

---

### 🔄 Alterar status da tarefa

```id="t6"
PATCH /tarefas?status=CONCLUIDO&id=123
```

**Status possíveis:**

* `PENDENTE`
* `ENVIADO`
* `CONCLUIDO` *(dependendo do enum definido)*

---

## ❌ Exclusão

### 🗑️ Deletar tarefa

```id="t7"
DELETE /tarefas?id=123
```

---

## 🧠 Regras de Negócio

* 🔑 Toda tarefa pertence a um usuário (via token JWT)
* 🕒 `dataCriacao` é gerada automaticamente
* 🔄 `dataAlteracao` é atualizada em edições
* 📌 Status inicial sempre será `PENDENTE`
* 📅 Consultas por período retornam apenas tarefas pendentes
* 📦 Cadastro em lote otimiza múltiplas inserções

---

## ⚠️ Tratamento de Erros

* `ResourceNotFoundException` → Tarefa não encontrada
* Validação implícita em operações de update/delete

---

## 🛠️ Tecnologias Utilizadas

* Java 21
* Spring Boot
* Spring Data JPA
* MapStruct
* JWT (Spring Security)
* Lombok

---

## 🔄 Fluxo de Funcionamento

1. Usuário autenticado cria uma tarefa
2. Tarefa é registrada com status `PENDENTE`
3. Microserviço de notificações pode consumir essas tarefas
4. Status é atualizado após processamento (ex: envio de email)

---

## 🔗 Integração com Outros Microsserviços

* 👤 **Usuário Service**

  * Fornece autenticação (JWT)
  * Identifica o dono da tarefa

* 📧 **Notificação Service**

  * Consome tarefas pendentes
  * Atualiza status após envio

---


## 📌 Observações

* Este serviço é **stateless**
* Escalável horizontalmente
* Pode ser facilmente integrado a sistemas de mensageria

---

## 👨‍💻 Autor

Projeto desenvolvido com foco em:

* Arquitetura de microsserviços
* Processamento de tarefas agendadas
* Integração entre serviços
* Boas práticas com Spring Boot
