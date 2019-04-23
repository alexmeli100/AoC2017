#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>
#include <time.h>

#define COL 128
#define ROW 128

int solve(char *, char [ROW][COL]);
const char* hex_char_to_bin(char);
int count_bits(char str[], int length);
void hex_to_bin(char *hex_str, char *arr, int length);
int connected(char bits[ROW][COL]);
void dfs(int marked[ROW][COL], char bits[ROW][COL], int i, int j);