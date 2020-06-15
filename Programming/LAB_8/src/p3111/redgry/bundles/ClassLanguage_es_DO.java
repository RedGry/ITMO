package p3111.redgry.bundles;

import java.util.ListResourceBundle;

public class ClassLanguage_es_DO extends ListResourceBundle {
    private static final Object[][] contents = {
            {"hello", "Hola!"},
            {"windowClose", "Cuadro de mensaje cerrado"},
            {"err_login", "Nombre de usuario o contraseña incorrectos! \nInténtalo de nuevo"},
            {"suc_login", "Has iniciado sesión correctamente!"},
            {"suc_register", "Estás registrado! \nIntenta entrar."},
            {"err_register", "El usuario con este apodo ya ha sido creado! \nInténtalo de nuevo"},
            {"Entry", "Entrada"},
            {"Info", "Información"},
            {"HairColor", "Color de pelo"},
            {"update", "Actualización"},
            {"WriteId", "Introduzca el ID"},
            {"uPick", "Usted ha elegido"},
            {"PersonCreatedByUser", "Person creado por el usuario: "},
            {"with", "\n con "},
            {"Pname", "\n{Nombre: '"},
            {"cooridanateX", ",\n Coordenadas {x= "},
            {"Pheight", "},\n Crecimiento: "},
            {"Pbirthday", ",\n Cumpleaños: "},
            {"PpassportID", ",\n ID de pasaporte: "},
            {"PhairColor", ",\n Color de pelo: "},
            {"LocationName", ",\n Nombre de la ubicación: "},
            {"LocationX", ",\n Coordenadas de ubicación{ x= "},

            //Блок arg.fxml:
            {"InputData", "Datos de entrada"},
            {"count_less_number", "Introduzca el V"},
            {"input_id", "Introduzca el ID"},
            {"delete_id", "Eliminar objeto"},
            {"input_birthday", "Introduzca la fecha YYYY-MM-DD"},
            {"add", "Añadir objeto"},

            //Блок authorization.fxml:
            {"selectLanguage", "Seleccionar idioma"},
            {"registrationLogin", "Registro / Inicio De Sesión"},
            {"loginBut", "entrar"},
            {"regBut", "registro"},

            //Блок personDialog.fxml:
            {"parameters_person", "Introduzca las opciones Person"},



            //Блок команд:
            {"suc_help", "remove_greater_key: Eliminar de la colección todos los elementos cuya clave exceda la especificada.\n" +
                    "clear: Borrar la colección (elimina los elementos que le pertenecen de la colección).\n" +
                    "show: Genere todos los elementos de la colección en la vista de cadena en el flujo de salida estándar.\n" +
                    "insert: Añadir un nuevo elemento con la clave especificada.\n" +
                    "update: Actualizar el valor de un elemento de colección cuyo id es igual al especificado.\n" +
                    "history: Muestra los últimos 15 comandos (sin argumentos).\n" +
                    "remove_greater: Eliminar de la colección todos los elementos que excedan el especificado.\n" +
                    "print_ascending: Muestra el ID de los elementos de la colección en orden ascendente.\n" +
                    "count_less_than_location: Muestra el número de elementos cuyo valor de campo location es menor que el especificado.\n" +
                    "exit: Cierre del programa sin guardar.\n" +
                    "help: Muestra la ayuda de los comandos disponibles.\n" +
                    "remove_any_by_birthday: Eliminar de la colección un elemento cuyo valor de campo birthday sea equivalente al especificado. (Formato de fecha: YYYY-MM-DD)\n" +
                    "remove_key: Eliminar un elemento de la colección por su ID.\n" +
                    "info: Muestra la información de la colección en el flujo estándar."},
            {"suc_info", "Información: "},
            {"count_less", "Los elementos de la colección son más pequeños por el producto de coordenadas de ubicación de un número determinado: "},
            {"collection_empty", "La colección está vacía!"},
            {"print_ascending", "ID de colección en orden ascendente:\n"},
            {"suc_insert", "Personaje añadido con éxito!"},
            {"err_insert", "El personaje ya existe con la clave dada."},
            {"suc_update", "El personaje se ha actualizado con éxito con ID: "},
            {"err_updateUser", "El personaje no te pertenece con ID: "},
            {"err_update", "Este personaje no se puede actualizar porque no está en la colección."},
            {"suc_clear", "Colección limpiada con éxito! \nTodos los elementos que pertenecen a usted se han eliminado!"},
            {"suc_remove", "Personaje eliminado de ID: "},
            {"err_remove", "El personaje no se encuentra o no te pertenece conID: "},
            {"suc_history", "Historia de los equipos:\n"},
            {"err_removeBirthday", "No hay personajes con una fecha de nacimiento determinada en la colección."},
            {"suc_removeBirthday", "De la colección se han eliminado todos los personajes con la fecha de nacimiento especificada que pertenecen a usted! \n"},
            {"err_greater", "El personaje ya existe con el ID dado."},
            {"error_greater", "En la colección no hay personajes superioresID: "},
            {"suc_greater", "Los personajes que pertenecen a usted se eliminan con el exceso ID: "},

            //Блок Ошибок:
            {"Error", "Error"},
            {"error_arg", "El comando no tenía un argumento o era un formato incorrecto!"},
            {"InvalidData", "Datos de comando incorrectos!"},
            {"IncorrectField", "Campos llenos incorrectamente!"},
            {"IncorrectNumber", "El número está fuera del rango!"},
            {"IncorrectDataField", "Campo lleno incorrectamente!"},
            {"ErrorCreatingObject", "Error al crear el objeto"},
            {"UnexpectedException", "Excepción inesperada"},
            {"incorrectLogPas", "Nombre de Usuario o Contraseña incorrectos."},
            {"uncorrectLogPas", "Introduzca otro nombre de usuario o contraseña!"},
            {"idNotFound", "No se encontró ningún personaje con ID dado!"},
            {"error_argBirthday", "Formato de fecha incorrecto! \nFormato de fecha: YYYY-MM-DD"},


            //Блок main.fxml:
            {"file", "Fichero"},
            {"exit", "Salida"},

            {"help", "Asistencia"},
            {"info", "Información de la colección"},
            {"helpCom", "Información del equipo"},

            {"language", "Seleccionar idioma"},

            {"username", "Usuario: "},
            {"updateTable", "Actualizar valores de tabla"},

            {"addCommands", "Agregar:"},
            {"insertCom", "Añadir Carácter"},

            {"removeCommands", "Comandos De Eliminación:"},
            {"removeID", "Eliminar por ID"},
            {"removeBirthday", "Eliminar por Cumpleaños"},
            {"removeGreater", "Eliminar Caracteres grandes"},
            {"removeGreaterID", "Eliminar ID grandes"},
            {"clearCom", "Limpiar"},

            {"updateCommands", "Comandos De Actualización:"},
            {"updateCom", "Renovar"},

            {"printCommands", "Comandos De Salida:"},
            {"countLessCom", "Retirar cantidad <V Localización"},
            {"printCom", "ID de Caracteres ascendente"},
            {"historyCom", "Historia de los equipos"},

            {"tabPerson", "Tabla Person"},
            {"tabVisual", "Área de visualización"}
    };


    @Override
    protected Object[][] getContents() {
        return contents;
    }
}
