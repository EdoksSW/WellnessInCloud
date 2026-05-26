package model.commerce;

import model.enums.TipoCertificato;

import java.time.LocalDate;

public class CertificatoMedico {
    private LocalDate dataEmissione, dataScadenza;
    private TipoCertificato tipo;
    private String percorsoFile;
}
