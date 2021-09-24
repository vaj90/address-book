/*
Name: Allan John Valiente, Student ID: 101285226
Name: Farah Sheherin, Student ID: 101297029
*/

package ca.gbc.comp2130.assignment2;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Main extends Application {
    static ContactManager contactManager;
    TableView<Contact> tableContact;
    Stage window;
    Scene homeScene,formScene,viewScene;
    HBox btnContainer,
         tableContainer,
         searchContainer,
         fNameBox,
         lNameBox,
         bDateBox,
         s1Box,s2Box,cityBox,pCodeBox,provBox,countryBox,
         eBox,nBox,
         PhoneBox,
         btnBox;
    VBox hAddressBox,hPBox,wPBox,noteBox,homeBox,formBox;
    AnchorPane home,form;
    Alert msgBox;
    ButtonType yes,no;
    ComboBox cboCity;
    Label lblFName,
            lblContactTitle,
            lblContactForm,
            lblContactView,
            lblLName,
            lblBDate,
            lblHAddress,
            lblS1Address,
            lblS2Address,
            lblCity,
            lblPostal,
            lblProvince,
            lblCountry,
            lblEmail,
            lblHPhone,
            lblWPhone,
            lblNotes,
            lblSearch;
    DatePicker dpBDate;
    TextArea taStreet1,taStreet2,taNotes;
    TextField txtFName,txtLName,txtCity,txtPostalCode,txtProvince,txtCountry,txtEmail,txtHPhone,txtWPhone,txtSearch;
    ScrollPane sp;
    DateTimeFormatter dateFormatter,newDateFormatter;
    String[] contactField = {"First Name", "Last Name", "Birthday", "Home Address", "Email", "Home Phone", "Work Phone", "Notes"};
    String actionIdentifier = "",alertContent="";
    static int MAX_CONTACT = 100;



    public void createTableColumn(){
        for ( String field: contactField ) {
            TableColumn<Contact, String> col = new TableColumn<>(field);
            if(field.indexOf(" ")>0){
                field = field.replaceAll("\\s+", "");
            }
            if(field=="Birthday"){
                col.setCellValueFactory(cellData -> {
                    String birthDate ="" ;
                     Contact contact = cellData.getValue();
                     MyDate bDate = contact.getBirthday();
                    if (bDate == null) {
                        birthDate = null ;
                    } else {
                        birthDate = bDate.getMonthShortForm() + " "+(bDate.getDay()>9? bDate.getDay()+"" : "0"+ bDate.getDay()) +" " + bDate.getYear();
                    }
                    return new SimpleStringProperty(birthDate);
                });
            }
            else if(field=="HomeAddress"){
                col.setCellValueFactory(cellData -> {
                    String addressStr ="" ;
                    Contact contact = cellData.getValue();
                    Address address = contact.getHomeAddress();
                    if (address == null) {
                        addressStr = null ;
                    } else {
                        addressStr = address.toString();
                    }
                    return new SimpleStringProperty(addressStr);
                });
            }
            else
            {
                col.setCellValueFactory( new PropertyValueFactory<>(field));
            }
            tableContact.getColumns().add(col);
        }
    }
    //LocalDate localDate = datePicker.getValue();
    //format: YYYY-MM-DD : 2014-02-25
    public MyDate createDate(String dateStr){
        String[] arrOfDate = dateStr.split("-");
        int month = Integer.parseInt(arrOfDate[1]);
        int day = Integer.parseInt(arrOfDate[2]);
        int year =  Integer.parseInt(arrOfDate[0]);;
        return new MyDate(day,month,year);
    }

    public void mockData(){
        String[][] addresses = {
                { "2121 Lake Shore Blvd West", "", "Etobicoke", "M8V 4E9", "Ontario", "Canada"},
                { "920 Yonge Street", "", "Toronto", "M4W 2J2", "Ontario", "Canada"},
                { "7503 Rue St Denis", "", "Montreal", "H2R 2E7", "Quebec", "Canada"},
                { "100 Peter Sosiak Bay", "", "Winnipeg", "R3T 0E1", "Manitoba", "Canada"},
                { "6056 Stanton Dr SW", "", "Edmonton", "T6X 0H1", "Alberta", "Canada"},
        };

        String[][] contacts = {
                {"Allan John","Valiente","111-111-1111","","allanjohn.valiente@gmail.com","sample data"},
                {"Farah","Sheherin","222-222-2222","","allanjohn.valiente@gmail.com","sample data"},
                {"Peter","Parker","333-333-3333","","peter.parker@gmail.com","sample data"},
                {"Tony","Stark","444-444-4444","","tony.stark@gmail.com","sample data"},
                {"Clark","Kent","555-555-5555","","clark.kent@gmail.com","sample data"},
        };
        Random rnd = new Random();
        int aRnd;

        for (String[] contact:contacts) {
            int month = rnd.nextInt(12)+1;
            int day = rnd.nextInt(28) +1;
            MyDate d1 = new MyDate(day,month,1990);
            aRnd = rnd.nextInt(addresses.length);
            String[] address = addresses[aRnd];
            Address ad = new Address(address[0],address[1],address[2],address[3],address[4],address[5]);
            contactManager.addContact(contact[0],contact[1],contact[2],contact[3],ad,contact[4],d1,contact[5]);
        }
    }
    public void fillTable(){
        Contact[] contacts = contactManager.getAllContacts();
        tableContact.getItems().clear();
        for (int i=0; i< MAX_CONTACT;i++) {
            if(contacts[i]!=null){
                tableContact.getItems().add(contacts[i]);
            }
        }
    }
    public void deleteContact(){
        Contact selectedItem = tableContact.getSelectionModel().getSelectedItem();
        if(selectedItem!=null){
            alertContent = String.format("Are you sure you want to delete " + selectedItem.getFirstName().toLowerCase()+" "+selectedItem.getLastName().toLowerCase()+" record?");
            msgBox = new Alert(AlertType.INFORMATION,"",yes,no);
            msgBox.setTitle("Delete Contact");
            msgBox.setHeaderText(alertContent);
            Optional<ButtonType> result = msgBox.showAndWait();
            if (result.isPresent() && result.get()== yes) {
                boolean isDelete = contactManager.deleteContact(selectedItem.getFirstName() , selectedItem.getLastName());
                if(isDelete){
                    msgBox = new Alert(AlertType.INFORMATION);
                    msgBox.setTitle("Contact");
                    msgBox.setHeaderText("Contact successfully deleted.");
                    tableContact.getItems().remove(selectedItem);
                }
                else{
                    msgBox = new Alert(AlertType.WARNING);
                    msgBox.setTitle("Error");
                    msgBox.setHeaderText("Some error occur in deleting contact, please try again!");
                }
                msgBox.show();
            }
        }
    }
    public void resetControl(){
        actionIdentifier = "";
        txtFName.setText("");
        txtLName.setText("");
        dpBDate.setValue(null);
        taStreet1.setText("");
        taStreet2.setText("");
        taNotes.setText("");
        txtCity.setText("");
        txtPostalCode.setText("");
        txtProvince.setText("");
        txtCountry.setText("");
        txtEmail.setText("");
        txtHPhone.setText("");
        txtWPhone.setText("");
        txtFName.setEditable(true);
        txtLName.setEditable(true);
    }
    public void addContact(){
        resetControl();
        actionIdentifier = "Add";
        window.setTitle("Add Contact");
        window.setScene(formScene);
        window.show();
    }
    public void editContact() {
        Contact selectedItem = tableContact.getSelectionModel().getSelectedItem();
        if(selectedItem!=null){
            actionIdentifier = "Update";
            window.setTitle("Update Contact");
            window.setScene(formScene);
            window.show();
            txtFName.setText(selectedItem.getFirstName());
            txtLName.setText(selectedItem.getLastName());
            String dateStr = selectedItem.getBirthday().getMonthShortForm() + " " +
                    (selectedItem.getBirthday().getDay()>9 ? selectedItem.getBirthday().getDay() +"": "0"+ selectedItem.getBirthday().getDay()  )+ ", " +
                             selectedItem.getBirthday().getYear();
            LocalDate localDate = LocalDate.parse(dateStr, dateFormatter);
            dpBDate.setValue(localDate);
            taStreet1.setText(selectedItem.getHomeAddress().streetInfo1);
            taStreet2.setText(selectedItem.getHomeAddress().streetInfo2);
            taNotes.setText(selectedItem.getNotes());
            txtCity.setText(selectedItem.getHomeAddress().city);
            txtPostalCode.setText(selectedItem.getHomeAddress().postalCode);
            txtProvince.setText(selectedItem.getHomeAddress().province);
            txtCountry.setText(selectedItem.getHomeAddress().country);
            txtEmail.setText(selectedItem.getEmail());
            txtHPhone.setText(selectedItem.getHomePhone());
            txtWPhone.setText(selectedItem.getWorkPhone());
            //disabled TextField
            txtFName.setEditable(false);
            txtLName.setEditable(false);
        }
    }
    public void redirectContact(){
        fillTable();
        resetControl();
        window.setTitle("Contact");
        window.setScene(homeScene);
        window.show();
    }
    public void saveContact(){
        String fName = txtFName.getText();
        String lName = txtLName.getText();
        String email = txtEmail.getText();
        String homePhone = txtHPhone.getText();
        String workPhone = txtWPhone.getText();
        String notes =taNotes.getText();
        String street1 = taStreet1.getText();
        String street2 = taStreet2.getText();
        String city = txtCity.getText();
        String postalCode = txtPostalCode.getText();
        String province = txtProvince.getText();
        String country = txtCountry.getText();
        LocalDate lDate = dpBDate.getValue();
        if(fName=="" || lName=="" || email=="" ||
            homePhone=="" || notes=="" ||
            street1=="" || city=="" ||
                postalCode=="" || province=="" || country=="" ){
            msgBox = new Alert(AlertType.WARNING);
            msgBox.setHeaderText("Some error occur, please fill in all the fields!");
            msgBox.setTitle("Error");
            msgBox.show();
        }
        else {
            String dateStr = lDate.format(newDateFormatter);
            MyDate bDate = createDate(dateStr);
            Address address = new Address(street1,street2,city,postalCode,province,country);
            System.out.print(actionIdentifier);
            alertContent = String.format("Are you sure you want to " + actionIdentifier.toLowerCase() + " record?");
            msgBox = new Alert(AlertType.INFORMATION,"",yes,no);
            msgBox.setHeaderText(alertContent);
            msgBox.setTitle("Save");
            Optional<ButtonType> result = msgBox.showAndWait();
            if(actionIdentifier=="Add"){
                if (result.isPresent() && result.get()== yes) {
                    boolean isAdd = contactManager.addContact(fName,lName,homePhone,workPhone,address,email,bDate,notes);
                    if(isAdd){
                        msgBox = new Alert(AlertType.INFORMATION);
                        msgBox.setHeaderText("Contact successfully added.");
                        msgBox.setTitle(actionIdentifier + " contact");
                        redirectContact();
                    }
                    else{
                        msgBox = new Alert(AlertType.WARNING,"Some error occur in saving contact!");
                        msgBox.setTitle("Error");
                    }
                    msgBox.show();
                }
            }
            else{
                if (result.isPresent() && result.get()== yes) {
                    boolean isUpdate = contactManager.updateContact(fName,lName,homePhone,workPhone,address,email,bDate,notes);
                    if(isUpdate){
                        msgBox = new Alert(AlertType.INFORMATION);
                        msgBox.setHeaderText("Contact successfully updated.");
                        msgBox.setTitle(actionIdentifier + " contact");
                        redirectContact();
                    }
                    else{
                        msgBox = new Alert(AlertType.WARNING,"Some error occur in updating contact!");
                        msgBox.setTitle("Error");
                    }
                    msgBox.show();
                }
            }
        }
    }
    public void searchContact(String param, String option){
        ArrayList<Contact> contacts = new ArrayList<Contact>() ;
        boolean hasSearch = false;
        if(option.equals("Search By Name")){
            contacts= contactManager.findContactsByName(param);
            hasSearch = true;
        }
        else if(option.equals("Search By City")){
            contacts= contactManager.findContactsByCity(param);
            hasSearch = true;
        }
        else {
            msgBox = new Alert(AlertType.INFORMATION);
            msgBox.setHeaderText("Please choose the search option.");
            msgBox.setTitle("Search");
            msgBox.show();
        }
        if(hasSearch){
            tableContact.getItems().clear();
            for (Contact contact:contacts) {
                tableContact.getItems().add(contact);
            }
        }
        else {
            fillTable();
        }
    }
    public void fillCombo(){
        cboCity.getItems().clear();
        cboCity.getItems().add("---Please select---");
        cboCity.getItems().add("Search By Name");
        cboCity.getItems().add("Search By City");
        cboCity.setValue("---Please select---");
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        // implement contactManager
        contactManager = new ContactManager(MAX_CONTACT);
        //implement mock data
        mockData();
        window = primaryStage;
        window.setTitle("Contact");
        window.resizableProperty().setValue(false);

        yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        msgBox = new Alert(AlertType.NONE);
        home = new AnchorPane();
        form = new AnchorPane();
        dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        newDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //Home Layout ---------------------------------------
        btnContainer = new HBox();
        btnContainer.setLayoutY(310);
        btnContainer.setPrefWidth(640);
        btnContainer.setPadding(new Insets(10,10,10,10));

        tableContainer = new HBox();
        tableContainer.setPrefWidth(640);
        tableContainer.setPadding(new Insets(10,10,10,10));

        tableContact = new TableView<>(FXCollections.observableArrayList());
        tableContact.setPrefWidth(620);
        tableContact.setPrefHeight(300);
        //set table column
        createTableColumn();
        tableContact.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        fillTable();

        //declare button
        Button btnAdd = new Button("Add");
        btnAdd.setPrefWidth(100);
        btnAdd.setOnAction(event -> {
            addContact();
        });
        Button btnEdit = new Button("Edit");
        btnEdit.setPrefWidth(100);
        btnEdit.setOnAction(event -> {
            editContact();
        });
        Button btnDelete = new Button("Delete");
        btnDelete.setPrefWidth(100);
        btnDelete.setOnAction(event -> {
            deleteContact();
        });
        Button btnRefresh = new Button("Refresh");
        btnRefresh.setPrefWidth(100);
        btnRefresh.setOnAction(event -> {
            fillTable();
        });

        lblContactTitle = new Label("Contact List");
        lblContactTitle.setPadding(new Insets(10));
        lblContactTitle.setStyle("-fx-font-weight: bold;-fx-font-size:18px;");

        lblSearch = new Label("Search");
        lblSearch.setStyle("-fx-font-weight: bold;-fx-font-size:14px;");
        lblSearch.setPrefWidth(60);
        txtSearch = new TextField();
        txtSearch.setPrefWidth(420);
        txtSearch.textProperty().addListener((e,o,n)->{
            String param = n;
            String option = (String) cboCity.getValue();
            if(option=="---Please select---"|| option==null) option = "";
            searchContact(param,option);
        });
        cboCity = new ComboBox();
        cboCity.setPrefWidth(140);
        fillCombo();
        cboCity.setValue("---Please select---");
        cboCity.setOnAction(event -> {
            String param = txtSearch.getText();
            String option = (String) cboCity.getValue();
            if(option=="---Please select---"|| option==null) option = "";
            searchContact(param,option);
        });
        searchContainer = new HBox();
        searchContainer.setPadding(new Insets(10,10,5,10));
        searchContainer.getChildren().addAll(lblSearch, txtSearch,cboCity);

        btnContainer.getChildren().addAll(btnAdd,btnEdit,btnDelete,btnRefresh);
        btnContainer.setAlignment(Pos.CENTER_RIGHT);
        tableContainer.getChildren().add(tableContact);
        homeBox = new VBox();
        homeBox.getChildren().addAll(lblContactTitle,searchContainer,tableContainer,btnContainer);
        home.getChildren().add(homeBox);

        //Form Layout ---------------------------------------
        lblFName = new Label("First Name");
        lblFName.setStyle("-fx-font-weight: bold;-fx-font-size:14px;");
        lblFName.setPrefWidth(80);
        txtFName = new TextField();
        txtFName.setPrefWidth(360);
        fNameBox = new HBox();
        fNameBox.setPadding(new Insets(10,10,5,10));
        fNameBox.getChildren().addAll(lblFName, txtFName);

        lblLName = new Label("Last Name");
        lblLName.setStyle("-fx-font-weight: bold;-fx-font-size:14px;");
        lblLName.setPrefWidth(80);
        txtLName = new TextField();
        txtLName.setPrefWidth(360);
        lNameBox = new HBox();
        lNameBox.setPadding(new Insets(5,10,5,10));
        lNameBox.getChildren().addAll(lblLName,txtLName);

        lblBDate = new Label("Birth Date");
        lblBDate.setStyle("-fx-font-weight: bold;-fx-font-size:14px;");
        lblBDate.setPrefWidth(80);
        dpBDate = new DatePicker();
        dpBDate.setPrefWidth(120);
        bDateBox= new HBox();
        bDateBox.setPadding(new Insets(5,10,5,10));
        bDateBox.getChildren().addAll(lblBDate,dpBDate);

        lblHAddress = new Label("Home Address");
        lblHAddress.setStyle("-fx-font-weight: bold;-fx-font-size:14px;");
        lblHAddress.setPadding(new Insets(0,0,10,0));

        s1Box = new HBox();
        s1Box.setPadding(new Insets(0,0,10,0));
        lblS1Address = new Label("Street 1");
        lblS1Address.setPrefWidth(80);
        taStreet1 = new TextArea();
        taStreet1.setPrefSize(360,80);
        s1Box.getChildren().addAll(lblS1Address,taStreet1);

        s2Box = new HBox();
        s2Box.setPadding(new Insets(0,0,10,0));
        lblS2Address = new Label("Street 2");
        lblS2Address.setPrefWidth(80);
        taStreet2 = new TextArea();
        taStreet2.setPrefSize(360,80);
        s2Box.getChildren().addAll(lblS2Address,taStreet2);

        cityBox = new HBox();
        cityBox.setPadding(new Insets(0,0,10,0));
        lblCity = new Label("City");
        lblCity.setPrefWidth(80);
        txtCity = new TextField();
        txtCity.setPrefWidth(360);
        cityBox.getChildren().addAll(lblCity,txtCity);

        pCodeBox = new HBox();
        pCodeBox.setPadding(new Insets(0,0,10,0));
        lblPostal = new Label("Postal Code");
        lblPostal.setPrefWidth(80);
        txtPostalCode = new TextField();
        txtPostalCode.setPrefWidth(360);
        pCodeBox.getChildren().addAll(lblPostal,txtPostalCode);

        provBox = new HBox();
        provBox.setPadding(new Insets(0,0,10,0));
        lblProvince = new Label("Province");
        lblProvince.setPrefWidth(80);
        txtProvince = new TextField();
        txtProvince.setPrefWidth(360);
        provBox.getChildren().addAll(lblProvince,txtProvince);

        countryBox = new HBox();
        countryBox.setPadding(new Insets(0,0,10,0));
        lblCountry = new Label("Country");
        lblCountry.setPrefWidth(80);
        txtCountry = new TextField();
        txtCountry.setPrefWidth(360);
        countryBox.getChildren().addAll(lblCountry,txtCountry);

        hAddressBox = new VBox();
        hAddressBox.setPadding(new Insets(5,10,5,10));
        hAddressBox.getChildren().addAll(lblHAddress,s1Box,s2Box,cityBox,pCodeBox,provBox,countryBox);

        eBox = new HBox();
        eBox.setPadding(new Insets(5,10,20,10));
        lblEmail = new Label("Email");
        lblEmail.setPrefWidth(80);
        lblEmail.setStyle("-fx-font-weight: bold;-fx-font-size:14px;");
        txtEmail = new TextField();
        txtEmail.setPrefWidth(360);
        eBox.getChildren().addAll(lblEmail,txtEmail);

        hPBox = new VBox();
        hPBox.setPadding(new Insets(0,5,0,10));
        hPBox.setPrefWidth(230);
        lblHPhone = new Label("Home Phone");
        lblHPhone.setStyle("-fx-font-weight: bold;-fx-font-size:14px;");
        txtHPhone = new TextField();
        hPBox.getChildren().addAll(lblHPhone,txtHPhone);

        wPBox = new VBox();
        wPBox.setPadding(new Insets(0,10,0,5));
        wPBox.setPrefWidth(230);
        lblWPhone = new Label("Work Phone");
        lblWPhone.setStyle("-fx-font-weight: bold;-fx-font-size:14px;");
        txtWPhone = new TextField();
        wPBox.getChildren().addAll(lblWPhone,txtWPhone);

        PhoneBox = new HBox();
        PhoneBox.setPadding(new Insets(5,0,5,0));
        PhoneBox.getChildren().addAll(hPBox,wPBox);

        nBox = new HBox();
        nBox.setPadding(new Insets(5,10,5,10));
        noteBox = new VBox();
        noteBox.setPrefWidth(440);
        lblNotes = new Label("Note");
        lblNotes.setStyle("-fx-font-weight: bold;-fx-font-size:14px;");
        taNotes = new TextArea();
        taNotes.setPrefSize(440,80);
        noteBox.getChildren().addAll(lblNotes,taNotes);
        nBox.getChildren().add(noteBox);

        Button btnSave = new Button("Save");
        btnSave.setPrefWidth(100);
        btnSave.setOnAction(event -> {
            saveContact();
        });

        Button btnCancel = new Button("Cancel");
        btnCancel.setPrefWidth(100);
        btnCancel.setOnAction(event -> {
            window.setScene(homeScene);
            window.setTitle("Contact");
            window.show();
            resetControl();
        });

        btnBox = new HBox();
        btnBox.setPadding(new Insets(5,10,0,10));
        btnBox.getChildren().addAll(btnSave,btnCancel);
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        lblContactForm = new Label("Contact Form");
        lblContactForm.setPadding(new Insets(10));
        lblContactForm.setStyle("-fx-font-weight: bold;-fx-font-size:18px;");
        formBox = new VBox();
        formBox.getChildren().addAll(lblContactForm,fNameBox, lNameBox, bDateBox,hAddressBox,eBox,PhoneBox,nBox, btnBox);
        form.getChildren().addAll(formBox);

        sp = new ScrollPane();
        sp.setContent(form);
        sp.fitToHeightProperty().set(true);

        homeScene = new Scene(home,  640, 460);
        formScene = new Scene(sp,  465, 780);

        window.setScene(homeScene);
        window.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

}

