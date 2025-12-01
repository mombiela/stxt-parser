package dev.stxt.parser.utils;

import java.util.Locale;

/**
 * Clase utilitaria que proporciona métodos para la manipulación y normalización de cadenas de texto.
 * <p>
 * Esta clase contiene métodos estáticos que facilitan operaciones comunes sobre cadenas,
 * como la normalización de nombres y la limpieza de caracteres de espacios en blanco.
 * </p>
 * 
 * No debe instanciarse; para reforzarlo se declara un constructor privado.
 * 
 * @author Joan Costa Mombiela
 * @version 1.0
 */
public class Utils
{
    /**
     * Constructor privado para evitar la instanciación de la clase.
     * Las utilidades se deben usar únicamente mediante sus métodos estáticos.
     */
    private Utils() 
    {
        // Evita instanciación
    }

    /**
     * Normaliza una cadena de texto eliminando espacios y convirtiéndola a minúsculas.
     * <p>
     * Este método realiza las siguientes operaciones:
     * <ul>
     *   <li>Elimina espacios en blanco al principio y al final de la cadena ({@code trim()})</li>
     *   <li>Convierte todos los caracteres a minúsculas utilizando la configuración regional inglesa</li>
     * </ul>
     * Se utiliza {@link Locale#ENGLISH} para garantizar un comportamiento consistente
     * independientemente de la configuración regional del sistema, evitando problemas
     * con caracteres especiales como la "I" turca.
     * </p>
     * 
     * @param name la cadena de texto a normalizar
     * @return la cadena normalizada (sin espacios laterales y en minúsculas)
     * @throws NullPointerException si {@code name} es {@code null}
     */
    public static String uniform(String name)
    {
        return name.trim().toLowerCase(Locale.ENGLISH);
    }

    /**
     * Limpia una cadena de texto eliminando todos los caracteres de espacio en blanco.
     * <p>
     * Este método elimina los siguientes caracteres:
     * <ul>
     *   <li>Retornos de carro ({@code \r})</li>
     *   <li>Saltos de línea ({@code \n})</li>
     *   <li>Tabulaciones ({@code \t})</li>
     *   <li>Espacios en blanco simples y múltiples</li>
     * </ul>
     * El resultado es una cadena sin ningún tipo de carácter de espaciado.
     * </p>
     * 
     * <p><strong>Ejemplo:</strong></p>
     * <pre>
     * String texto = "Hola\n  Mundo\t";
     * String resultado = Utils.cleanupString(texto);
     * // resultado = "HolaMundo"
     * </pre>
     * 
     * @param input la cadena de texto a limpiar
     * @return la cadena sin caracteres de espacio en blanco
     * @throws NullPointerException si {@code input} es {@code null}
     */
    public static String cleanupString(String input)
    {
        return input.replaceAll("[\\r\\n\\t]+|\\s+", "");
    }
}