package model.enums;

public enum StatoPrenotazione {
    IN_ATTESA("in attesa"),
    CONFERMATA("confermata"),
    ANNULLATA("annullata");

    private final String label;

    StatoPrenotazione(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static StatoPrenotazione fromLabel(String label) {
        if (label == null) return null;
        String norm = label.trim();
        for (StatoPrenotazione s : values()) {
            if (s.label.equalsIgnoreCase(norm)) return s;
        }
        return StatoPrenotazione.valueOf(norm.toUpperCase().replace(' ', '_'));
    }
}
