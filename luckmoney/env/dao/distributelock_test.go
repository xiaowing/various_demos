package dao

import "testing"
import "time"
import util "github.com/xiaowing/various_demos/luckmoney/util"

func TestTryLock(t *testing.T) {
	guid := util.NewObjectId()
	lock, err := TryLock(guid)
	if err != nil {
		t.Log(err)
		t.Fail()
		return
	}

	if lock != nil {
		t.Logf("Advisory lock of key %d aquired: %v.\n", lock.Key, lock.IsStillValid())
		time.Sleep(1 * time.Second)
		lock.Release()
	} else {
		t.Logf("Advisory lock of key %d not aquired.\n", guid.GetLockKey())
	}

}
