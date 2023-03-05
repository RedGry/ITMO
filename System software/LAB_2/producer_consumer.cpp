#include <pthread.h>
#include <atomic>
#include <iostream>
#include <thread>
#include <vector>
using namespace std;

pthread_mutex_t value_mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t data_changed = PTHREAD_COND_INITIALIZER;
pthread_cond_t data_read = PTHREAD_COND_INITIALIZER;
bool finish = false;
// false - producer changed the value, but it hasn't been read yet
// true - consumer has changed the value just now, and producer still doesn't
// change the value
bool read = false;
pthread_mutex_t mutex_on_read = PTHREAD_MUTEX_INITIALIZER;

struct global_struct {
  size_t n;
  long max_time;
  bool debug_key = false;
};

struct global_struct global_struct;

struct producer_data {
  int *shared_value_ptr;
  int *number_ptr;
  size_t vector_size;
};

int get_tid() {
  // 1 to 3+N thread ID
  static atomic<int> unique_tid{0};
  thread_local static int *tid;
  if (tid == 0) {
    unique_tid++;
    tid = new int(unique_tid);
  }
  return *tid;
}

void *producer_routine(void *arg) {
  (void)arg;
  // read data, loop through each value and update the value, notify consumer,
  // wait for consumer to process
  struct producer_data *producer_data = (struct producer_data *)arg;

  finish = false;
  read = false;

  int *shared_value_ptr = producer_data->shared_value_ptr;
  size_t vector_size = producer_data->vector_size;
  int *number_ptr = producer_data->number_ptr;

  size_t i;
  for (i = 0; i < vector_size; i++) {
    pthread_mutex_lock(&value_mutex);
    *(shared_value_ptr) = number_ptr[i];
    pthread_cond_signal(&data_changed);
    pthread_mutex_lock(&mutex_on_read);
    read = false;
    pthread_mutex_unlock(&mutex_on_read);
    while (!read) pthread_cond_wait(&data_read, &value_mutex);
    pthread_mutex_unlock(&value_mutex);
  }
  finish = true;
  pthread_cond_broadcast(&data_changed);
  return nullptr;
}

void *consumer_routine(void *arg) {
  (void)arg;
  // for every update issued by producer, read the value and add to sum
  // return pointer to result (for particular consumer)
  int *shared_value_ptr = (int *)arg;

  pthread_setcancelstate(PTHREAD_CANCEL_DISABLE, NULL);

  int *psum = (int *)malloc(sizeof(int));
  *psum = 0;

  while (!finish) {
    pthread_mutex_lock(&value_mutex);
    while (read && !finish) {
      pthread_cond_wait(&data_changed, &value_mutex);
    }
    if (finish) {
      pthread_mutex_unlock(&value_mutex);
      break;
    }
    pthread_mutex_lock(&mutex_on_read);
    *psum += *shared_value_ptr;
    read = true;
    pthread_mutex_unlock(&mutex_on_read);
    if (global_struct.debug_key) cout << get_tid() << ", " << *psum << endl;
    pthread_cond_signal(&data_read);
    pthread_mutex_unlock(&value_mutex);
    long random_time = (global_struct.max_time == 0)
                           ? 0
                           : (rand() % (global_struct.max_time + 1));
    this_thread::sleep_for((chrono::milliseconds)random_time);
  }
  pthread_exit((void *)psum);
}

void *consumer_interruptor_routine(void *arg) {
  (void)arg;
  // interrupt random consumer while producer is running
  pthread_t *consumers_pool_ptr = (pthread_t *)arg;

  int random_consumer;
  while (!finish) {
    random_consumer = rand() % global_struct.n;
    pthread_cancel(consumers_pool_ptr[random_consumer]);
  }
  return nullptr;
}

// the declaration of run threads can be changed as you like
int run_threads(size_t n, long max_time, vector<int> numbers, bool debug_key) {
  // start N threads and wait until they're done
  // return aggregated sum of values
  global_struct.n = n;
  global_struct.max_time = max_time;
  global_struct.debug_key = debug_key;

  int shared_value = 0;

  struct producer_data *producer_data =
      (struct producer_data *)malloc(sizeof(struct producer_data));
  producer_data->number_ptr = &numbers[0];
  producer_data->vector_size = numbers.size();
  producer_data->shared_value_ptr = &shared_value;

  pthread_t producer;
  pthread_create(&producer, NULL, &producer_routine, producer_data);

  pthread_t *consumers_pool_ptr = new pthread_t[n];

  size_t i;
  for (i = 0; i < n; i++) {
    pthread_t consumer;
    pthread_create(&consumer, NULL, &consumer_routine, &shared_value);
    consumers_pool_ptr[i] = consumer;
  }

  pthread_t interrupt;
  pthread_create(&interrupt, NULL, &consumer_interruptor_routine,
                 consumers_pool_ptr);

  int sum = 0;
  for (i = 0; i < n; i++) {
    int *psum;
    pthread_join(consumers_pool_ptr[i], (void **)&psum);
    sum += *psum;
    free(psum);
  }

  pthread_join(producer, NULL);
  pthread_join(interrupt, NULL);

  free(producer_data);
  delete[] consumers_pool_ptr;

  return (int)sum;
}
