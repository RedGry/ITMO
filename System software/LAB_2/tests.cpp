#define DOCTEST_CONFIG_IMPLEMENT_WITH_MAIN
#include "../producer_consumer.h"
#include "doctest.h"
using namespace std;

TEST_CASE("") {
  vector<int32_t> data{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
  CHECK(run_threads(3, 100, data, false) == 55);
}

TEST_CASE("") {
  vector<int32_t> data{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
  CHECK(run_threads(3, 10, data, false) == 55);
}

TEST_CASE("") {
  vector<int32_t> data{2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                       2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
  CHECK(run_threads(10, 10, data, false) == 40);
}

TEST_CASE("") {
  vector<int32_t> data{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
  CHECK(run_threads(10, 4, data, false) == 55);
}

TEST_CASE("") {
  vector<int32_t> data{1, 6, 3, 5, 3, 6, 6, 6, 6, 8, 4, 7, 34, 78, 3, 0, 8, 45};
  CHECK(run_threads(10, 5, data, false) == 229);
}

TEST_CASE("") {
  vector<int32_t> data{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
  CHECK(run_threads(10, 0, data, false) == 55);
}

TEST_CASE("") {
  vector<int32_t> data{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
  CHECK(run_threads(5000, 0, data, false) == 55);
}

TEST_CASE("") {
  vector<int32_t> data{};
  CHECK(run_threads(5000, 0, data, false) == 0);
}
