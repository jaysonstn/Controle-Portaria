package br.com.portaria;

import br.com.portaria.model.Registro;
import br.com.portaria.model.Bloqueado;
import br.com.portaria.dao.DatabaseManager;
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
import javafx.util.Callback;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


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
        // Topo com Logo + Título alinhado à esquerda
        HBox topBox = new HBox(15);  // 15 de espaçamento entre logo e título
        topBox.setAlignment(Pos.CENTER_LEFT);  // Alinha tudo à esquerda
        topBox.setPadding(new Insets(20, 20, 20, 30));  // top, right, bottom, left (mais espaço à esquerda)
        topBox.setStyle("-fx-background-color: #2C3E50;");

        // Logo
        try {
        Image logo = new Image("file:Logo.png");  // Caminho relativo à raiz do projeto
        ImageView imageView = new ImageView(logo);
        imageView.setFitWidth(100);    // Tamanho menor para caber bem à esquerda
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        topBox.getChildren().add(imageView);
        } catch (Exception e) {
        System.out.println("Logo não encontrada. Coloque logo.png na raiz do projeto.");
    }

// Título
Label titulo = new Label("Controle de Acesso de Portaria");
titulo.setFont(new Font("Arial Bold", 28));
titulo.setTextFill(Color.WHITE);
topBox.getChildren().add(titulo);

root.setTop(topBox);

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

    // Nova coluna: Excluir
    TableColumn<Registro, Void> colExcluir = new TableColumn<>("Ação");
    colExcluir.setMinWidth(100);
    Callback<TableColumn<Registro, Void>, TableCell<Registro, Void>> cellFactory = new Callback<>() {
        @Override
        public TableCell<Registro, Void> call(final TableColumn<Registro, Void> param) {
            return new TableCell<>() {
                private final Button btnExcluir = new Button("Excluir");

                {
                    btnExcluir.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-font-weight: bold;");
                    btnExcluir.setOnAction(event -> {
                        Registro registro = getTableView().getItems().get(getIndex());
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmação");
                        alert.setHeaderText("Excluir registro?");
                        alert.setContentText("Tem certeza que deseja excluir o registro de " +
                                registro.getIdEntidade() + " (" + registro.getTipoAcesso() + ") ?");

                        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                            dbManager.deletarRegistro(registro.getId());
                            registrosObs.remove(registro);
                            showAlert("Registro excluído com sucesso!");
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btnExcluir);
                    }
                }
            };
        }
    };
    colExcluir.setCellFactory(cellFactory);

    table.getColumns().addAll(colTipo, colId, colNome, colAcesso, colData, colExcluir);
    return table;
}

   private TableView<Bloqueado> criarTabelaBloqueados() {
    TableView<Bloqueado> table = new TableView<>();
    table.setStyle("-fx-background-color: #ECF0F1; -fx-text-fill: #2C3E50;");

    TableColumn<Bloqueado, String> colId = new TableColumn<>("ID/Placa Bloqueado");
    colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIdEntidade()));

    // Nova coluna: Desbloquear
    TableColumn<Bloqueado, Void> colAcao = new TableColumn<>("Ação");
    colAcao.setMinWidth(120);
    Callback<TableColumn<Bloqueado, Void>, TableCell<Bloqueado, Void>> cellFactory = new Callback<>() {
        @Override
        public TableCell<Bloqueado, Void> call(final TableColumn<Bloqueado, Void> param) {
            return new TableCell<>() {
                private final Button btnDesbloquear = new Button("Desbloquear");

                {
                    btnDesbloquear.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; -fx-font-weight: bold;");
                    btnDesbloquear.setOnAction(event -> {
                        Bloqueado bloqueado = getTableView().getItems().get(getIndex());
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmação");
                        alert.setHeaderText("Desbloquear?");
                        alert.setContentText("Tem certeza que deseja desbloquear " + bloqueado.getIdEntidade() + "?");

                        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                            dbManager.desbloquear(bloqueado.getIdEntidade());
                            bloqueadosObs.remove(bloqueado);
                            showAlert("Desbloqueado com sucesso!");
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btnDesbloquear);
                    }
                }
            };
        }
    };
    colAcao.setCellFactory(cellFactory);

    table.getColumns().addAll(colId, colAcao);
    return table;
}

    private GridPane criarGridAcoes() {
    GridPane grid = new GridPane();
    grid.setHgap(15);
    grid.setVgap(20);
    grid.setPadding(new Insets(30));
    grid.setAlignment(Pos.CENTER);

    // Título da aba
    Label tituloAcoes = new Label("Registrar Acesso");
    tituloAcoes.setFont(new Font("Arial Bold", 24));
    tituloAcoes.setTextFill(Color.WHITE);  // Força branco
    grid.add(tituloAcoes, 0, 0, 2, 1);

    Label lblTipo = new Label("Tipo:");
    lblTipo.setFont(new Font(16));
    lblTipo.setTextFill(Color.WHITE);  // Branco

    ComboBox<String> cbTipo = new ComboBox<>(FXCollections.observableArrayList("Veículo", "Colaborador"));
    cbTipo.setValue("Veículo");
    cbTipo.setPrefWidth(250);

    Label lblId = new Label("ID / Placa:");
    lblId.setFont(new Font(16));
    lblId.setTextFill(Color.WHITE);

    TextField tfId = new TextField();
    tfId.setPrefWidth(250);
    tfId.setPromptText("Ex: 00123 ou ABC-1234");

    Label lblNome = new Label("Nome / Modelo:");
    lblNome.setFont(new Font(16));
    lblNome.setTextFill(Color.WHITE);

    TextField tfNome = new TextField();
    tfNome.setPrefWidth(250);
    tfNome.setPromptText("Ex: João Silva ou Gol Prata");

    // Botões
    Button btnEntrada = new Button("Registrar Entrada");
    btnEntrada.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; -fx-font-size: 16; -fx-padding: 12 30; -fx-background-radius: 8;");
    btnEntrada.setPrefWidth(250);
    btnEntrada.setOnAction(e -> registrarAcesso(cbTipo.getValue(), tfId.getText(), tfNome.getText(), "Entrada"));

    Button btnSaida = new Button("Registrar Saída");
    btnSaida.setStyle("-fx-background-color: #E67E22; -fx-text-fill: white; -fx-font-size: 16; -fx-padding: 12 30; -fx-background-radius: 8;");
    btnSaida.setPrefWidth(250);
    btnSaida.setOnAction(e -> registrarAcesso(cbTipo.getValue(), tfId.getText(), tfNome.getText(), "Saída"));

    Button btnBloquear = new Button("Bloquear Acesso");
    btnBloquear.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-font-size: 16; -fx-padding: 12 30; -fx-background-radius: 8;");
    btnBloquear.setPrefWidth(250);
    btnBloquear.setOnAction(e -> bloquear(tfId.getText()));

    // Posicionamento
    grid.add(lblTipo, 0, 1);
    grid.add(cbTipo, 1, 1);
    grid.add(lblId, 0, 2);
    grid.add(tfId, 1, 2);
    grid.add(lblNome, 0, 3);
    grid.add(tfNome, 1, 3);

    VBox boxBotoes = new VBox(15, btnEntrada, btnSaida, btnBloquear);
    boxBotoes.setAlignment(Pos.CENTER);
    grid.add(boxBotoes, 0, 4, 2, 1);

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