package view;

import controller.*;

import java.util.ArrayList;

class Menus {
    private static Controller mainController;
    private static AdminController adminController;
    private static SellerController sellerController;
    private static CustomerController customerController;

    public static void init(){
        mainController = View.mainController;
        adminController = View.adminController;
        sellerController = View.sellerController;
        customerController = View.customerController;
    }

    public static class AccountMenu extends Menu {
        private Menu previousMenu;
        private Menu nextMenu;

        AccountMenu(String name) {
            super(name, false, null, Constants.Menus.accountMenuPattern, Constants.Menus.accountMenuCommand);
            Menu.setAccountMenu(this);
            previousMenu = null;
            nextMenu = null;
            initSubMenus();
            initSubActions();
        }

        @Override
        public void execute() {
            String userType = mainController.getType();
            for (Menu menu : subMenus.values()) {
                if (userType.matches(menu.getCommandPattern())) {
                    menu.run();
                }
            }
        }

        @Override
        public void show() {
            // no execution needed!
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new AnonymousUserAccountMenu("Login/Register Menu", this, this.previousMenu, this.nextMenu, Constants.anonymousUserType));
            subMenus.put(2, new AdminMenu("Admin Menu", this, this.previousMenu, this.nextMenu, Constants.adminUserType));
            subMenus.put(3, new SellerMenu("Seller Menu", this, this.previousMenu, this.nextMenu, Constants.sellerUserType));
            subMenus.put(4, new CustomerMenu("Customer Menu", this, this.previousMenu, this.nextMenu, Constants.customerUserType));
        }

        @Override
        protected void initSubActions() {
            //no actions available.
        }

        @Override
        public void run() {
            getStackTrace().push(this);
            previousMenu = Menu.getCallingMenu();
            nextMenu = null;
            this.execute();
        }

        void run(Menu previousMenu, Menu nextMenu) {
            this.previousMenu = previousMenu;
            this.nextMenu = nextMenu;
            this.execute();
        }
    }

    public static class FirstMenu extends Menu {
        FirstMenu(String name) {
            super(name, true, null, null, null);
            initSubMenus();
            initSubActions();
        }

        @Override
        public void initSubMenus() {
            subMenus.put(1, new SaleMenu("Sale menu", this));
            subMenus.put(2, new AllProductsMenu("products menu", this));
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ExitAction());
        }
    }
    
    public static class AllProductsMenu extends Menu {
        private ArrayList<String> categoryTree;
        private ArrayList<String[]> availableCategories;
        private String[] currentFilters;
        private StringBuilder currentSort;
        private ArrayList<String[]> currentProducts;

        AllProductsMenu(String name, Menu parent) {
            super(name, true, parent, Constants.Menus.allProductsMenuPattern, Constants.Menus.allProductsMenuCommand);
            this.categoryTree = new ArrayList<>();
            this.currentFilters = new String[]{"false", Double.toString(0.00), Double.toString(0.00), null, null, null, Double.toString(0.00)};
            currentSort = new StringBuilder();
            currentProducts = new ArrayList<>();
            this.availableCategories = new ArrayList<>();
            initSubMenus();
            initSubActions();
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new SortMenu("product sorting menu", this, getAvailableSorts(), currentSort));
            subMenus.put(2, new FilterMenu("product filtering menu", this, getAvailableFilters(), currentFilters));
        }

        private String[] getAvailableSorts() {
            return mainController.getProductAvailableSorts();
        }

        private String[] getAvailableFilters() {
            return mainController.getProductAvailableFilters();
        }
        
        @Override
        public void show() {
            subActions.get(subMenus.size() + 1 + floatingMenusIndexModification()).run("show products -all");
            super.show();
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ShowProductsAction( this.categoryTree, this.currentFilters,this.currentSort,this.currentProducts));
            subActions.put(index + 2, new Actions.ShowCategories( this.categoryTree, availableCategories));
            subActions.put(index + 4, new Actions.ChooseCategoryAction( this.categoryTree, availableCategories));
            subActions.put(index + 5, new Actions.RevertCategoryAction( categoryTree));
            subActions.put(index + 6, new Actions.ProductDetailMenu( currentProducts));
            subActions.put(index + 7, new Actions.BackAction( parent));
        }
    }


    //TODO: this motha fucka.
    public static class ProductDetailMenu extends Menu {
        private StringBuilder productID;
        private StringBuilder subProductID;
        ProductDetailMenu(String name){
            super(name, false, null, null, null);
            Menu.setProductDetailMenu(this);
           subProductID = new StringBuilder();
            initSubMenus();
            initSubActions();
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new ProductReviewMenu("comments menu", this, productID));
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.DigestProduct( productID));
            subActions.put(index + 2, new Actions.AddToCart( subProductID));
            subActions.put(index + 3, new Actions.SelectSeller( subProductID, productID));
            subActions.put(index + 4, new Actions.ShowCurrentSeller( subProductID));
            subActions.put(index + 4, new Actions.CompareProductByID( productID));
            subActions.put(index + 5, new Actions.BackAction(null));
        }

        public void runByProductID(String productID) {
            this.productID.delete(0, productID.length());
            this.productID.append(productID);
            ((Actions.BackAction)subActions.get(subActions.size() - 1)).setParent(Menu.getCallingMenu());
            this.run();
        }
    }

    public static class ProductReviewMenu extends Menu {
        private StringBuilder productID;
        ProductReviewMenu(String name, Menu parent, StringBuilder productID) {
            super(name, true, parent, Constants.Menus.productReviewMenuPattern, Constants.Menus.productReviewMenuCommand);
            this.productID = productID;
            initSubMenus();
            initSubActions();
        }

        @Override
        public void show() {
            subActions.get(subMenus.size() + 1 + floatingMenusIndexModification()).run("show comments");
            super.show();
        }

        @Override
        protected void initSubMenus() {
            //no available sub menus.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ShowReviews( productID));
            subActions.put(index + 2, new Actions.AddComment( productID));
            subActions.put(index + 3, new Actions.BackAction(parent));
        }
    }

    public static class SortMenu extends Menu {
        private StringBuilder currentSort;
        private String[] availableSorts;

        SortMenu(String name, Menu parent, String[] availableSorts, StringBuilder currentSort){
            super(name, true, parent, Constants.Menus.sortMenuPattern, Constants.Menus.sortMenuCommand);
            this.currentSort = currentSort;
            this.availableSorts = availableSorts.clone();
            initSubMenus();
            initSubActions();
        }

        @Override
        protected void initSubMenus() {
            //no available sub menu.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ChooseSorting( currentSort, availableSorts));
            subActions.put(index + 2, new Actions.ShowCurrentSort( currentSort));
            subActions.put(index + 3, new Actions.DisableSort( currentSort));
            subActions.put(index + 4, new Actions.BackAction( parent));
        }
    }

    public static class FilterMenu extends Menu {
        private String[] currentFilters;
        private String[] availableFilters;

        FilterMenu(String name, Menu parent, String[] availableFilters, String[] currentFilters){
            super(name, true, parent, Constants.Menus.filterMenuPattern, Constants.Menus.filterMenuCommand);
            this.currentFilters = currentFilters;
            this.availableFilters = availableFilters.clone();
            initSubMenus();
            initSubActions();
        }

        @Override
        protected void initSubMenus() {
            //no available sub meu.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ChooseFiltering( currentFilters, availableFilters));
            subActions.put(index + 2, new Actions.ShowCurrentFilters( currentFilters, availableFilters));
            subActions.put(index + 3, new Actions.DisableFilter( currentFilters, availableFilters));
            subActions.put(index + 4, new Actions.BackAction( parent));
        }
    }

    public static class SaleMenu extends Menu {
        private StringBuilder currentSort;
        private String[] currentFilters;
        private ArrayList<String[]> currentProducts;
        private ArrayList<String[]> currentSales;

        SaleMenu(String name, Menu parent) {
            super(name, true, parent, Constants.Menus.saleMenuPattern, Constants.Menus.saleMenuCommand);
            this.currentSort = new StringBuilder();
            this.currentFilters = new String[]{"false", Double.toString(0.00), Double.toString(0.00), null, null, null, Double.toString(0.00)};
            this.currentProducts = new ArrayList<>();
            this.currentSales = new ArrayList<>();
            initSubMenus();
            initSubActions();
        }

        @Override
        public void show() {
            subActions.get(subMenus.size() + 1 + floatingMenusIndexModification()).run("show offs");
            super.show();
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new SortMenu("sale sort menu", this, getAvailableSorts(), this.currentSort));
            subMenus.put(2, new FilterMenu("sale filter menu", this, getAvailableFilters(), this.currentFilters));
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ShowSales(currentSales));
            subActions.put(index + 2, new Actions.ShowInSaleProducts( currentSort, currentFilters, currentProducts));
            subActions.put(index + 3, new Actions.ProductDetailMenu( currentProducts));
            subActions.put(index + 4, new Actions.BackAction(parent));
        }

        private String[] getAvailableSorts() {
            return mainController.getProductAvailableSorts();
        }

        private String[] getAvailableFilters() {
            return mainController.getProductAvailableFilters();
        }
    }

    public static class AnonymousUserAccountMenu extends Menu {
        private Menu previousMenu;
        private Menu nextMenu;
        AnonymousUserAccountMenu(String name, Menu parent, Menu previousMenu, Menu nextMenu, String userType) {
            super(name, false, parent, userType, null);
            this.previousMenu = previousMenu;
            this.nextMenu = nextMenu;
            initSubMenus();
            initSubActions();
        }

        @Override
        public void run() {
            if (mainController.getType().equalsIgnoreCase(Constants.anonymousUserType)) {
                super.run();
            } else {
                parent.run();
            }
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new ShoppingCartMenu("anonymous user shopping cart menu", this));
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.LoginAction());
            subActions.put(index + 2, new Actions.RegisterAction());
            subActions.put(index + 3, new Actions.BackAction( previousMenu));
        }
    }

    public static class AdminMenu extends Menu {
        private Menu previousMenu;
        private Menu nextMenu;
        AdminMenu(String name, Menu parent, Menu previousMenu, Menu nextMenu, String userType) {
            super(name, false, parent, userType, null);
            this.previousMenu = previousMenu;
            this.nextMenu = nextMenu;
            initSubMenus();
            initSubActions();
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new PersonalInfoMenu("admin personal info", this) {
                @Override
                protected String[] getEditableFields() {
                    return adminController.getPersonalInfoEditableFields();
                }
            });
            subMenus.put(2, new UserManagingMenu("user managing menu", this));
            subMenus.put(3, new ProductManagingMenu("product managing menu", this));
            subMenus.put(4, new DiscountCodesManagingMenu( "discount code managing menu", this));
            subMenus.put(5, new RequestManagingMenu("request managing menu", this));
            subMenus.put(6, new CategoryManagingMenu("category managing menu", this));
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.Logout());
            subActions.put(index + 2, new Actions.BackAction( previousMenu));
        }
    }

    public static abstract class PersonalInfoMenu extends Menu {
        PersonalInfoMenu(String name, Menu parent){
            super(name, false, parent, Constants.Menus.viewPersonalInfoPattern, Constants.Menus.viewPersonalInfoCommand);
            initSubMenus();
            initSubActions();
        }

        @Override
        protected void initSubMenus() {
            //no available subMenus
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ViewPersonalInfo());
            subActions.put(index + 2, new Actions.EditField( getEditableFields()));
            subActions.put(index + 3, new Actions.BackAction(parent));
        }

        protected abstract String[] getEditableFields();
    }

    public static class UserManagingMenu extends Menu{
        private ArrayList<String[]> users;
        UserManagingMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.userManagingMenuPattern, Constants.Menus.userManagingMenuCommand);
            users = new ArrayList<>();
            initSubMenus();
            initSubActions();
        }

        @Override
        public void show() {
            subActions.get(subMenus.size() + 1 + floatingMenusIndexModification()).run("show users");
            super.show();
        }

        @Override
        protected void initSubMenus() {
            //no sub menus available.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.AdminViewAllUsers( users));
            subActions.put(index + 2, new Actions.AdminViewUser( users));
            subActions.put(index + 3, new Actions.AdminDeleteUser( users));
            subActions.put(index + 4, new Actions.AdminCreateAdmin());
            subActions.put(index + 5, new Actions.BackAction(parent));
        }
    }

    public static class ProductManagingMenu extends Menu {
        private ArrayList<String[]> currentProducts;
        ProductManagingMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.productManagingMenuPattern, Constants.Menus.productManagingMenuCommand);
            this.currentProducts = new ArrayList<>();
            initSubMenus();
            initSubActions();
        }

        @Override
        public void show() {
            subActions.get(subMenus.size() + 1 + floatingMenusIndexModification()).run("show products");
            super.show();
        }


        @Override
        protected void initSubMenus() {
            //no available sub menu.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.AdminShowProducts( currentProducts));
            subActions.put(index + 2, new Actions.AdminRemoveProductByID( currentProducts));
            subActions.put(index + 3, new Actions.BackAction( parent));
        }
    }

    public static class DiscountCodesManagingMenu extends Menu {
        private ArrayList<String> discountCodes;
        DiscountCodesManagingMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.discountCodesManagingMenuPattern, Constants.Menus.discountCodesManagingMenuCommand);
            this.discountCodes = new ArrayList<>();
            initSubMenus();
            initSubActions();
        }

        @Override
        public void show() {
            subActions.get(subMenus.size() + 1 + floatingMenusIndexModification()).run("show discount codes");
            super.show();
        }

        @Override
        protected void initSubMenus() {
            //no available sub menu.
        }

        private String[] getEditableFields() {
            return adminController.getDiscountEditableFields();
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.AdminShowDiscountCodes( discountCodes));
            subActions.put(index + 1, new Actions.AdminCreateDiscountCode());
            subActions.put(index + 2, new Actions.AdminViewDiscountCode( discountCodes));
            subActions.put(index + 3, new Actions.AdminEditDiscountCode( discountCodes, getEditableFields()));
            subActions.put(index + 4, new Actions.AdminRemoveDiscountCode( discountCodes));
            subActions.put(index + 5, new Actions.BackAction( parent));
        }
    }

    public static class RequestManagingMenu extends Menu {
        private ArrayList<String[]> pendingRequests;
        RequestManagingMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.requestManagingMenuPattern, Constants.Menus.requestManagingMenuCommand);
            pendingRequests = new ArrayList<>();
            initSubMenus();
            initSubActions();
        }

        @Override
        public void show() {
            subActions.get(floatingMenusIndexModification() + subMenus.size() + 1).run("show pending requests");
            super.show();
        }

        @Override
        protected void initSubMenus() {
            //no available sub menu.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.AdminShowPendingRequests(pendingRequests));
            subActions.put(index + 2, new Actions.AdminShowArchiveRequests());
            subActions.put(index + 2, new Actions.AdminViewRequestDetail( pendingRequests));
            subActions.put(index + 3, new Actions.BackAction( parent));
        }
    }

    public static class CategoryManagingMenu extends Menu {
        private ArrayList<String> currentCategories;
        CategoryManagingMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.categoryManagingMenuPattern, Constants.Menus.categoryManagingMenuCommand);
            this.currentCategories = new ArrayList<>();
            initSubMenus();
            initSubActions();
        }

        private String[] getEditableFields() {
            return adminController.getCategoryEditableFields();
        }

        @Override
        public void show() {
            subActions.get(floatingMenusIndexModification() + subMenus.size() + 1).run("show categories");
            super.show();
        }

        @Override
        protected void initSubMenus() {
            //no available sub menu.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.AdminShowCategories( currentCategories));
            subActions.put(index + 2, new Actions.AdminEditCategory( getEditableFields(), currentCategories));
            subActions.put(index + 3, new Actions.AdminAddCategory());
            subActions.put(index + 4, new Actions.AdminRemoveCategory( currentCategories));
        }
    }

    public static class SellerMenu extends Menu {
        private Menu previousMenu;
        private Menu nextMenu;
        private ArrayList<String[]> sellLogs;
        SellerMenu(String name, Menu parent, Menu previousMenu, Menu nextMenu, String userType) {
            super(name, false, parent, userType, null);
            this.previousMenu = previousMenu;
            this.nextMenu = nextMenu;
            this.sellLogs = new ArrayList<>();
            initSubMenus();
            initSubActions();
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new PersonalInfoMenu("seller personal info menu",this) {
                @Override
                protected String[] getEditableFields() {
                    return sellerController.getPersonalInfoEditableFields();
                }
            });
            subMenus.put(2, new SellerProductMenu("seller product menu", this));
            subMenus.put(3, new SellerSalesMenu("seller sales menu", this));
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ShowSellerCompanyInfo());
            subActions.put(index + 2, new Actions.ShowSellerBalance());
            subActions.put(index + 3, new Actions.ShowSellerSellHistory( sellLogs));
            subActions.put(index + 4, new Actions.ViewSingleSellHistory( sellLogs));
            subActions.put(index + 5, new Actions.Logout());
            subActions.put(index + 6, new Actions.BackAction(previousMenu));
        }
    }

    public static class SellerSalesMenu extends Menu {
        private ArrayList<String[]> currentSales;
        SellerSalesMenu(String name, Menu parent){
            super(name, false, parent, Constants.Menus.sellerSaleManagingMenuPattern, Constants.Menus.sellerSaleManagingMenuCommand);
            this.currentSales = new ArrayList<>();
            initSubMenus();
            initSubActions();
        }

        private String[] getEditableFields() {
            return sellerController.getSaleEditableFields();
        }

        @Override
        public void show() {
            subActions.get(floatingMenusIndexModification() + subMenus.size() + 1).run("show sales");
            super.show();
        }

        @Override
        protected void initSubMenus() {
            //no available sub menu.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.SellerShowSales( currentSales));
            subActions.put(index + 2, new Actions.SellerViewSaleDetails( currentSales));
            subActions.put(index + 3, new Actions.SellerEditSale( getEditableFields(), currentSales));
            subActions.put(index + 4, new Actions.SellerAddSale());
            subActions.put(index + 5, new Actions.BackAction( parent));
        }
    }

    public static class SellerProductMenu extends Menu {
        private ArrayList<String[]> sellerProducts;
        SellerProductMenu(String name, Menu parent){
            super(name, false, parent, Constants.Menus.sellerProductManagingMenuPattern, Constants.Menus.sellerProductManagingMenuCommand);
            this.sellerProducts = new ArrayList<>();
            initSubMenus();
            initSubActions();
        }

        @Override
        public void show() {
            subActions.get(floatingMenusIndexModification() + subMenus.size() + 1).run("show seller products");
            super.show();
        }

        private String[] getEditableFields() {
            return sellerController.getProductEditableFields();
        }

        @Override
        protected void initSubMenus() {
            //no available sub menu.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.SellerShowProducts( sellerProducts));
            subActions.put(index + 2, new Actions.SellerViewProductDetails( sellerProducts));
            subActions.put(index + 3, new Actions.SellerViewProductBuyers(sellerProducts));
            subActions.put(index + 4, new Actions.SellerEditProduct( getEditableFields(), sellerProducts));
            subActions.put(index + 4, new Actions.SellerAddProduct());
            subActions.put(index + 4, new Actions.SellerRemoveProduct( sellerProducts));
            subActions.put(index + 5, new Actions.BackAction(parent));
        }


    }
    
    public static class CustomerMenu extends Menu {
        private Menu previousMenu;
        private Menu nextMenu;
        CustomerMenu(String name, Menu parent, Menu previousMenu, Menu nextMenu, String userType) {
            super(name, false, parent, userType, null);
            this.previousMenu = previousMenu;
            this.nextMenu = nextMenu;
            initSubMenus();
            initSubActions();
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new PersonalInfoMenu("customer personal info menu", this) {
                @Override
                protected String[] getEditableFields() {
                    return customerController.getPersonalInfoEditableFields();
                }
            });
            subMenus.put(2, new ShoppingCartMenu("customer shopping cart", this));
            subMenus.put(3, new CustomerOrderLogMenu("customer order log menu", this));
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ShowCustomerBalance());
            subActions.put(index + 2, new Actions.ShowCustomerDiscountCodes());
            subActions.put(index + 3, new Actions.Logout());
            subActions.put(index + 4, new Actions.BackAction( previousMenu));
        }
    }

    //add product detail menu
    public static class ShoppingCartMenu extends Menu {
        private ArrayList<String[]> currentProducts;
        ShoppingCartMenu(String name, Menu parent){
            super(name, false, parent, Constants.Menus.shoppingCartMenuPattern, Constants.Menus.shoppingCartMenuCommand);
            this.currentProducts = new ArrayList<>();
            initSubMenus();
            initSubActions();
        }

        @Override
        public void show() {
            String type = mainController.getType();
            if (type.equalsIgnoreCase(Constants.adminUserType) || type.equalsIgnoreCase(Constants.sellerUserType)) {
                System.out.println("you dont possess a shopping cart");
                parent.run();
            }
            else {
                super.show();
            }
        }

        @Override
        protected void initSubMenus() {
            //no available sub menu.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ShoppingCartShowProducts( currentProducts));
            subActions.put(index + 2, new Actions.ShoppingCartViewProduct( currentProducts));
            subActions.put(index + 3, new Actions.ShoppingCartIncreaseProductCount( currentProducts));
            subActions.put(index + 4, new Actions.ShoppingCartDecreaseProductCount(currentProducts));
            subActions.put(index + 5, new Actions.ShoppingCartShowTotalPrice());
            subActions.put(index + 6, new Actions.ShoppingCartPurchase( this));
            subActions.put(index + 7, new Actions.BackAction( parent));
        }
    }

    public static class CustomerOrderLogMenu extends Menu {
        private ArrayList<String[]> currentOrderLogs;
        CustomerOrderLogMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.customerOrderLogMenuPattern, Constants.Menus.customerOrderLogMenuCommand);
            this.currentOrderLogs = new ArrayList<>();
            initSubMenus();
            initSubActions();
        }

        @Override
        public void show() {
            subActions.get(floatingMenusIndexModification() + subMenus.size() + 1).run("show orders");
            super.show();
        }

        @Override
        protected void initSubMenus() {
            //no available sub menu.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.CustomerShowOrders( currentOrderLogs));
            subActions.put(index + 2, new Actions.CustomerViewOrder( currentOrderLogs));
            subActions.put(index + 3, new Actions.CustomerRateProduct());
            subActions.put(index + 4, new Actions.BackAction( parent));
        }
    }
}
