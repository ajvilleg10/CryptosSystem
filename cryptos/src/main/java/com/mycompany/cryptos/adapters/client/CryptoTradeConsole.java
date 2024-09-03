/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptos.adapters.client;

import com.mycompany.cryptos.adapters.persistence.DataInitializer;
import com.mycompany.cryptos.adapters.persistence.InMemoryOrderStore;
import com.mycompany.cryptos.adapters.persistence.InMemoryTransactionStore;
import com.mycompany.cryptos.adapters.persistence.InMemoryUserStore;
import com.mycompany.cryptos.adapters.persistence.MarketPriceService;
import com.mycompany.cryptos.application.OrderOperation;
import com.mycompany.cryptos.application.TransactionOperation;
import com.mycompany.cryptos.application.UserOperation;
import com.mycompany.cryptos.domain.model.Crypto;
import com.mycompany.cryptos.domain.model.Transaction;
import com.mycompany.cryptos.domain.model.User;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

/**
 *
 * @author User
 */
public class CryptoTradeConsole {
    
    private final UserOperation userOperation;
    private final OrderOperation orderOperation;
    private final TransactionOperation transactionOperation;
    private final MarketPriceService marketPriceService;
    private final DataInitializer dataInitializer;
    private Optional<User> currentUser = Optional.empty();
    private final Scanner scanner = new Scanner(System.in);

    public CryptoTradeConsole(UserOperation userOperation, OrderOperation orderOperation, TransactionOperation transactionOperation, MarketPriceService marketPriceService, DataInitializer dataInitializer) {
        this.userOperation = userOperation;
        this.orderOperation = orderOperation;
        this.transactionOperation = transactionOperation;
        this.marketPriceService = marketPriceService;
        this.dataInitializer = dataInitializer;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        InMemoryUserStore userStore = new InMemoryUserStore();
        InMemoryOrderStore orderStore = new InMemoryOrderStore();
        InMemoryTransactionStore transactionStore = new InMemoryTransactionStore();
        
        MarketPriceService marketPriceService = new MarketPriceService();
        
        UserOperation userOp = new UserOperation(userStore);
        OrderOperation orderOp = new OrderOperation(userStore, orderStore,transactionStore);
        TransactionOperation transactionOp = new TransactionOperation(transactionStore,userStore);
        
        DataInitializer dataInitializer = new DataInitializer(marketPriceService);
        List<Crypto> initialCryptos = dataInitializer.initializeCryptos();

        CryptoTradeConsole console = new CryptoTradeConsole(userOp, orderOp, transactionOp, marketPriceService, dataInitializer);
        console.start();
    }
    
    public void start() {
        while (true) {
            if (currentUser.isEmpty()) {
                showMainMenu();
            } else {
                showUserMenu();
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\n--- Crypto Trade System ---");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Quit");
        System.out.print("Enter your choice: ");

        int choice = getValidIntInput();

        switch (choice) {
            case 1 -> register();
            case 2 -> login();
            case 3 -> exitApplication();
            default -> System.out.println("Invalid option. Please try again.");
        }
    }

    private void showUserMenu() {
         System.out.println("\n--- User Menu ---");
        System.out.println("1. Deposit Fiat Money");
        System.out.println("2. View Wallet Balance");
        System.out.println("3. Buy Crypto");
        System.out.println("4. Sell Crypto");
        System.out.println("5. View Transaction History");
        System.out.println("6. Logout");
        System.out.print("Enter your choice: ");

        int choice = getValidIntInput();

        switch (choice) {
            case 1 -> depositFiat();
            case 2 -> viewWalletBalance();
            case 3 -> buyCryptocurrency();
            case 4 -> sellCryptocurrency();
            case 5 -> viewTransactionHistory();
            case 6 -> logout();
            default -> System.out.println("Invalid option. Please try again.");
        }
    }
    
    private void register() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        User user = userOperation.signUpUser(name, email, password);
        System.out.println("User registered successfully. Your user ID is: " + user.getID());
    }

    private void login() {
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        Optional<User> user = userOperation.signInUser(email, password);
        if (user.isPresent()) {
            currentUser = user;
            System.out.println("Login successful. Welcome, " + user.get().getName() + "!");
        } else {
            System.out.println("Invalid credentials. Please try again.");
        }
    }

    private void depositFiat() {
        if (checkUserLoggedIn()) {
            System.out.print("Enter amount to deposit: ");
            BigDecimal amount = getValidBigDecimalInput();
            userOperation.depositFunds(currentUser.get().getID(), amount);
            System.out.println("Deposit successful.");
        }
    }

    private void viewWalletBalance() {
        if (checkUserLoggedIn()) {
            User user = currentUser.get();
            System.out.println("Fiat Balance: " + user.getWallet().getBalance());
            user.getWallet().getCryptos().forEach((crypto, amount) ->
                    System.out.println(crypto.getName() + " (" + crypto.getSymbol() + "): " + amount));
        }
    }

    public void buyCryptocurrency() {
        if (checkUserLoggedIn()) {
            System.out.print("Enter cryptocurrency symbol to buy (e.g., BTC, ETH): ");
            String symbol = scanner.nextLine().toUpperCase(Locale.ROOT);
            System.out.print("Enter amount to buy: ");
            BigDecimal amount = getValidBigDecimalInput();
            System.out.print("Enter maximum price to pay per unit: ");
            BigDecimal maxPrice = getValidBigDecimalInput();
            try{
                BigDecimal actualMarketPrice = marketPriceService.getMarketPrice(new Crypto("", symbol, BigDecimal.ZERO));

                if (actualMarketPrice.compareTo(maxPrice) <= 0) {
                    BigDecimal availableSupply = dataInitializer.getAvailableSupply(symbol);

                    if (availableSupply.compareTo(amount) >= 0) {
                        dataInitializer.updateSupply(symbol, amount);

                        Crypto crypto = new Crypto(symbol, symbol, actualMarketPrice);
                        orderOperation.setBuyOrder(currentUser.get().getID(), crypto, amount, maxPrice);
                        System.out.println("Order placed successfully!");
                    } else {
                        System.out.println("Not enough supply available for " + symbol + ". Available: " + availableSupply);
                    }
                } else {
                    System.out.println("The current market price (" + actualMarketPrice + ") exceeds your maximum price (" + maxPrice + ").");
                }
            }catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void sellCryptocurrency() {
        if (checkUserLoggedIn()) {
            System.out.print("Enter cryptocurrency symbol to sell (e.g., BTC): ");
            String symbol = scanner.nextLine();
            System.out.print("Enter amount to sell: ");
            BigDecimal amount = getValidBigDecimalInput();
            System.out.print("Enter minimum price to accept per unit: ");
            BigDecimal minPrice = getValidBigDecimalInput();

            Crypto crypto = new Crypto(symbol, symbol, BigDecimal.ZERO);
            orderOperation.setSellOrder(currentUser.get().getID(), crypto, amount, minPrice);
        }
    }

    private void viewTransactionHistory() {
        if (checkUserLoggedIn()) {
            List<Transaction> transactions = transactionOperation.getUserTransactionHistory(currentUser.get().getID());
            transactions.forEach(System.out::println);
        }
    }

    private void logout() {
        currentUser = Optional.empty();
        System.out.println("You have been logged out.");
    }

    private boolean checkUserLoggedIn() {
        if (currentUser.isEmpty()) {
            System.out.println("Please log in first.");
            return false;
        }
        return true;
    }

    private void exitApplication() {
        System.out.println("Quitting the application.");
        System.exit(0);
    }

    private int getValidIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine();
        return input;
    }

    private BigDecimal getValidBigDecimalInput() {
        while (!scanner.hasNextBigDecimal()) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.next();
        }
        BigDecimal input = scanner.nextBigDecimal();
        scanner.nextLine();
        return input;
    } 
    
}
