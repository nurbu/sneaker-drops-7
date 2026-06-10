package com.pluralsight.sneakerdrops;

import com.pluralsight.sneakerdrops.data.BrandRepository;
import com.pluralsight.sneakerdrops.data.SneakerRepository;
import com.pluralsight.sneakerdrops.models.Brand;
import com.pluralsight.sneakerdrops.models.Sneaker;
import com.pluralsight.sneakerdrops.service.DropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component

public class StartupRunner implements CommandLineRunner {

    private final DropService dropService;
    private final BrandRepository brandRepository;
    private final SneakerRepository sneakerRepository;
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    public StartupRunner(DropService dropService, BrandRepository brandRepository, SneakerRepository sneakerRepository) {
        this.dropService = dropService;
        this.brandRepository = brandRepository;
        this.sneakerRepository = sneakerRepository;
    }

    @Override
    public void run(String... args) {
        System.out.println(dropService.getStatus());
        seedData();

        boolean istrue = true;
        while (istrue) {
            System.out.println("1: List all sneakers");
            System.out.println("2: Find by model");
            System.out.println("3: Find under a certain price");
            System.out.println("4: Find by release year");
            System.out.println("5: Find by max price and min release year");
            System.out.println("0: Quit");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> listSneakers();
                case 2 -> findByModel();
                case 3 -> findByPrice();
                case 4 -> findByYear();
                case 5 -> findByPriceAndYear();
                case 0 -> istrue = false;
            }
        }
    }

    private void findByPriceAndYear() {
        System.out.println("Enter max price for your sneaker");
        int maxPrice = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter min year for your sneaker");
        int minYear = scanner.nextInt();
        List<Sneaker> sneakers = sneakerRepository.search(maxPrice, minYear);
        for (Sneaker sneaker : sneakers) {
            System.out.println(sneaker);
        }
    }

    private void findByModel() {
        System.out.println("Please enter the model name");
        String modelName = scanner.nextLine();
        List<Sneaker> sneakers = sneakerRepository.findByModelContaining(modelName);
        for (Sneaker sneaker : sneakers) {
            System.out.println(sneaker);
        }
    }

    private void findByPrice() {
        System.out.println("Please enter the max price for a sneaker");
        double price = scanner.nextDouble();
        scanner.nextLine();
        List<Sneaker> sneakers = sneakerRepository.findByPriceLessThan(price);
        for (Sneaker sneaker : sneakers) {
            System.out.println(sneaker);
        }
    }

    private void findByYear() {
        System.out.println("Please enter the year");
        int year = scanner.nextInt();
        scanner.nextLine();
        List<Sneaker> sneakers = sneakerRepository.findByReleaseYear(year);
        for (Sneaker sneaker : sneakers) {
            System.out.println(sneaker);
        }
    }

    private void listSneakers() {
        System.out.println("Sneakers in stock: " + sneakerRepository.count());
        sneakerRepository.findAll().forEach(System.out::println);
    }

    private void seedData() {
        if (brandRepository.count() == 0) {
            brandRepository.save(new Brand("Nike"));
            brandRepository.save(new Brand("Adidas"));
            brandRepository.save(new Brand("New Balance"));
        }

        if (sneakerRepository.count() == 0) {
            sneakerRepository.save(new Sneaker("Jordans_1", 200, 1998));
            sneakerRepository.save(new Sneaker("Jordans_4", 300, 2004));
            sneakerRepository.save(new Sneaker("Jordans_11", 150, 2007));
        }

    }
}
