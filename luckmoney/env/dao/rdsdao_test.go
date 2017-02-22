package dao

import "testing"

func TestInitEnvelopeHset(t *testing.T) {
	key := "xxxyyy"

	err := InitEnvelopeHset(key, 200.0, 8, 10)
	if err != nil {
		t.Fail()
	}

	err = DecrMoneyFromEnvelopeHset(key, -12.8)
	if err != nil {
		t.Log(err)
		t.Fail()
	}

	money, divides, err := GetEnvelopMembers(key)
	if err != nil {
		t.Log(err)
		t.Fail()
	}

	t.Logf("The rest money of key %s is: %f RMB, %d chances left.\n",
		key, money, divides)
}
