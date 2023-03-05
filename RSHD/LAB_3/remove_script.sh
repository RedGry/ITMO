#!/bin/bash
BACKUP_CNT=`ls -l /var/postgres/postgres1/backups | grep ^d | grep -c backup`
BACKUP_OLDEST=`ls -l /var/postgres/postgres1/backups | grep ^d | grep backup | awk '{print $9}' | sort | head -1`
MAX_BACKUP_CNT=14

if (($BACKUP_CNT > $MAX_BACKUP_CNT)); then
	echo "$(date) : Backup count is $BACKUP_CNT. Remove the oldest one $BACKUP_OLDEST" >> back_log.log
	rm -rf /var/postgres/postgres1/backups/$BACKUP_OLDEST
else
	echo "$(date) : Backup count is $BACKUP_CNT" >> back_log.log
fi

