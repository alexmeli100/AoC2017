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
void create_lengths(int *lengths, char *string, int string_size);