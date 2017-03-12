package main

import (
	"flag"
	"fmt"
	"runtime"
	"sync"

	env "github.com/xiaowing/various_demos/luckmoney/env"
)

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

	envID, err := env.Create(*inputAmountPtr, *inputDividesPtr)
	if err != nil {
		fmt.Printf("Envelope creation failed due to ERROR: %v.\n", err)
		return
	}

	var wait sync.WaitGroup
	wait.Add(participants)

	for i := 0; i < participants; i++ {
		go func(currentNo int) {
			grabbedMoney, err := env.Grab(envID)
			if err != nil {
				fmt.Printf("Participant No.%d grabbed: %3.2f due to ERROR: %v.\n",
					(currentNo + 1), grabbedMoney, err)
			} else {
				fmt.Printf("Participant No.%d grabbed: %3.2f.\n", (currentNo + 1), grabbedMoney)
			}
			wait.Done()
		}(i)
	}
	wait.Wait()

	money, divs, err := env.CheckBalance(envID)
	if err != nil {
		fmt.Printf("Cannot check the balance due to ERROR: %v.\n", err)
	}
	fmt.Printf("Currently there is $%3.2f left and %d divides left.\n", money, divs)
}
