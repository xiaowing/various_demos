#!/bin/bash

usage() {
    echo "$0 start|stop instance_dir"
}

if [ $# != 2 ]
then
    usage
    exit -1
fi

STARTLOG_DIR=pgstart_log
PG_DATADIR=$2

start() {
    START_LOG=pgstart_log/pg_`date "+%Y%m%d%H%M%S"`.log
    pg_ctl start -D ${PG_DATADIR} -l ${START_LOG}
    sleep 1s
    if [ -f ${PG_DATADIR}/postmaster.pid ]
    then
        echo "OK"
        ps -ef | grep "postgres" | grep -v "grep"
    else
        echo "${PG_DATADIR}/postmaster.pid not found"
        echo "Details:"
        echo "`cat ${START_LOG}`"
    fi
}

stop() {
    if [ -f ${PG_DATADIR}/postmaster.pid ]
    then
        echo "server $PG_DATADIR stopping..."
        pg_ctl stop -D ${PG_DATADIR} -m fast
    else
        echo "${PG_DATADIR}/postmaster.pid not found"
        ps -ef | grep "postgres" | grep -v "grep"
        echo "kill the postmaster process manually."
    fi
}

echo "pg_ctl is located in `which pg_ctl`"
mkdir -p ${STARTLOG_DIR}

case $1 in
    start)
        start
        ;;
    stop)
        stop
        ;;
    *)
        usage
        exit -1
        ;;
esac

exit 0

