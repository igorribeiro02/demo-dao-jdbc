# Demo DAO JDBC Project

Um sistema de gerenciamento de vendedores (Sellers) e departamentos construído em **Java puro** utilizando **JDBC** (Java Database Connectivity) e o padrão de projeto **DAO (Data Access Object)**.

O objetivo deste projeto é demonstrar o domínio sobre a manipulação de dados relacionais sem o uso de frameworks ORM (como Hibernate), garantindo o entendimento profundo de como as transações, conexões e mapeamentos objeto-relacional funcionam na prática.

## 🚀 Tecnologias Utilizadas

* **Java 21**
* **JDBC (Java Database Connectivity)**
* **MySQL** (Banco de dados)
* **Padrão DAO** (Data Access Object)
* **Eclipse IDE** (Estrutura do projeto)

## ⚙️ Arquitetura e Padrões de Projeto

O projeto segue uma arquitetura em camadas para garantir a separação de responsabilidades:

* **Model Entities:** Classes POJO (`Seller`, `Department`) que representam as tabelas do banco.
* **Model DAO:** Interfaces que definem os contratos de acesso a dados (`SellerDao`, `DepartmentDao`).
* **DAO Implementation:** Implementação concreta usando JDBC (`SellerDaoJDBC`).
* **DB:** Classe utilitária para gerenciamento de conexões e tratamento de exceções personalizadas (`DbException`, `DbIntegrityException`).

## 📋 Funcionalidades

O sistema permite realizar operações de **CRUD** (Create, Read, Update, Delete) completas:

* Inserir novo vendedor (`insert`).
* Atualizar dados de um vendedor (`update`).
* Deletar um vendedor por ID (`deleteById`).
* Buscar vendedor por ID (`findById`).
* Listar todos os vendedores (`findAll`).
* Listar vendedores por departamento (`findByDepartment`).

## 🔧 Como Executar

### Pré-requisitos
* Java JDK 21 instalado.
* MySQL Server rodando.

### Configuração do Banco de Dados
Execute o script SQL abaixo no seu MySQL para criar o banco e as tabelas esperadas pelo projeto:

```sql
CREATE DATABASE coursejdbc;
USE coursejdbc;

CREATE TABLE department (
  Id INT(11) NOT NULL AUTO_INCREMENT,
  Name VARCHAR(60) DEFAULT NULL,
  PRIMARY KEY (Id)
);

CREATE TABLE seller (
  Id INT(11) NOT NULL AUTO_INCREMENT,
  Name VARCHAR(60) NOT NULL,
  Email VARCHAR(100) NOT NULL,
  BirthDate DATETIME NOT NULL,
  BaseSalary DOUBLE NOT NULL,
  DepartmentId INT(11) NOT NULL,
  PRIMARY KEY (Id),
  FOREIGN KEY (DepartmentId) REFERENCES department (Id)
);

INSERT INTO department (Name) VALUES ('Computers'), ('Electronics');
