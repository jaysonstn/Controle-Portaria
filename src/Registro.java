import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Registro {
    private int id;
    private String tipoEntidade; // "Veiculo" ou "Colaborador"
    private String idEntidade; // Placa ou ID
    private String nomeModelo; // Nome ou Modelo
    private String tipoAcesso; // "Entrada" ou "Saída"
    private LocalDateTime dataHora;

    public Registro(String tipoEntidade, String idEntidade, String nomeModelo, String tipoAcesso, LocalDateTime dataHora) {
        this.tipoEntidade = tipoEntidade;
        this.idEntidade = idEntidade;
        this.nomeModelo = nomeModelo;
        this.tipoAcesso = tipoAcesso;
        this.dataHora = dataHora;
    }

    // Construtor para novos registros
    public Registro(String tipoEntidade, String idEntidade, String nomeModelo, String tipoAcesso) {
        this(tipoEntidade, idEntidade, nomeModelo, tipoAcesso, LocalDateTime.now());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoEntidade() {
        return tipoEntidade;
    }

    public String getIdEntidade() {
        return idEntidade;
    }

    public String getNomeModelo() {
        return nomeModelo;
    }

    public String getTipoAcesso() {
        return tipoAcesso;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return tipoEntidade + ": " + idEntidade + " (" + nomeModelo + ") - " + tipoAcesso + " em " + dataHora.format(formatter);
    }
}