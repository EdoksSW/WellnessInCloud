package model.commerce;

import java.math.BigDecimal;

public class Pagamento {
    private BigDecimal importo;
    private String metodo;
    private int id_pagamento;

    
    public Pagamento(){}
    
    public Pagamento(BigDecimal importo, String metodo, int id_pagamento) {
        this.importo = importo;
        this.metodo = metodo;
        this.id_pagamento = id_pagamento;
    }

    public BigDecimal getImporto() {
        return importo;
    }

    public void setImporto(BigDecimal importo) {
        this.importo = importo;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public int getId_pagamento() {
        return id_pagamento;
    }

    public void setId_pagamento(int id_pagamento) {
        this.id_pagamento = id_pagamento;
    }

    public void set() {
    }
}
