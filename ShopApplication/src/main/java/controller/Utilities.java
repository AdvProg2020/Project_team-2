package controller;

import model.*;
import model.account.Account;
import model.account.Customer;
import model.account.Seller;
import model.log.BuyLog;
import model.log.LogItem;
import model.log.SellLog;
import model.request.EditProductRequest;
import model.request.EditSaleRequest;
import model.request.Request;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;

public class Utilities {
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

    public static DateFormat getDateFormat() {
        return dateFormat;
    }

    static class Pack {
        public static String[] saleInfo(Sale sale) {
            String[] salePack = new String[6];
            salePack[0] = sale.getId();
            salePack[1] = Double.toString(sale.getPercentage());
            salePack[2] = sale.getSeller().getStoreName();
            salePack[3] = dateFormat.format(sale.getStartDate());
            salePack[4] = dateFormat.format(sale.getEndDate());
            salePack[5] = Integer.toString(sale.getSubProducts().size());
            return salePack;
        }

        public static String[] product(Product product) {
            String[] productPack = new String[3];
            productPack[0] = product.getId();
            productPack[1] = product.getName();
            productPack[2] = product.getBrand();
            return productPack;
        }

        public static String[] subProduct(SubProduct subProduct) {
            String[] subProductPack = new String[4];
            subProductPack[0] = subProduct.getId();
            subProductPack[1] = subProduct.getSeller().getStoreName();
            subProductPack[2] = Double.toString(subProduct.getPriceWithSale());
            subProductPack[3] = Integer.toString(subProduct.getRemainingCount());
            return subProductPack;
        }

        public static String[] subProductExtended(SubProduct subProduct) {
            String[] subProductPack = new String[9];
            Product product = subProduct.getProduct();
            subProductPack[0] = product.getId();
            subProductPack[1] = product.getName();
            subProductPack[2] = product.getBrand();
            subProductPack[3] = product.getCategory().getName();
            subProductPack[4] = product.getInfoText();
            subProductPack[5] = Integer.toString(subProduct.getRemainingCount());
            subProductPack[6] = Double.toString(subProduct.getRawPrice());
            subProductPack[7] = subProduct.getSale() != null ? subProduct.getSale().getId() : "-";
            subProductPack[8] = subProduct.getSale() != null ? Double.toString(subProduct.getPriceWithSale()) : "-";
            return subProductPack;
        }

        public static String[] review(Review review) {
            String[] reviewPack = new String[4];
            reviewPack[0] = review.getReviewer().getUsername();
            reviewPack[1] = review.getTitle();
            reviewPack[2] = review.getText();
            reviewPack[3] = review.hasBought() ? "yes" : "no";
            return reviewPack;
        }

        public static String[] productInCart(SubProduct subProduct, int count) {
            String[] productPack = new String[8];
            productPack[0] = subProduct.getId();
            productPack[1] = subProduct.getProduct().getId();
            productPack[2] = subProduct.getProduct().getName();
            productPack[3] = subProduct.getProduct().getBrand();
            productPack[4] = subProduct.getSeller().getUsername();
            productPack[5] = subProduct.getSeller().getStoreName();
            productPack[6] = Integer.toString(count);
            productPack[7] = Double.toString(subProduct.getPriceWithSale());
            return productPack;
        }

        public static String[] productInSale(SubProduct subProduct) {
            String[] productPack = new String[5];
            productPack[0] = subProduct.getProduct().getId();
            productPack[1] = subProduct.getProduct().getName();
            productPack[2] = subProduct.getProduct().getBrand();
            productPack[3] = Double.toString(subProduct.getRawPrice());
            productPack[4] = Double.toString(subProduct.getPriceWithSale());
            return productPack;
        }

        public static String[] sellLog(SellLog sellLog) {
            String[] sellPack = new String[9];
            sellPack[0] = sellLog.getId();
            sellPack[1] = dateFormat.format(sellLog.getDate());
            sellPack[2] = sellLog.getCustomer().getUsername();
            sellPack[3] = Double.toString(sellLog.getReceivedMoney());
            sellPack[4] = Double.toString(sellLog.getTotalSaleAmount());
            sellPack[5] = sellLog.getReceiverName();
            sellPack[6] = sellLog.getReceiverPhone();
            sellPack[7] = sellLog.getReceiverAddress();
            sellPack[8] = sellLog.getShippingStatus().toString();
            return sellPack;
        }

        public static String[] sellLogItem(LogItem item) {
            String[] productPack = new String[8];
            Product product = item.getSubProduct().getProduct();
            productPack[0] = product.getId();
            productPack[1] = product.getName();
            productPack[2] = product.getBrand();
            productPack[3] = Integer.toString(item.getCount());
            productPack[4] = Double.toString(item.getPrice());
            productPack[5] = Double.toString(item.getSaleAmount());
            return productPack;
        }

        public static String[] discountInfo(Discount discount) {
            String[] discountInfo = new String[5];
            discountInfo[0] = discount.getDiscountCode();
            discountInfo[1] = dateFormat.format(discount.getStartDate());
            discountInfo[2] = dateFormat.format(discount.getEndDate());
            discountInfo[3] = Double.toString(discount.getMaximumAmount());
            discountInfo[4] = Double.toString(discount.getPercentage());
            return discountInfo;
        }

        public static String[] personalInfo(Account account) {
            String[] info;
            if (account instanceof Customer) {
                info = new String[7];
                info[6] = String.valueOf(((Customer) account).getBalance());
            } else if (account instanceof Seller) {
                info = new String[8];
                info[6] = String.valueOf(((Seller) account).getBalance());
                info[7] = ((Seller) account).getStoreName();
            } else
                info = new String[6];
            info[0] = account.getUsername();
            info[1] = account.getClass().getName();
            info[2] = account.getFirstName();
            info[3] = account.getLastName();
            info[4] = account.getEmail();
            info[5] = account.getPhone();
            return info;
        }

        public static String[] digest(Product product) {
            String[] productInfo = new String[5];
            productInfo[0] = product.getId();
            productInfo[1] = product.getName();
            productInfo[2] = product.getBrand();
            productInfo[3] = product.getInfoText();
            productInfo[4] = Double.toString(product.getAverageRatingScore());
            return productInfo;
        }

        public static String[] category(Category category) {
            String[] categoryPack = new String[2];
            categoryPack[0] = category.getId();
            categoryPack[1] = category.getName();
            return categoryPack;
        }

        public static String[] buyLog(BuyLog buyLog) {
            String[] orderPack = new String[9];
            orderPack[0] = buyLog.getId();
            orderPack[1] = buyLog.getCustomer().getUsername();
            orderPack[2] = buyLog.getReceiverName();
            orderPack[3] = buyLog.getReceiverPhone();
            orderPack[4] = buyLog.getReceiverAddress();
            orderPack[5] = dateFormat.format(buyLog.getDate());
            orderPack[6] = buyLog.getShippingStatus().toString();
            orderPack[7] = Double.toString(buyLog.getPaidMoney());
            orderPack[8] = Double.toString(buyLog.getTotalDiscountAmount());
            return orderPack;
        }

        public static String[] buyLogItem(LogItem item) {
            String[] productPack = new String[8];
            Product product = item.getSubProduct().getProduct();
            productPack[0] = product.getId();
            productPack[1] = product.getName();
            productPack[2] = product.getBrand();
            productPack[3] = item.getSeller().getUsername();
            productPack[4] = item.getSeller().getStoreName();
            productPack[5] = Integer.toString(item.getCount());
            productPack[6] = Double.toString(item.getPrice());
            productPack[7] = Double.toString(item.getSaleAmount());
            return productPack;
        }

        public static String[] request(Request request) {
            String[] requestPack = new String[4];
            requestPack[0] = request.getId();
            requestPack[1] = request.getClass().getSimpleName();
            requestPack[2] = dateFormat.format(request.getDate());
            requestPack[3] = request.getStatus().toString();
            return requestPack;
        }

        public static String[] saleChange(EditSaleRequest request) {
            String[] saleChange = new String[2];
            saleChange[0] = request.getField().toString();
            saleChange[1] = request.getNewValue();
            return saleChange;
        }

        public static String[] productChange(EditProductRequest request) {
            String[] productChange = new String[2];
            productChange[0] = request.getField().toString();
            productChange[1] = request.getNewValue();
            return productChange;
        }

        public static String[] customerDiscountRemainingCount(Customer customer, int count) {
            String[] personPack = new String[2];
            personPack[0] = customer.getUsername();
            personPack[1] = Integer.toString(count);
            return personPack;
        }

        public static String invalidProductIds(ArrayList<String> subProductIds) {
            StringBuilder invalidSubProductIds = new StringBuilder();
            String falseSubProduct;
            for (String subProductId : subProductIds) {
                falseSubProduct = "\n" + subProductId;
                invalidSubProductIds.append(falseSubProduct);
            }
            return invalidSubProductIds.toString();
        }

        public static String invalidAccountIds(ArrayList<String> accountIds) {
            StringBuilder invalidAccountIds = new StringBuilder();
            String falseAccount;
            for (String accountId : accountIds) {
                falseAccount = "\n" + accountId;
                invalidAccountIds.append(falseAccount);
            }
            return invalidAccountIds.toString();
        }
    }

    static class Field {
        public static String[] customerPersonalInfoEditableFields() {
            String[] editableFields = new String[6];
            editableFields[0] = "firstName";
            editableFields[1] = "lastName";
            editableFields[2] = "phone";
            editableFields[3] = "email";
            editableFields[4] = "password";
            editableFields[5] = "balance";
            return editableFields;
        }

        public static String[] sellerPersonalInfoEditableFields() {
            String[] editableFields = new String[7];
            editableFields[0] = "firstName";
            editableFields[1] = "lastName";
            editableFields[2] = "phone";
            editableFields[3] = "email";
            editableFields[4] = "password";
            editableFields[5] = "storeName";
            editableFields[6] = "balance";
            return editableFields;
        }

        public static String[] adminPersonalInfoEditableFields() {
            String[] editableFields = new String[5];
            editableFields[0] = "firstName";
            editableFields[1] = "lastName";
            editableFields[2] = "phone";
            editableFields[3] = "email";
            editableFields[4] = "password";
            return editableFields;
        }

        public static String[] productEditableFields() {
            String[] editableFields = new String[5];
            editableFields[0] = "name";
            editableFields[1] = "brand";
            editableFields[2] = "info text";
            editableFields[3] = "price";
            editableFields[4] = "count";
            return editableFields;
        }

        public static String[] saleEditableFields() {
            String[] saleEditableFields = new String[4];
            saleEditableFields[0] = "start date";
            saleEditableFields[1] = "end date";
            saleEditableFields[2] = "percentage";
            saleEditableFields[3] = "maximum";
            return saleEditableFields;
        }

        public static String[] discountEditableFields() {
            String[] editableFields = new String[4];
            editableFields[0] = "start date";
            editableFields[1] = "end date";
            editableFields[2] = "maximum amount";
            editableFields[3] = "percentage";
            return editableFields;
        }

        public static String[] getCategoryEditableFields() {
            String[] editableFields = new String[2];
            editableFields[0] = "name";
            editableFields[1] = "parent name";
            return editableFields;
        }
    }

    static class Filter {
        public static String[] productAvailableFilters() {
            String[] availableFilters = new String[7];
            availableFilters[0] = "available";
            availableFilters[1] = "minPrice";
            availableFilters[2] = "macPrice";
            availableFilters[3] = "contains";
            availableFilters[4] = "brand";
            availableFilters[5] = "storeName";
            availableFilters[6] = "minRatingScore";
            return availableFilters;
        }

        static class ProductFilter {
            public static void available(ArrayList<Product> products, boolean available) {
                if (available)
                    products.removeIf(product -> (product.getTotalRemainingCount() == 0));
            }

            public static void minPrice(ArrayList<Product> products, double minPrice) {
                if (minPrice != 0)
                    products.removeIf(product -> product.getMaxPrice() < minPrice);
            }

            public static void maxPrice(ArrayList<Product> products, double maxPrice) {
                if (maxPrice != 0)
                    products.removeIf(product -> product.getMinPrice() > maxPrice);
            }

            public static void name(ArrayList<Product> products, String contains) {
                if (!contains.equals(""))
                    products.removeIf(product -> !(product.getName().toLowerCase().contains(contains.toLowerCase())));
            }

            public static void brand(ArrayList<Product> products, String brand) {
                if (!brand.equals(""))
                    products.removeIf(product -> !(product.getBrand().toLowerCase().contains(brand.toLowerCase())));
            }

            public static void storeName(ArrayList<Product> products, String storeName) {
                if (!storeName.equals("")) {
                    products.removeIf(product -> !product.isSoldInStoreWithName(storeName.toLowerCase()));
                }
            }

            public static void ratingScore(ArrayList<Product> products, double minRatingScore) {
                products.removeIf(product -> product.getAverageRatingScore() < minRatingScore);
            }
        }

        static class SubProductFilter {
            public static void available(ArrayList<SubProduct> subProducts, boolean available) {
                if (available)
                    subProducts.removeIf(subProduct -> (subProduct.getRemainingCount() == 0));
            }
            public static void minPrice(ArrayList<SubProduct> subProducts, double minPrice) {
                if (minPrice != 0)
                    subProducts.removeIf(subProduct -> subProduct.getPriceWithSale() < minPrice);
            }
            public static void maxPrice(ArrayList<SubProduct> subProducts, double maxPrice) {
                if (maxPrice != 0)
                    subProducts.removeIf(subProduct -> subProduct.getPriceWithSale() > maxPrice);
            }
            public static void name(ArrayList<SubProduct> subProducts, String contains) {
                if (!contains.equals(""))
                    subProducts.removeIf(subProduct -> !(subProduct.getProduct().getName().toLowerCase().contains(contains.toLowerCase())));
            }
            public static void brand(ArrayList<SubProduct> subProducts, String brand) {
                if (!brand.equals(""))
                    subProducts.removeIf(subProduct -> !(subProduct.getProduct().getBrand().toLowerCase().contains(brand.toLowerCase())));
            }
            public static void storeName(ArrayList<SubProduct> subProducts, String storeName) {
                if (!storeName.equals(""))
                    subProducts.removeIf(subProduct -> !subProduct.getSeller().getStoreName().contains(storeName.toLowerCase()));
            }
            public static void ratingScore(ArrayList<SubProduct> subProducts, double minRatingScore){
                subProducts.removeIf(subProduct -> subProduct.getProduct().getAverageRatingScore() < minRatingScore);
            }
        }
    }
    static class Sort {
        public static String[] productAvailableSorts() {
            String[] availableSorts = new String[6];
            availableSorts[0] = "price";
            availableSorts[1] = "rating score";
            availableSorts[2] = "name";
            availableSorts[3] = "category name";
            availableSorts[4] = "total remaining count";
            availableSorts[5] = "view count";
            return availableSorts;
        }

        public static class ProductPriceComparator implements Comparator<Product> {
            private int direction;

            public ProductPriceComparator(boolean isIncreasing) {
                direction = isIncreasing ? 1 : -1;
            }

            @Override
            public int compare(Product o1, Product o2) {
                return direction * Double.compare(o1.getMinPrice(), o2.getMinPrice());
            }
        }

        public static class ProductRatingScoreComparator implements Comparator<Product> {
            private int direction;

            public ProductRatingScoreComparator(boolean isIncreasing) {
                direction = isIncreasing ? 1 : -1;
            }

            @Override
            public int compare(Product o1, Product o2) {
                return direction * Double.compare(o1.getAverageRatingScore(), o2.getAverageRatingScore());
            }
        }

        public static class ProductNameComparator implements Comparator<Product> {
            private int direction;

            public ProductNameComparator(boolean isIncreasing) {
                direction = isIncreasing ? 1 : -1;
            }

            @Override
            public int compare(Product o1, Product o2) {
                return direction * o1.getName().compareTo(o2.getName());
            }
        }

        public static class ProductCategoryNameComparator implements Comparator<Product> {
            private int direction;

            public ProductCategoryNameComparator(boolean isIncreasing) {
                direction = isIncreasing ? 1 : -1;
            }

            @Override
            public int compare(Product o1, Product o2) {
                return direction * o1.getCategory().getName().compareTo(o2.getCategory().getName());
            }
        }

        public static class ProductRemainingCountComparator implements Comparator<Product> {
            private int direction;

            public ProductRemainingCountComparator(boolean isIncreasing) {
                direction = isIncreasing ? 1 : -1;
            }

            @Override
            public int compare(Product o1, Product o2) {
                return direction * Integer.compare(o1.getTotalRemainingCount(), o2.getTotalRemainingCount());
            }
        }

        public static class ProductViewCountComparator implements Comparator<Product> {
            private int direction;

            public ProductViewCountComparator(boolean isIncreasing) {
                direction = isIncreasing ? 1 : -1;
            }

            @Override
            public int compare(Product o1, Product o2) {
                return direction * Integer.compare(o1.getViewCount(), o2.getViewCount());
            }
        }

        public static class SubProductPriceComparator implements Comparator<SubProduct> {
            private int direction;

            public SubProductPriceComparator(boolean isIncreasing) {
                direction = isIncreasing ? 1 : -1;
            }

            @Override
            public int compare(SubProduct o1, SubProduct o2) {
                return direction * Double.compare(o1.getPriceWithSale(), o2.getPriceWithSale());
            }
        }

        public static class SubProductRatingScoreComparator implements Comparator<SubProduct> {
            private int direction;

            public SubProductRatingScoreComparator(boolean isIncreasing) {
                direction = isIncreasing ? 1 : -1;
            }

            @Override
            public int compare(SubProduct o1, SubProduct o2) {
                return direction * Double.compare(o1.getProduct().getAverageRatingScore(), o2.getProduct().getAverageRatingScore());
            }
        }

        public static class SubProductNameComparator implements Comparator<SubProduct> {
            private int direction;

            public SubProductNameComparator(boolean isIncreasing) {
                direction = isIncreasing ? 1 : -1;
            }

            @Override
            public int compare(SubProduct o1, SubProduct o2) {
                return direction * o1.getProduct().getName().compareTo(o2.getProduct().getName());
            }
        }

        public static class SubProductCategoryNameComparator implements Comparator<SubProduct> {
            private int direction;

            public SubProductCategoryNameComparator(boolean isIncreasing) {
                direction = isIncreasing ? 1 : -1;
            }

            @Override
            public int compare(SubProduct o1, SubProduct o2) {
                return direction * o1.getProduct().getCategory().getName().compareTo(o2.getProduct().getCategory().getName());
            }
        }

        public static class SubProductRemainingCountComparator implements Comparator<SubProduct> {
            private int direction;

            public SubProductRemainingCountComparator(boolean isIncreasing) {
                direction = isIncreasing ? 1 : -1;
            }

            @Override
            public int compare(SubProduct o1, SubProduct o2) {
                return direction * Integer.compare(o1.getRemainingCount(), o2.getRemainingCount());
            }
        }

        public static class SubProductViewCountComparator implements Comparator<SubProduct> {
            private int direction;

            public SubProductViewCountComparator(boolean isIncreasing) {
                direction = isIncreasing ? 1 : -1;
            }

            @Override
            public int compare(SubProduct o1, SubProduct o2) {
                return direction * Integer.compare(o1.getProduct().getViewCount(), o2.getProduct().getViewCount());
            }
        }
    }
}