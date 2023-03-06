# Облачные и туманные вычисления
Полностью лабораторную работу к сожалению по соображениям не могу предоставить.  

+ [Экзамен](./docs/Oblachki_zache.pdf)

Здесь содержаться основные способы взаимодействия с Yandex Cloud через Python.  
Методы находятся вот здесь: [\*ТЫК*](./yandexCloudUtils.py)

## Очереди
Получение сообщений из очереди:
```python
message = get_ymq_queue(with_queue=True, url=YMQ_QUEUE_URL).receive_messages(MaxNumberOfMessages=1, WaitTimeSeconds=5)
```

Чтение body сообщения:
```python
body = json.loads(message[0].body)
```

Удаление сообщений из очереди:
```python
for m in message:
    get_ymq_queue().Message(queue_url=YMQ_QUEUE_URL, receipt_handle=m.receipt_handle).delete()
```

## Хранилище (Storage)
Создание клиентского соединения
```pyton
client = get_storage_client()
```

Загрузка и скачивание файлов в хранилище:
```python
client.upload_file("./README.md", BUCKET, OBJ_NAME)
client.download_file(BUCKET, "README.md", "test.md")
```

Получение временной ссылки для взаимодействия с обьектов в хранилище:
```python
client.generate_presigned_url('get_object', Params={"Bucket": BUCKET, "Key": OBJ_NAME}, ExpiresIn=3600)
```

## Взаимодействие с БД
[Пример](./db.py)
