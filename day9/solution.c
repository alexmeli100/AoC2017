#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

int main(int argc, char *argv[]) {

  int depth = 1;
  int score, inGarbage, ignore, garbage;
  int c;

  score  = garbage = 0;
  inGarbage = ignore = false;

  FILE *fp;

  if (argc != 2) {
    fprintf(stderr, "Wrong number of arguments");
    exit(1);
  }

  if (!(fp = fopen(argv[1], "r"))) {
    perror(argv[1]);
    exit(1);
  }

  while ((c = fgetc(fp)) != EOF) {
    if (ignore) 
      ignore = false;
    else if (c == '!')
      ignore = true;
    else if (c == '>')
      inGarbage = false;
    else if (inGarbage)
      garbage++;
    else if (c == '<') 
      inGarbage = true;
    else if (c == '{' && !inGarbage) 
      score += depth++;
    else if (c == '}' && !inGarbage) 
      depth--;
  }

  fprintf(stdout, "Score is: %d\nGarbage is: %d", score, garbage);

  fclose(fp);
  return 0;

}