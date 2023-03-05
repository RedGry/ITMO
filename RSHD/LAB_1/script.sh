#!/bin/bash

psql -h pg -d studs -f ~/Download/RSHD_LAB1/script.sql 2>&1 | sed 's|.*NOTICE:  ||g'