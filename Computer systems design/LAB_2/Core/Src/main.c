/* USER CODE BEGIN Header */
/**
 ******************************************************************************
 * @file           : main.c
 * @brief          : Main program body
 ******************************************************************************
 * @attention
 *
 * <h2><center>&copy; Copyright (c) 2021 STMicroelectronics.
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
#include "usart.h"
#include "gpio.h"

/* Private includes ----------------------------------------------------------*/
/* USER CODE BEGIN Includes */

#include <stdbool.h>
#include <string.h>
#include <ctype.h>
#include <stdio.h>
#include <stdarg.h>
#include <usart.h>
#include <ctype.h>

/* USER CODE END Includes */

/* Private typedef -----------------------------------------------------------*/
/* USER CODE BEGIN PTD */

/* USER CODE END PTD */

/* Private define ------------------------------------------------------------*/
/* USER CODE BEGIN PD */
#define UART_TIMEOUT 10
#define BUF_SIZE 1024
/* USER CODE END PD */

/* Private macro -------------------------------------------------------------*/
/* USER CODE BEGIN PM */

/* USER CODE END PM */

/* Private variables ---------------------------------------------------------*/

/* USER CODE BEGIN PV */

/* USER CODE END PV */

/* Private function prototypes -----------------------------------------------*/
void SystemClock_Config(void);
/* USER CODE BEGIN PFP */

/* USER CODE END PFP */

/* Private user code ---------------------------------------------------------*/
/* USER CODE BEGIN 0 */
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

static void buf_push(RingBuffer *buf, char *el) {
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

static bool buf_pop(RingBuffer *buf, /* out */ char *el) {
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

static struct RingBuffer ringBuffer;
static struct RingBuffer ringBufferTx;

static char el[2] = {"\0\0"};

static bool is_button_active() {
    return HAL_GPIO_ReadPin(GPIOC, GPIO_PIN_15) == GPIO_PIN_RESET;
}

static void set_green_led(bool on) { HAL_GPIO_WritePin(GPIOD, GPIO_PIN_13, on ? GPIO_PIN_SET : GPIO_PIN_RESET); }

static void set_yellow_led(bool on) { HAL_GPIO_WritePin(GPIOD, GPIO_PIN_14, on ? GPIO_PIN_SET : GPIO_PIN_RESET); }

static void set_red_led(bool on) { HAL_GPIO_WritePin(GPIOD, GPIO_PIN_15, on ? GPIO_PIN_SET : GPIO_PIN_RESET); }


struct ButtonState {
    bool is_pressed;
    bool signaled;
    uint32_t press_start_time;
};

static bool update_button_state(struct ButtonState *state) {
    if (state->is_pressed) {
        state->is_pressed = is_button_active();

        if (state->signaled) {
            return false;
        }

        if ((HAL_GetTick() - state->press_start_time) > 20 /* ms */) {
            state->signaled = true;
            return true;
        }
        return false;
    }

    if (is_button_active()) {
        state->press_start_time = HAL_GetTick();
        state->is_pressed = true;
        state->signaled = false;
    }

    return false;
}

struct Status {
    bool interrupt_enable;
    uint32_t pmask;
};

static struct Status status;

bool transmit_busy = false;

void enable_interrupt(struct Status *status) {
	HAL_NVIC_EnableIRQ(USART6_IRQn);
	status->interrupt_enable = true;
}

void disable_interrupt(struct Status *status) {
	HAL_UART_AbortReceive(&huart6);
	HAL_NVIC_DisableIRQ(USART6_IRQn);
    status->interrupt_enable = false;
}

void transmit_uart(const struct Status *status, char *buf, size_t size) {
  if (status->interrupt_enable) {
    if (transmit_busy) {
      buf_push(&ringBufferTx, buf);
    } else {
      HAL_UART_Transmit_IT(&huart6, buf, size);
      transmit_busy = true;
    }
    return;
  }
  HAL_UART_Transmit(&huart6, buf, size, 100);
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
  case HAL_OK: {
    buf_push(&ringBuffer, el);
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
  buf_push(&ringBuffer, el);
  transmit_uart(&status, el, 1);
}

void HAL_UART_TxCpltCallback(UART_HandleTypeDef *huart) {
  char buf[1024];
  if (buf_pop(&ringBufferTx, buf)) {
    HAL_UART_Transmit_IT(&huart6, buf, strlen(buf));
  } else {
    transmit_busy = false;
  }
}

enum ValueDefenition {
    LeftValue,
	LeftValueWithOperand,
    RightValue,
	RightValueWithEquals,
    ResultValue,
    ErrorValue
};

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
  MX_USART6_UART_Init();
  /* USER CODE BEGIN 2 */

  /* USER CODE END 2 */



  /* Infinite loop */
  /* USER CODE BEGIN WHILE */
    char interrupt_enabled_msg[] = {"Interrupts ON"};
    char interrupt_disabled_msg[] = {"Interrupts OFF"};
    char error_msg[] = {"\r\nerror\n"};

    struct ButtonState buttonState = {.press_start_time = 0, .signaled = false, .is_pressed = false};

    enable_interrupt(&status);
    buf_init(&ringBuffer);

    uint32_t arg1 = 0;
    uint32_t arg2 = 0;
    int64_t res = 0;
    char res_str [8];
    char op;
    enum ValueDefenition exprState = LeftValue;
    set_red_led(false);

    while (1) {
    	if (update_button_state(&buttonState)) {
    	            if (status.interrupt_enable) {
    	                disable_interrupt(&status);
    	                transmit_uart_nl(&status, interrupt_disabled_msg, sizeof(interrupt_disabled_msg));
    	            } else {
    	                enable_interrupt(&status);
    	                transmit_uart_nl(&status, interrupt_enabled_msg, sizeof(interrupt_enabled_msg));
    	            }
    	        }

    	        receive_uart(&status);

    	        char c[2];

    	        if ((exprState != ResultValue && exprState != ErrorValue) && !buf_pop(&ringBuffer, c)) {
    	            continue;
    	        }

    	        switch (exprState) {
    	            case LeftValue: {
    	                if (!isdigit(c[0])) {
    	                	exprState = ErrorValue;
    	                    break;
    	                }
    	                exprState = LeftValueWithOperand;
    	                set_red_led(false);
    	                arg1 = arg1 * 10 + (c[0] - 48);
    	                uint16_t new_arg1 = (uint16_t) arg1;
    	                if (arg1 != new_arg1) {
    	                    exprState = ErrorValue;
    	                }
    	                break;
    	            }
    	            case LeftValueWithOperand: {
						if (!isdigit(c[0])) {
							switch (c[0]) {
								case '+':
								case '-':
								case '*':
								case '/': {
									op = c[0];
									exprState = RightValue;
									break;
								}
								default:
									exprState = ErrorValue;
									break;
							}
							break;
						}
						arg1 = arg1 * 10 + (c[0] - 48);
						uint16_t new_arg1 = (uint16_t) arg1;
						if (arg1 != new_arg1) {
							exprState = ErrorValue;
						}
						break;
					}
    	            case RightValue: {
    	                if (!isdigit(c[0])) {
    	                    exprState = ErrorValue;
    	                    break;
    	                }
    	                exprState = RightValueWithEquals;
    	                arg2 = arg2 * 10 + (c[0] - 48);
    	                uint16_t new_arg2 = (uint16_t) arg2;
    	                if (arg2 != new_arg2) {
    	                    exprState = ErrorValue;
    	                }
    	                break;
    	            }
    	            case RightValueWithEquals: {
						if (!isdigit(c[0])) {
							if (c[0] == '=') {
								exprState = ResultValue;
							} else {
								exprState = ErrorValue;
							}
							break;
						}
						arg2 = arg2 * 10 + (c[0] - 48);
						uint16_t new_arg2 = (uint16_t) arg2;
						if (arg2 != new_arg2) {
							exprState = ErrorValue;
						}
						break;
					}
    	            case ResultValue: {
    	            	int32_t arg1_c = arg1;
    	            	int32_t arg2_c = arg2;
    	                switch (op) {
    	                    case '+':
    	                        res = arg1_c + arg2_c;
    	                        break;
    	                    case '-':
    	                        res = arg1_c - arg2_c;
    	                        break;
    	                    case '*':
    	                        res = arg1_c * arg2_c;
    	                        break;
    	                    case '/':{
    	                    	if (arg2_c == 0) {
    	                    		exprState = ErrorValue;
    	                    		continue;
    	                    	} else {
    	                    		res = arg1_c / arg2_c;
    	                    	}
    	                        break;
    	                    }
    	                    default:
    	                        break;
    	                }

    	                int16_t actual_res = res;
    	                if (actual_res != res) {
    	                    exprState = ErrorValue;
    	                    break;
    	                }

    	                sprintf(res_str, "%d", res);
    	                transmit_uart_nl(&status, res_str, strlen(res_str));
    	                arg1 = 0;
    	                arg2 = 0;
    	                res = 0;
    	                exprState = LeftValue;
    	                break;
    	            }
    	            case ErrorValue: {
    	                arg1 = 0;
    	                arg2 = 0;
    	                res = 0;
    	                set_red_led(true);
    	                transmit_uart_nl(&status, error_msg, sizeof(error_msg));
    	                exprState = LeftValue;
    	                break;
    	            }
    	            default:
    	                break;
    	        }
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
	__HAL_PWR_VOLTAGESCALING_CONFIG(PWR_REGULATOR_VOLTAGE_SCALE3);
	/** Initializes the RCC Oscillators according to the specified parameters
	 * in the RCC_OscInitTypeDef structure.
	 */
	RCC_OscInitStruct.OscillatorType = RCC_OSCILLATORTYPE_HSI;
	RCC_OscInitStruct.HSIState = RCC_HSI_ON;
	RCC_OscInitStruct.HSICalibrationValue = RCC_HSICALIBRATION_DEFAULT;
	RCC_OscInitStruct.PLL.PLLState = RCC_PLL_NONE;
	if (HAL_RCC_OscConfig(&RCC_OscInitStruct) != HAL_OK)
	{
		Error_Handler();
	}
	/** Initializes the CPU, AHB and APB buses clocks
	 */
	RCC_ClkInitStruct.ClockType = RCC_CLOCKTYPE_HCLK|RCC_CLOCKTYPE_SYSCLK
			|RCC_CLOCKTYPE_PCLK1|RCC_CLOCKTYPE_PCLK2;
	RCC_ClkInitStruct.SYSCLKSource = RCC_SYSCLKSOURCE_HSI;
	RCC_ClkInitStruct.AHBCLKDivider = RCC_SYSCLK_DIV1;
	RCC_ClkInitStruct.APB1CLKDivider = RCC_HCLK_DIV1;
	RCC_ClkInitStruct.APB2CLKDivider = RCC_HCLK_DIV1;

	if (HAL_RCC_ClockConfig(&RCC_ClkInitStruct, FLASH_LATENCY_0) != HAL_OK)
	{
		Error_Handler();
	}
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
