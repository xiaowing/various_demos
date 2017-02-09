package main

import (
	"flag"
	"fmt"
	"math"
	"math/rand"
	"runtime"
	"sync"
	"time"
)

// 用于浮点数比较的误差
const PRECISION = 0.001

// 红包的结构体定义
type Envelope struct {
	mu                sync.Mutex // 互斥锁
	residualMoney     float32    // 红包剩余的金额
	residualPartition int        // 红包分与的人数
}

// 红包初始化。传入金额，份数
func (self *Envelope) Init(money float32, divides int) {
	if (money <= 0.00) || (money > 200.00) {
		panic(fmt.Sprintf("Invalid money: %f.\n", money))
	}

	if divides <= 0 {
		panic(fmt.Sprintf("Invalid partitions: %d.\n", divides))
	}

	if money/float32(divides) < PRECISION {
		panic(fmt.Sprintf("Inappropriate money %f or partitions %d.\n", money, divides))
	}

	self.mu.Lock()
	defer self.mu.Unlock()

	self.residualMoney = money
	self.residualPartition = divides
}

// 抢红包的模拟逻辑。应并发执行
func (self *Envelope) Grab() float32 {
	r := rand.New(rand.NewSource(time.Now().UnixNano()))

	self.mu.Lock()
	defer self.mu.Unlock()

	if self.residualPartition == 0 {
		return 0.00
	}
	var result float32
	if self.residualPartition == 1 {
		self.residualPartition--
		result = self.residualMoney
		self.residualMoney -= result
		return result
	}

	result = r.Float32() * (self.residualMoney / float32(self.residualPartition) * 2)
	if areFloatsEqual(result, 0.00) {
		result = 0.01
	} else {
		result = float32(math.Floor(float64(result*100)) / 100)
	}

	self.residualPartition--
	self.residualMoney -= result

	return result
}

// 获得红包结构体的当前状态数据
func (self *Envelope) GetCurrentStatus() (money float32, remained int) {
	self.mu.Lock()
	defer self.mu.Unlock()

	return self.residualMoney, self.residualPartition
}

func areFloatsEqual(f1, f2 float32) bool {
	return math.Dim(float64(f1), float64(f2)) < PRECISION
}

func main() {
	inputAmountPtr := flag.Float64("m", 0.00, "The amount of the money in envelope.")
	inputDividesPtr := flag.Int("d", 0, "The parts that the money would be divided into.")
	var participants int
	flag.IntVar(&participants, "p", 0, "The number of participants.")

	flag.Parse()

	if participants <= 0 {
		panic(fmt.Sprintf("Invalid number of participants %d.\n", participants))
	}

	cpuNum := runtime.NumCPU()
	if cpuNum > 1 {
		runtime.GOMAXPROCS(cpuNum - 1)
	} else {
		runtime.GOMAXPROCS(cpuNum)
	}

	env := new(Envelope)
	env.Init(float32(*inputAmountPtr), *inputDividesPtr)

	var wait sync.WaitGroup
	wait.Add(participants)

	for i := 0; i < participants; i++ {
		go func(currentNo int, envelope *Envelope) {
			grabbedMoney := envelope.Grab()
			fmt.Printf("Participant No.%d grabbed: %3.2f.\n", (currentNo + 1), grabbedMoney)
			wait.Done()
		}(i, env)
	}

	wait.Wait()

	money, divs := env.GetCurrentStatus()

	fmt.Printf("Currently there is $%3.2f left and %d divides left.\n", money, divs)
}
