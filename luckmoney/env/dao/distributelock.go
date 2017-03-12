package dao

import (
	"database/sql"
	"fmt"

	// To use the universal interface of database.
	"log"

	_ "github.com/bmizerany/pq"
	util "github.com/xiaowing/various_demos/luckmoney/util"
)

const dbengine = "postgres"

var host = "127.0.0.1"
var port = 26550
var username = "wing"
var password = "xxxxyyyy"

type DistributeLock struct {
	Key     int64
	session *sql.DB
}

func TryLock(id util.ObjectId) (*DistributeLock, error) {
	connstring := fmt.Sprintf("host=%s port=%d dbname=template1 user=%s password=%s sslmode=disable",
		host, port, username, password)

	db, err := sql.Open(dbengine, connstring)
	if err != nil {
		log.Println(err)
		return nil, err
	}

	stmt := "SELECT pg_try_advisory_lock($1);"
	key := id.GetLockKey()

	row := db.QueryRow(stmt, key)
	var ret bool
	err = row.Scan(&ret)
	if err != nil {
		log.Println(err)
		db.Close()
		return nil, err
	}
	if !ret {
		db.Close()
		return nil, nil
	}

	lock := new(DistributeLock)
	lock.Key = key
	lock.session = db

	return lock, nil
}

func (self *DistributeLock) IsStillValid() bool {
	stmt := "SELECT pg_try_advisory_lock($1);"
	row := self.session.QueryRow(stmt, self.Key)
	var ret bool
	err := row.Scan(&ret)
	if err != nil {
		log.Println(err)
		self.session.Close()
		return false
	}

	if !ret {
		self.session.Close()
		return false
	}

	return true
}

func (self *DistributeLock) Release() {
	self.session.Close()
}
