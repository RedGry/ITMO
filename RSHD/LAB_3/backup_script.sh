#!/bin/bash

CURRENT_DATE=$(date "+%Y-$m-%d-%H:%M:%S")
BACKUP_DIR_NAME="backup_${CURRENT_DATE}"

#Остановка БД
pg_ctl stop -D $HOME/u08/dnk13

#Создание копии
/opt/csw/bin/rsync -avv $HOME/u08 $HOME/u03 postgres1@pg122:~/backups/$BACKUP_DIR_NAME --rsync-path="/opt/csw/bin/rsync"

#Запуск БД
postgres -D $HOME/u08/dnk13 >~/logfile 2>&1 &

#Запуск скрипта на узле копирования
ssh postgres1@pg122 "bash /var/postgres/postgres1/remove_script.sh"

echo "$(date): Backup $BACKUP_DIR_NAME was successfully created in directory ~/backups"

