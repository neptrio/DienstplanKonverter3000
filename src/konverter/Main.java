package konverter;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main extends Application implements EventHandler<ActionEvent> {

    private Button btnStart;
    private Button btnChooseFile;
    private Button btnChooseDirectory;

    private Text txtFilePath = new Text();
    private Text txtOutputDirectory = new Text();

    private String filePathToXML;
    private String absolutePathOutputDirectory = "C:\\";

    private ComboBox monthDropDown;
    private TextField yearInput;

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("DienstplanKonverter 3000");

        BorderPane bPane = new BorderPane();
        HBox header = addHeader();
        bPane.setTop(header);
        header.getChildren().add(getDateInput());
        bPane.setCenter(getVerticalOperationBtnPane());
        bPane.setRight(getParameterInfoPane());

        Scene scene = new Scene(bPane, 550, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == btnStart) {
            this.startGenerating();
        }
    }

    public Logger getLogger(String path) {
        Logger logger = Logger.getLogger("MyLog");

        try {
            FileHandler fh = new FileHandler(path + "\\log.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return logger;

    }

    private String getCurrentYear(){
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        return String.valueOf(year);

    }

    private HBox addHeader() {
        HBox headerBox = new HBox();
        headerBox.setPadding(new Insets(15, 12, 15, 12));
        headerBox.setSpacing(10);
        headerBox.setStyle("-fx-background-color: #ea4335;");

        Text title = new Text();
        title.setText("DienstplanKonverter 3000");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        title.setFill(Color.WHITE);

        headerBox.getChildren().add(title);

        return headerBox;
    }

    private HBox getDateInput(){
        HBox box = new HBox();
        box.setSpacing(10);

        ObservableList<String> months = FXCollections.observableArrayList(
                "Januar",
                "Februar",
                "März",
                "April",
                "Mai",
                "Juni",
                "Juli",
                "August",
                "September",
                "Oktober",
                "November",
                "Dezember"
        );

        monthDropDown = new ComboBox(months);
        monthDropDown.getSelectionModel().selectFirst();

        Label label = new Label("Jahr");
        label.setTextFill(Color.WHITE);
        yearInput = new TextField();
        yearInput.setText(getCurrentYear());

        box.getChildren().addAll(monthDropDown, label, yearInput);


        return box;
    }

    private VBox getVerticalOperationBtnPane() {
        VBox vButtons = new VBox();
        vButtons.setPadding(new Insets(10));
        vButtons.setSpacing(8);

        vButtons.getChildren().addAll(getBtnChooseFile(), getBtnChooseDirectory(), getBtnStart());

        return vButtons;
    }

    private VBox getParameterInfoPane() {
        VBox info = new VBox();
        info.setPadding(new Insets(10));
        info.setSpacing(8);

        Text titlePathXML = new Text();
        titlePathXML.setText("Dienstplan");
        titlePathXML.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

        Text outputDirectoryTitle = new Text();
        outputDirectoryTitle.setText("Ausgabeverzeichnis");
        outputDirectoryTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

        if (filePathToXML != null) {
            txtFilePath.setText(filePathToXML);
        } else {
            txtFilePath.setText("Keine Datei gewählt!");
        }
        txtOutputDirectory.setText(absolutePathOutputDirectory);

        info.getChildren().addAll(titlePathXML, txtFilePath, outputDirectoryTitle, txtOutputDirectory);

        return info;
    }

    private Button getBtnChooseFile() {
        btnChooseFile = new Button();
        btnChooseFile.setText("Dienstplan öffnen");
        btnChooseFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Dienstplan XML (*.xml)", "*.xml");
                fileChooser.getExtensionFilters().add(extensionFilter);
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    System.out.println(file.getAbsolutePath());
                    filePathToXML = file.getAbsolutePath();
                    txtFilePath.setText(filePathToXML);
                }
            }
        });

        return btnChooseFile;
    }

    private Button getBtnChooseDirectory() {
        btnChooseDirectory = new Button();
        btnChooseDirectory.setText("Ausgabeverzeichnis");
        btnChooseDirectory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("JavaFX Projects");
                File defaultDirectory = new File(System.getProperty("user.home"));
                if (!defaultDirectory.canRead()) {
                    defaultDirectory = new File("c:\\");
                }
                chooser.setInitialDirectory(defaultDirectory);
                File selectedDirectory = chooser.showDialog(primaryStage);
                if (selectedDirectory != null) {
                    absolutePathOutputDirectory = selectedDirectory.getAbsolutePath();
                    txtOutputDirectory.setText(absolutePathOutputDirectory);
                }
            }
        });

        return btnChooseDirectory;
    }

    private Button getBtnStart() {
        btnStart = new Button();
        btnStart.setText("Generieren");
        btnStart.setTranslateY(50);
        btnStart.setOnAction(this);

        return btnStart;
    }

    private void startGenerating() {
        if (filePathToXML == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setContentText("Zum Fortfahren bitte Dienstplan auswählen");

            alert.showAndWait();
        } else {

            File dienstplanXML = new File(filePathToXML);

            IDienstplanParser dienstplanParser = new DienstplanParser();
            ArrayList<Employee> employees = dienstplanParser.parse(dienstplanXML);

            Logger logger = getLogger(absolutePathOutputDirectory);



            for (Employee employee : employees) {
                CalenderBuilder cb = new CalenderBuilder(
                        employee,
                        logger,
                        absolutePathOutputDirectory,
                        monthDropDown.getSelectionModel().getSelectedIndex(),
                        Integer.parseInt(yearInput.getText()));
                cb.start();
            }

            for (Handler h : logger.getHandlers()) {
                h.close();
            }
        }
    }
}
