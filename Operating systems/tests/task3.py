def solver(byte, sector, track, surface, records, byte_rec):
    byte_track = byte * sector
    byte_surface = byte_track * track
    byte_disk = byte_surface * surface
    file_size = records * byte_rec
    print(f'1 сектор: {byte} байт')
    print(f'1 дорожка: {byte} * {sector} = {byte_track} байт')
    print(f'1 поверхность: {track} * {byte_track} = {byte_surface} байт')
    print(f'1 диск: {surface} * {byte_surface} = {byte_disk} байт')
    print(f'Размер файла: {records} * {byte_rec} = {file_size} байт')
    
    records_in_one_sector = int(byte / byte_rec)
    
    # Количество секторов для файла:
    sectors_for_file = int(records / records_in_one_sector) + 1 if (records / records_in_one_sector) != int(records / records_in_one_sector) else int(records / records_in_one_sector)
    
    # Количество дорожек для файла:
    tracks_for_file = int(sectors_for_file / sector) + 1 if int(sectors_for_file / sector) != sectors_for_file / sector else int(sectors_for_file / sector)
    
    # Количество секторов в последней дорожке:
    sectors_on_last_track = sectors_for_file - int(sectors_for_file / sector) * sector
    
    # Какое полное количество байт (включая потери фрагментации) займет файл?
    real_size_file = sectors_for_file * byte
    
    
    
    ########################################
    ###                                  ###
    ###      \/      ОТВЕТЫ      \/      ###
    ###                                  ###
    ########################################
    
    print('\nРешение 1:')
    print(f'Какое общее количество секторов необходимо для хранения всех записей? Ответ: {sectors_for_file}')

    print('\nРешение 2:')
    print(f'Какой общее количество дорожек необходимо для хранения всех записей? Ответ: {tracks_for_file}')

    print('\nРешение 3:')
    print(f'Какое полное количество байт (включая потери фрагментации) займет файл? Ответ: {real_size_file}')
    
    print('\nРешение 4:')
    print(f'Сколько секторов будет занято на последней дорожке файла? Ответ: {sectors_on_last_track}')


byte = 1024   # (Кол-во байт на 1 секторе)
sector = 47   # (Кол-во секторов на 1 дорожке)
track = 1100   # (Кол-во дорожек на 1 поверхносте)
surface = 2   # (Кол-во поверхностей на 1 диске)
records = 33383     # (Кол-во записей)
byte_rec = 163     # (Кол-во байт для 1 записи)

solver(byte, sector, track, surface, records, byte_rec)

