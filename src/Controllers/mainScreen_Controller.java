package Controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class mainScreen_Controller implements Initializable {

    @FXML
    private TextField fileRoad_textField;

    @FXML
    private TextArea textArea;

    @FXML
    private CheckBox bindDataStructureColumnProperties_checkBox;

    @FXML
    private CheckBox createClassVerable_checkBox;

    @FXML
    private CheckBox createTextFieldListener_checkBox;

    @FXML
    private CheckBox addTextFieldListenerCommand_checkBox;

    @FXML
    private CheckBox createColumnProperties_checkBox;

    @FXML
    private Label createdColumnCount_label;

    @FXML
    private Label createdTextFieldCount_label;

    @FXML
    private TextArea textListenerCommand_textArea;

    @FXML
    private VBox command_Vbox;

    @FXML
    void startBtn_onAction() {

        if (file != null)
        {
            try
            {
                textArea.setText("");
                createdColumnCount_label.setText("0");
                createdTextFieldCount_label.setText("0");

                ArrayList<String> textFieldNames = new ArrayList<>();



                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(file);
                doc.getDocumentElement().normalize();


                NodeList tableColumnNodeList = doc.getElementsByTagName("TableColumn"); // Get nodes containing column names
                for (int itr = 0; itr < tableColumnNodeList.getLength(); itr++) // loop created to parse node contents
                {

                    Node node = tableColumnNodeList.item(itr);

                    if (node.getNodeType() == Node.ELEMENT_NODE)
                    {
                        Element element = (Element) node; //Variable To Hold Element İnside Node
                        columnNames.add(element.getAttribute("fx:id")); // Add column Names to list
                    }
                }


                NodeList textFieldNodeList = doc.getElementsByTagName("JFXTextField"); // Get Nodes Containing Textfield Names
                for (int itr2 = 0; itr2 < textFieldNodeList.getLength(); itr2 ++) // loop created to parse node contents
                {

                    Node node2 = textFieldNodeList.item(itr2);

                    if (node2.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node2; // Variable To Hold Element İnside Node
                        textFieldNames.add(element.getAttribute("fx:id")); // Add TextField Names to list
                    }
                }


                if (createClassVerable_checkBox.isSelected())
                {
                    for (String data : columnNames)
                    {
                        String dataName = data.replaceAll("_column","");
                        String finalData = "private ? "+dataName+" = \"\";\n";
                        textArea.appendText(finalData);
                    }
                }
                else
                {
                    if (createColumnProperties_checkBox.isSelected())
                    {
                        for (String data : columnNames)
                        {
                            if (bindDataStructureColumnProperties_checkBox.isSelected())
                            {
                                String dataStructureName = data.replaceAll("_column","");
                                String finalPropertyData = data+".setCellValueFactory(new PropertyValueFactory<>(\""+dataStructureName+"\"));\n";
                                textArea.appendText(finalPropertyData);
                            }
                            else
                            {
                                String finalPropertyData = data+".setCellValueFactory(new PropertyValueFactory<>(\"\"));\n";
                                textArea.appendText(finalPropertyData);
                            }

                        }

                        createdColumnCount_label.setText(String.valueOf(columnNames.size()));
                    }

                }

                if (createTextFieldListener_checkBox.isSelected())
                {
                    for (String data : textFieldNames)
                    {
                        if (addTextFieldListenerCommand_checkBox.isSelected())
                        {
                            String command = textListenerCommand_textArea.getText();
                            String textListenerData = "\n"+data+".textProperty().addListener((observable, oldval, newval) -> {\n\tPlatform.runLater(()-> {\n"+command+"\n\n\t});\n});\n\n";
                            textArea.appendText(textListenerData);
                        }
                        else
                        {
                            String textListenerData = "\n"+data+".textProperty().addListener((observable, oldval, newval) -> {\n\tPlatform.runLater(()-> {\n\n\n\t});\n});\n\n";
                            textArea.appendText(textListenerData);
                        }
                    }
                    createdTextFieldCount_label.setText(String.valueOf(textFieldNames.size()));
                }






            }
            catch (ParserConfigurationException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SAXException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @FXML
    void selectFileBtn_onAction() {

        Platform.runLater(()->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("XML Dosyası Seçin");

            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("FXML","*.fxml"),
                    new FileChooser.ExtensionFilter("XML","*.xml"));


            file = fileChooser.showOpenDialog(fileRoad_textField.getScene().getWindow());

            if (file != null)
            {
                fileRoad_textField.setText(file.getPath());
            }

        });
    }

    @FXML
    void copyClipboardBtn_onAction() {

        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(textArea.getText());
        Clipboard.getSystemClipboard().setContent(clipboardContent);

    }

    @FXML
    void excelBtn_onAction() {

        if (file != null)
        {
            try
            {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Objects.requireNonNull(this.getClass().getClassLoader().getResource("FXML/createExcelScreen.fxml")));
                Parent root = loader.load();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.setTitle("Create Excel");
                stage.setMaximized(false);
                stage.setResizable(false);
                excelScreen_Controller controller = (excelScreen_Controller) loader.getController();
                controller.file = file;
                stage.showAndWait();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    File file = null;

    ArrayList<String> columnNames = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {




        createColumnProperties_checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if (createColumnProperties_checkBox.isSelected())
                {
                    bindDataStructureColumnProperties_checkBox.setDisable(false);
                }
                else
                {
                    bindDataStructureColumnProperties_checkBox.setDisable(true);
                }
            }
        });
        createTextFieldListener_checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if (createTextFieldListener_checkBox.isSelected())
                {
                    addTextFieldListenerCommand_checkBox.setDisable(false);


                }
                else
                {

                    addTextFieldListenerCommand_checkBox.setDisable(true);
                    command_Vbox.setDisable(true);
                }
            }
        });

        addTextFieldListenerCommand_checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if (addTextFieldListenerCommand_checkBox.isSelected())
                {
                    command_Vbox.setDisable(false);

                }
                else
                {
                    command_Vbox.setDisable(true);

                }
            }
        });
        createClassVerable_checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if (createClassVerable_checkBox.isSelected())
                {
                    bindDataStructureColumnProperties_checkBox.setSelected(false);
                    createTextFieldListener_checkBox.setSelected(false);
                    addTextFieldListenerCommand_checkBox.setSelected(false);
                    createColumnProperties_checkBox.setSelected(false);

                    bindDataStructureColumnProperties_checkBox.setDisable(true);
                    createTextFieldListener_checkBox.setDisable(true);
                    addTextFieldListenerCommand_checkBox.setDisable(true);
                    createColumnProperties_checkBox.setDisable(true);
                }
                else
                {

                    createTextFieldListener_checkBox.setDisable(false);
                    createColumnProperties_checkBox.setDisable(false);
                }
            }
        });

    }
}
