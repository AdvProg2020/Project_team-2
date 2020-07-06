package model;

import model.account.Admin;
import model.account.Customer;
import model.account.Seller;
import model.log.BuyLog;
import model.log.LogItem;
import model.log.SellLog;
import model.request.Request;
import model.sellable.Sellable;
import model.sellable.SubSellable;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;

import static java.util.Map.entry;

public class ModelUtilities {
    private static Map<String, String> abbreviations = Map.ofEntries(
            entry(Customer.class.getSimpleName(), "CST"),
            entry(Seller.class.getSimpleName(), "SLR"),
            entry(Admin.class.getSimpleName(), "ADM"),
            entry(BuyLog.class.getSimpleName(), "BLG"),
            entry(SellLog.class.getSimpleName(), "SLG"),
            entry(LogItem.class.getSimpleName(), "LGI"),
            entry(Request.class.getSimpleName(), "REQ"),
            entry(Cart.class.getSimpleName(), "CRT"),
            entry(Category.class.getSimpleName(), "CTG"),
            entry(Discount.class.getSimpleName(), "DSC"),
            entry(Sellable.class.getSimpleName(), "PRO"),
            entry(Rating.class.getSimpleName(), "RAT"),
            entry(Review.class.getSimpleName(), "REV"),
            entry(Sale.class.getSimpleName(), "SAL"),
            entry(SubSellable.class.getSimpleName(), "SBP")
    );

    private static String fixedLengthNumber(int number, int length) {
        return String.format("%0" + length + "d", number);
    }

    public static String fixedPath(String path, final String DEFAULT_PATH) {
        if (path == null || !new File(path).exists())
            path = DEFAULT_PATH;

        return path;
    }

    public static String generateNewId(String className, int lastNum) {
        StringBuilder id = new StringBuilder();
        Calendar calendar = Calendar.getInstance();

        id.append(abbreviations.get(className));
        id.append(fixedLengthNumber(calendar.get(Calendar.YEAR) % 100, 2));
        id.append(fixedLengthNumber(calendar.get(Calendar.MONTH), 2));
        id.append(fixedLengthNumber(calendar.get(Calendar.DAY_OF_MONTH), 2));
        id.append(fixedLengthNumber(lastNum, 4));

        return id.toString();
    }

    public static <T extends ModelBasic> List<T> getAllInstances(Collection<T> instances, boolean... suspense) {
        boolean checkSuspense = (suspense.length == 0) || suspense[0]; // optional (default = true)

        ArrayList<T> updatedList = new ArrayList<>(instances);
        if (checkSuspense)
            updatedList.removeIf(ModelBasic::isSuspended);
        updatedList.sort(Comparator.comparing(ModelBasic::getId));

        return updatedList;
    }

    public static <T extends ModelBasic> T getInstanceById(Map<String, T> instances, String id, boolean... suspense) {
        boolean checkSuspense = (suspense.length == 0) || suspense[0]; // optional (default = true)

        T instance = instances.get(id);
        if (checkSuspense && instance != null && instance.isSuspended())
            instance = null;

        return instance;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface ModelOnly {
    }
}
