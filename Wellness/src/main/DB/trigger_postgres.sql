-- ============================================================
--  1) TOTALE ORDINE
--  Ricalcola ordine.totale a ogni modifica delle righe d'ordine.
-- ============================================================
CREATE OR REPLACE FUNCTION aggiorna_totale_ordine() RETURNS trigger AS $$
DECLARE
    v_ord integer := COALESCE(NEW.id_ordine, OLD.id_ordine);
BEGIN
    UPDATE ordine
    SET totale = COALESCE((
        SELECT SUM(od.quantita * p.prezzo)
        FROM ordine_dettaglio od
        JOIN prodotto p ON p.id_prodotto = od.id_prodotto
        WHERE od.id_ordine = v_ord
    ), 0)
    WHERE id_ordine = v_ord;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_totale_ordine ON ordine_dettaglio;
CREATE TRIGGER trg_totale_ordine
AFTER INSERT OR UPDATE OR DELETE ON ordine_dettaglio
FOR EACH ROW EXECUTE FUNCTION aggiorna_totale_ordine();


-- ============================================================
--  2) GIACENZA PRODOTTO
--  Scarica/ripristina la scorta e vieta di scendere sotto zero.
-- ============================================================
CREATE OR REPLACE FUNCTION gestisci_giacenza() RETURNS trigger AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE prodotto SET giacenza = giacenza - NEW.quantita
        WHERE id_prodotto = NEW.id_prodotto;
    ELSIF TG_OP = 'DELETE' THEN
        UPDATE prodotto SET giacenza = giacenza + OLD.quantita
        WHERE id_prodotto = OLD.id_prodotto;
    ELSE  -- UPDATE
        UPDATE prodotto SET giacenza = giacenza + OLD.quantita - NEW.quantita
        WHERE id_prodotto = NEW.id_prodotto;
    END IF;

    IF (SELECT giacenza FROM prodotto
        WHERE id_prodotto = COALESCE(NEW.id_prodotto, OLD.id_prodotto)) < 0 THEN
        RAISE EXCEPTION 'Giacenza insufficiente per il prodotto %',
            COALESCE(NEW.id_prodotto, OLD.id_prodotto);
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_giacenza ON ordine_dettaglio;
CREATE TRIGGER trg_giacenza
AFTER INSERT OR UPDATE OR DELETE ON ordine_dettaglio
FOR EACH ROW EXECUTE FUNCTION gestisci_giacenza();


-- ============================================================
--  3) TOTALITA' DELLA GENERALIZZAZIONE
--  Ogni utente deve essere almeno admin, cliente o staff.
--  Constraint trigger differito: il controllo scatta al COMMIT,
--  cosi utente + sottotipo possono stare nella stessa transazione.
-- ============================================================
CREATE OR REPLACE FUNCTION check_totalita_utente() RETURNS trigger AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM admin   WHERE codice_fiscale = NEW.codice_fiscale)
   AND NOT EXISTS (SELECT 1 FROM cliente WHERE codice_fiscale = NEW.codice_fiscale)
   AND NOT EXISTS (SELECT 1 FROM staff   WHERE codice_fiscale = NEW.codice_fiscale) THEN
        RAISE EXCEPTION 'Utente % deve essere almeno Admin, Cliente o Staff', NEW.codice_fiscale;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_totalita_utente ON utente;
CREATE CONSTRAINT TRIGGER trg_totalita_utente
AFTER INSERT OR UPDATE ON utente
DEFERRABLE INITIALLY DEFERRED
FOR EACH ROW EXECUTE FUNCTION check_totalita_utente();
