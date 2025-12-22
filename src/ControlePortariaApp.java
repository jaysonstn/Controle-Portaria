

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class ControlePortariaApp extends Application {
    private DatabaseManager dbManager = new DatabaseManager();
    private ObservableList<Registro> registrosObs = FXCollections.observableArrayList();
    private ObservableList<Bloqueado> bloqueadosObs = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        // Carregar dados iniciais do DB
        registrosObs.addAll(dbManager.listarRegistros());
        bloqueadosObs.addAll(dbManager.listarBloqueados());

        // Layout principal
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #2C3E50;"); // Dark mode

        // Título
        Label titulo = new Label("Controle de Acesso de Portaria");
        titulo.setFont(new Font("Arial", 24));
        titulo.setTextFill(Color.WHITE);
        titulo.setPadding(new Insets(10));
        root.setTop(titulo);
        BorderPane.setAlignment(titulo, Pos.CENTER);

        // Abas para navegação
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: #34495E;");

        // Aba Registros
        Tab tabRegistros = new Tab("Registros");
        TableView<Registro> tableRegistros = criarTabelaRegistros();
        tableRegistros.setItems(registrosObs);
        VBox vboxRegistros = new VBox(10, tableRegistros);
        vboxRegistros.setPadding(new Insets(10));
        tabRegistros.setContent(vboxRegistros);

        // Aba Bloqueados
        Tab tabBloqueados = new Tab("Bloqueados");
        TableView<Bloqueado> tableBloqueados = criarTabelaBloqueados();
        tableBloqueados.setItems(bloqueadosObs);
        VBox vboxBloqueados = new VBox(10, tableBloqueados);
        vboxBloqueados.setPadding(new Insets(10));
        tabBloqueados.setContent(vboxBloqueados);

        // Aba Ações
        Tab tabAcoes = new Tab("Ações");
        GridPane gridAcoes = criarGridAcoes();
        gridAcoes.setPadding(new Insets(10));
        gridAcoes.setHgap(10);
        gridAcoes.setVgap(10);
        tabAcoes.setContent(gridAcoes);

        tabPane.getTabs().addAll(tabAcoes, tabRegistros, tabBloqueados);
        root.setCenter(tabPane);

        // Cena
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Portaria App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TableView<Registro> criarTabelaRegistros() {
        TableView<Registro> table = new TableView<>();
        table.setStyle("-fx-background-color: #ECF0F1; -fx-text-fill: #2C3E50;");

        TableColumn<Registro, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTipoEntidade()));

        TableColumn<Registro, String> colId = new TableColumn<>("ID/Placa");
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIdEntidade()));

        TableColumn<Registro, String> colNome = new TableColumn<>("Nome/Modelo");
        colNome.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNomeModelo()));

        TableColumn<Registro, String> colAcesso = new TableColumn<>("Acesso");
        colAcesso.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTipoAcesso()));

        TableColumn<Registro, String> colData = new TableColumn<>("Data/Hora");
        colData.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getDataHora().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));

        table.getColumns().addAll(colTipo, colId, colNome, colAcesso, colData);
        return table;
    }

    private TableView<Bloqueado> criarTabelaBloqueados() {
        TableView<Bloqueado> table = new TableView<>();
        table.setStyle("-fx-background-color: #ECF0F1; -fx-text-fill: #2C3E50;");

        TableColumn<Bloqueado, String> colId = new TableColumn<>("ID/Placa Bloqueado");
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIdEntidade()));

        table.getColumns().add(colId);
        return table;
    }

    private GridPane criarGridAcoes() {
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: #34495E;");

        // Campos comuns
        Label lblTipo = new Label("Tipo:");
        lblTipo.setTextFill(Color.WHITE);
        ComboBox<String> cbTipo = new ComboBox<>(FXCollections.observableArrayList("Veículo", "Colaborador"));
        cbTipo.setValue("Veículo");

        Label lblId = new Label("ID/Placa:");
        lblId.setTextFill(Color.WHITE);
        TextField tfId = new TextField();

        Label lblNome = new Label("Nome/Modelo:");
        lblNome.setTextFill(Color.WHITE);
        TextField tfNome = new TextField();

        // Botões de ações
        Button btnEntrada = new Button("Registrar Entrada");
        btnEntrada.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white;");
        btnEntrada.setOnAction(e -> registrarAcesso(cbTipo.getValue(), tfId.getText(), tfNome.getText(), "Entrada"));

        Button btnSaida = new Button("Registrar Saída");
        btnSaida.setStyle("-fx-background-color: #E67E22; -fx-text-fill: white;");
        btnSaida.setOnAction(e -> registrarAcesso(cbTipo.getValue(), tfId.getText(), tfNome.getText(), "Saída"));

        Button btnBloquear = new Button("Bloquear");
        btnBloquear.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white;");
        btnBloquear.setOnAction(e -> bloquear(tfId.getText()));

        // Posicionamento
        grid.add(lblTipo, 0, 0);
        grid.add(cbTipo, 1, 0);
        grid.add(lblId, 0, 1);
        grid.add(tfId, 1, 1);
        grid.add(lblNome, 0, 2);
        grid.add(tfNome, 1, 2);
        grid.add(btnEntrada, 0, 3);
        grid.add(btnSaida, 1, 3);
        grid.add(btnBloquear, 0, 4, 2, 1);

        return grid;
    }

    private void registrarAcesso(String tipo, String idEntidade, String nomeModelo, String acesso) {
        if (idEntidade.isEmpty() || nomeModelo.isEmpty()) {
            showAlert("Preencha todos os campos!");
            return;
        }
        if (dbManager.isBloqueado(idEntidade)) {
            showAlert("Entidade bloqueada! Acesso negado.");
            return;
        }
        String tipoEntidade = tipo.equals("Veículo") ? "Veiculo" : "Colaborador";
        Registro reg = new Registro(tipoEntidade, idEntidade, nomeModelo, acesso);
        dbManager.adicionarRegistro(reg);
        registrosObs.add(reg);
        showAlert(acesso + " registrada com sucesso!");
    }

    private void bloquear(String idEntidade) {
        if (idEntidade.isEmpty()) {
            showAlert("Preencha o ID/Placa!");
            return;
        }
        dbManager.adicionarBloqueado(idEntidade);
        bloqueadosObs.add(new Bloqueado(idEntidade));
        showAlert("Bloqueio adicionado!");
    }

    private void showAlert(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        //System.setProperty("file.encoding", "UTF-8");
        launch(args);
    }
}