-- V2__create_conversations_table.sql
-- Create conversations table

CREATE TABLE conversations (
                               id BIGSERIAL PRIMARY KEY,
                               user_id BIGINT NOT NULL,
                               title VARCHAR(255),
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT fk_conversation_user
                                   FOREIGN KEY (user_id)
                                       REFERENCES users(id)
                                       ON DELETE CASCADE
);

-- Create index on user_id for faster queries
CREATE INDEX idx_conversations_user_id ON conversations(user_id);