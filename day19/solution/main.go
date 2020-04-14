package main

import (
	"io/ioutil"
	"log"
	"strings"
	"unicode"
)

type RouteMap struct {
	dir  complex128
	pos  complex128
	grid map[complex128]rune
}

func newMap(g []string) *RouteMap {
	grid := make(map[complex128]rune)
	p := strings.Index(g[0], "|")

	for y, line := range g {
		for x, v := range line {
			if !unicode.IsSpace(v) {
				grid[complex(float64(x), float64(y))] = v
			}
		}
	}

	return &RouteMap{
		dir:  complex(0, 1),
		pos:  complex(float64(p), 0),
		grid: grid,
	}
}

func (r RouteMap) nextDir(d complex128) complex128 {
	dir := d * complex(0, 1)

	if _, ok := r.grid[r.pos+dir]; ok {
		return dir
	}

	return d * complex(0, -1)
}

func (r *RouteMap) solve() (string, int) {
	path := make([]rune, 0)
	dir := r.dir
	steps := 0

	for {
		if _, ok := r.grid[r.pos]; !ok {
			break
		}

		steps += 1

		if r.grid[r.pos] == '+' {
			dir = r.nextDir(dir)
		}

		if unicode.IsLetter(r.grid[r.pos]) {
			path = append(path, r.grid[r.pos])
		}

		r.pos += dir
	}

	return string(path), steps
}

func main() {
	input, err := ioutil.ReadFile("input.txt")

	if err != nil {
		log.Fatal(err)
	}

	r := newMap(strings.Split(string(input), "\n"))

	res, steps := r.solve()

	log.Println(res)
	log.Println(steps)
}
