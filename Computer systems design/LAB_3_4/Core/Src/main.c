/* USER CODE BEGIN Header */
/**
  ******************************************************************************
  * @file           : main.c
  * @brief          : Main program body
  ******************************************************************************
  * @attention
  *
  * <h2><center>&copy; Copyright (c) 2022 STMicroelectronics.
  * All rights reserved.</center></h2>
  *
  * This software component is licensed by ST under BSD 3-Clause license,
  * the "License"; You may not use this file except in compliance with the
  * License. You may obtain a copy of the License at:
  *                        opensource.org/licenses/BSD-3-Clause
  *
  ******************************************************************************
  */
/* USER CODE END Header */

/* Includes ------------------------------------------------------------------*/
#include "main.h"

/* Private includes ----------------------------------------------------------*/
/* USER CODE BEGIN Includes */
#include "ctype.h"
#include "stdbool.h"
#include "stdint.h"
#include "stdio.h"
#include "string.h"
#include "notes.h"
/* USER CODE END Includes */

/* Private typedef -----------------------------------------------------------*/
/* USER CODE BEGIN PTD */

/* USER CODE END PTD */

/* Private define ------------------------------------------------------------*/
/* USER CODE BEGIN PD */
#define BUF_SIZE 8192

#define KB_I2C_ADDRESS (0xE2)
#define KB_I2C_READ_ADDRESS ((KB_I2C_ADDRESS) | 1)
#define KB_I2C_WRITE_ADDRESS ((KB_I2C_ADDRESS) & ~1)
#define KB_INPUT_REG (0x0)
#define KB_OUTPUT_REG (0x1)
#define KB_CONFIG_REG (0x3)
#define KB_KEY_DEBOUNCE_TIME (200)
/* USER CODE END PD */

/* Private macro -------------------------------------------------------------*/
/* USER CODE BEGIN PM */

/* USER CODE END PM */

/* Private variables ---------------------------------------------------------*/
I2C_HandleTypeDef hi2c1;

TIM_HandleTypeDef htim1;
TIM_HandleTypeDef htim6;

UART_HandleTypeDef huart6;

/* USER CODE BEGIN PV */

/* USER CODE END PV */

/* Private function prototypes -----------------------------------------------*/
void SystemClock_Config(void);
static void MX_GPIO_Init(void);
static void MX_TIM1_Init(void);
static void MX_TIM6_Init(void);
static void MX_USART6_UART_Init(void);
static void MX_I2C1_Init(void);
/* USER CODE BEGIN PFP */

/* USER CODE END PFP */

/* Private user code ---------------------------------------------------------*/
/* USER CODE BEGIN 0 */

// ----- RingBuffer -----

struct RingBuffer {
    char data[BUF_SIZE];
    uint8_t head;
    uint8_t tail;
    bool empty;
};

typedef struct RingBuffer RingBuffer;

static void buf_init(RingBuffer *buf) {
    buf->head = 0;
    buf->tail = 0;
    buf->empty = true;
}

static void buf_push(RingBuffer *buf, char * el) {
	uint64_t size = strlen(el);

	if (buf->head + size + 1 > BUF_SIZE) {
		buf->head = 0;
	}

	strcpy(&buf->data[buf->head], el);
//    buf->data[buf->head] = el;
    buf->head += size + 1;

    if (buf->head == BUF_SIZE) {
        buf->head = 0;
    }

    buf->empty = false;
}

static bool buf_pop(RingBuffer *buf, /* out */ char * el) {
    if (buf->empty) {
        return false;
    }

    uint64_t size = strlen(&buf->data[buf->tail]);

    strcpy(el, &buf->data[buf->tail]);
    buf->tail += size + 1;

    if (buf->tail == BUF_SIZE || buf->tail == '\0') {
        buf->tail = 0;
    }

    if (buf->tail == buf->head) {
        buf->empty = true;
    }

    return true;
}

static struct RingBuffer ringBufferRx;
static struct RingBuffer ringBufferTx;

static char el[2] = {"\0\0"};

// ----- UART -----

struct Status {
    bool interrupt_enable;
    uint32_t pmask;
};

static struct Status status;

bool transmit_busy = false;

void enable_interrupt(struct Status *status) {
    status->interrupt_enable = true;
}

void disable_interrupt(struct Status *status) {
    status->interrupt_enable = false;
}

void transmit_uart(const struct Status *status, char *buf, size_t size) {
    if (status->interrupt_enable) {
        if (transmit_busy) {
        	buf_push(&ringBufferTx, buf);
        }
        else {
        	HAL_UART_Transmit_IT(&huart6, buf, size);
        	transmit_busy = true;
        }
        return;
    }
    HAL_UART_Transmit(&huart6, buf, size, 500);
}

void transmit_uart_nl(const struct Status *status, char *buf, size_t size) {
    transmit_uart(status, buf, size);
    transmit_uart(status, "\r\n", 2);
}

void receive_uart(const struct Status *status) {
    if (status->interrupt_enable) {
        HAL_UART_Receive_IT(&huart6, el, sizeof(char));
        return;
    }
    HAL_StatusTypeDef stat = HAL_UART_Receive(&huart6, el, sizeof(char), 0);
    switch (stat) {
        case HAL_OK:{
            buf_push(&ringBufferRx, el);
            transmit_uart(status, el, 1);
            break;
        }
        case HAL_ERROR:
        case HAL_BUSY:
        case HAL_TIMEOUT:
            break;
    }
}

void HAL_UART_RxCpltCallback(UART_HandleTypeDef *huart) {
	buf_push(&ringBufferRx, el);
	transmit_uart(&status, el, 1);
}

void HAL_UART_TxCpltCallback(UART_HandleTypeDef *huart) {
	char buf[1024];
	if (buf_pop(&ringBufferTx, buf)) {
		HAL_UART_Transmit_IT(&huart6, buf, strlen(buf));
	}
	else {
		transmit_busy = false;
	}
}

// --------------------- TIMER ---------------------

uint32_t user_melody[256];
uint32_t user_delays[256];
uint32_t user_melody_size = 0;

uint32_t duration = 1;
bool melody_playing = true;
uint32_t i = 0;

// Начальная мелодия старта инициализация
uint32_t * melody = start_melody;
uint32_t * melody_delays = start_delays;
uint32_t melody_size = sizeof(start_melody) / sizeof (uint32_t);


void play_melody(uint32_t * m, uint32_t * d, uint32_t size) {
	i = 0;
	melody = m;
	melody_delays = d;
	melody_size = size;
	melody_playing = true;
}


void HAL_TIM_PeriodElapsedCallback(TIM_HandleTypeDef * htim) {
	if (htim->Instance == TIM6) {
        if (!melody_playing) {
            return;
        }
		if (duration > 0) duration--;
		if (duration == 0) {
			if (melody[i] == 0) {
				// Выключаем звук
				htim1.Instance->CCR1 = 0; // 0%
				duration = melody_delays[i];
			} else {
				// Проигрываем ноты
				duration = melody_delays[i];
				htim1.Instance->ARR = 90000000 / (melody[i] * htim1.Instance->PSC) - 1;
     			htim1.Instance->CCR1 = htim1.Instance->ARR >> 1; // 50% громкость (100%) - скважность
			}
			i++;
			if (i == melody_size){
                i = 0;
                duration = 1;
                melody_playing = false;

                // Выключаем звук
                htim1.Instance->CCR1 = 0; // 0%
            }
		}
	}
}

// --------------------- KEYBOARD ---------------------
bool is_music_mode = true;
bool is_settings_2 = false;
uint32_t last_pressing_time = 0;
int last_pressed_btn_index = -1;

bool is_co = false;
bool is_test_keyboard_mode = false;
bool last_btn_state = false;

char music[] = {'1', '2', '3', '4', '5', 'x', '!', '!', '!', '?', '!', '\r'};
char settings[] = {'1', '2', '3', '4', '5', '6', '7', '8', '9', ',', '0', 'e'};
char save_exit[] = {'\r', 'q', 's', '!', '!', '!', '!', '!', '!', '?', '!', 'e'};

bool is_btn_press() {
	 return HAL_GPIO_ReadPin(GPIOC,GPIO_PIN_15) == 0;
 }

int stroka[4] = {0, 0, 0, 0};
int stolbec[3] = {0, 0, 0};

int get_pressed_btn_index(){
	const uint32_t t = HAL_GetTick();
	if (t - last_pressing_time < KB_KEY_DEBOUNCE_TIME) return -1;
	int index = -1;
	uint8_t reg_buffer = ~0;
	uint8_t tmp = 0;
	int i = 0;
	HAL_I2C_Mem_Write(&hi2c1, KB_I2C_WRITE_ADDRESS, KB_OUTPUT_REG, 1, &tmp, 1, KB_KEY_DEBOUNCE_TIME);
	for (int row = 0; row < 4; row++) {
		uint8_t buf = ~((uint8_t) (1 << row));
		HAL_I2C_Mem_Write(&hi2c1, KB_I2C_WRITE_ADDRESS, KB_CONFIG_REG, 1, &buf, 1, KB_KEY_DEBOUNCE_TIME);
		HAL_Delay(100);
		HAL_I2C_Mem_Read(&hi2c1, KB_I2C_READ_ADDRESS, KB_INPUT_REG, 1, &reg_buffer, 1, KB_KEY_DEBOUNCE_TIME);
//		if (i == 0)
		switch(reg_buffer >> 4){
			case 6: index = (i == 0) ? row * 3 + 1 : -1; i++; stroka[row] += 1; stolbec[0] += 1; break;
			case 5: index = (i == 0) ? row * 3 + 2 : -1; i++; stroka[row] += 1; stolbec[1] += 1; break;
			case 3: index = (i == 0) ? row * 3 + 3 : -1; i++; stroka[row] += 1; stolbec[2] += 1; break;
			case 0:
			case 1:
			case 2:
			case 4:
				index = -1;
		}
	}
	if (index != -1) last_pressing_time = t;
	if (index == last_pressed_btn_index){
		return -1;
	}
//	if (i != 1) return -1;
	last_pressed_btn_index = index;

	int sum_stroka = 0;
	int sum_stolbec = 0;

	for (int j=0; j <= 3; j++){
		sum_stroka += stroka[j];
		stroka[j] = 0;
	}

	for (int j=0; j <= 2; j++){
		sum_stolbec += stolbec[j];
		stolbec[j] = 0;
	}

	if (sum_stroka != 1){
		return -1;
	}
	if (sum_stolbec != 1){
		return -1;
	}

	return index;
}

char key2char(const int key){
    return is_music_mode ? music[key - 1] : (is_settings_2 ? save_exit[key - 1] : settings[key - 1]);
}

static void set_green_led(bool on) { HAL_GPIO_WritePin(GPIOD, GPIO_PIN_13, on ? GPIO_PIN_SET : GPIO_PIN_RESET); }

static void set_yellow_led(bool on) { HAL_GPIO_WritePin(GPIOD, GPIO_PIN_14, on ? GPIO_PIN_SET : GPIO_PIN_RESET); }

static void set_red_led(bool on) { HAL_GPIO_WritePin(GPIOD, GPIO_PIN_15, on ? GPIO_PIN_SET : GPIO_PIN_RESET); }


/* USER CODE END 0 */

/**
  * @brief  The application entry point.
  * @retval int
  */
int main(void)
{
  /* USER CODE BEGIN 1 */

  /* USER CODE END 1 */

  /* MCU Configuration--------------------------------------------------------*/

  /* Reset of all peripherals, Initializes the Flash interface and the Systick. */
  HAL_Init();

  /* USER CODE BEGIN Init */

  /* USER CODE END Init */

  /* Configure the system clock */
  SystemClock_Config();

  /* USER CODE BEGIN SysInit */

  /* USER CODE END SysInit */

  /* Initialize all configured peripherals */
  MX_GPIO_Init();
  MX_TIM1_Init();
  MX_TIM6_Init();
  MX_USART6_UART_Init();
  MX_I2C1_Init();
  /* USER CODE BEGIN 2 */
//  uint8_t a = 0x70;
//  HAL_I2C_Mem_Write(&hi2c1, KB_I2C_WRITE_ADDRESS, KB_CONFIG_REG, 1, &a, 1, 100);
  HAL_TIM_PWM_Start(&htim1, TIM_CHANNEL_1);
  HAL_TIM_Base_Start_IT(&htim6);
  /* USER CODE END 2 */

  /* Infinite loop */
  /* USER CODE BEGIN WHILE */
  bool prog_start = true;

  char empty_msg[] = {"\r"};
  char hello_msg[] = {""
		  "Привествую в нашем простой музыкально шкатулке!\r\n"
  };
  char layout_music[] = {
		  "\r\nМузыкальная раскладка:\r\n"
		  "\t1 | 2 | 3\r\n"
		  "\t----------\r\n"
		  "\t4 | 5 | x\r\n"
		  "\t----------\r\n"
		  "\t- | - | -\r\n"
		  "\t----------\r\n"
		  "\t? | - | Enter\r\n"
  };
  char layout_settings[] = {
		  "\r\nПользовательская раскладка 1:\r\n"
		  "\t1 | 2 | 3\r\n"
		  "\t----------\r\n"
		  "\t4 | 5 | 6\r\n"
		  "\t----------\r\n"
		  "\t7 | 8 | 9\r\n"
		  "\t----------\r\n"
		  "\t, | 0 | next\r\n"
		  "\r\n"
		  "\r\nПользовательская раскладка 2:\r\n"
		  "\te | q | s\r\n"
		  "\t----------\r\n"
		  "\t- | - | -\r\n"
		  "\t----------\r\n"
		  "\t- | - | -\r\n"
		  "\t----------\r\n"
		  "\t? | - | back\r\n"
  };
  char list_melody_msg[] = {
		  "\r\nДоступные мелодии:\r\n"
		  "\t1. - StarWars: Imperial March.\r\n"
		  "\t2. - Undertale: Megalovania.\r\n"
		  "\t3. - Zelda.\r\n"
		  "\t4. - Simple Melody.\r\n"
		  "\t5. - Ваша мелодия.\r\n"
		  "\tx. - Завершить мелодию.\r\n"
		  "\t? - Получить информацию.\r\n"
		  "\tEnter - Настройки вашей мелодии."
  };
  char list_settings_msg[] = {
		  "\r\nДоступные мелодии:\r\n"
		  "\t0-9. - Цифры.\r\n"
		  "\t','. - Разделитель\r\n"
		  "\tnext. - Следующая раскладка.\r\n"
		  "\tback. - Предыдущая раскладка\r\n"
		  "\t?. - Получить информацию.\r\n"
		  "\ts. - Сохранить мелодию и выйти.\r\n"
		  "\tq - Выйти без сохранения.\r\n"
  };

  char stop_melody_msg[] = {
		  " - Мелодия остановлена!"
  };

  // Сообщения выбора мелодии
  char invalid_option_msg[] = {" - Неправильный ввод!"};
  char starwars_msg[] = {" - Играет: \"StarWars: Imperial March\""};
  char megalovania_msg[] = {" - Играет: \"Undertale: Megalovania\""};
  char zelda_msg[] = {" - Играет: \"Zelda\""};
  char simple_msg[] = {" - Играет: \"Antoshka Melody\""};
  char user_msg[] = {" - �?грает: \"Ваша мелодия\""};
  char user_bad_msg[] = {" - Вы не создали свою мелодию! :("};
  char gg_msg[] = {" - Мелодия не играет!"};

  // Сообщения Настройки пользовательской мелодии
  char prompt_msg[] = {
		  "Вы перешли в режим настройки вашей мелодии!\r\n"
		  "Введите ваши ноты в данном формате:\r\n"
		  "\tЧАСТОТА,ДЛИТЕЛЬНОСТЬ\r\n"
          "Нажмите 'q', чтобы выйти.\r\n"
          "Нажмите 's', чтобы сохранить мелодию и выйти."
  };
  char note_save_msg[] = {" - Нота сохранена!"};
  char invalid_input_msg[] = {" - Неправильный формат! Попробуйте снова."};
  char input_limit_reached_msg[] = { "\r\nВаша мелодия слишком длинная :(" };
  char save_msg[] = {" - Вы успешно сохранили вашу мелодию и вышли!"};
  char exit_msg[] = {" - Вы вышли из решима настройки вашей мелодии!"};

  char test_keyboard_on[] = {"Тестовый режим запущен!"};
  char test_keyboard_off[] = {"Прикладной режим запущен!"};

  set_green_led(false);


  disable_interrupt(&status);
  buf_init(&ringBufferRx);
  buf_init(&ringBufferTx);


  while (1)
  {
	  if (prog_start){
	  		  transmit_uart_nl(&status, hello_msg, sizeof(hello_msg));
	  		  prog_start = false;
	  	  }

	  bool btn_state = is_btn_press();
	  if (last_btn_state && !btn_state){
		  is_test_keyboard_mode = !is_test_keyboard_mode;
		  if (is_test_keyboard_mode) transmit_uart_nl(&status, test_keyboard_on, sizeof(test_keyboard_on));
		  else transmit_uart_nl(&status, test_keyboard_off, sizeof(test_keyboard_off));
	  }
	  last_btn_state = btn_state;

	  int btn_index = get_pressed_btn_index();
	  if (btn_index != -1) {
		  char received_char[] = {key2char(btn_index)};

		  if (is_test_keyboard_mode){
			  transmit_uart_nl(&status, received_char, sizeof(received_char));
			  continue;
		  } else {
			  transmit_uart(&status, received_char, sizeof(received_char));
		  }

		  switch (received_char[0]) {
			  case '1': {
				  transmit_uart_nl(&status, starwars_msg, sizeof(starwars_msg));
				  play_melody(starwars2_melody, starwars2_delays, sizeof(starwars2_melody) / sizeof (uint32_t));
				  break;
			  }
			  case '2': {
			      transmit_uart_nl(&status, megalovania_msg, sizeof(megalovania_msg));
			      play_melody(megalovania_melody, megalovania_delays, sizeof(megalovania_melody) / sizeof (uint32_t));
			      break;
			  }
			  case '3': {
			      transmit_uart_nl(&status, zelda_msg, sizeof(zelda_msg));
			      play_melody(zelda_melody, zelda_delays, sizeof(zelda_melody) / sizeof (uint32_t));
			      break;
			  }
			  case '4': {
			      transmit_uart_nl(&status, simple_msg, sizeof(simple_msg));
			      play_melody(antoshka_melody, antoshka_delays, sizeof(antoshka_melody) / sizeof (uint32_t));
			      break;
			  }
			  case '5': {
				  if (user_melody_size != 0){
					  transmit_uart_nl(&status, user_msg, sizeof(user_msg));
					  play_melody(user_melody, user_delays, user_melody_size + 1);
					  break;
				  }
			      transmit_uart_nl(&status, user_bad_msg, sizeof(user_bad_msg));
			      break;
			  }
			  case '?': {
				  transmit_uart_nl(&status, layout_music, sizeof(layout_music));
				  transmit_uart_nl(&status, list_melody_msg, sizeof(list_melody_msg));
				  break;
			  }
			  case 'x': {
				  if (!melody_playing) {
					  transmit_uart_nl(&status, gg_msg, sizeof(gg_msg));
					  break;
				  }
				  play_melody(stop_melody, stop_delays, sizeof(stop_melody) / sizeof (uint32_t));
				  transmit_uart_nl(&status, stop_melody_msg, sizeof(stop_melody_msg));
				  break;
			  }
			  case '\r': {
				  bool settings_start = true;

				  uint32_t cur_melody[256];
				  uint32_t cur_delays[256];
				  uint32_t cur_size = 0;

				  uint32_t note = 0;
				  uint32_t delay = 0;
				  bool comma_encountered = 0;

				  set_green_led(true);

				  is_music_mode = false;

				  while (1) {
					  if (settings_start){
						  transmit_uart_nl(&status, prompt_msg, sizeof(prompt_msg));
						  transmit_uart_nl(&status, layout_settings, sizeof(layout_settings));
						  settings_start = false;
					  }

					  int btn_index = get_pressed_btn_index();
					  if (btn_index != -1) {
						  char received_char[] = {key2char(btn_index)};
						  if (received_char[0] != 'e'){
							  transmit_uart(&status, received_char, sizeof(received_char));
						  } else {
							  is_settings_2 = !is_settings_2;
							  set_red_led(is_settings_2);
							  set_green_led(!is_settings_2);
							  continue;
						  }

						  if (received_char[0] == '?'){
							  transmit_uart_nl(&status, layout_settings, sizeof(layout_settings));
							  transmit_uart_nl(&status, list_settings_msg, sizeof(list_settings_msg));
							  continue;
						  }

			              if (received_char[0] == 'q') {
			            	  transmit_uart_nl(&status, exit_msg, sizeof(exit_msg));
			            	  is_music_mode = true;
			            	  is_settings_2 = false;
			            	  set_green_led(0);
			            	  set_red_led(0);
			                  break;
			              }

			              if (received_char[0] == 's') {
			            	  is_music_mode = true;
			            	  is_settings_2 = false;
			            	  transmit_uart_nl(&status, save_msg, sizeof(save_msg));
			                  memcpy(user_melody, cur_melody, sizeof(user_melody) / sizeof (uint32_t));
			                  memcpy(user_delays, cur_delays, sizeof(user_melody) / sizeof (uint32_t));
			                  user_melody_size = cur_size;
			                  set_green_led(0);
			                  set_red_led(0);
			                  break;
			              }

			              if (received_char[0] == ',') {
			                  if (comma_encountered) {
			                      transmit_uart_nl(&status, invalid_input_msg, sizeof(invalid_input_msg));
			                      note = 0;
			                      delay = 0;
			                      comma_encountered = false;
			                      continue;
			                  }
			                  comma_encountered = true;
			                  continue;
			              }

			              if (received_char[0] == '\r') {
			                  transmit_uart_nl(&status, empty_msg, sizeof(empty_msg));
			                  cur_melody[cur_size] = note;
			                  cur_delays[cur_size] = delay;
			                  cur_size++;

			                  note = 0;
			                  delay = 0;
			                  comma_encountered = false;
			                  continue;
			              }

			              if (!isdigit(received_char[0])) {
			                  transmit_uart_nl(&status, invalid_input_msg, sizeof(invalid_input_msg));
			                  note = 0;
			                  delay = 0;
			                  comma_encountered = false;
			                  continue;
			              }

			              if (cur_size == 256) {
			                  transmit_uart_nl(&status, input_limit_reached_msg, sizeof(input_limit_reached_msg));
			              }

			              if (comma_encountered) {
			                  delay = delay * 10 + (received_char[0] - 48);
			              } else {
			                  note = note * 10 + (received_char[0] - 48);
			              }
			          }
				 }
			     break;
			  default: {
			      transmit_uart_nl(&status, invalid_option_msg, sizeof(invalid_option_msg));
			      break;
			  }
		  }
		  }
	  }
//          case '2': {
//              transmit_uart_nl(&status, megalovania_msg, sizeof(megalovania_msg));
//              play_melody(megalovania_melody, megalovania_delays, sizeof(megalovania_melody) / sizeof (uint32_t));
//              break;
//          }
//          case '3': {
//              transmit_uart_nl(&status, zelda_msg, sizeof(zelda_msg));
//              play_melody(zelda_melody, zelda_delays, sizeof(zelda_melody) / sizeof (uint32_t));
//              break;
//          }
//          case '4': {
//              transmit_uart_nl(&status, simple_msg, sizeof(simple_msg));
//              play_melody(antoshka_melody, antoshka_delays, sizeof(antoshka_melody) / sizeof (uint32_t));
//              break;
//          }
//          case '5': {
//        	  if (user_melody_size != 0){
//				  transmit_uart_nl(&status, user_msg, sizeof(user_msg));
//				  play_melody(user_melody, user_delays, sizeof(user_melody) / sizeof (uint32_t));
//				  break;
//        	  }
//        	  transmit_uart_nl(&status, user_bad_msg, sizeof(user_bad_msg));
//        	  break;
//          }
//          case '?': {
//        	  transmit_uart_nl(&status, list_melody_msg, sizeof(list_melody_msg));
//			  break;
//          }
//          case 'x': {
//        	  if (!melody_playing) {
//        		  transmit_uart_nl(&status, gg_msg, sizeof(gg_msg));
//        		  break;
//        	  }
//        	  play_melody(stop_melody, stop_delays, sizeof(stop_melody) / sizeof (uint32_t));
//        	  transmit_uart_nl(&status, stop_melody_msg, sizeof(stop_melody_msg));
//        	  break;
//          }
//          case '\r': {
//              transmit_uart_nl(&status, prompt_msg, sizeof(prompt_msg));
//
//              uint32_t cur_melody[256];
//              uint32_t cur_delays[256];
//              uint32_t cur_size = 0;
//
//              uint32_t note = 0;
//              uint32_t delay = 0;
//              bool comma_encountered = 0;
//
//              while (1) {
//                  receive_uart(&status);
//                  char c[2];
//                  while (!buf_pop(&ringBufferRx, c));
//
//                  if (c[0] == 'q') {
//                	  transmit_uart_nl(&status, exit_msg, sizeof(exit_msg));
//                      break;
//                  }
//
//                  if (c[0] == 's') {
//                	  transmit_uart_nl(&status, save_msg, sizeof(save_msg));
//                      memcpy(user_melody, cur_melody, sizeof(user_melody) / sizeof (uint32_t));
//                      memcpy(user_delays, cur_delays, sizeof(user_melody) / sizeof (uint32_t));
//                      user_melody_size = sizeof(user_melody) / sizeof (uint32_t);
//                      break;
//                  }
//
//                  if (c[0] == ',') {
//                      if (comma_encountered) {
//                          transmit_uart_nl(&status, invalid_input_msg, sizeof(invalid_input_msg));
//                          note = 0;
//                          delay = 0;
//                          comma_encountered = false;
//                          continue;
//                      }
//                      comma_encountered = true;
//                      continue;
//                  }
//
//                  if (c[0] == '\r') {
//                      cur_melody[cur_size] = note;
//                      cur_delays[cur_size] = delay;
//                      cur_size++;
//
//                      note = 0;
//                      delay = 0;
//                      comma_encountered = false;
//                      continue;
//                  }
//
//                  if (!isdigit(c[0])) {
//                      transmit_uart_nl(&status, invalid_input_msg, sizeof(invalid_input_msg));
//                      note = 0;
//                      delay = 0;
//                      comma_encountered = false;
//                      continue;
//                  }
//
//                  if (cur_size == 256) {
//                      transmit_uart_nl(&status, input_limit_reached_msg, sizeof(input_limit_reached_msg));
//                  }
//
//                  if (comma_encountered) {
//                      delay = delay * 10 + (c[0] - 48);
//                  } else {
//                      note = note * 10 + (c[0] - 48);
//                  }
//              }
//              break;
//          }
//          default: {
//              transmit_uart_nl(&status, invalid_option_msg, sizeof(invalid_option_msg));
//              break;
//          }
//      }
    /* USER CODE END WHILE */

    /* USER CODE BEGIN 3 */

  }
  /* USER CODE END 3 */
}

/**
  * @brief System Clock Configuration
  * @retval None
  */
void SystemClock_Config(void)
{
  RCC_OscInitTypeDef RCC_OscInitStruct = {0};
  RCC_ClkInitTypeDef RCC_ClkInitStruct = {0};

  /** Configure the main internal regulator output voltage
  */
  __HAL_RCC_PWR_CLK_ENABLE();
  __HAL_PWR_VOLTAGESCALING_CONFIG(PWR_REGULATOR_VOLTAGE_SCALE1);
  /** Initializes the CPU, AHB and APB busses clocks
  */
  RCC_OscInitStruct.OscillatorType = RCC_OSCILLATORTYPE_HSE;
  RCC_OscInitStruct.HSEState = RCC_HSE_ON;
  RCC_OscInitStruct.PLL.PLLState = RCC_PLL_ON;
  RCC_OscInitStruct.PLL.PLLSource = RCC_PLLSOURCE_HSE;
  RCC_OscInitStruct.PLL.PLLM = 15;
  RCC_OscInitStruct.PLL.PLLN = 216;
  RCC_OscInitStruct.PLL.PLLP = RCC_PLLP_DIV2;
  RCC_OscInitStruct.PLL.PLLQ = 4;
  if (HAL_RCC_OscConfig(&RCC_OscInitStruct) != HAL_OK)
  {
    Error_Handler();
  }
  /** Activate the Over-Drive mode
  */
  if (HAL_PWREx_EnableOverDrive() != HAL_OK)
  {
    Error_Handler();
  }
  /** Initializes the CPU, AHB and APB busses clocks
  */
  RCC_ClkInitStruct.ClockType = RCC_CLOCKTYPE_HCLK|RCC_CLOCKTYPE_SYSCLK
                              |RCC_CLOCKTYPE_PCLK1|RCC_CLOCKTYPE_PCLK2;
  RCC_ClkInitStruct.SYSCLKSource = RCC_SYSCLKSOURCE_PLLCLK;
  RCC_ClkInitStruct.AHBCLKDivider = RCC_SYSCLK_DIV1;
  RCC_ClkInitStruct.APB1CLKDivider = RCC_HCLK_DIV4;
  RCC_ClkInitStruct.APB2CLKDivider = RCC_HCLK_DIV2;

  if (HAL_RCC_ClockConfig(&RCC_ClkInitStruct, FLASH_LATENCY_5) != HAL_OK)
  {
    Error_Handler();
  }
}

/**
  * @brief I2C1 Initialization Function
  * @param None
  * @retval None
  */
static void MX_I2C1_Init(void)
{

  /* USER CODE BEGIN I2C1_Init 0 */

  /* USER CODE END I2C1_Init 0 */

  /* USER CODE BEGIN I2C1_Init 1 */

  /* USER CODE END I2C1_Init 1 */
  hi2c1.Instance = I2C1;
  hi2c1.Init.ClockSpeed = 400000;
  hi2c1.Init.DutyCycle = I2C_DUTYCYCLE_2;
  hi2c1.Init.OwnAddress1 = 0;
  hi2c1.Init.AddressingMode = I2C_ADDRESSINGMODE_7BIT;
  hi2c1.Init.DualAddressMode = I2C_DUALADDRESS_DISABLE;
  hi2c1.Init.OwnAddress2 = 0;
  hi2c1.Init.GeneralCallMode = I2C_GENERALCALL_DISABLE;
  hi2c1.Init.NoStretchMode = I2C_NOSTRETCH_DISABLE;
  if (HAL_I2C_Init(&hi2c1) != HAL_OK)
  {
    Error_Handler();
  }
  /** Configure Analogue filter
  */
  if (HAL_I2CEx_ConfigAnalogFilter(&hi2c1, I2C_ANALOGFILTER_ENABLE) != HAL_OK)
  {
    Error_Handler();
  }
  /** Configure Digital filter
  */
  if (HAL_I2CEx_ConfigDigitalFilter(&hi2c1, 0) != HAL_OK)
  {
    Error_Handler();
  }
  /* USER CODE BEGIN I2C1_Init 2 */

  /* USER CODE END I2C1_Init 2 */

}

/**
  * @brief TIM1 Initialization Function
  * @param None
  * @retval None
  */
static void MX_TIM1_Init(void)
{

  /* USER CODE BEGIN TIM1_Init 0 */

  /* USER CODE END TIM1_Init 0 */

  TIM_ClockConfigTypeDef sClockSourceConfig = {0};
  TIM_MasterConfigTypeDef sMasterConfig = {0};
  TIM_OC_InitTypeDef sConfigOC = {0};
  TIM_BreakDeadTimeConfigTypeDef sBreakDeadTimeConfig = {0};

  /* USER CODE BEGIN TIM1_Init 1 */

  /* USER CODE END TIM1_Init 1 */
  htim1.Instance = TIM1;
  htim1.Init.Prescaler = 89;
  htim1.Init.CounterMode = TIM_COUNTERMODE_UP;
  htim1.Init.Period = 999;
  htim1.Init.ClockDivision = TIM_CLOCKDIVISION_DIV1;
  htim1.Init.RepetitionCounter = 0;
  htim1.Init.AutoReloadPreload = TIM_AUTORELOAD_PRELOAD_DISABLE;
  if (HAL_TIM_Base_Init(&htim1) != HAL_OK)
  {
    Error_Handler();
  }
  sClockSourceConfig.ClockSource = TIM_CLOCKSOURCE_INTERNAL;
  if (HAL_TIM_ConfigClockSource(&htim1, &sClockSourceConfig) != HAL_OK)
  {
    Error_Handler();
  }
  if (HAL_TIM_PWM_Init(&htim1) != HAL_OK)
  {
    Error_Handler();
  }
  sMasterConfig.MasterOutputTrigger = TIM_TRGO_RESET;
  sMasterConfig.MasterSlaveMode = TIM_MASTERSLAVEMODE_DISABLE;
  if (HAL_TIMEx_MasterConfigSynchronization(&htim1, &sMasterConfig) != HAL_OK)
  {
    Error_Handler();
  }
  sConfigOC.OCMode = TIM_OCMODE_PWM1;
  sConfigOC.Pulse = 500;
  sConfigOC.OCPolarity = TIM_OCPOLARITY_HIGH;
  sConfigOC.OCNPolarity = TIM_OCNPOLARITY_HIGH;
  sConfigOC.OCFastMode = TIM_OCFAST_DISABLE;
  sConfigOC.OCIdleState = TIM_OCIDLESTATE_RESET;
  sConfigOC.OCNIdleState = TIM_OCNIDLESTATE_RESET;
  if (HAL_TIM_PWM_ConfigChannel(&htim1, &sConfigOC, TIM_CHANNEL_1) != HAL_OK)
  {
    Error_Handler();
  }
  sBreakDeadTimeConfig.OffStateRunMode = TIM_OSSR_DISABLE;
  sBreakDeadTimeConfig.OffStateIDLEMode = TIM_OSSI_DISABLE;
  sBreakDeadTimeConfig.LockLevel = TIM_LOCKLEVEL_OFF;
  sBreakDeadTimeConfig.DeadTime = 0;
  sBreakDeadTimeConfig.BreakState = TIM_BREAK_DISABLE;
  sBreakDeadTimeConfig.BreakPolarity = TIM_BREAKPOLARITY_HIGH;
  sBreakDeadTimeConfig.AutomaticOutput = TIM_AUTOMATICOUTPUT_DISABLE;
  if (HAL_TIMEx_ConfigBreakDeadTime(&htim1, &sBreakDeadTimeConfig) != HAL_OK)
  {
    Error_Handler();
  }
  /* USER CODE BEGIN TIM1_Init 2 */

  /* USER CODE END TIM1_Init 2 */
  HAL_TIM_MspPostInit(&htim1);

}

/**
  * @brief TIM6 Initialization Function
  * @param None
  * @retval None
  */
static void MX_TIM6_Init(void)
{

  /* USER CODE BEGIN TIM6_Init 0 */

  /* USER CODE END TIM6_Init 0 */

  TIM_MasterConfigTypeDef sMasterConfig = {0};

  /* USER CODE BEGIN TIM6_Init 1 */

  /* USER CODE END TIM6_Init 1 */
  htim6.Instance = TIM6;
  htim6.Init.Prescaler = 89;
  htim6.Init.CounterMode = TIM_COUNTERMODE_UP;
  htim6.Init.Period = 999;
  htim6.Init.AutoReloadPreload = TIM_AUTORELOAD_PRELOAD_DISABLE;
  if (HAL_TIM_Base_Init(&htim6) != HAL_OK)
  {
    Error_Handler();
  }
  sMasterConfig.MasterOutputTrigger = TIM_TRGO_RESET;
  sMasterConfig.MasterSlaveMode = TIM_MASTERSLAVEMODE_DISABLE;
  if (HAL_TIMEx_MasterConfigSynchronization(&htim6, &sMasterConfig) != HAL_OK)
  {
    Error_Handler();
  }
  /* USER CODE BEGIN TIM6_Init 2 */

  /* USER CODE END TIM6_Init 2 */

}

/**
  * @brief USART6 Initialization Function
  * @param None
  * @retval None
  */
static void MX_USART6_UART_Init(void)
{

  /* USER CODE BEGIN USART6_Init 0 */

  /* USER CODE END USART6_Init 0 */

  /* USER CODE BEGIN USART6_Init 1 */

  /* USER CODE END USART6_Init 1 */
  huart6.Instance = USART6;
  huart6.Init.BaudRate = 115200;
  huart6.Init.WordLength = UART_WORDLENGTH_8B;
  huart6.Init.StopBits = UART_STOPBITS_1;
  huart6.Init.Parity = UART_PARITY_NONE;
  huart6.Init.Mode = UART_MODE_TX_RX;
  huart6.Init.HwFlowCtl = UART_HWCONTROL_NONE;
  huart6.Init.OverSampling = UART_OVERSAMPLING_16;
  if (HAL_UART_Init(&huart6) != HAL_OK)
  {
    Error_Handler();
  }
  /* USER CODE BEGIN USART6_Init 2 */

  /* USER CODE END USART6_Init 2 */

}

/**
  * @brief GPIO Initialization Function
  * @param None
  * @retval None
  */
static void MX_GPIO_Init(void)
{
  GPIO_InitTypeDef GPIO_InitStruct = {0};

  /* GPIO Ports Clock Enable */
  __HAL_RCC_GPIOC_CLK_ENABLE();
  __HAL_RCC_GPIOH_CLK_ENABLE();
  __HAL_RCC_GPIOE_CLK_ENABLE();
  __HAL_RCC_GPIOD_CLK_ENABLE();
  __HAL_RCC_GPIOB_CLK_ENABLE();

  /*Configure GPIO pin Output Level */
  HAL_GPIO_WritePin(GPIOD, GPIO_PIN_13|GPIO_PIN_14|GPIO_PIN_15, GPIO_PIN_RESET);

  /*Configure GPIO pin : PC15 */
  GPIO_InitStruct.Pin = GPIO_PIN_15;
  GPIO_InitStruct.Mode = GPIO_MODE_INPUT;
  GPIO_InitStruct.Pull = GPIO_NOPULL;
  HAL_GPIO_Init(GPIOC, &GPIO_InitStruct);

  /*Configure GPIO pins : PD13 PD14 PD15 */
  GPIO_InitStruct.Pin = GPIO_PIN_13|GPIO_PIN_14|GPIO_PIN_15;
  GPIO_InitStruct.Mode = GPIO_MODE_OUTPUT_PP;
  GPIO_InitStruct.Pull = GPIO_NOPULL;
  GPIO_InitStruct.Speed = GPIO_SPEED_FREQ_LOW;
  HAL_GPIO_Init(GPIOD, &GPIO_InitStruct);

}

/* USER CODE BEGIN 4 */

/* USER CODE END 4 */

/**
  * @brief  This function is executed in case of error occurrence.
  * @retval None
  */
void Error_Handler(void)
{
  /* USER CODE BEGIN Error_Handler_Debug */
  /* User can add his own implementation to report the HAL error return state */

  /* USER CODE END Error_Handler_Debug */
}

#ifdef  USE_FULL_ASSERT
/**
  * @brief  Reports the name of the source file and the source line number
  *         where the assert_param error has occurred.
  * @param  file: pointer to the source file name
  * @param  line: assert_param error line source number
  * @retval None
  */
void assert_failed(uint8_t *file, uint32_t line)
{
  /* USER CODE BEGIN 6 */
  /* User can add his own implementation to report the file name and line number,
     tex: printf("Wrong parameters value: file %s on line %d\r\n", file, line) */
  /* USER CODE END 6 */
}
#endif /* USE_FULL_ASSERT */

/************************ (C) COPYRIGHT STMicroelectronics *****END OF FILE****/
