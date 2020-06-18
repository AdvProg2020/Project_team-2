package view.GUI;

import controller.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

//TODO: purchase menu

public class Controllers {
    private static Controller mainController;
    private static AdminController adminController;
    private static SellerController sellerController;
    private static CustomerController customerController;

    public static void init() {
        mainController = View.mainController;
        adminController = View.adminController;
        sellerController = View.sellerController;
        customerController = View.customerController;
    }

    public static class ProductBoxController {

        @FXML
        private Label sale;

        @FXML
        private ImageView image;

        @FXML
        private Label name;

        @FXML
        private Label rating;

        @FXML
        private Label priceBefore;

        @FXML
        private Label priceAfter;

        private String[] subProduct;

        public static Parent createBox(String[] subProduct) {
            FXMLLoader loader = new FXMLLoader(View.class.getResource("/fxml/" + Constants.FXMLs.productBox + ".fxml"));
            Parent p;
            try {
                p = loader.load();
                ProductBoxController pbc = loader.getController();
                pbc.setInfo(subProduct);
                pbc.setAction(p);
                return p;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private void setInfo(String[] subProductInfo) {
            subProduct = subProductInfo;
        }

        private void setAction(Parent p) {
            p.setOnMouseClicked(e -> ProductDetailMenu.display(subProduct[4]));
        }
    }

    public static class PersonalInfoMenuController {

        @FXML
        private ToggleButton imageEditBTN;

        @FXML
        private Button logoutBTN;

        @FXML
        private Button changePasswordBTN;

        @FXML
        private Button buyLogBTN;

        @FXML
        private Button discountsBTN;

        @FXML
        private TextField lastName;

        @FXML
        private TextField firstName;

        @FXML
        private Label firstNameLabel;

        @FXML
        private Label lastNameLabel;

        @FXML
        private ToggleButton fullNameEditBTN;

        @FXML
        private Label typeLBL;

        @FXML
        private TextField phoneNumberField;

        @FXML
        private TextField emailField;

        @FXML
        private TextField balanceField;

        @FXML
        private TextField storeNameField;

        @FXML
        private Label phoneNumberLBL;

        @FXML
        private Label emailLBL;

        @FXML
        private Label balanceLBL;

        @FXML
        private Label storeNameLBL;

        @FXML
        private ToggleButton PhoneNumberEditBTN;

        @FXML
        private ToggleButton emailEditBTN;

        @FXML
        private ToggleButton balanceEditBTN;

        @FXML
        private ToggleButton StoreNameEditBTN;

        private String[] personalInfo;
        private boolean isPopup;


        public static void display(String username) {
            FXMLLoader loader = new FXMLLoader(View.class.getResource( "/fxml/" + Constants.FXMLs.personalInfoMenu + ".fxml"));
            Parent p = null;
            try {
                p = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            if (username == null) {
                ((PersonalInfoMenuController)loader.getController()).setInfo(null, false);
                View.addToStack(Constants.FXMLs.personalInfoMenu);
                BaseController.setMainPane(p);
            } else {
                ((PersonalInfoMenuController)loader.getController()).setInfo(username, true);
                View.popupWindow("Account detail menu", p, 632, 472);
            }
        }

        private void setInfo(String username, boolean isPopup) {
            try {
                if (username == null) {
                    personalInfo = mainController.viewPersonalInfo();
                } else {
                    personalInfo = mainController.viewPersonalInfo(username);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            this.isPopup = isPopup;

            switch (personalInfo[personalInfo.length - 1]) {
                case Constants.customerUserType:
                    initCustomer();
                    break;
                case Constants.sellerUserType:
                    initSeller();
                    break;
                case Constants.adminUserType:
                    initAdmin();
                    break;
            }
        }

        private void initCustomer() {
            initCustomerButtons();
            initValues();
        }

        private void initCustomerButtons() {
            //TODO: complete
            initAdminButtons();
        }

        private void initSeller() {
            initSellerButtons();
            initValues();
        }

        private void initSellerButtons() {
            //TODO: complete
            initAdminButtons();
        }

        private void initAdmin() {
            initAdminButtons();
            initValues();
        }

        private void initAdminButtons() {
            //TODO: complete
            logoutBTN.setOnAction(e -> {
                try {
                    mainController.logout();
                    View.type.set(Constants.anonymousUserType);
                    if (isPopup) balanceLBL.getScene().getWindow().hide();
                    else View.goBack();

                } catch (Exceptions.NotLoggedInException ex) {
                    ex.printStackTrace();
                    return;
                }
            });
        }

        private void initValues() {

        }


        private void showPersonalInfo(String[] info) {
            System.out.println("1. username: " + info[0]);
            System.out.println("2. type: " + info[1]);
            System.out.println("3. first name: " + info[2]);
            System.out.println("4. last name: " + info[3]);
            System.out.println("5. email: " + info[4]);
            System.out.println("6. phone number: " + info[5]);
            if (info.length > 6) {
                System.out.println("7. balance: " + info[6]);
            }
            if (info.length > 7) {
                System.out.println("8. store name: " + info[7]);
            }
        }
    }

    public static class MainMenuController {
        private static void display() {
            View.getStackTrace().clear();
            View.stackSize.set(0);
            View.setMainPane(Constants.FXMLs.mainMenu);
        }
    }

    public static class ProductsMenuController implements Initializable {


        @FXML
        private ChoiceBox<String> sortBy;

        @FXML
        private ToggleButton isIncreasing;

        @FXML
        private ToggleGroup increasingToggleGroup;

        @FXML
        private ToggleButton isDecreasing;

        @FXML
        private CheckBox available;

        @FXML
        private Slider minPriceSlider;

        @FXML
        private Slider maxPriceSlider;

        @FXML
        private TextField filterName;

        @FXML
        private ChoiceBox<String> filterBrand;

        @FXML
        private ChoiceBox<String> filterSeller;

        @FXML
        private ChoiceBox<String> filterCategory;

        @FXML
        private ScrollPane productsPane;

        public static ArrayList<String[]> products;

        public static void display(ArrayList<String[]> products) {
            ProductsMenuController.products = products;
            View.setMainPane(Constants.FXMLs.productsMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initChoiceBoxes();
            initActions();
        }

        private void initChoiceBoxes() {
            ArrayList<String> brands = (ArrayList<String>) products.stream().map(p -> p[2]).collect(Collectors.toList());
            HashSet<String> b = new HashSet<>(brands);
            filterBrand.setItems(FXCollections.observableArrayList(b));


        }

        private void initActions() {

        }
    }

    public static class ProductDetailMenu {
        public static void display(String productId) {

        }
    }

    public static class LoginPopupController implements Initializable {
        private static Stage PopupStage;

        @FXML
        private ImageView usernameIcon;

        @FXML
        private TextField usernameField;

        @FXML
        private ImageView passwordIcon;

        @FXML
        private PasswordField passwordField;

        @FXML
        private Label errorLBL;

        @FXML
        private Button loginBTN;

        @FXML
        private Hyperlink registerLink;

        public static void display(Stage stage) {
            PopupStage = stage;
            PopupStage.setWidth(600);
            PopupStage.setHeight(400);
            PopupStage.setResizable(false);
            try {
                PopupStage.setScene(new Scene(View.loadFxml(Constants.FXMLs.loginPopup)));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            try {
                PopupStage.initModality(Modality.APPLICATION_MODAL);
            } catch (Exception e) {
            }
            PopupStage.show();
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            errorLBL.setText("");
            initListeners();
            initActions();
        }

        private void initListeners() {
            usernameField.textProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue.length() == 0) return;
                char lastInput = newValue.charAt(newValue.length() - 1);
                if (String.valueOf(lastInput).matches("\\W")) usernameField.setText(oldValue);
            }));
            passwordField.textProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue.length() == 0) return;
                char lastInput = newValue.charAt(newValue.length() - 1);
                if (String.valueOf(lastInput).matches("\\W")) passwordField.setText(oldValue);
            }));
        }

        private void initActions() {
            loginBTN.setOnAction(e -> {
                String username = usernameField.getText();
                String password = passwordField.getText();
                if (username == null || password == null) return;

                try {
                    mainController.login(username, password);
                    View.type.set(mainController.getType());
                    PopupStage.close();
                } catch (Exceptions.UsernameDoesntExistException | Exceptions.WrongPasswordException ex) {
                    errorLBL.setText("invalid username or password");
                    errorLBL.setTextFill(Color.RED);
                    ex.printStackTrace();
                }
            });
            registerLink.setOnAction(e -> RegisterPopupController.display(PopupStage));
        }
    }

    public static class RegisterPopupController implements Initializable {
        private static Stage PopupStage;

        @FXML
        private Hyperlink customerLoginHL;

        @FXML
        private Hyperlink sellerLoginHL;

        @FXML
        private TextField customerFirstName;

        @FXML
        private Label customerFirstNameError;

        @FXML
        private TextField customerPhoneNumber;

        @FXML
        private Label customerPhoneNumberError;

        @FXML
        private TextField customerBalance;

        @FXML
        private Label customerBalanceError;

        @FXML
        private TextField customerLastName;

        @FXML
        private Label customerLastNameError;

        @FXML
        private TextField customerEmail;

        @FXML
        private Label customerEmailError;

        @FXML
        private TextField customerUsername;

        @FXML
        private Label customerUsernameError;

        @FXML
        private PasswordField customerPassword;

        @FXML
        private Label customerPasswordError;

        @FXML
        private Button customerRegister;

        @FXML
        private TextField sellerFirstName;

        @FXML
        private Label sellerFirstNameError;

        @FXML
        private TextField sellerPhoneNumber;

        @FXML
        private Label sellerPhoneNumberError;

        @FXML
        private TextField sellerBalance;

        @FXML
        private Label sellerBalanceError;

        @FXML
        private TextField sellerLastName;

        @FXML
        private Label sellerLastNameError;

        @FXML
        private TextField sellerEmail;

        @FXML
        private Label sellerEmailError;

        @FXML
        private TextField sellerStoreName;

        @FXML
        private Label sellerStoreNameError;

        @FXML
        private TextField sellerUsername;

        @FXML
        private Label sellerUsernameError;

        @FXML
        private PasswordField sellerPassword;

        @FXML
        private Label sellerPasswordError;

        @FXML
        private Button sellerRegister;

        public static void display(Stage stage) {
            PopupStage = stage;
            PopupStage.setHeight(390);
            try {
                PopupStage.setScene(new Scene(View.loadFxml(Constants.FXMLs.registerPopup)));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initTexts();
            initVisibilities();
            initListeners();
            initActions();
        }

        private void initTexts() {
            sellerUsernameError.setText("Please enter a username");
            customerPasswordError.setText("Please enter a password");
            sellerPasswordError.setText("Please enter a password");
            customerFirstNameError.setText("Invalid entry!");
            sellerFirstNameError.setText("Invalid entry!");
            customerLastNameError.setText("Invalid entry!");
            sellerLastNameError.setText("Invalid entry!");
            customerPhoneNumberError.setText("Please enter a phone number");
            sellerPhoneNumberError.setText("Please enter a phone number");
            customerEmailError.setText("Invalid entry! enter a valid email address");
            sellerEmailError.setText("Please enter an email address");
            customerBalanceError.setText("Please enter your initial balance");
            sellerBalanceError.setText("Please enter your initial balance");
        }

        private void initVisibilities() {
            customerUsernameError.setVisible(false);
            sellerUsernameError.setVisible(false);
            customerPasswordError.setVisible(false);
            sellerPasswordError.setVisible(false);
            customerFirstNameError.setVisible(false);
            sellerFirstNameError.setVisible(false);
            customerLastNameError.setVisible(false);
            sellerLastNameError.setVisible(false);
            customerPhoneNumberError.setVisible(false);
            sellerPhoneNumberError.setVisible(false);
            customerEmailError.setVisible(false);
            sellerEmailError.setVisible(false);
            customerBalanceError.setVisible(false);
            sellerBalanceError.setVisible(false);
        }

        private void initListeners() {
            addListener(customerUsername, "\\w");
            addListener(sellerUsername, "\\w");
            addListener(customerPassword, "\\w");
            addListener(sellerPassword, "\\w");
            addListener(customerPhoneNumber, "[0-9]");
            addListener(sellerPhoneNumber, "[0-9]");
        }

        private void addListener(TextField textField, String regex) {
            textField.textProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue.length() == 0) return;
                char lastInput = newValue.charAt(newValue.length() - 1);
                if (!String.valueOf(lastInput).matches(regex)) textField.setText(oldValue);
            }));
        }

        private void initActions() {
            customerRegister.setOnAction(e -> {
                if (areCustomerFieldsAvailable()) {
                    try {
                        mainController.creatAccount(Constants.customerUserType, customerUsername.getText(),
                                customerPassword.getText(), customerFirstName.getText(), customerLastName.getText(),
                                customerEmail.getText(), customerPhoneNumber.getText(), Double.valueOf(customerBalance.getText()), null);
                        LoginPopupController.display(PopupStage);
                    } catch (Exceptions.UsernameAlreadyTakenException ex) {
                        customerUsernameError.setText("sorry! username already taken");
                        customerUsernameError.setVisible(true);
                    } catch (Exceptions.AdminRegisterException ex) {
                        //wont happen
                    }
                }
            });
            sellerRegister.setOnAction(e -> {
                if (areSellerFieldsAvailable()) {
                    try {
                        mainController.creatAccount(Constants.sellerUserType, sellerUsername.getText(),
                                sellerPassword.getText(), sellerFirstName.getText(), sellerLastName.getText(),
                                sellerEmail.getText(), sellerPhoneNumber.getText(), Double.valueOf(sellerBalance.getText()), null);
                        LoginPopupController.display(PopupStage);
                    } catch (Exceptions.UsernameAlreadyTakenException ex) {
                        sellerUsernameError.setText("sorry! username already taken");
                        sellerUsernameError.setVisible(true);
                    } catch (Exceptions.AdminRegisterException ex) {
                        //wont happen
                    }
                }
            });
            sellerLoginHL.setOnAction(e -> LoginPopupController.display(PopupStage));
            customerLoginHL.setOnAction(e -> LoginPopupController.display(PopupStage));
        }

        private boolean areCustomerFieldsAvailable() {
            boolean areAvailable = true;
            if (customerUsername.getText().equals("")) {
                customerUsernameError.setText("Please enter a username");
                customerUsernameError.setVisible(true);
                areAvailable = false;
            } else customerUsernameError.setVisible(false);
            if (customerPassword.getText().equals("")) {
                customerPasswordError.setVisible(true);
                areAvailable = false;
            } else customerPasswordError.setVisible(false);
            if (!customerFirstName.getText().matches(Constants.IRLNamePattern)) {
                customerFirstNameError.setVisible(true);
                areAvailable = false;
            } else customerFirstNameError.setVisible(false);
            if (!customerLastName.getText().matches(Constants.IRLNamePattern)) {
                customerLastNameError.setVisible(true);
                areAvailable = false;
            } else customerLastNameError.setVisible(false);
            if (customerPhoneNumber.getText().equals("")) {
                customerPhoneNumberError.setVisible(true);
                areAvailable = false;
            } else customerPhoneNumberError.setVisible(false);
            if (!customerEmail.getText().matches(Constants.emailPattern)) {
                customerEmailError.setVisible(true);
                areAvailable = false;
            } else customerEmailError.setVisible(false);
            if (!customerBalance.getText().matches(Constants.doublePattern)) {
                customerBalanceError.setVisible(true);
                areAvailable = false;
            } else customerBalanceError.setVisible(false);
            return areAvailable;
        }


        private boolean areSellerFieldsAvailable() {
            boolean areAvailable = true;
            if (sellerUsername.getText().equals("")) {
                sellerUsernameError.setText("Please enter a username");
                sellerUsernameError.setVisible(true);
                areAvailable = false;
            } else sellerUsernameError.setVisible(false);
            if (sellerPassword.getText().equals("")) {
                sellerPasswordError.setVisible(true);
                areAvailable = false;
            } else sellerPasswordError.setVisible(false);
            if (!sellerFirstName.getText().matches(Constants.IRLNamePattern)) {
                sellerFirstNameError.setVisible(true);
                areAvailable = false;
            } else sellerFirstNameError.setVisible(false);
            if (!sellerLastName.getText().matches(Constants.IRLNamePattern)) {
                sellerLastNameError.setVisible(true);
                areAvailable = false;
            } else sellerLastNameError.setVisible(false);
            if (sellerPhoneNumber.getText().equals("")) {
                sellerPhoneNumberError.setVisible(true);
                areAvailable = false;
            } else sellerPhoneNumberError.setVisible(false);
            if (!sellerEmail.getText().matches(Constants.emailPattern)) {
                sellerEmailError.setVisible(true);
                areAvailable = false;
            } else sellerEmailError.setVisible(false);
            if (!sellerBalance.getText().matches(Constants.doublePattern)) {
                sellerBalanceError.setVisible(true);
                areAvailable = false;
            } else sellerBalanceError.setVisible(false);
            return areAvailable;
        }
    }

    public static class AdminProductManagingMenu implements Initializable{
        @FXML
        private TableView<ProductWrapper> productsTable;

        @FXML
        private TableColumn<ProductWrapper, String> idCol;

        @FXML
        private TableColumn<ProductWrapper, String> nameCOL;

        @FXML
        private TableColumn<ProductWrapper, String> categoryCOL;

        @FXML
        private TableColumn<ProductWrapper, Button> detailsCOL;

        @FXML
        private TableColumn<ProductWrapper, Button> removeCOL;

        @FXML
        private Label errorLBL;

        private ArrayList<ProductWrapper> allProducts;

        public static void display() {
            View.setMainPane(Constants.FXMLs.adminProductManagingMenu);
        }

        private class ProductWrapper {
            String id;
            String nameBrand;
            String category;
            Button detailBTN = new Button();
            Button removeBTN = new Button();

            public ProductWrapper(String[] productPack) {
                this(productPack[0], productPack[1] + "-" + productPack[2], productPack[3]);
            }

            public ProductWrapper(String id, String nameBrand, String category) {
                this.id = id;
                this.nameBrand = nameBrand;
                this.category = category;

                detailBTN.setOnAction(e -> ProductDetailMenu.display(id));
                detailBTN.getStyleClass().add("details-button");

                removeBTN.setOnAction(e -> {
                    try {
                        adminController.removeProduct(id);
                    } catch (Exceptions.InvalidProductIdException ex) {
                        ex.printStackTrace();
                    }
                });
                removeBTN.getStyleClass().add("remove-button");
            }
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            allProducts =  new ArrayList<>();
            for (String[] product : adminController.manageAllProducts()) {
                allProducts.add(new ProductWrapper(product));
            }

            initTable();
        }

        private void initTable() {
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameCOL.setCellValueFactory(new PropertyValueFactory<>("nameBrand"));
            categoryCOL.setCellValueFactory(new PropertyValueFactory<>("category"));
            detailsCOL.setCellValueFactory(new PropertyValueFactory<>("detailBTN"));
            removeCOL.setCellValueFactory(new PropertyValueFactory<>("removeBTN"));
        }
    }

    public static class AdminDiscountManagingMenuController implements Initializable {

        private static ArrayList<DiscountWrapper> allDiscountWrappers = new ArrayList<>();

        @FXML
        private TableView<DiscountWrapper> discounts;

        @FXML
        private TableColumn<DiscountWrapper, String> idCol;

        @FXML
        private TableColumn<DiscountWrapper, String> codeCOL;

        @FXML
        private TableColumn<DiscountWrapper, String> percentageCOL;

        @FXML
        private TableColumn<DiscountWrapper, String> startDateCOL;

        @FXML
        private TableColumn<DiscountWrapper, String> endDateCOL;

        @FXML
        private TableColumn<DiscountWrapper, Button> detailsCOL;

        @FXML
        private TableColumn<DiscountWrapper, Button> removeCOL;

        @FXML
        private Label errorLBL;

        @FXML
        private Button addDiscountBTN;

        public static void display() {
            View.setMainPane(Constants.FXMLs.adminDiscountManagingMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initActions();
            initDiscounts();
            initTable();
        }

        private void initActions() {
            addDiscountBTN.setOnAction(e -> AdminDiscountManagingPopupController.display(null));
        }
        private void initDiscounts() {
            allDiscountWrappers.clear();

            for (String discount : adminController.viewDiscountCodes()) {
                String[] details;
                try {
                    details = adminController.viewDiscountCode(discount);
                } catch (Exceptions.DiscountCodeException e) {
                    e.printStackTrace();
                    return;
                }
                allDiscountWrappers.add(new DiscountWrapper(details));
            }
        }

        private void initTable() {
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            codeCOL.setCellValueFactory(new PropertyValueFactory<>("code"));
            percentageCOL.setCellValueFactory(new PropertyValueFactory<>("percentage"));
            startDateCOL.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            endDateCOL.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            removeCOL.setCellValueFactory(new PropertyValueFactory<>("remove"));
            detailsCOL.setCellValueFactory(new PropertyValueFactory<>("detail"));

            discounts.setItems(FXCollections.observableArrayList(allDiscountWrappers));
        }

        private  class DiscountWrapper {
            String id;
            String code;
            double percentage;
            double maximumAmount;
            Button detail = new Button();
            Button remove = new Button();
            String startDate;
            String endDate;

            public DiscountWrapper(String[] details) {
                this(details[0], details[1], details[2], details[3], Double.parseDouble(details[5]), Double.parseDouble(details[4]));
            }

            DiscountWrapper(String id, String code, String startDate, String endDate, double percentage, double maximumAmount) {
                this.id = id;
                this.code = code;
                this.percentage = percentage;
                this.maximumAmount = maximumAmount;
                detail.setOnAction(e -> showDiscountDetails());
                remove.setOnAction(e -> {
                    try {
                        adminController.removeDiscountCode(code);
                    } catch (Exceptions.DiscountCodeException ex) {
                        ex.printStackTrace();
                    }
                });
                detail.getStyleClass().add("details-button");
                remove.getStyleClass().add("remove-button");
            }

            private void showDiscountDetails() {
                ArrayList<String[]> customersWithCode;
                try {
                    customersWithCode = adminController.peopleWhoHaveThisDiscount(code);
                } catch (Exceptions.DiscountCodeException e) {
                    e.printStackTrace();
                    return;
                }

                AdminDiscountManagingPopupController.display( this);
            }

            public String getId() {
                return id;
            }

            public String getCode() {
                return code;
            }

            public String getPercentage() {
                return percentage + "%";
            }

            public double getMaximumAmount() {
                return maximumAmount;
            }

            public Button getDetail() {
                return detail;
            }

            public Button getRemove() {
                return remove;
            }

            public String getStartDate() {
                return startDate;
            }

            public String getEndDate() {
                return endDate;
            }
        }
    }

    public static class AdminRequestManagingMenuController implements Initializable {

        public static void display() {
            View.setMainPane(Constants.FXMLs.adminRequestManagingMenu);
        }

        @FXML
        private TableView<RequestWrapper> pending;

        @FXML
        private TableColumn<RequestWrapper, String> pendingIdCol;

        @FXML
        private TableColumn<RequestWrapper, String> pendingTypeCOL;

        @FXML
        private TableColumn<RequestWrapper, String> pendingDateCOL;

        @FXML
        private TableColumn<RequestWrapper, Button> pendingDetailsCOL;

        @FXML
        private TableColumn<RequestWrapper, HBox> pendingAcceptCOL;

        @FXML
        private TableView<RequestWrapper> archive;

        @FXML
        private TableColumn<RequestWrapper, String> archiveIdCol;

        @FXML
        private TableColumn<RequestWrapper, String> archiveTypeCOL;

        @FXML
        private TableColumn<RequestWrapper, String> archiveDateCOL;

        @FXML
        private TableColumn<RequestWrapper, Button> archiveDetailsCOL;

        @FXML
        private TableColumn<RequestWrapper, String> archiveStatusCOL;

        ArrayList<RequestWrapper> pendingRequests;
        ArrayList<RequestWrapper> archivedRequests;

        public class RequestWrapper {
            String id, type, date, status;
            Button decline = new Button();
            Button details = new Button();
            Button accept = new Button();
            HBox acceptDecline = new HBox(accept, decline);

            public RequestWrapper(String[] info) {
                this(info[0], info[1], info[2], info[3]);
            }

            public RequestWrapper(String id, String type, String date, String status) {
                this.id = id;
                this.type = type;
                this.date = date;
                this.status = status;

                initButtons();
            }

            private void initButtons() {
                decline.getStyleClass().add("decline-button");
                details.getStyleClass().add("details-button");
                accept.getStyleClass().add("accept-button");

                acceptDecline.setAlignment(Pos.CENTER);

                decline.setOnAction(e -> {
                    try {
                        adminController.acceptRequest(id, false);
                    } catch (Exceptions.InvalidRequestIdException ex) {
                        ex.printStackTrace();
                        return;
                    }
                    status = Constants.REQUEST_DECLINE;
                    archive.getItems().add(this);
                    archivedRequests.add(this);

                    pending.getItems().remove(this);
                    pendingRequests.remove(this);
                });
//                TODO: wtf.
//                details.setOnAction(e -> );

                accept.setOnAction(e -> {
                    try {
                        adminController.acceptRequest(id, true);
                    } catch (Exceptions.InvalidRequestIdException ex) {
                        ex.printStackTrace();
                        return;
                    }
                    status = Constants.REQUEST_ACCEPT;
                    archive.getItems().add(this);
                    archivedRequests.add(this);

                    pending.getItems().remove(this);
                    pendingRequests.remove(this);
                });
            }

            public String getId() {
                return id;
            }

            public String getType() {
                return type;
            }

            public String getDate() {
                return date;
            }

            public String getStatus() {
                return status;
            }

            public Button getDecline() {
                return decline;
            }

            public Button getDetails() {
                return details;
            }

            public Button getAccept() {
                return accept;
            }

            public HBox getAcceptDecline() {
                return acceptDecline;
            }
        }


        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initRequests();
            initTable();
        }

        private void initRequests() {
            pendingRequests = new ArrayList<>();
            for (String[] pendingRequest : adminController.getPendingRequests()) {
                pendingRequests.add(new RequestWrapper(pendingRequest));
            }

            archivedRequests = new ArrayList<>();
            for (String[] archivedRequest : adminController.getArchivedRequests()) {
                archivedRequests.add(new RequestWrapper(archivedRequest));
            }
        }

        private void initTable() {
            pendingIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            pendingTypeCOL.setCellValueFactory(new PropertyValueFactory<>("type"));
            pendingDetailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
            pendingAcceptCOL.setCellValueFactory(new PropertyValueFactory<>("acceptDecline"));
            pendingDateCOL.setCellValueFactory(new PropertyValueFactory<>("date"));

            archiveIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            archiveTypeCOL.setCellValueFactory(new PropertyValueFactory<>("type"));
            archiveDateCOL.setCellValueFactory(new PropertyValueFactory<>("date"));
            archiveDetailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
            archiveStatusCOL.setCellValueFactory(new PropertyValueFactory<>("status"));

            pending.getItems().addAll(pendingRequests);
            archive.getItems().addAll(archivedRequests);
        }
    }

    public static class AdminCategoryManagingMenuController implements Initializable{

        ArrayList<CategoryWrapper> wrappers;

        @FXML
        private TableView<CategoryWrapper> categories;

        @FXML
        private TableColumn<CategoryWrapper, String> idCOL;

        @FXML
        private TableColumn<CategoryWrapper, String> nameCOL;

        @FXML
        private TableColumn<CategoryWrapper, String> parentCOL;

        @FXML
        private TableColumn<CategoryWrapper, Button> detailsCOL;

        @FXML
        private TableColumn<CategoryWrapper, Button> removeCOL;

        @FXML
        private Label errorLBL;

        @FXML
        private Button addCategoryBTN;

        public class CategoryWrapper {
            String id, name, parent;
            Button remove, details;

            public CategoryWrapper(String[] info) {
                this(info[0], info[1], info[2]);
            }

            public CategoryWrapper(String id, String name, String parent) {
                this.id = id;
                this.name = name;
                this.parent = parent;
                remove = new Button();
                details = new Button();

                remove.setOnAction(e -> {
                    try {
                        adminController.removeCategory(name);
                        categories.getItems().remove(this);
                    } catch (Exceptions.InvalidCategoryException ex) {
                        ex.printStackTrace();
                    }
                });

                details.setOnAction(e -> AdminCategoryManagingPopupController.display(name));
            }


            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getParent() {
                return parent;
            }

            public Button getRemove() {
                return remove;
            }

            public Button getDetails() {
                return details;
            }
        }

        public static void display() {
            View.setMainPane(Constants.FXMLs.adminCategoryManagingMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initCategories();
            initTable();
            initActions();
        }

        private void initCategories() {
            var categories = adminController.manageCategories();
            wrappers = new ArrayList<>();
            for (String[] category : categories) {
                wrappers.add(new CategoryWrapper(category));
            }
        }

        private void initTable() {
            idCOL.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameCOL.setCellValueFactory(new PropertyValueFactory<>("name"));
            parentCOL.setCellValueFactory(new PropertyValueFactory<>("parent"));
            detailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
            removeCOL.setCellValueFactory(new PropertyValueFactory<>("remove"));

            this.categories.getItems().addAll(wrappers);
        }

        private void initActions() {
            addCategoryBTN.setOnAction(e -> AdminCategoryManagingPopupController.display(null));
        }
    }

    public static class AdminCategoryManagingPopupController {

        @FXML
        private TableView<?> properties;

        @FXML
        private TableColumn<?, ?> Property;

        @FXML
        private TableView<?> products;

        @FXML
        private TableColumn<?, ?> productCOL;

        @FXML
        private TableColumn<?, ?> productRemoveCOL;

        @FXML
        private TableView<?> subCategories;

        @FXML
        private TableColumn<?, ?> subCategoryCOL;

        @FXML
        private TableColumn<?, ?> subCategoryRemoveCOL;

        @FXML
        private Label errorLBL;

        @FXML
        private Label idLBL;

        @FXML
        private TextField codeField;

        @FXML
        private TextField maxField;

        @FXML
        private Button addBTN;

        @FXML
        private Button editBTN;

        @FXML
        private Button discardBTN;

        public static void display(String categoryName) {
            FXMLLoader loader = new FXMLLoader(View.class.getResource(View.getLocation(Constants.FXMLs.adminCategoryManagingPopup)));
            Parent p = null;
            try {
                p = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            AdminCategoryManagingPopupController controller = loader.getController();
            controller.init(categoryName);

            View.popupWindow("Add category", p, 650, 500);
        }

        private void init(String categoryName) {

        }
    }


    public static class SellerSalesManagingMenuController implements Initializable {
        private ArrayList<SaleWrapper> sellerSales = new ArrayList<>();

        @FXML
        private TableView<SaleWrapper> sales;

        @FXML
        private TableColumn<SaleWrapper, String> idCol;

        @FXML
        private TableColumn<SaleWrapper, String> percentageCOL;

        @FXML
        private TableColumn<SaleWrapper, String> startDateCOL;

        @FXML
        private TableColumn<SaleWrapper, String> endDateCOL;

        @FXML
        private TableColumn<SaleWrapper, Button> detailsCOL;

        @FXML
        private TableColumn<SaleWrapper, Button> removeCOL;

        @FXML
        private Label errorLBL;

        @FXML
        private Button addSaleBTN;

        private class SaleWrapper {
            String id, seller, startDate, endDate;
            double percentage;
            int numOfProducts;
            Button remove = new Button();
            Button details = new Button();

            public SaleWrapper(String[] info) {
                this(info[0], info[1], info[3], info[4], Double.parseDouble(info[2]), Integer.parseInt(info[5]));
            }

            public SaleWrapper(String id, String seller, String startDate, String endDate, double percentage, int numOfProducts) {
                this.id = id;
                this.seller = seller;
                this.startDate = startDate;
                this.endDate = endDate;
                this.percentage = percentage;
                this.numOfProducts = numOfProducts;

                remove.getStyleClass().add("remove-button");
                remove.setOnAction(e -> {
                    //TODO:
                    //sellerController.sale
                    sales.getItems().remove(this);
                });
                details.getStyleClass().add("details-button");
                //TODO:
                //details.setOnAction(e -> );

            }

            public Property percentageProperty() {
                SimpleStringProperty percentageProperty = new SimpleStringProperty();
                percentageProperty.bind(new SimpleStringProperty(String.valueOf(percentage)).concat("%"));
                return percentageProperty;
            }
        }

        public static void display() {
            View.setMainPane(Constants.FXMLs.sellerSaleManagingMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            for (String[] sale : sellerController.viewSales()) {
                sellerSales.add(new SaleWrapper(sale));
            }

            initTable();
            initActions();
        }

        private void initTable() {
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            percentageCOL.setCellValueFactory(new PropertyValueFactory<>("percentage"));
            startDateCOL.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            endDateCOL.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            detailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
            removeCOL.setCellValueFactory(new PropertyValueFactory<>("remove"));
        }

        private void initActions() {
            //TODO:
            //addSaleBTN.setOnAction(e -> );
        }
    }

    public static class SellerProductManagingMenuController implements Initializable{
        public static void display() {
            View.setMainPane(Constants.FXMLs.sellerProductManagingMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }

    public static class SellerLogMenuController implements Initializable {
        public static void display() {
            View.setMainPane(Constants.FXMLs.sellerLogsMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
        }
    }

    //add product detail menu
    public static class ShoppingCartMenuController implements Initializable {
        //TODO decrease/increase buttons.

        private static ArrayList<String[]> cartProducts = new ArrayList<>();
        private static ArrayList<SubProductWrapper> subProducts = new ArrayList<>();
        private SimpleDoubleProperty totalPriceProperty = new SimpleDoubleProperty(0);
        NumberBinding totalPriceBinding = new SimpleDoubleProperty(0).add(0);

        private class SubProductWrapper {
            String subProductId;
            String productId;
            Button nameBrandSeller;
            double unitPrice;
            SimpleIntegerProperty countProperty = new SimpleIntegerProperty();
            TextField countField;
            HBox countGroup = new HBox();
            SimpleDoubleProperty totalPrice;
            Button remove;

            public SubProductWrapper(String[] productInCartPack) {
                this(productInCartPack[0], productInCartPack[1],
                        productInCartPack[2] + " " + productInCartPack[3] + " (" + productInCartPack[4] + ")",
                        Double.parseDouble(productInCartPack[7]), Integer.parseInt(productInCartPack[6]));
            }

            public SubProductWrapper(String id, String productId, String nameBrandSeller, double unitPrice, int count) {
                this.subProductId = id;
                this.productId =productId;
                this.nameBrandSeller = new Button(nameBrandSeller);
                this.nameBrandSeller.setOnAction(e -> ProductDetailMenu.display(productId));
                this.unitPrice = unitPrice;
                this.countProperty.set(count);
                this.totalPrice.bind(new SimpleDoubleProperty(unitPrice).multiply(countProperty));
                remove = new Button();
                remove.getStyleClass().add("remove-button");
                remove.setOnAction(e -> {
                    try {
                        mainController.removeSubProduct(this.subProductId);
                        productsTable.getItems().remove(this);
                    } catch (Exceptions.InvalidSubProductIdException ex) {
                        ex.printStackTrace();
                    }
                });
            }

            public SimpleDoubleProperty getTotalPriceProperty() {
                return totalPrice;
            }
        }

        @FXML
        private TableColumn<SubProductWrapper, Button> removeCOL;

        @FXML
        private TableView<SubProductWrapper> productsTable;

        @FXML
        private TableColumn<SubProductWrapper, String> productName;

        @FXML
        private TableColumn<SubProductWrapper, Double> productUnitPrice;

        @FXML
        private TableColumn<SubProductWrapper, Integer> count;

        @FXML
        private TableColumn<SubProductWrapper, Double> totalPrice;

        @FXML
        private Button clearCartBTN;

        @FXML
        private Label errorLBL;

        @FXML
        private Label totalPriceLBL;

        @FXML
        private Button purchaseBTN;

        public static void display() {
            try {
                cartProducts = mainController.getProductsInCart();
            } catch (Exceptions.UnAuthorizedAccountException e) {
                e.printStackTrace();
                return;
            }
            View.setMainPane(Constants.FXMLs.shoppingCartMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            productsTable.addEventFilter(ScrollEvent.ANY, event -> {
                if (event.getDeltaX() != 0)
                    event.consume();
            });

            iniTable();
            initLabels();
            initActions();
        }

        private void initActions() {
            purchaseBTN.setOnAction(e -> PurchaseMenuController.display());

            clearCartBTN.setOnAction(e -> {
                if (cartProducts.size() == 0) {
                    errorLBL.setText("the cart is already empty!");
                } else {
                    mainController.clearCart();
                }
            });
        }

        private void initLabels() {
            errorLBL.setText("");

            for (SubProductWrapper subProduct : subProducts) {
                if (totalPriceBinding == null) {
                    totalPriceBinding = subProduct.getTotalPriceProperty().add(0);
                } else {
                    totalPriceBinding = totalPriceBinding.add(subProduct.getTotalPriceProperty());
                }
            }
            totalPriceProperty.bind(totalPriceBinding);

            totalPriceLBL.textProperty().bind(totalPriceBinding.asString().concat("$"));
        }

        private void iniTable() {
            initCols();

            //TODO: name-brand(storeName)
            for (String[] cartProduct : cartProducts) {
                subProducts.add(new SubProductWrapper(cartProduct));
            }

            productsTable.setItems(FXCollections.observableArrayList(subProducts));
        }

        private void initCols() {
            productName.setCellValueFactory(new PropertyValueFactory<>("nameBrandSeller"));
            productUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
            count.setCellValueFactory(new PropertyValueFactory<>("count"));
            totalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
            removeCOL.setCellValueFactory(new PropertyValueFactory<>("remove"));
        }
    }

    public static class PurchaseMenuController {
        public static void display() {
            View.setMainPane(Constants.FXMLs.purchaseMenu);
        }
    }

    //TODO: can be added to CustomerMenu??
    public static class CustomerOrderLogMenu {

    }

    public static class AdminManagingMenuController implements Initializable {
        public static void display() {
            View.setMainPane(Constants.FXMLs.adminManagingMenu);
        }

        @FXML
        private Button manageAccounts;

        @FXML
        private Button manageProducts;

        @FXML
        private Button manageCategories;

        @FXML
        private Button manageDiscounts;

        @FXML
        private Button manageRequests;

        @Override
        public void initialize(URL location, ResourceBundle resources) {

            initActions();
        }

        private void initActions() {
            manageAccounts.setOnAction(e -> AdminAccountManagingMenuController.display());
            manageCategories.setOnAction(e -> AdminCategoryManagingMenuController.display());
            manageDiscounts.setOnAction(e -> AdminDiscountManagingMenuController.display());
            manageProducts.setOnAction(e -> AdminProductManagingMenu.display());
            manageRequests.setOnAction(e -> AdminRequestManagingMenuController.display());
        }
    }

    public static class AdminAccountManagingMenuController implements Initializable {

        public static ArrayList<AccountWrapper> customers = new ArrayList<>();
        public static ArrayList<AccountWrapper> sellers = new ArrayList<>();

        public class AccountWrapper {
            String id, username, firstName, lastName, phone, email, fullName;
            SimpleStringProperty type;
            Button remove = new Button();
            Button details = new Button();

            public AccountWrapper(String[] pack) {
                this(pack[0], pack[1], pack[2], pack[3], pack[4], pack[5], pack[6]);
            }

            public AccountWrapper(String id, String username, String firstName, String lastName, String phone, String email, String type) {
                this.id = id;
                this.username = username;
                this.firstName = firstName;
                this.lastName = lastName;
                this.phone = phone;
                this.email = email;
                this.type  = new SimpleStringProperty(type);
                fullName = firstName + " " + lastName;

                this.remove.getStyleClass().add("remove-button");
                this.details.getStyleClass().add("details-button");
                initButtons();
            }

            private void initButtons() {
                remove.setOnAction(e -> {
                    try {
                        adminController.deleteUsername(username);
                        //assumed its in customers.
                        accounts.getItems().remove(this);
                    } catch (Exceptions.UsernameDoesntExistException ex) {
                        ex.printStackTrace();
                    } catch (Exceptions.ManagerDeleteException ex) {
                        errorLBL.setText("You cannot remove omni-admin");
                    }
                });

                remove.disableProperty().bind(
                        Bindings.when(View.isManager.or(type.isEqualTo(Constants.sellerUserType)).or(type.isEqualTo(Constants.customerUserType)))
                        .then(false).otherwise(true)
                );
                remove.opacityProperty().bind(Bindings.when(remove.disableProperty()).then(0.5).otherwise(1));

                details.setOnAction(e -> PersonalInfoMenuController.display(username));
            }

            public String getId() {
                return id;
            }

            public String getUsername() {
                return username;
            }

            public String getFirstName() {
                return firstName;
            }

            public String getLastName() {
                return lastName;
            }

            public String getPhone() {
                return phone;
            }

            public String getEmail() {
                return email;
            }

            public String getFullName() {
                return fullName;
            }

            public Button getRemove() {
                return remove;
            }

            public Button getDetails() {
                return details;
            }
        }

        @FXML
        private TableView<AccountWrapper> accounts;

        @FXML
        private TableColumn<AccountWrapper, String> idCOL;

        @FXML
        private TableColumn<AccountWrapper, String> usernameCOL;

        @FXML
        private TableColumn<AccountWrapper, String> fullNameCOL;

        @FXML
        private TableColumn<AccountWrapper, String> phoneCOL;

        @FXML
        private TableColumn<AccountWrapper, Button> detailsCOL;

        @FXML
        private TableColumn<AccountWrapper, Button> removeCOL;

        @FXML
        private Label errorLBL;

        @FXML
        private Button registerAdminBTN;

        public static void display() {
            View.setMainPane(Constants.FXMLs.adminAccountManagingMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initActions();
            initTable();
            initAccounts();
        }

        private void initActions() {
            //TODO: registerAdminBTN.setOnAction(e -> AdminRegistrationPopup.display());
        }

        private void initTable() {
            idCOL.setCellValueFactory(new PropertyValueFactory<>("id"));
            usernameCOL.setCellValueFactory(new PropertyValueFactory<>("username"));
            fullNameCOL.setCellValueFactory(new PropertyValueFactory<>("fullName"));
            phoneCOL.setCellValueFactory(new PropertyValueFactory<>("phone"));
            detailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
            removeCOL.setCellValueFactory(new PropertyValueFactory<>("remove"));
        }

        private void initAccounts(){
            ArrayList<String[]> all = adminController.manageUsers();
            for (String[] strings : all) {
                if (strings[6].equals("Seller")) {
                    sellers.add(new AccountWrapper(strings));
                } else if (strings[6].equals("Customer")) {
                    customers.add(new AccountWrapper(strings));
                } else {

                }
            }

            accounts.getItems().addAll(sellers);
            accounts.getItems().addAll(customers);
        }
    }

    public static class AdminCategoryManagingPopup implements Initializable {
        public static void display() {

        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }

    public static class AdminDiscountManagingPopupController {

        @FXML
        private TableView<CustomerWrapper> customersTable;

        @FXML
        private TableColumn<CustomerWrapper, String> usernameCOL;

        @FXML
        private TableColumn<CustomerWrapper, TextField> countCOL;

        @FXML
        private TableColumn<CustomerWrapper, CheckBox> removeCOL;

        @FXML
        private Label errorLBL;

        @FXML
        private Label idLBL;

        @FXML
        private TextField codeField;

        @FXML
        private TextField percentageField;

        @FXML
        private TextField maxField;

        @FXML
        private DatePicker startDate;

        @FXML
        private DatePicker endDate;

        @FXML
        private Button addBTN;

        @FXML
        private Button editBTN;

        @FXML
        private Button discardBTN;

        @FXML
        private HBox saveDiscardHBox;

        private AdminDiscountManagingMenuController.DiscountWrapper discount;
        private ArrayList<CustomerWrapper> allCustomers;

        private SimpleBooleanProperty codeFieldChanged = new SimpleBooleanProperty(false);
        private SimpleBooleanProperty percentageFieldChanged = new SimpleBooleanProperty(false);
        private SimpleBooleanProperty maxFieldChanged = new SimpleBooleanProperty(false);
        private SimpleBooleanProperty startDateChanged = new SimpleBooleanProperty(false);
        private SimpleBooleanProperty endDateChanged = new SimpleBooleanProperty(false);

        private class CustomerWrapper {
            CheckBox hasCode;
            String id;
            String username;
            TextField count;

            public CustomerWrapper(String[] customerPack, boolean hasCode) {
                this(customerPack[2], customerPack[0], Integer.parseInt(customerPack[1]), true);
            }

            public CustomerWrapper(String id, String username, int count, boolean hasCode) {
                if (hasCode) this.count.setText(count + "");
                else this.count.setText("5");
                this.id = id;
                this.username = username;
                this.hasCode.setSelected(hasCode);
                this.count.editableProperty().bind(this.hasCode.selectedProperty());
                this.count.opacityProperty().bind(
                        Bindings.when(this.hasCode.selectedProperty()).then(1).otherwise(0.5)
                );
                //TODO: count listener and save changes handle customers
            }

            public Property hasCodeProperty() {
                return hasCode.selectedProperty();
            }
        }

        public static void display(AdminDiscountManagingMenuController.DiscountWrapper discount) {

            FXMLLoader loader = new FXMLLoader(View.class.getResource(View.getLocation(Constants.FXMLs.adminDiscountManagingPopup)));
            Parent p = null;
            try {
                p = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            AdminDiscountManagingPopupController controller = loader.getController();
            controller.init(discount);
            View.popupWindow((discount == null) ? "Create Discount":"Discount Details", p, 800, 500);
        }

        private void init(AdminDiscountManagingMenuController.DiscountWrapper discount) {
            //TODO: should be checked.
            this.discount = discount;
            ArrayList<String[]> customersWithCode = null;
            try {
                customersWithCode = adminController.peopleWhoHaveThisDiscount(discount.id);
            } catch (Exceptions.DiscountCodeException e) {
                e.printStackTrace();
                return;
            }

            allCustomers = new ArrayList<>();
            for (String[] customer : customersWithCode) {
                allCustomers.add(new CustomerWrapper(customer, true));
            }

            ArrayList<String[]> allUsers = adminController.manageUsers();
            allUsers.removeAll(customersWithCode.stream().map(s -> new String[]{s[2], s[0]}).collect(Collectors.toList()));

            for (String[] user: allUsers) {
                allCustomers.add(new CustomerWrapper(user[2], user[0], 0, false));
            }

            initBindings();
            initVisibility();
            initActions();
            initTable();
            initValues();
        }

        private void initBindings() {
            codeFieldChanged.bind(Bindings.when(codeField.textProperty().isEqualTo(discount.code)).then(false).otherwise(true));
            percentageFieldChanged.bind(Bindings.when(percentageField.textProperty().isEqualTo(discount.percentage + "")).then(false).otherwise(true));
            maxFieldChanged.bind(Bindings.when(maxField.textProperty().isEqualTo(discount.maximumAmount + "")).then(false).otherwise(true));
            startDateChanged.bind(Bindings.when(startDate.accessibleTextProperty().isEqualTo(discount.startDate)).then(false).otherwise(true));
            endDateChanged.bind(Bindings.when(endDate.accessibleTextProperty().isEqualTo(discount.endDate)).then(false).otherwise(true));
        }

        private void initVisibility() {
            boolean isDetail = discount != null;
            saveDiscardHBox.setVisible(isDetail);
            addBTN.setVisible( ! isDetail);
            codeField.editableProperty().setValue( ! isDetail);

            editBTN.opacityProperty().bind(
                    Bindings.createObjectBinding(() -> {
                        if (codeFieldChanged.get() || percentageFieldChanged.get() || maxFieldChanged.get()
                        || endDateChanged.get() || startDateChanged.get()) return 1;
                        else return 0.5;
                    }, codeFieldChanged, percentageFieldChanged, maxFieldChanged, endDateChanged, startDateChanged)
            );

            editBTN.disableProperty().bind(editBTN.opacityProperty().isEqualTo(1));
            discardBTN.opacityProperty().bind(editBTN.opacityProperty());
            discardBTN.disableProperty().bind(editBTN.disableProperty());
        }

        private void initActions() {
            addBTN.setOnAction(e -> {
                if (fieldValidation()) {
                    try {
                        adminController.createDiscountCode(codeField.getText(), startDate.getValue().toString(), endDate.getValue().toString(),
                                Double.parseDouble(percentageField.getText()), Double.parseDouble(maxField.getText()),
                                allCustomers.stream().filter(c -> c.hasCode.isSelected()).map(c -> new String[] {c.id, String.valueOf(c.count)}).collect(Collectors.toCollection(ArrayList::new)));
                        customersTable.getScene().getWindow().hide();
                    } catch (Exceptions.InvalidAccountsForDiscount invalidAccountsForDiscount) {
                        invalidAccountsForDiscount.printStackTrace();
                    } catch (Exceptions.InvalidFormatException ex) {
                        ex.printStackTrace();
                        printError("Invalid date format question mark.");
                    } catch (Exceptions.ExistingDiscountCodeException ex) {
                        ex.printStackTrace();
                        printError("This code already exists!");
                    }
                }
            });

            editBTN.setOnAction(e -> {
                if (fieldValidation()) {
                    try {
                        if (percentageFieldChanged.get()) {
                            adminController.editDiscountCode(discount.code, "percentage", percentageField.getText());
                            discount.percentage = Double.parseDouble(percentageField.getText());
                        }
                        if (maxFieldChanged.get()) {
                            adminController.editDiscountCode(discount.code, "maximum amount", maxField.getText());
                            discount.maximumAmount = Double.parseDouble(maxField.getText());
                        }
                        if (startDateChanged.get()) {
                            adminController.editDiscountCode(discount.code, "start date", startDate.getAccessibleText());
                            discount.startDate = startDate.getAccessibleText();
                        }
                        if (endDateChanged.get()) {
                            adminController.editDiscountCode(discount.code, "end date", endDate.getAccessibleText());
                            discount.endDate = endDate.getAccessibleText();
                        }

                        errorLBL.setTextFill(Color.GREEN);
                        errorLBL.setText("Changes saved successfully!");
                    } catch (Exception ex) {
                        printError(ex.getMessage());
                    }
                }
            });

            discardBTN.setOnAction(e -> initValues());
        }

        private void printError(String errorText) {
            errorLBL.setTextFill(Color.RED);
            errorLBL.setText(errorText);
        }

        private boolean fieldValidation() {
            if ( ! codeField.getText().matches("^\\w+$")) {
                printError("Invalid discount code! use only characters, digits and _ .");
                return false;
            } else if ( ! percentageField.getText().matches(Constants.doublePattern)) {
                printError("Invalid percentage! enter a floating point number (ex. 50.5)");
                return false;
            } else if ( ! maxField.getText().matches(Constants.doublePattern)) {
                printError("Invalid maximum amount! enter a floating point number (ex. 40.5)");
                return false;
            } else if ( ! startDate.accessibleTextProperty().get().matches(Constants.datePattern)) {
                printError("Please enter a valid starting date");
                return false;
            } else if (( ! endDate.accessibleTextProperty().get().matches(Constants.datePattern) ) || endDate.getValue().compareTo(startDate.getValue()) <= 0) {
                printError("Please enter a valid ending date.");
                return false;
            } else return true;
        }

        private void initTable() {
            //CheckBox hasCode;
            //            String id;
            //            String username;
            //            TextField count;
            countCOL.setCellValueFactory(new PropertyValueFactory<>("count"));
            removeCOL.setCellValueFactory(new PropertyValueFactory<>("hasCode"));
            usernameCOL.setCellValueFactory(new PropertyValueFactory<>("username"));
        }

        private void initValues() {
            codeField.setText(discount.code);
            percentageField.setText(discount.percentage + "");
            maxField.setText(discount.maximumAmount + "");
            startDate.setValue(LocalDate.parse(discount.startDate));
            endDate.setValue(LocalDate.parse(discount.endDate));
        }
    }

    public static class AdminRequestManagingPopup implements Initializable {
        public static void display() {

        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }

    public static class AdminProductManagingPopup implements Initializable {
        public static void display() {

        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }

    public static class AdminAccountManagingPopup implements Initializable {
        public static void display() {

        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }

    public static class SellerSaleManagingPopup implements Initializable {
        public static void display() {

        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }

    public static class SellerProductManagingPopup implements Initializable {
        public static void display() {

        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }

    public static class CategoryPopup implements Initializable {
        public static void display() {

        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }

    public static class SellerManagingMenuController implements Initializable {
        @FXML
        private Button manageProducts;

        @FXML
        private Button manageSales;

        @FXML
        private Button sellLogs;
        public static void display() {
            View.setMainPane(Constants.FXMLs.sellerManagingMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initActions();
        }

        private void initActions() {
            manageProducts.setOnAction(e -> SellerProductManagingMenuController.display());
            manageSales.setOnAction(e -> SellerSalesManagingMenuController.display());
        }
    }

    public static class SellerLogsManagingMenuController {

    }

    public static class BaseController implements Initializable {
        private static BaseController currentBase;

        @FXML
        private BorderPane mainPane;
        @FXML
        private Button backBTN;
        @FXML
        private Button logoBTN;
        @FXML
        private TextField searchField;
        @FXML
        private Button searchBTN;
        @FXML
        private Button accountBTN;
        @FXML
        private Button loginBTN;
        @FXML
        private Button cartBTN;
        @FXML
        private Button manageBTN;

        public static BaseController getCurrentBase() {
            return currentBase;
        }

        public static void setMainPane(Parent parent) {
            currentBase.mainPane.setCenter(parent);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            currentBase = this;
            initButtons();

        }

        private void initButtons() {
            initActions();
            initTexts();
            initVisibility();
        }

        private void initVisibility() {
            View.type.set(mainController.getType());
            cartBTN.visibleProperty().bind(
                    Bindings.when(View.type.isEqualTo(Constants.adminUserType).or(View.type.isEqualTo(Constants.sellerUserType)))
                            .then(false).otherwise(true)
            );
            manageBTN.visibleProperty().bind(cartBTN.visibleProperty().not());
            loginBTN.visibleProperty().bind(
                    Bindings.when(View.type.isEqualTo(Constants.anonymousUserType))
                            .then(true).otherwise(false)
            );
            accountBTN.visibleProperty().bind(loginBTN.visibleProperty().not());
            backBTN.visibleProperty().bind(View.getStackSizeProperty().greaterThan(1));
        }

        private void initActions() {
            logoBTN.setOnAction(e -> MainMenuController.display());
            accountBTN.setOnAction(e -> PersonalInfoMenuController.display(null));
            loginBTN.setOnAction(e -> LoginPopupController.display(new Stage()));
            cartBTN.setOnAction(e -> ShoppingCartMenuController.display());
            searchBTN.setOnAction(e -> search(searchField.getText()));
            manageBTN.setOnAction(e -> {
                switch (mainController.getType()) {
                    case Constants.adminUserType:
                        AdminManagingMenuController.display();
                        break;
                    case Constants.sellerUserType:
                        SellerManagingMenuController.display();
                }
            });
            backBTN.setOnAction(e -> {
                if (View.getStackTrace().size() < 2) return;

                View.goBack();
            });
        }

        private void initTexts() {
            accountBTN.textProperty().bind(
                    Bindings.createObjectBinding(() -> {
                        try {
                            String username = mainController.viewPersonalInfo()[0];
                            return username;
                        } catch (Exceptions.NotLoggedInException e) {
                            return null;
                        }
                    }, View.type)
            );
            //TODO: temporary
        }

        private void search(String input) {
            if (input != null) {
                ArrayList<String[]> products = getCurrentProducts();
                if (products != null) {
                    try {
                        ProductsMenuController.display(mainController.showProducts(getProductIDs(products),
                                null, false, new String[]{"false", "0", "0", input, null, null, "0"},
                                new HashMap<>()));
                    } catch (Exceptions.InvalidProductIdException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        //search utils.
        private ArrayList<String[]> getCurrentProducts() {
            try {
                return (ArrayList<String[]>) mainController.getProductsOfThisCategory(Constants.SUPER_CATEGORY_NAME).clone();
            } catch (Exceptions.InvalidCategoryException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }

        private ArrayList<String> getProductIDs(ArrayList<String[]> currentProducts) {
            ArrayList<String> productIDS = new ArrayList<>();
            for (String[] product : currentProducts) {
                productIDS.add(product[0]);
            }
            return productIDS;
        }
    }
}