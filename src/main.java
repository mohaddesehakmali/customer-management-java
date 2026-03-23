import exception.InvalidCustomerException;
import service.CustomerService;
import util.InputUtil;

public class main {
    public static void main(String[] args) {
        CustomerService customerService = new CustomerService();

        while (true) {
            System.out.println("\n===== CUSTOMER MANAGEMENT MENU =====");
            System.out.println("1. Add Customer");
            System.out.println("2. View All Customers");
            System.out.println("3. Search Customer By ID");
            System.out.println("4. Update Customer");
            System.out.println("5. Delete Customer");
            System.out.println("0. Exit");

            int choice = InputUtil.getInt("Choose an option: ");

            try {
                switch (choice) {
                    case 1:
                        String name = InputUtil.getString("Enter name: ");
                        String nationalId = InputUtil.getString("Enter national ID (10 digits): ");
                        String email = InputUtil.getString("Enter email: ");
                        customerService.addCustomer(name, nationalId, email);
                        break;

                    case 2:
                        customerService.viewAllCustomers();
                        break;

                    case 3:
                        int searchId = InputUtil.getInt("Enter customer ID: ");
                        customerService.searchCustomerById(searchId);
                        break;

                    case 4:
                        int updateId = InputUtil.getInt("Enter customer ID to update: ");
                        String newName = InputUtil.getString("Enter new name: ");
                        String newNationalId = InputUtil.getString("Enter new national ID (10 digits): ");
                        String newEmail = InputUtil.getString("Enter new email: ");
                        customerService.updateCustomer(updateId, newName, newNationalId, newEmail);
                        break;

                    case 5:
                        int deleteId = InputUtil.getInt("Enter customer ID to delete: ");
                        customerService.deleteCustomer(deleteId);
                        break;

                    case 0:
                        System.out.println("Exiting program...");
                        return;

                    default:
                        System.out.println("Invalid option.");
                }
            } catch (InvalidCustomerException e) {
                System.out.println("Validation error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }
}