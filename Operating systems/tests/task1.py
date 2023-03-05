def solver(cpu_time, io_time, total, threads):
    cycles = total / (cpu_time + io_time)
    total_cpu_time = cpu_time * cycles
    total_wait_time = io_time * cycles
    cpu = []
    wait = []
    ready = []
    total_time = []
    ready_before = []
    for i in range(threads):
        total_ready_time = (threads * cpu_time - (io_time + cpu_time)) * (cycles - 1) + i * cpu_time
        cpu.append(total_cpu_time)
        wait.append(total_wait_time)
        ready.append(total_ready_time)
        total_time.append(total_cpu_time + total_wait_time + total_ready_time)
        total = total_cpu_time + total_wait_time + total_ready_time
        if i != 0:
            ready_before.append(i * cpu_time)

        print(f'P{i+1}: '
              f'cpu_time=[{total_cpu_time}]; '
              f'wait_time=[{total_wait_time}]; '
              f'ready_time=[{total_ready_time}]; '
              f'total=[{total}]')
    print(f'Сколько времени потратит система до завершения всех процессов? Ответ: {max(total_time)}')
    print(f'Сколько времени потратит система до завершения первого процессов? Ответ: {min(total_time)}')
    print(f'Какое Максимальное время ожидания во всех очередях возможно для процесса? Ответ: {max(ready)}')
    print(f'Какое Минимальное время ожидания во всех очередях возможно для процесса? Ответ: {min(ready)}')
    print(f'Какое Максимальное время ожидания в очереди готовых к исполнению программы возможно для процесса? Ответ: {max(ready_before)}')
    print(f'Какое Минимальное время ожидания в очереди готовых к исполнению программы возможно для процесса? Ответ: {min(ready_before)}')


cpu_time = 5
io_time = 5
total = 100
threads = 4

solver(cpu_time, io_time, total, threads)