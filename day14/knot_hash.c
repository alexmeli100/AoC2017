#include "knot_hash.h"

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
  int string_size = strlen(string);

  // allocate memory for an array to hold the acsii characters + suffixes
  int *lengths = malloc((string_size + 5) * sizeof(int)); 
  create_lengths(lengths, string, string_size);

  for (int i = 0; i < ARRAY_LENGTH; i++) {
    arr[i] = i;
  }

  for (int i = 0; i < 64; i++) {
    hash_func(arr, lengths, &curr, &step, string_size + 5);
  }

  dense(arr, result, ARRAY_LENGTH, RESULT_LENGTH);

  for (int i = 0, j = 0; i < RESULT_LENGTH; i++, j+=2) {
    snprintf(res + j, 3, "%02X", result[i]);
  }

  free(lengths);
}

void create_lengths(int *lengths, char *string, int string_size) {
  int suffixes[] = {17, 31, 73, 47, 23};

  for (int i = 0; i < string_size; i++) {
    lengths[i] = (int) string[i];
  }

  memcpy(lengths + string_size, suffixes, 5 * sizeof(int));

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



