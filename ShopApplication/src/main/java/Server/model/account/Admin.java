package Server.model.account;

import Server.model.ModelUtilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Admin extends Account {
    protected static Admin manager = null; // head of other admins (shouldn't be suspended)
    protected static Map<String, Admin> allAdmins = new HashMap<>();
    private static int lastNum = 1;
    private static double commission = 0;

    public Admin(String username, String password, String firstName, String lastName, String email, String phone, String image) {
        super(username, password, firstName, lastName, email, phone, image);
        initialize();
    }

    public static Admin getManager() {
        return manager;
    }

    public static List<Admin> getAllAdmins(boolean... suspense) {
        return ModelUtilities.getAllInstances(allAdmins.values(), suspense);
    }

    public static Admin getAdminById(String accountId, boolean... suspense) {
        return ModelUtilities.getInstanceById(allAdmins, accountId, suspense);
    }

    @Override
    public void initialize() {
        if (accountId == null)
            accountId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allAdmins.put(accountId, this);
        lastNum++;
        super.initialize();

        if (manager == null)
            manager = this;
    }

    public static double getCommission() {
        return commission;
    }

    public static void setCommission(double commission) {
        Admin.commission = commission;
    }
}
