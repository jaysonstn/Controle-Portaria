package br.com.portaria.dao;

import br.com.portaria.model.Registro;
import br.com.portaria.model.Bloqueado;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:portaria.db";

    public DatabaseManager() {
        criarTabelas();
    }

    private void criarTabelas() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sqlRegistros = "CREATE TABLE IF NOT EXISTS registros (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "tipo_entidade TEXT NOT NULL," +
                    "id_entidade TEXT NOT NULL," +
                    "nome_modelo TEXT NOT NULL," +
                    "tipo_acesso TEXT NOT NULL," +
                    "data_hora TEXT NOT NULL)";
            Statement stmt = conn.createStatement();
            stmt.execute(sqlRegistros);

            String sqlBloqueados = "CREATE TABLE IF NOT EXISTS bloqueados (" +
                    "id_entidade TEXT PRIMARY KEY)";
            stmt.execute(sqlBloqueados);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void adicionarRegistro(Registro registro) {
        String sql = "INSERT INTO registros (tipo_entidade, id_entidade, nome_modelo, tipo_acesso, data_hora) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, registro.getTipoEntidade());
            pstmt.setString(2, registro.getIdEntidade());
            pstmt.setString(3, registro.getNomeModelo());
            pstmt.setString(4, registro.getTipoAcesso());
            pstmt.setString(5, registro.getDataHora().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Registro> listarRegistros() {
        List<Registro> registros = new ArrayList<>();
        String sql = "SELECT * FROM registros";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Registro reg = new Registro(
                        rs.getString("tipo_entidade"),
                        rs.getString("id_entidade"),
                        rs.getString("nome_modelo"),
                        rs.getString("tipo_acesso"),
                        LocalDateTime.parse(rs.getString("data_hora"), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                );
                reg.setId(rs.getInt("id"));
                registros.add(reg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registros;
    }

    public void adicionarBloqueado(String idEntidade) {
        String sql = "INSERT OR IGNORE INTO bloqueados (id_entidade) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idEntidade);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Bloqueado> listarBloqueados() {
        List<Bloqueado> bloqueados = new ArrayList<>();
        String sql = "SELECT * FROM bloqueados";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bloqueados.add(new Bloqueado(rs.getString("id_entidade")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bloqueados;
    }

    public boolean isBloqueado(String idEntidade) {
        String sql = "SELECT COUNT(*) FROM bloqueados WHERE id_entidade = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idEntidade);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Deletar um registro específico pelo ID
    public void deletarRegistro(int id) {
            String sql = "DELETE FROM registros WHERE id = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
    }
}

    // Desbloquear (remover da tabela bloqueados)
    public void desbloquear(String idEntidade) {
            String sql = "DELETE FROM bloqueados WHERE id_entidade = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idEntidade);
            pstmt.executeUpdate();
        } catch (SQLException e) {
        e.printStackTrace();
    }
}

}