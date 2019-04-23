#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define ARRAY_LENGTH 256
#define BUFFERSIZE 4096
#define RESULT_LENGTH 16

void swap(int arr[], int i, int j);
void reverse(int arr[], int start, int stop, int step);
void knot_hash(char *string, char []);
void hash_func(int arr[], int lengths[], int *curr, int *step, int length);
void dense(int[], int[], int, int);

int main(int argc, char *argv[]) {
  char buff[BUFFERSIZE];

  FILE *fp;
  char res[33];

  if (argc != 2) {
    fprintf(stderr, "Wrong number of arguments");
  }

  if (!(fp = fopen(argv[1], "r"))) {
    perror(argv[1]);
    exit(1);
  }

  fgets(buff, BUFFERSIZE, fp);
  
  knot_hash(buff, res);
  printf("%s\n", res);

  return 0;
}

// swap 2 elements of an array
void swap(int arr[], int i, int j) {
  int temp = arr[i];
  arr[i] = arr[j];
  arr[j] = temp;
}

// reverse array given start and end indices. indices may wrap around
void reverse(int arr[], int start, int stop, int length) {
  for (int i = start, j = stop, k = 0; k < length / 2; i++, j--, k++) {
      int temp = j % ARRAY_LENGTH;
      int j_index = temp < 0 ? temp + ARRAY_LENGTH : temp;
      int i_index = i % ARRAY_LENGTH;
      
      swap(arr, i_index, j_index);
    }
}

void knot_hash(char *string, char res[]) {
  int curr = 0; // current position
  int step = 0; // skip size
  int arr[ARRAY_LENGTH];
  int result[RESULT_LENGTH];
  int suffixes[] = {17, 31, 73, 47, 23};
  int string_size = strlen(string);

  // allocate memory for an array to hold the acsii characters + suffixes
  int *lengths = malloc((string_size + 5) * sizeof(int)); 

  for (int i = 0; i < string_size; i++) {
    lengths[i] = (int) string[i];
  }

  memcpy(lengths + string_size, suffixes, 5 * sizeof(int));

  for (int i = 0; i < ARRAY_LENGTH; i++) {
    arr[i] = i;
  }

  for (int i = 0; i < 64; i++) {
    hash_func(arr, lengths, &curr, &step, string_size + 5);
  }

  dense(arr, result, ARRAY_LENGTH, RESULT_LENGTH);

  for (int i = 0, j = 0; i < RESULT_LENGTH; i++, j += 2) {
    snprintf(res + j, 3, "%02X", result[i]);
  }

  free(lengths);
}

// hash for one iteration
void hash_func(int arr[], int lengths[], int *curr, int *step, int length) {
    for (int i = 0; i < length; i++) {
      
      int stop = ((*curr + lengths[i]) % ARRAY_LENGTH) - 1;

      reverse(arr, *curr, stop, lengths[i]);

      *curr = (*curr + lengths[i] + *step) % ARRAY_LENGTH;
     (*step)++;     
  }
}

// dense the array by xoring each 16 consecutive ints
void dense(int arr[], int result[], int length1, int length2) {
  int temp = arr[0];

  for (int i = 1, j = 0; i <= length1; i++) {
    if (i % length2 == 0) {
      result[j] = temp;
      temp = arr[i];
      j++;
    } else {
        temp ^= arr[i];
    }
  }
}



