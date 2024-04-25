package com.booking.service;

import java.util.List;

import com.booking.models.Customer;
import com.booking.models.Employee;
import com.booking.models.Person;
import com.booking.models.Reservation;
import com.booking.models.Service;
import com.booking.repositories.PersonRepository;


public class PrintService {
    
    private static List<Person> personList = PersonRepository.getAllPerson();
    
    public static void printMenu(String title, String[] menuArr){
        int num = 1;
        System.out.println(title);
        for (int i = 0; i < menuArr.length; i++) {
            if (i == (menuArr.length - 1)) {
                num = 0;
            }
            System.out.println(num + ". " + menuArr[i]);
            num++;
        }
    }

    public String printServices(List<Service> serviceList){
        String result = "";
        // Bisa disesuaikan kembali
        for (Service service : serviceList) {
            result += service.getServiceName() + ", ";
        }
        return result;
    }

    public void showRecentReservation(List<Reservation> reservationList){
        int num = 1;
        System.out.printf("| %-4s | %-4s | %-11s | %-15s | %-15s | %-15s | %-10s |\n",
                "No.", "ID", "Nama Customer", "Service", "Biaya Service", "Pegawai", "Workstage");
        System.out.println("+========================================================================================+");
        for (Reservation reservation : reservationList) {
            if (reservation.getWorkstage().equalsIgnoreCase("Waiting") || reservation.getWorkstage().equalsIgnoreCase("In process")) {
                System.out.printf("| %-4s | %-4s | %-11s | %-15s | %-15s | %-15s | %-10s |\n",
                num, reservation.getReservationId(), reservation.getCustomer().getName(), printServices(reservation.getServices()), reservation.getReservationPrice(), reservation.getEmployee().getName(), reservation.getWorkstage());
                num++;
            }
        }
    }

    public static void showAllCustomer(){
        System.out.println("List of Customers:");
        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.printf("| %-10s | %-20s | %-20s | %-15s | %-10s |\n", "ID", "Name", "Address", "Membership", "Wallet");
        System.out.println("-------------------------------------------------------------------------------------------");
        for (Person person : personList) {
            if (person instanceof Customer) {
                Customer customer = (Customer) person;
                System.out.printf("| %-10s | %-20s | %-20s | %-15s | %-10s |\n",
                    customer.getId(), customer.getName(), customer.getAddress(),
                    customer.getMember().getMembershipName(), customer.getWallet());
            }
        }
        System.out.println("---------------------------------------------------------------------------");
    }

    public static void showAllEmployee(){
        System.out.println("List of Employee:");
        System.out.println("-------------------------------------------------------------------------");
        System.out.printf("| %-10s | %-20s | %-20s | %-10s |\n", "ID", "Name", "Address", "Experience");
        for(Person person : personList){
            if (person instanceof Employee) {
                Employee employee = (Employee) person;
                System.out.printf("| %-10s | %-20s | %-20s | %-10s |\n",
                employee.getId(),employee.getName(), employee.getAddress(), employee.getExperience());
            }
        }
        System.out.println("-------------------------------------------------------------------------");
    }

    public void showHistoryReservation(){
        
    }
}
