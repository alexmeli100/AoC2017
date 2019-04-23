def puzzle_solution(path):

  with open(path) as input:
    numbers = list(map(int, input.readlines()))
  i = 0
  steps = 0

  while 0 <= i < len(numbers):
    if numbers[i] == 0:
      numbers[i] += 1
      steps += 1
    else:
      temp = numbers[i]
      if temp >= 3: 
        numbers[i] -= 1  
      else: 
        numbers[i] += 1
      i += temp
      steps += 1
  return steps

def main():
  import os


  print(puzzle_solution(os.path.join(os.getcwd(), "input.txt")))

if __name__ == "__main__":main()