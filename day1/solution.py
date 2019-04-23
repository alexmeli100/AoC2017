def part1(s):
  return sum([int(x) for i, x in enumerate(s) if x == s[(i+1) % len(s)]])

def part2(s):
  return sum(int(x) for i, x in enumerate(s) if x == s[(len(s) // 2 + i) % len(s)])

def main():
  with open("input.txt", "r") as input:
    s = input.read()
    print(f"Part 1: {part1(s)}")
    print(f"Part 2: {part2(s)}")

if __name__ == "__main__":main()