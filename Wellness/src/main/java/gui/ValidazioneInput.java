package gui;

import java.util.regex.Pattern;

public class ValidazioneInput
{
    private static final String REGEX_NOME="^[a-zA-Z]{2,50}$";

    private static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

   private static final String REGEX_TELEFONO = "^[0-9]{9,11}$";

    private static final String REGEX_CODICE_FISCALE = "^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$";

    private static final String REGEX_CAP = "^[0-9]{5}$";

   private static final String REGEX_CIVICO = "^[0-9]+[a-zA-Z/]*$";

    private static final String REGEX_QUANTITA = "^[1-9][0-9]*$";

    private static final String REGEX_PREZZO = "^[0-9]+(\\.[0-9]{1,2})?$";

    private static final String REGEX_PASSWORD ="^[a-zA-Z0-9]{16}$";

    public static boolean isNomeCognomeValido(String testo) {
        if (testo == null || testo.trim().isEmpty()) return false;
        return Pattern.matches(REGEX_NOME, testo.trim());
    }

    public static boolean isEmailValida(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return Pattern.matches(REGEX_EMAIL, email.trim());
    }

    public static boolean isTelefonoValido(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) return false;
        return Pattern.matches(REGEX_TELEFONO, telefono.trim());
    }

    public static boolean isCodiceFiscaleValido(String cf) {
        if (cf == null || cf.trim().isEmpty()) return false;
        // Convertiamo in automatico in maiuscolo per evitare fallimenti dovuti al case-sensitive
        return Pattern.matches(REGEX_CODICE_FISCALE, cf.trim().toUpperCase());
    }

    public static boolean isCapValido(String cap) {
        if (cap == null || cap.trim().isEmpty()) return false;
        return Pattern.matches(REGEX_CAP, cap.trim());
    }

    public static boolean isCivicoValido(String civico) {
        if (civico == null || civico.trim().isEmpty()) return false;
        return Pattern.matches(REGEX_CIVICO, civico.trim());
    }

    public static boolean isQuantitaValida(String quantita) {
        if (quantita == null || quantita.trim().isEmpty()) return false;
        return Pattern.matches(REGEX_QUANTITA, quantita.trim());
    }

    public static boolean isPrezzoValido(String prezzo) {
        if (prezzo == null || prezzo.trim().isEmpty()) return false;
        return Pattern.matches(REGEX_PREZZO, prezzo.trim());
    }

    public static boolean isPasswordValida(String password) {
        if(password == null || password.trim().isEmpty()) return false;

        return Pattern.matches(REGEX_PASSWORD, password.trim());
    }
}
