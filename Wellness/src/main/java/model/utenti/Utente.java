package model.utenti;
import java.time.LocalDate;

public abstract class Utente {

    private String codiceFiscale, nome, cognome, email, telefono, password;
    private LocalDate dataNascita;
    private int eta;
    private String via;
    private int civico;
    private String cap;
    private String cartaFedelta;

    public Utente(String codiceFiscale, String nome, String cognome, String email, String telefono, String password,
                  LocalDate dataNascita, int eta, String via, int civico, String cap, String cartaFedelta) {
        this.codiceFiscale = codiceFiscale;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
        this.password = password;
        this.dataNascita = dataNascita;
        this.eta = eta;
        this.via = via;
        this.civico = civico;
        this.cap = cap;
        this.cartaFedelta = cartaFedelta;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getEta() {
        return eta;
    }

    public void setEta(int eta) {
        this.eta = eta;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public int getCivico() {
        return civico;
    }

    public void setCivico(int civico) {
        this.civico = civico;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getCartaFedelta() {
        return cartaFedelta;
    }

    public void setCartaFedelta(String cartaFedelta) {
        this.cartaFedelta = cartaFedelta;
    }
}
