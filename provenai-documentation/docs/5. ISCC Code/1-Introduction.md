# Introduction
[ISCC](https://iscc.codes/) is a universal identifier of all types of digital content. The ISCC can be generated for content but also for parts of chunks of it. For example a section of a document. The relation between the parent and child elements can be preserved in the ISCC identifier. Thus we can identify the work any chucks are taken from. The ISCC is a content code, that is created from the content file itself. Processing the content with the algorithms defined by ISCC specification creates a unique composite code, consisting of four major elements The ISCC can be created offline on any local device or app, that supports the suggested standard.  The ISCC is short enough to be written on any blockchain while preserving its unique features.

The minimal viable, first iteration ISCC will be a byte structure built from the following components:
- Meta-Code: The meta-code is a similarity preserving hash from generic metadata like title and creators.
- Content-Code:  This code is a similarity preserving hash from the extracted content. It is independent of the file format and encoding.
- Data-Code: This similarity preserving hash is produced by chunks of the raw data. It groups  It groups complete encoded files with similar content and encoding.
- Instance-Code: The top hash of a Merkle tree generated from chunks of raw data of an encoded media blob. It identifies a concrete manifestation and proves the integrity of the full content.


## Setup ISCC Code Web Service

### Pre-requisites
Install Docker on your machine.

### Steps

#### 1. Cloning the ISCC-web Indentity Repository

```
git clone https://github.com/iscc/iscc-web.git && cd iscc-web
```

#### 2. Deployment
Create the following files:

##### Caddyfile
```
{
  email {$ISCC_WEB_SITE_EMAIL}
}

{$ISCC_WEB_SITE_ADDRESS} {
  reverse_proxy app:8000
}
```

##### .env

```
ISCC_WEB_ENVIRONMENT=production
ISCC_WEB_SITE_EMAIL=admin@example.com
ISCC_WEB_SITE_ADDRESS=https://example.com
ISCC_WEB_PRIVATE_FILES=true
ISCC_WEB_MAX_UPLOAD_SIZE=1073741824
ISCC_WEB_STORAGE_EXPIRY=3600
ISCC_WEB_CLEANUP_INTERVAL=600
ISCC_WEB_LOG_LEVEL=INFO
ISCC_WEB_IO_READ_SIZE=2097152
FORWARDED_ALLOW_IPS=*
```

##### docker-compose.yaml

```
version: "3.8"

volumes:
  caddy-config:
  caddy-data:

services:
  app:
    image: ghcr.io/iscc/iscc-web:main
    init: true
    env_file: .env
  caddy:
    image: caddy:2.6.1-alpine
    restart: unless-stopped
    env_file: .env
    volumes:
      - ./Caddyfile:/etc/caddy/Caddyfile
      - caddy-config:/config
      - caddy-data:/data
    ports:
      - "80:80"
      - "443:443"
      - "443:443/udp"
    depends_on:
      - app
```
Make sure you have a DNS entry pointing to your servers IP and set the correct ISCC_WEB_SITE_ADDRESS in your .env file. You should also change ISCC_WEB_SITE_EMAIL.


#### 3. Setup Services
With the iscc-web package we can now run all APIs with docker:

```
cd docker-compose && docker-compose up
```
##### Port Mapping
- ISCC Web Services: http://localhost:8000/api/v1/

