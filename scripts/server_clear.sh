#!/usr/bin/env bash

# Limpar o conteúdo do diretório /home/ec2-user/server, exceto o diretório "scripts"
find /home/ec2-user/server -mindepth 1 -maxdepth 1 ! -name 'scripts' -exec rm -rf {} +