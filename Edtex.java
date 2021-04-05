import java.sql.*;
import java.util.Scanner;

enum Subscription {
    NORMAL, PLATINUM, DIAMOND
};

/**
 * Edtex
 */
public class Edtex {

    public static void main(String[] args) {

        EdtexCustomer customer;

        Scanner reader = new Scanner(System.in);

        System.out.println("Enter Customer Name:");
        String name = reader.nextLine();

        System.out.println("Select Customer Subscription:\n1. Diamond\n2. Platinum\nEnter option:");
        int sub = reader.nextInt();

        reader.close();

        switch(sub){
            case 1:
            customer = new EdtexCustomer(name, Subscription.DIAMOND);
            break;
            case 2:
            customer = new EdtexCustomer(name, Subscription.PLATINUM);
            break;
            default:
            customer = new EdtexCustomer(name);
        }

        CustomerVisit cVisit = new CustomerVisit();

        cVisit.addProductCost(1000);
        cVisit.addServicesCost(2000);

        /**
         * Printing Bill for the customer
         */
        System.out.println("Your total Bill : " + Discount.getBill(cVisit,customer).toString());
        System.out.println("Thanks for visiting " + customer.getName());


        

    }
}

/**
 * EdtexCustomer
 */
class EdtexCustomer {

    final private String id;
    private String name;
    private Subscription subscription;

    EdtexCustomer(String name, Subscription subscription) {
        this.id = String.valueOf(System.currentTimeMillis());
        this.name = name;
        this.subscription = subscription;
        addCustomerDatabase();
    }

    EdtexCustomer(String name) {
        this.id = String.valueOf(System.currentTimeMillis());
        this.name = name;
        this.subscription = Subscription.NORMAL;
        addCustomerDatabase();
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Subscription getSubscription() {
        return this.subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    void addCustomerDatabase(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/edtex", "root", "root");
            Statement statement = conn.createStatement();
            statement.executeUpdate("insert into edtexCustomer (id,name,subscription) values('"+this.id+"','"+this.name+"','"+this.subscription.ordinal()+"')");
            

            conn.close();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

  

}

/**
 * Discount
 */

class Discount {

    static double platinumServiceDiscount = 0.14;
    static double diamondServiceDiscount = 0.07;
    static double productDiscount = 0.14;

    public static Double getBill(CustomerVisit cVisit,EdtexCustomer customer) {

        Subscription subscription = customer.getSubscription();
        double productCost = cVisit.getProductCost();
        double servicesCost = cVisit.getServicesCost();

        double discount = cVisit.getProductCost() * productDiscount;

        if (subscription == Subscription.DIAMOND) {
            discount += productCost * diamondServiceDiscount;
        } else if (subscription == Subscription.PLATINUM) {

            discount += servicesCost * platinumServiceDiscount;
        }

        double bill = servicesCost + productCost - discount;
        cVisit.addCustomerVisitDatabase();
        addBillDatabase(customer.getId(),cVisit.getId(),bill);

        return bill;
    }

    public static void addBillDatabase(String cid, String vid, double bill){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/edtex", "root", "root");
            Statement statement = conn.createStatement();
            statement.executeUpdate("insert into discount (cid,vid,bill) values('"+cid+"','"+vid+"','"+bill+"')");
            

            conn.close();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    
}


/**
 * CustomerVisit
 */

class CustomerVisit {
    private double productsCost = 0;
    private double servicesCost = 0;
    private String vid;

    CustomerVisit() {
        this.vid = String.valueOf(System.currentTimeMillis());
    }


    public String getId() {
        return this.vid;
    }

    public void addProductCost(double cost) {
        this.productsCost += cost;
    }

    public double getProductCost() {
        return this.productsCost;
    }

    public void addServicesCost(double cost) {
        this.servicesCost += cost;
    }

    public double getServicesCost() {
        return this.servicesCost;
    }

    void addCustomerVisitDatabase(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/edtex", "root", "root");
            Statement statement = conn.createStatement();
            statement.executeUpdate("insert into customervisit (vid,productcost,servicecost) values('"+this.vid+"','"+this.productsCost+"','"+this.servicesCost+"')");
            

            conn.close();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


}


  