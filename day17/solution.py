# part1
def solve1():
  step = 359
  lock = [0]
  pos = 0

  for i in range(2017):
    new = (pos + step) % len(lock)
    new += 1
    lock.insert(new, i+1)
    pos = new

  print(f"Part1: {lock[pos+1]}")

# part2
def solve2():
  step = 359
  pos = 0

  for i in range(1, 50000000+1):
    pos = (pos + step) % i + 1
    if pos == 1:
      val = i
  print(f"Part2: {val}")


def main():
  solve1()
  solve2()

if __name__ == "__main__":main()