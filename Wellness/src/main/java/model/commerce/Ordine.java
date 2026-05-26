package model.commerce;

import model.enums.StatoOrdine;

import java.math.BigDecimal;

public class Ordine {
    private BigDecimal totale;
    private StatoOrdine stato;
    private Pagamento pagamento;
}
