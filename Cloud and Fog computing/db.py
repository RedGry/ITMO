from datetime import datetime
import os

import psycopg2
from psycopg2 import Error


def db_save_user(data):
    id = None
    try:
        connection = psycopg2.connect(user=os.environ["DB_USER"],
                                      password=os.environ["DB_PASSWORD"],
                                      host=os.environ["DB_HOST"],
                                      port=os.environ["DB_PORT"],
                                      database=os.environ["DB_NAME"],
                                      sslmode="verify-full",
                                      target_session_attrs="read-write"
                                      )
        cursor = connection.cursor()
        connection.autocommit = True

        insert = (f"INSERT INTO users2 ...;")
        cursor.execute(insert)

        cursor.execute(
            f"SELECT id FROM ....;")
        id = cursor.fetchone()[0]

    except (Exception, Error) as error:
        print("PostgreSQL error: ", error)
    finally:
        if connection:
            cursor.close()
            connection.close()
    return id