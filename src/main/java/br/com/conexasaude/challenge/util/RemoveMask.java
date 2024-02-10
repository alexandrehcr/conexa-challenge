package br.com.conexasaude.challenge.util;

public record RemoveMask () {
    public static String removeCpfMask(String string) {
        return string.replaceAll("[ .-]", "");
    }
    public static String removePhoneMask(String string) {
        return string.replaceAll("[ ()-]", "");
    }
}
