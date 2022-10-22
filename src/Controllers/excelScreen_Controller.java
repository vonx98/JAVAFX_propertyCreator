package Controllers;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
import java.util.Locale;
import java.util.ResourceBundle;

public class excelScreen_Controller implements Initializable {

    @FXML
    private ListView<CheckBox> columnName_listview;

    @FXML
    private TextField functionName_textField;

    @FXML
    private TextArea output_textArea;

    @FXML
    private TextField veriableName_textField;

    @FXML
    void copyClipboardBtn_onAction() {

    }

    @FXML
    void createBtn_onAction() {

        if (!veriableName_textField.getText().isEmpty() && !functionName_textField.getText().isEmpty())
        {
            String data = veriableName_textField.getText();
            String funcName = functionName_textField.getText();

            ArrayList<String> checkedNames = new ArrayList<>();

            String excelText = "public void "+funcName+"(ObservableList<"+data+"> masterData,String xlsname, boolean run) throws IOException {\n\n" +
                    "NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.getDefault());\n" +
                    "    DecimalFormat dcf = new DecimalFormat(\"#,###,###.###\");\n" +
                    "HSSFWorkbook workbook = new HSSFWorkbook();\n" +
                    "HSSFSheet sheet = workbook.createSheet(xlsname + \".xls\");\n" +
                    "\n" +
                    "HSSFFont font = workbook.createFont();\n" +
                    "HSSFCellStyle style = workbook.createCellStyle();\n" +
                    "\n" +
                    "sheet.getPrintSetup().setLandscape(true);\n" +
                    "sheet.getPrintSetup().setFitHeight((short) 1);\n" +
                    "sheet.getPrintSetup().setFitWidth((short) 1);\n" +
                    "sheet.setMargin(Sheet.LeftMargin, 0.1);\n" +
                    "sheet.setMargin(Sheet.RightMargin, 0.1);\n" +
                    "sheet.setMargin(Sheet.BottomMargin, 0.1);\n" +
                    "sheet.setMargin(Sheet.TopMargin, 0.1);\n" +
                    "\n" +
                    "\n" +
                    "font.setFontHeightInPoints((short) 10);\n" +
                    "       //font.setBold(true);\n" +
                    "\n" +
                    "style.setBorderLeft(BorderStyle.THIN);\n" +
                    "style.setBorderBottom(BorderStyle.THIN);\n" +
                    "       //style.setBottomBorderColor(IndexedColors.BLACK.getIndex());\n" +
                    "style.setBorderRight(BorderStyle.THIN);\n" +
                    "       //style.setRightBorderColor(IndexedColors.BLACK.getIndex());\n" +
                    "style.setBorderTop(BorderStyle.THIN);\n" +
                    "       //style.setTopBorderColor(IndexedColors.BLACK.getIndex());\n" +
                    "\n" +
                    "\n" +
                    "style.setFont(font);\n" +
                    "style.setWrapText(true);\n" +
                    "\n" +
                    "HSSFCellStyle headerStyle = workbook.createCellStyle();\n" +
                    "HSSFFont font2 = workbook.createFont();\n" +
                    "font2.setFontHeightInPoints((short) 10);\n" +
                    "font2.setBold(true);\n" +
                    "headerStyle.setBorderLeft(BorderStyle.THIN);\n" +
                    "headerStyle.setBorderRight(BorderStyle.THIN);\n" +
                    "headerStyle.setAlignment(HorizontalAlignment.CENTER);\n" +
                    "headerStyle.setFont(font2);\n" +
                    "\n" +
                    "\n" +
                    "HSSFRow headerRow1 = sheet.createRow(0);\n" +
                    "headerRow1.setHeight((short) -1);\n";

            for (CheckBox c : columnName_listview.getItems())
            {
                if (c.isSelected())
                {
                    checkedNames.add(c.getText());
                }
            }

            if (checkedNames.size() >0)
            {
                int headerCounter = 0;
                for (String headerName : checkedNames)
                {
                    excelText += "\nheaderRow1.createCell("+headerCounter+").setCellValue(\""+headerName+"\");";
                    excelText += "\nheaderRow1.getCell("+headerCounter+").setCellStyle(headerStyle);";
                    excelText += "\nsheet.autoSizeColumn("+headerCounter+");\n";
                    headerCounter += 1;
                }

                excelText += "\nint counter = 1;";
                excelText += "\n for ("+data+" cdata : masterData) {\n" +
                        "       HSSFRow row = sheet.createRow(counter);";

                int nameCounter = 0;
                for (String dataName : checkedNames)
                {
                    excelText += "\n        row.createCell("+nameCounter+").setCellValue(cdata.get"+dataName.substring(0,1).toUpperCase(Locale.ROOT)+dataName.substring(1)+"());";
                    excelText +="\nsheet.autoSizeColumn("+nameCounter+");";
                    excelText +="\nrow.getCell("+nameCounter+").setCellStyle(style);";
                    nameCounter += 1;
                }

                excelText += "\n        counter++;";
                excelText += "\n}";





                excelText += "\n\nFileOutputStream fileOut = new FileOutputStream(\"EXCELFILES/\" + xlsname + \".xls\");\n" +
                        "workbook.write(fileOut);\n" +
                        "fileOut.close();\n" +
                        "if (run) {\n" +
                        "   String cmd = \"cmd /c start \\\"\\\" \\\"EXCELFILES/\" + xlsname + \".xls\\\"\";\n" +
                        "   Runtime.getRuntime().exec(cmd);\n" +
                        "}\n" +
                        "}";

                String finalExcelText = excelText;
                Platform.runLater(()->{
                    output_textArea.setText(finalExcelText);
                });
            }


        }


    }

    ArrayList<String> cNames = new ArrayList<>();
    public File file = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(()->{

            try
            {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(file);
                doc.getDocumentElement().normalize();

                NodeList tableColumnNodeList = doc.getElementsByTagName("TableColumn"); // Get nodes containing column names
                for (int itr = 0; itr < tableColumnNodeList.getLength(); itr++) // loop created to parse node contents
                {

                    Node node = tableColumnNodeList.item(itr);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node; //Variable To Hold Element İnside Node
                        cNames.add(element.getAttribute("fx:id")); // Add column Names to list
                    }
                }
            }
            catch (ParserConfigurationException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SAXException e) {
                throw new RuntimeException(e);
            }

            for (String cName : cNames)
            {
                String variableName = cName.replaceAll("_column","");
                columnName_listview.getItems().add(new CheckBox(variableName));
            }
        });

    }


}
