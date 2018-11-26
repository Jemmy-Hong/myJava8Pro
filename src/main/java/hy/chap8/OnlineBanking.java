package hy.chap8;

 abstract class OnlineBanking {

    public void processCustomer(int id) {
        Customer c = DataBase.getCustomerWithId(id);
        makeCustomerHappy(c);
    }
    abstract void makeCustomerHappy(Customer c);

    // dummy Customer class
    static private class Customer {}

    // dummy Database class
    static private class DataBase {
        static Customer getCustomerWithId(int id) { return new Customer(); }
    }

}
