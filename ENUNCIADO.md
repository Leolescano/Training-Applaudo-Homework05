Vamos criar uma aplicação Spring Boot. Para facilitar, usaremos o Spring Initializr para criar nossa aplicação Spring Boot. Para este projeto, utilizaremos Spring Web, Spring Data e H2 como banco de dados; tenha isso em mente ao gerar o projeto com o Spring Initializr.

O que precisamos alcançar? Bem, vamos desenvolver um processo de cadastro com as seguintes funcionalidades:

Criar um usuário:

Devemos ser capazes de criar novos usuários em nosso repositório de dados (H2).
As informações que precisam ser capturadas são:
Email: um email válido e único.
Nome: obrigatório e com comprimento máximo de 50 caracteres.
Sobrenome: obrigatório e com comprimento máximo de 50 caracteres.
Número de telefone: valor opcional que deve seguir o padrão +503 #### ####.
Se o usuário já existir, um erro deve ser retornado com a seguinte mensagem: "O usuário já existe com o email fornecido".
Se os dados não forem consistentes, um erro deve ser retornado indicando quais campos têm erros.
Atualizar um usuário:

Devemos ser capazes de atualizar as informações do usuário em nosso repositório.
As únicas informações que podem ser atualizadas são:
Nome
Sobrenome
Número de telefone
Obter uma conta de usuário:

Devemos ser capazes de localizar um usuário pelo email.
Precisamos retornar as seguintes informações quando o usuário for encontrado com o email fornecido:
Nome
Sobrenome
Número de telefone
Se o usuário não for encontrado, um erro deve ser reportado ao nosso consumidor indicando: "Não conseguimos encontrar um usuário com o email fornecido".
