#!/usr/bin/env bash
cd /home/ec2-user/server/build/libs
sudo java -jar -Dserver.port=80 *.jar