package com.booking.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.booking.models.Customer;
import com.booking.models.Employee;
import com.booking.models.Person;
import com.booking.models.Reservation;
import com.booking.models.Service;
import com.booking.repositories.PersonRepository;
import com.booking.repositories.ServiceRepository;


public class ReservationService {

    private static List<Person> personList = PersonRepository.getAllPerson();
    private static List<Service> serviceList = ServiceRepository.getAllService();
    private static List<Reservation> reservationList = new ArrayList<>();
    List<Service> services = ServiceRepository.getAllService();
    private static Scanner input = new Scanner(System.in);


    public static void createReservation() {
        System.out.println("Create Reservation");
        PrintService.showAllCustomer();
        
        Customer customer = null;
        do {
            System.out.print("Enter Customer ID: ");
            String customerId = input.nextLine();

            for (Person person : personList) {
                if (person instanceof Customer && person.getId().equals(customerId)) {
                    customer = (Customer) person;
                    break;
                }
            }

            if (customer == null) {
                System.out.println("Customer yang dicari tidak tersedia.");
            }
        } while (customer == null);


        PrintService.showAllEmployee();
        Employee employee = null;
        do {
            System.out.print("Enter Employee ID: ");
            String employeeId = input.nextLine();

            for (Person person : personList) {
                if (person instanceof Employee && person.getId().equals(employeeId)) {
                    employee = (Employee) person;
                    break;
                }
            }

            if (employee == null) {
                System.out.println("Employee yang dicari tidak tersedia.");
            }
        } while (employee == null);

    
        List<Service> selectedServices = new ArrayList<>();
        showServices(serviceList);
        
        boolean continueSelecting = true;
        while (continueSelecting) {
            System.out.print("Enter Service ID: ");
            String serviceId = input.nextLine();
        
            Service selectedService = null;
            for (Service service : serviceList) {
                if (service.getServiceId().equals(serviceId)) {
                    selectedService = service;
                    break;
                }
            }
        
            if (selectedService == null) {
                System.out.println("Service yang dicari tidak tersedia.");
                continue;
            }
        
            if (selectedServices.contains(selectedService)) {
                System.out.println("Layanan ini sudah dipilih sebelumnya.");
                System.out.print("Add another service? (yes/no): ");
                String addAnother = input.nextLine();
                if (!addAnother.equalsIgnoreCase("yes")) {
                    continueSelecting = false;
                }
                continue;
            }
        
            selectedServices.add(selectedService);
        
            if (selectedServices.size() == serviceList.size()) {
                System.out.println("Semua layanan sudah dipilih.");
                break;
            }
        
            System.out.print("Add another service? (yes/no): ");
            String addAnother = input.nextLine();
            if (!addAnother.equalsIgnoreCase("yes")) {
                continueSelecting = false;
            }
        }
    
        double reservationPrice = calculateReservationPrice(selectedServices, customer);
    
        System.out.println("Saldo dompet sebelum: " + customer.getWallet());
        double newWalletBalance = customer.getWallet() - reservationPrice;
        System.out.println("Saldo dompet sesudah: " + newWalletBalance);

        if (newWalletBalance < 0) {
            System.out.println("Saldo dompet tidak mencukupi.");
            return;
        }
        customer.setWalletBalance(newWalletBalance);
        for (Person person : personList) {
            if (person instanceof Customer && person.getId().equals(customer.getId())) {
                ((Customer) person).setWallet(newWalletBalance);
                break;
            }
        }

        System.out.print("Enter Reservation ID: ");
        String reservationId = input.nextLine();
        Reservation reservation = new Reservation(reservationId, customer, employee, selectedServices, reservationPrice, "In Process");
        reservationList.add(reservation);
        System.out.println("Reservation created successfully!");

    }

    
    private static double calculateReservationPrice(List<Service> selectedServices, Customer customer) {
        double totalPrice = 0;
        for (Service service : selectedServices) {
            totalPrice += service.getPrice();
        }
        
        double discount = 0;
        switch (customer.getMember().getMembershipName()) {
            case "Silver":
                discount = 0.05;
                break;
            case "Gold":
                discount = 0.1;
                break;
            default:
                discount = 0;
                break;
        }
        
        return totalPrice * (1 - discount);
    }


    public static void getCustomerByCustomerId(){
        
    }


    public static void finishOrCancelReservation() {
        System.out.println("Finish/Cancel Reservation");
        ReservationService.showProcessReservations();
        System.out.print("Enter Reservation ID: ");
        String reservationId = input.nextLine();
    
        Reservation targetReservation = null;
        for (Reservation reservation : reservationList) {
            if (reservation.getReservationId().equals(reservationId)) {
                targetReservation = reservation;
                break;
            }
        }
    
        if (targetReservation == null) {
            System.out.println("Reservation not found.");
            return;
        }
    
        if (!targetReservation.getWorkstage().equals("In Process")) {
            System.out.println("Reservation is already finished or canceled.");
            return;
        }
    
        System.out.print("Enter 'Finish' to finish or 'Cancel' to cancel the reservation: ");
        String action = input.nextLine().trim();
    
        if (action.equalsIgnoreCase("Finish")) {
            targetReservation.setWorkstage("Finish");
            System.out.println("Reservation has been finished.");
        } else if (action.equalsIgnoreCase("Cancel")) {
            targetReservation.setWorkstage("Canceled");
            System.out.println("Reservation has been canceled.");
        } else {
            System.out.println("Invalid action.");
            return;
        }
    
        for (int i = 0; i < reservationList.size(); i++) {
            Reservation reservation = reservationList.get(i);
            if (reservation.getReservationId().equals(reservationId)) {
                reservationList.set(i, targetReservation);
                break;
            }
        }
    }


    private static String getServiceNames(List<Service> services) {
        StringBuilder serviceNames = new StringBuilder();
        for (Service service : services) {
            serviceNames.append(service.getServiceName()).append(", ");
        }
        return serviceNames.toString().isEmpty() ? "" : serviceNames.toString().substring(0, serviceNames.length() - 2);
    }


    public static void showReservations() {
        System.out.println("List of Reservations:");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-15s | %-15s | %-15s | %-15s | %-40s | %-15s |\n", "Reservation ID", "Customer ID", "Employee ID", "Workstage", "Services", "Reservation Price");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
        
        double totalReservationPrice = 0;
        
        for (Reservation reservation : reservationList) {
            System.out.printf("| %-15s | %-15s | %-15s | %-15s | %-40s | %-15.2f |\n",
                    reservation.getReservationId(), reservation.getCustomer().getId(), reservation.getEmployee().getId(),
                    reservation.getWorkstage(), getServiceNames(reservation.getServices()), reservation.getReservationPrice());
            
            if (reservation.getWorkstage().equalsIgnoreCase("Finish")) {
                totalReservationPrice += reservation.getReservationPrice();
            }
        }
        
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-15s | %-15s | %-15s | %-15s | %-40s | %-15.2f |\n", "", "", "", "", "Total:", totalReservationPrice);
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
    }
    

    public static void showProcessReservations() {
        System.out.println("List of Reservations:");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-15s | %-15s | %-15s | %-15s | %-40s | %-15s |\n", "Reservation ID", "Customer ID", "Employee ID", "Workstage", "Services", "Reservation Price");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
        for (Reservation reservation : reservationList) {
            if (reservation.getWorkstage().equalsIgnoreCase("In Process")) {
                System.out.printf("| %-15s | %-15s | %-15s | %-15s | %-40s | %-15.2f |\n",
                        reservation.getReservationId(), reservation.getCustomer().getId(), reservation.getEmployee().getId(),
                        reservation.getWorkstage(), getServiceNames(reservation.getServices()), reservation.getReservationPrice());
            }
        }
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
    }


    public static void showServices(List<Service> services) {
        System.out.println("List of Services:");
        System.out.println("------------------------------------------------------------------");
        System.out.printf("| %-10s | %-20s | %-10s |\n", "Service ID", "Service Name", "Price");
        System.out.println("------------------------------------------------------------------");
        for (Service service : services) {
            System.out.printf("| %-10s | %-20s | %-10.2f |\n",
                    service.getServiceId(), service.getServiceName(), service.getPrice());
        }
        System.out.println("------------------------------------------------------------------");
    }
}
