package Controller;

import java.util.ArrayList;
import java.util.Hashtable;

import Common.Validation;
import Model.Fruit;
import View.Menu;

public class Program extends Menu<String> {
    static String[] mainChoice = {"Create Fruit","View Order","Shopping(For buyer)","Exit"};
    ArrayList<Fruit> fruits;
    Hashtable<String,ArrayList<Fruit>> orders ;

    public Program(){
        super("Fruit Shop System",mainChoice);
        fruits = new ArrayList<>();
        orders = new Hashtable<>();
    }

    public void execute(int n){
        switch(n){
            case 1:{
                createFruit();
                break;
            }
            case 2:{
                viewOrder();
                break;
            }
            case 3:{
                shopping();
                break;
            }
            case 4:{
                System.exit(0);
            }
        }
    }
    
    public void createFruit(){
        System.out.println();
        while (true){
            String id = Validation.getFruitID();
            while(getFruitByID(id) != null){
                System.out.println("Duplicate");
                id = Validation.getFruitID();
            }
            String name = Validation.getString("Enter fruit name: ");
            double price = Validation.getDouble("Enter fruit price: ");
            int quantity = Validation.getInt("Enter fruit quantity: ");
            String origin = Validation.getString("Enter fruit origin: ");
            fruits.add(new Fruit(id, name, price, quantity, origin));
            System.out.println("Success");
            if(Validation.getYesNo("Do you want to continue (Y/N): ").equalsIgnoreCase("n")){
                break;
            }
        }
        
    }
    public Fruit getFruitByID(String id){
        for(Fruit fruit: fruits){
            if(fruit.getId().equalsIgnoreCase(id)){
                return fruit;
            }
        }
        return null;
    }
    
    
    public int displayListFruit(){
        int countItem = 0;
        if(fruits.isEmpty()){
            return -1;
        }
        for (Fruit fruit : fruits){
            if(fruit.getQuantity() != 0){
                countItem++;
                 if (countItem == 1) {
                    System.out.printf("%-10s%-20s%-20s%-15s\n", "Item", "Fruit name", "Origin", "Price");
                }
            }
            System.out.printf("%-10d%-20s%-20s%-15.0f$\n", countItem,fruit.getName(), fruit.getOrigin(),fruit.getPrice());
        }
        if (countItem==0){
            return -1;
        }
        int item = Validation.getInt("Enter item you want to buy: ", 1, countItem);
        return item;
    }
    
    public Fruit getFruit(int item){
        int count =0;
        for(Fruit fruit: fruits){
            if(fruit.getQuantity()!=0){
                count++;
            }
            if(item == count){
                return fruit;
            }
        }
        return null;
    }
    
    public Fruit checkFruitInOrder(ArrayList<Fruit> listOrder,String id){
        for(Fruit fruit : listOrder){
            if(fruit.getId().equalsIgnoreCase(id)){
                return fruit;
            }
        }
        return null;
    }
    
    public void shopping(){
        System.out.println();
        ArrayList<Fruit> listOrder = new ArrayList<>();
        while(true){
            int item = displayListFruit();
            if(item == -1){
                System.out.println("Out of stock");
                return;
            }
            
            Fruit fruit = getFruit(item);
            System.out.println("You selected: " + fruit.getName());
            int quantity = Validation.getInt("Enter quantity: ", 0, fruit.getQuantity());
            fruit.setQuantity(fruit.getQuantity() - quantity);
            Fruit fruitOrder = checkFruitInOrder(listOrder,fruit.getId());
            if(fruitOrder != null){
                fruitOrder.setQuantity(fruitOrder.getQuantity() + quantity);
            }else{
                if(quantity !=0){
                    listOrder.add(new Fruit(fruit.getId(), fruit.getName(), fruit.getPrice(), quantity, fruit.getOrigin()));
                }
            }
            if(Validation.getYesNo("Do you want to continue(Y/N): ").equalsIgnoreCase("n")){
            break;
        }
        }
        
        if(listOrder.isEmpty()){
            System.out.println("No order");
        }else{
            displayListOrder(listOrder);
            String name = setName();
            orders.put(name, listOrder);
        }    
    }
     public String setName() {
        String name = Validation.getString("Enter name: ");
        int count = 0;
        for (String name_key : orders.keySet()) {
            String real_name = name_key.split("#")[0];
            if (name.equals(real_name)) {
                count++;
            }
        }
        return name + "#" + count;
    }
    
    public void displayListOrder(ArrayList<Fruit> listOrder) {
        System.out.println();
        double total = 0;

        System.out.printf("%15s%15s%15s%15s\n", "Product", "Quantity", "Price", "Amount");
        for (Fruit fruit : listOrder) {
            System.out.printf("%15s%15d%15.0f$%15.0f$\n", fruit.getName(),fruit.getQuantity(), fruit.getPrice(), fruit.getPrice() * fruit.getQuantity());
            total += fruit.getPrice() * fruit.getQuantity();
        }
        System.out.println("Total: " + total);
    }

    public void viewOrder() {
        System.out.println();
        if (orders.isEmpty()) {
            System.out.println("No orders");
            return;
        }
        for (String name : orders.keySet()) {
            System.out.println("Customer: " + name.split("#")[0]);
            ArrayList<Fruit> listOrder = orders.get(name);
            displayListOrder(listOrder);
        }
}
}
