package pl.coderslab.workshop;


import pl.coderslab.workshop.entity.UserDao;


import java.util.Scanner;

public class Main01 {
    public static void main(String[] args) {
        Scanner scanner2 = new Scanner(System.in);
        UserDao userDao = new UserDao();
        boolean petla = true;

        while (petla) {
            for (User user :
                    userDao.findAll()) {
                System.out.println(user.toString());
            }
            System.out.println("Wybierz opcje (wpisz):");
            System.out.println("s - stwórz nowego uzytkownika");
            System.out.println("u - usun uzytkownika");
            System.out.println("m - zmodyfikuj uzytkownika");
            System.out.println("w - wyswietl uzytkownika");
            System.out.println("quit - zakonczenie");
            String wybor = scanner2.nextLine();
            switch (wybor) {
                case "s": {
                    createUser(scanner2, userDao);
                    break;
                }
                case "u": {
                    System.out.println("Podaj id uzytkownika do usuniecia");
                    int id = Integer.parseInt(scanner2.nextLine());
                    userDao.delete(id);
                    break;
                }
                case "m": {
                    System.out.println("Podaj id uzytkownika do zmodyfikowania");
                    int id = Integer.parseInt(scanner2.nextLine());
                    User userToModify = modifiedUser(scanner2,id,userDao);
                    userDao.update(userToModify);
                    break;
                }
                case "w": {
                    System.out.println("Podaj id uzytkownika do wyswietlenia");
                    int id = Integer.parseInt(scanner2.nextLine());
                    userDao.read(id);
                    break;
                }
                case "quit": {
                    petla = false;
                    break;
                }
                default: {
                    System.out.println("Wybierz poprawna opcje menu!");
                    break;
                }
            }

        }
    }

    private static void createUser(Scanner scanner, UserDao userDao) {
        User newUser = new User();
        String userName = "";
        String email = "";
        String password = "";
        System.out.println("Podaj imie uzytkownika");
        userName = scanner.nextLine();
        newUser.setUserName(userName);
        System.out.println("Podaj email uzytkownika");
        email = scanner.nextLine();
        newUser.setEmail(email);
        System.out.println("Podaj haslo uzytkownika");
        password = scanner.nextLine();
        newUser.setPassword(password);
        userDao.create(newUser);
    }

    private static User modifiedUser(Scanner scanner,int id, UserDao userDao) {
        User userToUpdate = userDao.read(id);
        scanner.reset();
        String userName = "";
        String email = "";
        String password = "";
        System.out.println("Podaj imie uzytkownika");
        userName = scanner.nextLine();
        userToUpdate.setUserName(userName);
        System.out.println("Podaj email uzytkownika");
        email = scanner.nextLine();
        userToUpdate.setEmail(email);
        System.out.println("Podaj haslo uzytkownika");
        password = scanner.nextLine();
        userToUpdate.setPassword(password);
        return userToUpdate;
    }
}
