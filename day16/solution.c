#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <time.h>

char *read_file(char *);
void swap(char [], int i, int j);
void spin(char [], int, int);
void dance(char [], char *);
int indexOf(char [], char, int);
void get_token(char [], char *, int *);
bool elementOf(char programs[], char seen[100][17], int size);
void solve2(char programs[], char *input);
void print_error(char *);

int main(int argc, char *argv[]) {
  clock_t start, end;

  start = clock();

  if (argc != 2) {
    fprintf(stderr, "Wrong number of arguments");
    exit(0);
  }

  char programs[] = "abcdefghijklmnop";
  char *buff = read_file(argv[1]);

  solve2(programs, buff);
  free(buff); 
  end = clock();

  printf("Took %f seconds", ((double) (end - start)) / CLOCKS_PER_SEC);

  return 0;

}

void dance(char programs[], char *input) {
  int a, b;
  char c, d, e;
  int size;
  char token[7];
  int offset = 0;

  get_token(token, input, &size);

  while (size != 0) {
    if (*token == 'x') {
      sscanf(token, "%c%2d/%2d", &c, &a, &b);
      swap(programs, a, b);
    } else if (*token == 'p') {
      sscanf(token, "%c%c/%c", &c, &d, &e);
      swap(programs, indexOf(programs, d, 16), indexOf(programs, e, 16));
    } else {
      sscanf(token, "%c%2d", &c, &a);
      spin(programs, a, 16);
    }
    offset += size + 1;
    get_token(token, input + offset, &size);
  }
} 

void solve2(char programs[], char *input) {
  char seen[100][17];
  bool cycle = false;
  int size = 0;

  for (int i = 0; i < 60; i++) {
    dance(programs, input);

    if (elementOf(programs, seen, size)) {
      break;
    } 
    snprintf(seen[size], sizeof(seen[0]), "%s", programs);
    size++;
  }
  printf("%s\n", seen[(1000000000 % size) - 1]);
}


void get_token(char token[], char *buff, int *size) {
  char c = *buff;
  int i = 0;

  while(c != ',' && c != '\0') {
    token[i++] = c;
    c = *++buff;
  }
  *size = i;
  token[i] = '\0';
}

bool elementOf(char programs[], char seen[100][17], int size) {
  for (int i = 0; i < size; i++) {
    if (strcmp(programs, seen[i]) == 0)
      return true;
  }
  return false;
}

void reverse(char arr[], int start, int end){
  while (start < end) {
      swap(arr, start, end);
      start++;
      end--;
  }
}

void spin(char arr[], int length, int size) {
  reverse(arr, 0, size-1);
  reverse(arr, 0, length-1);
  reverse(arr, length, size-1);
}

void swap(char arr[], int i, int j) {
  int temp = arr[i];
  arr[i] = arr[j]; 
  arr[j] = temp;
}

int indexOf(char arr[], char c, int length) {
  for (int i = 0; i < length; i++) {
    if(c == arr[i])
      return i;
  }
}

char *read_file(char *path) {
  FILE *fp;
  if ((fp = fopen(path, "rb")) == NULL) 
    print_error(path);

  if (fseek(fp, 0, SEEK_END) != 0) 
    print_error(path);

  long buffsize;

  if ((buffsize = ftell(fp)) == -1)
    print_error(path);

  char *buff = malloc((buffsize + 1) * sizeof(char));
  if (buff == NULL) 
    print_error(path);

  fseek(fp, 0, SEEK_SET);

  size_t length;
  if ((length = fread(buff, sizeof(char), buffsize, fp)) != buffsize) 
    print_error(path);

  buff[buffsize] = '\0';

  fclose(fp);
  return buff;
}

void print_error(char *str) {
  perror(str);
  exit(1);
}
