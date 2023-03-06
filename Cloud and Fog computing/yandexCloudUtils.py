import json
import os
import uuid
import pandas as pd

import boto3
import yandexcloud

from yandex.cloud.lockbox.v1.payload_service_pb2 import GetPayloadRequest
from yandex.cloud.lockbox.v1.payload_service_pb2_grpc import PayloadServiceStub

boto_session = None
storage_client = None
docapi_table = None
ymq_queue = None


def get_boto_session():
    global boto_session
    if boto_session is not None:
        return boto_session

    # extract values from secret
    access_key = os.environ["ACCESS_KEY"]
    secret_key = os.environ["SECRET_KEY"]

    if access_key is None or secret_key is None:
        raise Exception("secrets required")
    # print("Key id: " + access_key)

    # initialize boto session
    boto_session = boto3.session.Session(
        aws_access_key_id=access_key,
        aws_secret_access_key=secret_key
    )
    return boto_session


def get_ymq_queue(with_queue=False, queue_name=os.environ["YMQ_QUEUE_URL"]):
    ymq_queue = None

    if with_queue:
        ymq_queue = get_boto_session().resource(
            service_name='sqs',
            endpoint_url='https://message-queue.api.cloud.yandex.net',
            region_name='ru-central1'
        ).Queue(queue_name)
    else:
        ymq_queue = get_boto_session().resource(
            service_name='sqs',
            endpoint_url='https://message-queue.api.cloud.yandex.net',
            region_name='ru-central1'
        )
    return ymq_queue


def get_storage_client():
    global storage_client
    if storage_client is not None:
        return storage_client

    storage_client = get_boto_session().client(
        service_name='s3',
        endpoint_url='https://storage.yandexcloud.net',
        region_name='ru-central1'
    )
    return storage_client
