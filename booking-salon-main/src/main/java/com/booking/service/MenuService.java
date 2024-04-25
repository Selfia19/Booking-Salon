package com.booking.service;

import java.util.Scanner;

public class MenuService {
    // private static List<Person> personList = PersonRepository.getAllPerson();
    // private static List<Service> serviceList = ServiceRepository.getAllService();
    // private static List<Reservation> reservationList = new ArrayList<>();
    private static Scanner input = new Scanner(System.in);

    public static void mainMenu() {
        String[] mainMenuArr = {"Show Data", "Create Reservation", "Complete/cancel reservation", "Exit"};
        String[] subMenuArr = {"Recent Reservation", "Show Customer", "Show Available Employee", "List Reservation History","Back to main menu"};
    
        int optionMainMenu;
        int optionSubMenu;

		boolean backToMainMenu = false;
        boolean backToSubMenu = false;
        do {
            PrintService.printMenu("Main Menu", mainMenuArr);
            optionMainMenu = Integer.valueOf(input.nextLine());
            switch (optionMainMenu) {
                case 1:
                    do {
                        PrintService.printMenu("Show Data", subMenuArr);
                        optionSubMenu = Integer.valueOf(input.nextLine());
                        switch (optionSubMenu) {
                            case 1:
                                ReservationService.showProcessReservations();
                                break;
                            case 2:
                                PrintService.showAllCustomer();
                                break;
                            case 3:
                                PrintService.showAllEmployee();
                                break;
                            case 4:
                                ReservationService.showReservations();
                                break;
                            case 0:
                                backToSubMenu = true;
                        }
                    } while (!backToSubMenu);
                    break;
                case 2:
                    ReservationService.createReservation();
                    break;
                case 3:
                    ReservationService.finishOrCancelReservation();
                    break;
                case 0:
                    backToMainMenu = true;
                    break;
            }
        } while (!backToMainMenu);
	}
}