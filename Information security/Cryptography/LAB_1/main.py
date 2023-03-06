from collections import Counter
from tabulate import tabulate


# Считывает текст из файла
def read_file():
    while True:
        path = input("Введите путь к файлу: ").strip()
        try:
            with open(path, encoding='utf-8') as f:
                data = f.readlines()
                return data[0]
        except FileNotFoundError:
            print("Файл не найден!", '\n')
        except ValueError:
            print("Неправильный формат файла!", '\n')


# Проверяет какой язык используется
def check_lang(plaintext, lang_1, lang_2):
    num = 0
    spec_character = ",.:;'\"-?!/1234567890[]= …“—«»”*&^(){}"
    upper_text = ""
    answer = []
    for letter in plaintext:
        upper_text += letter.upper()
        if spec_character.find(letter) != -1:
            continue
        num = lang_1.find(letter.upper())
    if num != -1:
        answer.append("EN")

    for letter in plaintext:
        if spec_character.find(letter) != -1:
            continue
        num = lang_2.find(letter.upper())
    if num != -1:
        answer.append("RU")
    answer.append(upper_text)
    return answer


def encrypt(plaintext, key, ALPHABET):
    result_text = ""
    key_ind = 0

    for letter in plaintext:
        num = ALPHABET.find(letter.upper())
        if num != -1:
            num = (ALPHABET.find(letter.upper()) + ALPHABET.find(key[key_ind % len(key)])) % len(ALPHABET)
            key_ind += 1
            if letter.islower():
                result_text += ALPHABET[num].lower()
            else:
                result_text += ALPHABET[num]
        else:
            result_text += letter

    return result_text


def decrypt(ciphertext, key, ALPHABET):
    result_text = ""
    key_ind = 0

    for letter in ciphertext:
        num = ALPHABET.find(letter.upper())
        if num != -1:
            num = (ALPHABET.find(letter.upper()) - ALPHABET.find(key[key_ind % len(key)])) % len(ALPHABET)
            key_ind += 1
            if letter.islower():
                result_text += ALPHABET[num].lower()
            else:
                result_text += ALPHABET[num]
        else:
            result_text += letter

    return result_text


# Формируем словарь сопоставляющий по частоте буквы в тексте и в языке
def mapping_dict(counter, DICT):
    decrypt_arr = []

    # Упорядочиваем по частоте появление букв в тексте
    for i in range(len(counter)):
        decrypt_arr.append(counter.most_common(len(counter))[i][0])
    return dict(zip(decrypt_arr, DICT))


# Простой частотный анализ
def freq_analysis(ciphertext, ALPHABET, DICT):
    spec_character = ",.:;'\"-?!/1234567890[]= …“—«»”*&^(){}"
    modified_ciphertext = "".join([(char if char not in spec_character else '') for char in ciphertext])
    modified_ciphertext = modified_ciphertext.replace('\n', '')

    chars_counter = Counter(modified_ciphertext.upper())

    amount = 0
    for i in range(len(ALPHABET)):
        amount += chars_counter[ALPHABET[i]]

    table = []
    for i in range(len(ALPHABET)):
        table.append([ALPHABET[i], chars_counter[ALPHABET[i]], round(chars_counter[ALPHABET[i]] / amount * 100, 3)])

    print("\n Частотный анализ:")
    print(tabulate(table, headers=["Буква", "Количество", "Частота"], tablefmt="grid", floatfmt="2.5f"), "\n")

    new_DICT = mapping_dict(chars_counter, DICT)

    # Если в исходном зашифрованном тексте встречается символ, то сохраняется, иначе буква сопоставляется по словарю
    new_result = ""
    j = 0
    for i in ciphertext:
        num = ALPHABET.find(i.upper())
        if num != -1:
            if ciphertext[j].islower():
                new_result += (new_DICT.get(modified_ciphertext[j].upper())).lower()
            else:
                new_result += new_DICT.get(modified_ciphertext[j].upper())
            j += 1
        else:
            new_result += i

    return new_result


# Запускает основную программу
if __name__ == '__main__':
    # Алфавит для сдвига
    ALPHABET_EN = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    ALPHABET_RU = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"

    # Упорядоченные по частоте появления в языке буквы
    DICT_EN = [*'ETAOINSHRDLCUMWFGYPBVKJXQZ']   # ETAOINSRHDLUCMFYWGPBVKXQJZ
    DICT_RU = [*'ОЕЁАИТНСРВЛКМДПУЯЫГЗБЧЙХЪЖЬЮШЦЩЭФ']

    mode = ""
    key = ""
    key_lang = ""
    ALPHABET = ALPHABET_RU
    DICT = DICT_RU

    # Считываем текст с файла
    text = read_file()
    text_lang = check_lang(text, ALPHABET_EN, ALPHABET_RU)[0]

    print_text = text.replace(". ", ".\n").replace("! ", "!\n").replace("? ", "?\n")
    print(f'Исходный текст: {print_text} \n')

    # Выбираем режим Шифрование / Расшифрование
    while True:
        mode = input("Введите 'enc' для шифрования или 'dec' для расшифрования: ")
        if mode == "enc" or mode == "dec":
            break
        print("Выбран неправильный режим!", '\n')

    key = input("Введите ключ: ")
    key_lang, new_key = check_lang(key, ALPHABET_EN, ALPHABET_RU)

    # Проверяем языки ключа и исходного текста
    if key_lang != text_lang:
        print("Язык ключа и текста не совпадают, попробуйте снова!")
        print(f"key_lang: {key_lang} != {text_lang} :text_lang")
        exit(-1)

    if text_lang == "EN":
        ALPHABET = ALPHABET_EN
        DICT = DICT_EN

    # Шифрование и расшифрование исходного текста
    result = encrypt(text, new_key, ALPHABET) if mode == "enc" else decrypt(text, new_key, ALPHABET)
    print_result = result.replace(". ", ".\n").replace("! ", "!\n").replace("? ", "?\n")
    print(f"Результат: {print_result} \n")

    # Если у нас было шифрование, тогда производим частотный анализ
    if mode == "enc":
        freq_result = freq_analysis(result, ALPHABET, DICT)
        print_freq_result = freq_result.replace(". ", ".\n").replace("! ", "!\n").replace("? ", "?\n")
        print(f"Частотный анализ результат: {print_freq_result}")

