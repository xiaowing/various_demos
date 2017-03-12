package env

import (
	"fmt"
	"math"
	"math/rand"
	"time"

	dao "github.com/xiaowing/various_demos/luckmoney/env/dao"
	util "github.com/xiaowing/various_demos/luckmoney/util"
)

const EnvlopExpireSec = 24 * 60 * 60

func Create(money float64, divides int) (util.ObjectId, error) {
	if (money <= 0.00) || (money > 200.00) {
		return "", fmt.Errorf("Invalid money: %f.\n", money)
	}

	if divides <= 0 {
		return "", fmt.Errorf("Invalid partitions: %d.\n", divides)
	}

	if money/float64(divides) < 0.01 {
		return "", fmt.Errorf("Inappropriate money %f or partitions %d.\n", money, divides)
	}

	envelopID := util.NewObjectId()
	strID := envelopID.Hex()

	err := dao.InitEnvelopeHset(strID, money, divides, EnvlopExpireSec)
	if err != nil {
		return "", err
	}

	return envelopID, nil
}

func Grab(envelopeId util.ObjectId) (float64, error) {
	strID := envelopeId.Hex()
	rand.Seed(time.Now().UnixNano())

	var lock *dao.DistributeLock
	var err error

	for lock == nil {
		lock, err = dao.TryLock(envelopeId)
		if err != nil {
			//fmt.Printf("Distributed lock cannot acquire due to %v.\n", err)
			return 0.0, err
		}

		if lock == nil {
			randnum := rand.Intn(10)
			time.Sleep(time.Duration(randnum) * 50 * time.Millisecond)
		}
	}
	defer lock.Release()

	balance, leftDivs, daoErr := dao.GetEnvelopMembers(strID)
	if daoErr != nil {
		//fmt.Printf("Cannot get information from redis due to %v.\n", daoErr)
		return 0.0, daoErr
	}

	if leftDivs == 0 {
		return 0.0, nil
	}

	if leftDivs == 1 {
		daoErr = dao.DecrMoneyFromEnvelopeHset(strID, (0.0 - balance))
		if daoErr != nil {
			//fmt.Printf("Cannot decrease money to redis due to %v.\n", daoErr)
			return 0.0, daoErr
		}
		return balance, nil
	}

	r := rand.New(rand.NewSource(time.Now().UnixNano()))
	var result float64

	result = r.Float64() * (balance / float64(leftDivs) * 2)
	if util.AreFloatsEqual(result, 0.00) {
		result = 0.01
	} else {
		result = float64(math.Floor(float64(result*100)) / 100)
	}

	daoErr = dao.DecrMoneyFromEnvelopeHset(strID, (0.0 - result))
	if daoErr != nil {
		//fmt.Printf("Cannot decrease money to redis due to %v.\n", daoErr)
		return 0.0, daoErr
	}
	return result, nil
}

func CheckBalance(envelopeId util.ObjectId) (float64, int, error) {
	strID := envelopeId.Hex()

	return dao.GetEnvelopMembers(strID)
}
