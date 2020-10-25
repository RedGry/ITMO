package servlets;

import javax.servlet.http.HttpServletRequest;

// Класс для изменения разделителя чисел

public class Utils {
    public static double getDoubleParameter(HttpServletRequest request, String parameter) {
        return Double.parseDouble(request.getParameter(parameter).replace(",", "."));
    }
}