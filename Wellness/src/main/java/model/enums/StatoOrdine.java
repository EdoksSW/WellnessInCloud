package model.enums;

public enum StatoOrdine {
    IN_ELABORAZIONE("in elaborazione"),
    PAGATO("pagato"),
    SPEDITO("spedito"),
    CONSEGNATO("consegnato"),
    ANNULLATO("annullato");

    private final String label;

    StatoOrdine(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static StatoOrdine fromLabel(String label) {
        if (label == null) return null;
        String norm = label.trim();
        for (StatoOrdine s : values()) {
            if (s.label.equalsIgnoreCase(norm)) return s;
        }
        return StatoOrdine.valueOf(norm.toUpperCase().replace(' ', '_'));
    }
}
