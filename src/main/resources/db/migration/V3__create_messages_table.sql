-- V3__create_messages_table.sql
-- Create messages table

CREATE TABLE messages (
                          id BIGSERIAL PRIMARY KEY,
                          conversation_id BIGINT NOT NULL,
                          role VARCHAR(20) NOT NULL,
                          content TEXT NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_message_conversation
                              FOREIGN KEY (conversation_id)
                                  REFERENCES conversations(id)
                                  ON DELETE CASCADE
);

-- Create index on conversation_id for faster queries
CREATE INDEX idx_messages_conversation_id ON messages(conversation_id);