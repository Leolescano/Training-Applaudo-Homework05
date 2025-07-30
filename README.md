# SpringBoot-Cadastro

## 📝 Descrição do Projeto

Esta é uma aplicação RESTful desenvolvida com Spring Boot para gerenciar o cadastro de usuários. O projeto implementa funcionalidades essenciais de um sistema CRUD (Criar, Ler, Atualizar, Deletar) para a entidade `User`, utilizando um banco de dados em memória H2 para persistência dos dados.

O projeto foi desenvolvido seguindo os requisitos especificados no arquivo `ENUNCIADO.md`.

## ✨ Funcionalidades

-   **Criar Usuário**: Permite o cadastro de novos usuários com validações específicas.
-   **Atualizar Usuário**: Permite a atualização de informações de um usuário existente (Nome, Sobrenome, Telefone).
-   **Buscar Usuário**: Permite a busca de um usuário pelo seu e-mail.
-   **Listar Todos os Usuários**: Retorna uma lista de todos os usuários cadastrados.
-   **Deletar Usuário**: Remove um usuário do sistema pelo seu ID.

## 🛠️ Tecnologias Utilizadas

-   **Java 17**: Versão da linguagem Java.
-   **Spring Boot 3.1.4**: Framework principal para a construção da aplicação.
-   **Spring Web**: Para a criação de APIs RESTful.
-   **Spring Data JPA**: Para a persistência de dados e abstração do acesso ao banco de dados.
-   **Hibernate**: Implementação JPA utilizada pelo Spring Data.
-   **H2 Database**: Banco de dados relacional em memória para ambiente de desenvolvimento e teste.
-   **Maven**: Ferramenta de gerenciamento de dependências e build do projeto.
-   **Lombok**: Para reduzir código boilerplate (getters, setters, construtores).
-   **JUnit 5 & Mockito**: Para a escrita de testes unitários.
-   **JaCoCo**: Para análise e geração de relatórios de cobertura de testes.

## 📋 Pré-requisitos

Antes de começar, você precisará ter as seguintes ferramentas instaladas em seu ambiente:

-   JDK 17
-   Maven (ou pode usar o Maven Wrapper incluído no projeto)

## 🚀 Como Executar o Projeto

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/Leolescano/SpringBoot-Cadastro.git
    cd SpringBoot-Cadastro
    ```

2.  **Execute a aplicação usando o Maven Wrapper:**
    *   No Linux/macOS:
        ```bash
        ./mvnw spring-boot:run
        ```
    *   No Windows:
        ```bash
        ./mvnw.cmd spring-boot:run
        ```

A aplicação será iniciada na porta `8080`. Por padrão, o perfil `test` está ativo, utilizando o banco de dados em memória H2.

## 🗄️ Banco de Dados (H2)

A aplicação está configurada para usar um banco de dados H2 em memória. Você pode acessar o console do H2 para visualizar e gerenciar os dados.

-   **URL do Console H2**: http://localhost:8080/h2-console
-   **Configurações de Conexão**:
    -   **Driver Class**: `org.h2.Driver`
    -   **JDBC URL**: `jdbc:h2:mem:testdb`
    -   **User Name**: `sa`
    -   **Password**: (deixe em branco)

Ao acessar a URL, certifique-se de que o campo `JDBC URL` esteja preenchido com `jdbc:h2:mem:testdb` antes de conectar.

## 🧪 Executando os Testes

Para executar a suíte de testes unitários e verificar a cobertura de código, utilize o seguinte comando:

```bash
./mvnw test
```

O projeto está configurado com o plugin JaCoCo para garantir uma cobertura de testes de no mínimo 80% das linhas de código. O relatório de cobertura pode ser encontrado em `target/site/jacoco/index.html` após a execução dos testes.

## 📖 Endpoints da API

A seguir estão os endpoints disponíveis na API.

**Base URL**: `http://localhost:8080`

---

### 1. Criar Usuário

Cria um novo usuário no sistema.

-   **URL**: `/users/create`
-   **Método**: `POST`
-   **Headers**: `Content-Type: application/json`
-   **Corpo da Requisição (Request Body)**:

    ```json
    {
        "firstName": "Leonardo",
        "lastName": "Lescano",
        "email": "leo.lescano@example.com",
        "phone": "+503 1234 5678"
    }
    ```

-   **Validações**:
    -   `email`: Deve ser um e-mail válido e único no sistema.
    -   `firstName`: Obrigatório, com no máximo 50 caracteres.
    -   `lastName`: Obrigatório, com no máximo 50 caracteres.
    -   `phone`: Opcional. Se fornecido, deve seguir o padrão `+503 #### ####`.

-   **Resposta de Sucesso (200 OK)**:

    ```json
    {
        "id": 1,
        "firstName": "Leonardo",
        "lastName": "Lescano",
        "email": "leo.lescano@example.com",
        "phone": "+503 1234 5678",
        "createdAt": "2023-10-27T10:00:00.000000"
    }
    ```

-   **Respostas de Erro (400 Bad Request)**:
    -   Se o e-mail já existir: `"The user already exists with the given email account."`
    -   Se houver campos obrigatórios faltando: `"The following fields are required: Email, First Name, Last Name"`
    -   Se o formato do e-mail for inválido: `"The email address is not in a valid format."`
    -   Se o formato do telefone for inválido: `"The phone number does not have the correct pattern (+503 #### ####)."`

---

### 2. Atualizar Usuário

Atualiza os dados de um usuário existente. O e-mail não pode ser alterado.

-   **URL**: `/users/update/{userId}`
-   **Método**: `PUT`
-   **Parâmetro de URL**: `userId` (ID do usuário a ser atualizado)
-   **Headers**: `Content-Type: application/json`
-   **Corpo da Requisição (Request Body)**:

    ```json
    {
        "firstName": "Leo",
        "lastName": "Lescano Silva",
        "phone": "+503 9876 5432"
    }
    ```

-   **Resposta de Sucesso (200 OK)**: Retorna o objeto do usuário com os dados atualizados.

-   **Respostas de Erro (400 Bad Request)**:
    -   Se o usuário com o `userId` fornecido não for encontrado: `"User not found"`
    -   Se for feita uma tentativa de atualizar o e-mail: `"Cannot update a user's email"`

---

### 3. Buscar Usuário por E-mail

Retorna os dados de um usuário específico com base no seu e-mail.

-   **URL**: `/users/get/{email}`
-   **Método**: `GET`
-   **Parâmetro de URL**: `email` (E-mail do usuário a ser buscado)

-   **Resposta de Sucesso (200 OK)**:

    ```json
    {
        "firstName": "Leonardo",
        "lastName": "Lescano",
        "phone": "+503 1234 5678"
    }
    ```

-   **Resposta de Erro (400 Bad Request)**: Se nenhum usuário for encontrado com o e-mail fornecido, retorna a mensagem: `"We could not find a user with the given email."`

---

### 4. Listar Todos os Usuários

Retorna uma lista com todos os usuários cadastrados no sistema.

-   **URL**: `/users/all`
-   **Método**: `GET`

-   **Resposta de Sucesso (200 OK)**:

    ```json
    [
        {
            "id": 1,
            "firstName": "Leonardo",
            "lastName": "Lescano",
            "email": "leo.lescano@example.com",
            "phone": "+503 1234 5678",
            "createdAt": "2023-10-27T10:00:00.000000"
        }
    ]
    ```

-   **Resposta de Erro (400 Bad Request)**: Se a tabela de usuários estiver vazia, retorna a mensagem: `"The User table is empty."`

---

### 5. Deletar Usuário

Remove um usuário do sistema com base no seu ID.

-   **URL**: `/users/delete/{userId}`
-   **Método**: `DELETE`
-   **Parâmetro de URL**: `userId` (ID do usuário a ser deletado)

-   **Resposta de Sucesso (200 OK)**:
    ```
    "User id 1 successfully deleted."
    ```

-   **Resposta de Erro (400 Bad Request)**: Se o usuário com o `userId` fornecido não for encontrado: `"User not found"`

---

## 📂 Estrutura do Projeto

O projeto segue a estrutura padrão de uma aplicação Spring Boot, organizada da seguinte forma para manter a separação de responsabilidades:

```
.
├── src
│   ├── main
│   │   ├── java/com/applaudo/homework5 # Código fonte da aplicação
│   │   │   ├── config         # Configurações (ex: perfil de teste para o H2)
│   │   │   ├── controllers    # Controladores REST que expõem os endpoints da API
│   │   │   ├── dto            # Data Transfer Objects para formatar respostas da API
│   │   │   ├── entities       # Entidades JPA que mapeiam as tabelas do banco de dados
│   │   │   ├── repositories   # Repositórios Spring Data JPA para acesso aos dados
│   │   │   ├── services       # Camada de serviço com a lógica de negócio
│   │   │   └── utils          # Classes utilitárias (ex: validações)
│   │   └── resources      # Arquivos de configuração da aplicação
│   └── test               # Código de teste (unitário e de integração)
└── pom.xml                # Arquivo de configuração do Maven
```

## ✒️ Autor

-   **Leonardo Lescano** - Leolescano