/*
  # Create Sessions Storage Schema

  ## Description
  Creates tables for storing completed coaching sessions with full transcript,
  AI conversation history, and session metrics for the History screen.

  ## New Tables
  
  ### `sessions`
  - `id` (uuid, primary key) - Unique session identifier
  - `created_at` (timestamptz) - Session creation time
  - `started_at` (timestamptz) - When recording started
  - `ended_at` (timestamptz) - When recording stopped
  - `mode` (text) - Session mode: ONE_ON_ONE, TEAM_MEETING, DIFFICULT_CONVERSATION
  - `duration_seconds` (integer) - Total session duration
  - `user_id` (uuid, nullable) - For future auth integration
  
  ### `session_messages`
  - `id` (uuid, primary key) - Message identifier
  - `session_id` (uuid, foreign key) - References sessions table
  - `created_at` (timestamptz) - Message timestamp
  - `message_type` (text) - Type: TRANSCRIPT, USER_QUESTION, AI_RESPONSE, etc.
  - `content` (text) - Message content
  - `speaker` (text, nullable) - For transcript messages: USER, OTHER, SYSTEM
  - `metadata` (jsonb, nullable) - Additional data (emotion, priority, etc.)
  
  ### `session_metrics`
  - `session_id` (uuid, primary key, foreign key) - One-to-one with sessions
  - `talk_ratio_user` (integer) - User talk percentage
  - `question_count` (integer) - Total questions asked
  - `open_question_count` (integer) - Open-ended questions
  - `empathy_score` (integer) - Empathy rating 0-100
  - `listening_score` (integer) - Listening rating 0-100
  - `clarity_score` (integer) - Clarity rating 0-100
  - `interruption_count` (integer) - Number of interruptions
  - `sentiment` (text) - Overall sentiment: CALM, FOCUSED, STRESSED, ANXIOUS
  - `temperature` (integer) - Conversation temperature 0-100
  
  ## Security
  - Enable RLS on all tables
  - Allow read access to all users (for now, will add auth later)
  - Allow insert access to all users (for now, will add auth later)
*/

-- Create sessions table
CREATE TABLE IF NOT EXISTS sessions (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  created_at timestamptz DEFAULT now(),
  started_at timestamptz NOT NULL,
  ended_at timestamptz NOT NULL,
  mode text NOT NULL,
  duration_seconds integer NOT NULL DEFAULT 0,
  user_id uuid
);

-- Create session_messages table
CREATE TABLE IF NOT EXISTS session_messages (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  session_id uuid NOT NULL REFERENCES sessions(id) ON DELETE CASCADE,
  created_at timestamptz DEFAULT now(),
  message_type text NOT NULL,
  content text NOT NULL,
  speaker text,
  metadata jsonb
);

-- Create session_metrics table
CREATE TABLE IF NOT EXISTS session_metrics (
  session_id uuid PRIMARY KEY REFERENCES sessions(id) ON DELETE CASCADE,
  talk_ratio_user integer DEFAULT 0,
  question_count integer DEFAULT 0,
  open_question_count integer DEFAULT 0,
  empathy_score integer DEFAULT 0,
  listening_score integer DEFAULT 0,
  clarity_score integer DEFAULT 0,
  interruption_count integer DEFAULT 0,
  sentiment text DEFAULT 'CALM',
  temperature integer DEFAULT 30
);

-- Enable RLS
ALTER TABLE sessions ENABLE ROW LEVEL SECURITY;
ALTER TABLE session_messages ENABLE ROW LEVEL SECURITY;
ALTER TABLE session_metrics ENABLE ROW LEVEL SECURITY;

-- Create policies for sessions table
CREATE POLICY "Allow public read access to sessions"
  ON sessions FOR SELECT
  TO public
  USING (true);

CREATE POLICY "Allow public insert to sessions"
  ON sessions FOR INSERT
  TO public
  WITH CHECK (true);

-- Create policies for session_messages table
CREATE POLICY "Allow public read access to session_messages"
  ON session_messages FOR SELECT
  TO public
  USING (true);

CREATE POLICY "Allow public insert to session_messages"
  ON session_messages FOR INSERT
  TO public
  WITH CHECK (true);

-- Create policies for session_metrics table
CREATE POLICY "Allow public read access to session_metrics"
  ON session_metrics FOR SELECT
  TO public
  USING (true);

CREATE POLICY "Allow public insert to session_metrics"
  ON session_metrics FOR INSERT
  TO public
  WITH CHECK (true);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_sessions_created_at ON sessions(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_sessions_mode ON sessions(mode);
CREATE INDEX IF NOT EXISTS idx_session_messages_session_id ON session_messages(session_id);
CREATE INDEX IF NOT EXISTS idx_session_messages_type ON session_messages(message_type);