-- User table
CREATE TABLE IF NOT EXISTS app."user" (
                                      id TEXT PRIMARY KEY,
                                      name TEXT NOT NULL,
                                      email TEXT NOT NULL UNIQUE,
                                      email_verified BOOLEAN NOT NULL DEFAULT FALSE,
                                      image TEXT,
                                      created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
    );

-- Session table
CREATE TABLE IF NOT EXISTS app.session (
                                       id TEXT PRIMARY KEY,
                                       "expiresAt" TIMESTAMP NOT NULL,
                                       token TEXT NOT NULL UNIQUE,
                                       "createdAt" TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP,
    "ipAddress" TEXT,
    "userAgent" TEXT,
    "userId" TEXT REFERENCES "user"(id) ON DELETE CASCADE
    );

-- Account table
CREATE TABLE IF NOT EXISTS app.account (
                                       id TEXT PRIMARY KEY,
                                       "accountId" TEXT NOT NULL,
                                       "providerId" TEXT NOT NULL,
                                       "userId" TEXT REFERENCES "user"(id) ON DELETE CASCADE,
    "accessToken" TEXT,
    "refreshToken" TEXT,
    "idToken" TEXT,
    "accessTokenExpiresAt" TIMESTAMP,
    "refreshTokenExpiresAt" TIMESTAMP,
    scope TEXT,
    password TEXT,
    "createdAt" TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP
    );

-- Verification table
CREATE TABLE IF NOT EXISTS app.verification (
                                            id TEXT PRIMARY KEY,
                                            identifier TEXT NOT NULL,
                                            value TEXT NOT NULL,
                                            "expiresAt" TIMESTAMP NOT NULL,
                                            "createdAt" TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
    );

-- Tag table
CREATE TABLE IF NOT EXISTS app.tag (
                                   id BIGSERIAL PRIMARY KEY,
                                   name VARCHAR(255),
    "userId" TEXT REFERENCES "user"(id),
    UNIQUE (name, "userId")
    );

-- Bookmark table
CREATE TABLE IF NOT EXISTS app.bookmark (
                                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    date_added TIMESTAMP,
    description TEXT,
    favicon VARCHAR(255),
    is_archived BOOLEAN NOT NULL DEFAULT FALSE,
    is_pinned BOOLEAN NOT NULL DEFAULT FALSE,
    last_visited TIMESTAMP,
    title VARCHAR(255),
    url VARCHAR(255),
    view_count INTEGER NOT NULL DEFAULT 0,
    "userId" TEXT REFERENCES "user"(id)
    );

-- Bookmark <-> Tag join table
CREATE TABLE IF NOT EXISTS app.bookmark_tags (
                                             "bookmark_id" UUID REFERENCES bookmark(id),
    "tag_id" BIGINT REFERENCES tag(id),
    PRIMARY KEY ("bookmark_id", "tag_id")
    );

-- Passkey table
CREATE TABLE IF NOT EXISTS app.passkey (
                                       id TEXT PRIMARY KEY,
                                       name TEXT,
                                       public_key TEXT NOT NULL,
                                       "userId" TEXT REFERENCES "user"(id) ON DELETE CASCADE,
    "credentialID" TEXT,
    counter INTEGER NOT NULL,
    "deviceType" TEXT,
    "backedUp" BOOLEAN NOT NULL,
    transports TEXT,
    created_at TIMESTAMP,
    aaguid TEXT
    );
