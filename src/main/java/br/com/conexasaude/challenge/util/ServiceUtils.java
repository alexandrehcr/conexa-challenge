package br.com.conexasaude.challenge.util;

public class ServiceUtils {
    public static String removeCpfMask(String string) {
        return string.replaceAll("[ .-]", "");
    }
    public static String removePhoneMask(String string) {
        return string.replaceAll("[ ()-]", "");
    }
}
