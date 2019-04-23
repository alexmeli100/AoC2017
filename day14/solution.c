#include "knot_hash.h"
#include "solution.h"

int main(int argc, char *argv[]) {

  if (argc != 2) {
    fprintf(stderr, "Wrong number of arguments");
    exit(1);
  }

  char str_arr[ROW][COL];
  int result = solve(argv[1], str_arr);

  printf("%d\n", result);

}

int solve(char *str, char str_arr[ROW][COL]) {
  char input[13];
  char hash[33];
  int connected_components;
  int count = 0;

  for (int i = 0; i < ROW; i++) {
    snprintf(input, strlen(str) + 5, "%s-%d", str, i);
    knot_hash(input, hash);
    hex_to_bin(hash, str_arr[i], 32);
    count += count_bits(str_arr[i], COL);
  }

  connected_components = connected(str_arr);
  return connected_components;
}

int count_bits(char str[], int length) {
  int count = 0;

  for (int i = 0; i < length; i++) {
    if (str[i] == '1') 
      count++;
  }
  return count;
}

void hex_to_bin(char *hex_str, char arr[], int length) {
  for (int i = 0, j = 0; i < length; i++, j += 4) {
    memcpy(arr + j, hex_char_to_bin(hex_str[i]), 4 * sizeof(char));
  }
}

const char* hex_char_to_bin(char c) {

  switch(toupper(c)) {

      case '0': return "0000";
      case '1': return "0001";
      case '2': return "0010";
      case '3': return "0011";
      case '4': return "0100";
      case '5': return "0101";
      case '6': return "0110";
      case '7': return "0111";
      case '8': return "1000";
      case '9': return "1001";
      case 'A': return "1010";
      case 'B': return "1011";
      case 'C': return "1100";
      case 'D': return "1101";
      case 'E': return "1110";
      case 'F': return "1111";
  }
}

int connected(char bits[ROW][COL]) {
  int marked[ROW][COL];
  int count = 0;

  for (int i = 0; i < ROW; i++) {
    for (int j = 0; j < COL; j++) {
      marked[i][j] = 0;
    }
  }

  for (int i = 0; i < ROW; i++) {
    for (int j = 0; j < COL; j++) {
      if (bits[i][j] == '0' || marked[i][j]) 
        continue;

      count++;
      dfs(marked, bits, i, j);
        
    }
  }
  return count;
}

void dfs(int marked[ROW][COL], char bits[ROW][COL], int i, int j) {
  if (marked[i][j]) 
    return;

  if (bits[i][j] == '0') 
    return;
  marked[i][j] = 1;

  if (i > 0) 
    dfs(marked, bits, i - 1, j);
  if (j > 0)
    dfs(marked, bits, i, j - 1);
  if (i < 127)
    dfs(marked, bits, i + 1, j);
  if (j < 127)
    dfs(marked, bits, i, j+1);

}



