-- Create schema for application objects
CREATE SCHEMA IF NOT EXISTS chatbot;

-- Grant privileges to application user
GRANT ALL PRIVILEGES ON DATABASE chatbotdb TO chatbot_user;
GRANT ALL PRIVILEGES ON SCHEMA chatbot TO chatbot_user;

ALTER DEFAULT PRIVILEGES IN SCHEMA chatbot
GRANT ALL ON TABLES TO chatbot_user;

ALTER DEFAULT PRIVILEGES IN SCHEMA chatbot
GRANT ALL ON SEQUENCES TO chatbot_user;


-- Optional extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";


CREATE TABLE IF NOT EXISTS chatbot.chat_session (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS chatbot.chat_message (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id UUID REFERENCES chatbot.chat_session(id) ON DELETE CASCADE,
    role VARCHAR(20),
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );


-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_chat_session_user
    ON chatbot.chat_session(user_id);

CREATE INDEX IF NOT EXISTS idx_chat_message_session
    ON chatbot.chat_message(session_id);

CREATE INDEX IF NOT EXISTS idx_chat_message_created
    ON chatbot.chat_message(created_at);
