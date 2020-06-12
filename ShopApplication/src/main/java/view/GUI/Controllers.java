package view.GUI;

import controller.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

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

    public static class PersonalInfoMenuController {
        public static void display() {

        }
    }

    public static class MainMenu {
        private static void display() {

        }
    }

    public static class ProductsMenuController {
        public static void display(ArrayList<String[]> products) {

        }
    }

    public static class ProductDetailMenu {
        public static void display(String[] subProduct) {

        }
    }

    //TODO: deprecated: added in product detail menu
    public static class ProductReviewMenu {

    }

    //TODO: deprecated: added in products menu and sale menu
    public static class SortMenu {

    }

    //TODO: deprecated: added in products menu and sale menu
    public static class FilterMenu {

    }

    public static class SaleMenu {
       public static void display() {

       }
    }

    //TODO: controls loginPopUp
    public static class LoginPopUpController implements Initializable {
        private static Stage popUpStage;

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
            popUpStage = stage;
            popUpStage.setWidth(480);
            popUpStage.setHeight(320);
            popUpStage.setResizable(false);
            try {
                popUpStage.setScene(new Scene(View.loadFxml("LoginPopUp")));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            popUpStage.initModality(Modality.APPLICATION_MODAL);
            popUpStage.show();
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            errorLBL.setText("");
            initListeners();
            initActions();
        }

        private void initListeners() {
            usernameField.textProperty().addListener(((observable, oldValue, newValue) -> {
                char lastInput = newValue.charAt(newValue.length() - 1);
                if (String.valueOf(lastInput).matches("\\W"))  usernameField.setText(oldValue);
            }));
            passwordField.textProperty().addListener(((observable, oldValue, newValue) -> {
                char lastInput = newValue.charAt(newValue.length() - 1);
                if (String.valueOf(lastInput).matches("\\W"))  passwordField.setText(oldValue);
            }));
        }

        private void initActions() {
            loginBTN.setOnAction(e -> {
                String username = usernameField.getText();
                String password = passwordField.getText();
                if (username == null || password == null) return;
                else {
                    try {
                        mainController.login(username, password);
                    } catch (Exceptions.UsernameDoesntExistException | Exceptions.WrongPasswordException ex) {
                        errorLBL.setText("invalid username or password");
                        errorLBL.setTextFill(Color.RED);
                        ex.printStackTrace();
                    }
                }
            });
            registerLink.setOnAction(e -> RegisterPopUpController.display(popUpStage));
        }
    }

    public static class RegisterPopUpController implements Initializable {
        private static Stage popUpStage;

        public static void display(Stage stage) {
            popUpStage = stage;
            try {
                popUpStage.setScene(new Scene(View.loadFxml("RegisterPopUp")));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }


    public static class AdminUserManagingMenu {

    }

    //TODO: uses products menu but with different properties.
    public static class AdminProductManagingMenu {

    }


    public static class AdminDiscountManagingMenu {

    }

    public static class AdminRequestManagingMenu {

    }

    public static class AdminCategoryManagingMenu {

    }


    public static class SellerSalesMenu {

    }

    public static class SellerProductMenu {

    }

    //add product detail menu
    public static class ShoppingCartMenu {
        public static void display() {

        }
    }

    //TODO: can be added to CustomerMenu??
    public static class CustomerOrderLogMenu {

    }

    public static class AdminManagingMenuController implements Initializable{
        public static void display() {

        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }

    public static class SellerManagingMenuController implements Initializable {
        public static void display() {

        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }

    public static class ProductBoxController implements Initializable {

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {

        }
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
            logoBTN.setOnAction(e -> MainMenu.display());
            accountBTN.setOnAction(e -> PersonalInfoMenuController.display());
            loginBTN.setOnAction(e -> LoginPopUpController.display(new Stage()));
            cartBTN.setOnAction(e -> ShoppingCartMenu.display());
            searchBTN.setOnAction(e -> search(searchField.getText()));
            manageBTN.setOnAction(e -> {
                switch (mainController.getType()) {
                    case Constants.adminUserType:
                        AdminManagingMenuController.display();
                    case Constants.sellerUserType:
                        SellerManagingMenuController.display();
                }
            });
            backBTN.setOnAction(e -> {
                ArrayList<String> stackTrace = View.getStackTrace();
                if (stackTrace.size() < 2) return;
                else {
                    View.getStackSizeProperty().subtract(1);
                    View.setMainPane(stackTrace.get(stackTrace.size() - 1));
                }
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
            loginBTN.setText("Login");
            manageBTN.setText("manage");
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