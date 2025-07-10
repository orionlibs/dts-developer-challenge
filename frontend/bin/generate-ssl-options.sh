#!/usr/bin/env bash

localhost_ssl_folder="src/main/resources/localhost-ssl"
CONF_FILE="$localhost_ssl_folder/openssl-req.cnf"

if [ ! -f "$localhost_ssl_folder/localhost.key" -o ! -f "$localhost_ssl_folder/localhost.crt" ]; then
  mkdir -p "$localhost_ssl_folder"

  openssl req \
    -nodes \
    -x509 \
    -newkey rsa:4096 \
    -keyout "$localhost_ssl_folder/localhost.key" \
    -out    "$localhost_ssl_folder/localhost.crt" \
    -sha256 \
    -days 3650 \
    -config "$CONF_FILE" \
    -subj   "/C=GB/ST=A/L=B/O=C/OU=D/CN=E" \
    -extensions v3_req
fi
