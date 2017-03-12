package dao

import (
	"fmt"
	"log"

	"errors"

	redis "github.com/garyburd/redigo/redis"
)

var redisHost = "127.0.0.1"
var redisPort = 6379
var redisPassword = "xxxxyyyy"

var hsetMoney = "money"
var hsetPartition = "partition"

var ErrNoEnvelope = errors.New("Specified envelope does not exist.")

func InitEnvelopeHset(key string, money float64, divides int, expsec int) error {
	optionPwd := redis.DialPassword(redisPassword)
	optionDb := redis.DialDatabase(0)

	c, err := redis.Dial("tcp", fmt.Sprintf("%s:%d", redisHost, redisPort), optionPwd, optionDb)
	if err != nil {
		log.Println(err)
		return err
	}
	defer c.Close()

	m := map[string]interface{}{
		hsetMoney:     money,
		hsetPartition: float64(divides),
	}

	_, rderr := c.Do("multi")
	if rderr != nil {
		log.Println(rderr)
		return rderr
	}

	_, rderr = c.Do("hmset", redis.Args{}.Add(key).AddFlat(m)...)
	if rderr != nil {
		log.Println(rderr)
		return rderr
	}

	_, rderr = c.Do("expire", key, expsec)
	if rderr != nil {
		log.Println(rderr)
		return rderr
	}

	_, rderr = c.Do("exec")
	if rderr != nil {
		log.Println(rderr)
		return rderr
	}

	return nil
}

func DecrMoneyFromEnvelopeHset(key string, decr float64) error {
	optionPwd := redis.DialPassword(redisPassword)
	optionDb := redis.DialDatabase(0)

	c, err := redis.Dial("tcp", fmt.Sprintf("%s:%d", redisHost, redisPort), optionPwd, optionDb)
	if err != nil {
		log.Println(err)
		return err
	}
	defer c.Close()

	ret, rderr := c.Do("exists", key)
	if rderr != nil {
		log.Println(rderr)
		return rderr
	}

	retval, _ := ret.(int64)
	if retval == 0 {
		return ErrNoEnvelope
	}

	_, rderr = c.Do("multi")
	if rderr != nil {
		log.Println(rderr)
		return rderr
	}

	_, rderr = c.Do("hincrbyfloat", key, hsetMoney, decr)
	if rderr != nil {
		log.Println(rderr)
		return rderr
	}

	_, rderr = c.Do("hincrby", key, hsetPartition, -1)
	if rderr != nil {
		log.Println(rderr)
		return rderr
	}

	_, rderr = c.Do("exec")
	if rderr != nil {
		log.Println(rderr)
		return rderr
	}

	return nil
}

func GetEnvelopMembers(key string) (float64, int, error) {
	optionPwd := redis.DialPassword(redisPassword)
	optionDb := redis.DialDatabase(0)

	c, err := redis.Dial("tcp", fmt.Sprintf("%s:%d", redisHost, redisPort), optionPwd, optionDb)
	if err != nil {
		log.Println(err)
		return 0, 0.0, err
	}
	defer c.Close()

	remainedMoney, rderr := redis.Float64(c.Do("hget", key, hsetMoney))
	if rderr != nil {
		log.Println(rderr)
		return 0, 0.0, err
	}

	remainedPartition, rderr := redis.Int(c.Do("hget", key, hsetPartition))
	if rderr != nil {
		log.Println(rderr)
		return 0, 0.0, err
	}

	return remainedMoney, remainedPartition, nil
}
