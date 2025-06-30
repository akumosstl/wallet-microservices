-- Create the main database
CREATE DATABASE IF NOT EXISTS akumos_db;
USE akumos_db;

-- Create schemas
CREATE SCHEMA IF NOT EXISTS account;
CREATE SCHEMA IF NOT EXISTS transaction;
CREATE SCHEMA IF NOT EXISTS wallet;

-- Account Table
CREATE TABLE IF NOT EXISTS account.account (
    account_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    document VARCHAR(255),
    document_type VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255),
    account_no VARCHAR(255),
    wallet_id BIGINT
);

-- TransactionLogs Table
CREATE TABLE IF NOT EXISTS transaction.transaction_logs (
    log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    object VARCHAR(255),
    code INTEGER,
    date DATETIME,
    type VARCHAR(255)
);

-- Wallet Table
CREATE TABLE IF NOT EXISTS wallet.wallet (
    wallet_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    wallet_address VARCHAR(255),
    wallet_balance DOUBLE,
    date_created DATETIME,
    account_id BIGINT
);
