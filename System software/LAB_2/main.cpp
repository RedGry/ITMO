#include <iostream>
#include <sstream>
#include <vector>
#include "producer_consumer.h"
using namespace std;

int main(int argc, char* argv[]) {
  bool debug_key = false;
  int index = 1;
  if ((argc == 4) && string(argv[index]) == "-debug") {
    index++;
    debug_key = true;
  }
  size_t n = atoi(argv[index++]);
  long max_time = atoi(argv[index]);

  vector<int> numbers;
  string s;
  int a;
  getline(cin, s);
  stringstream inp(s);
  while (inp >> a) {
    numbers.push_back(a);
  }

  std::cout << run_threads(n, max_time, numbers, debug_key) << std::endl;
  return 0;
}
