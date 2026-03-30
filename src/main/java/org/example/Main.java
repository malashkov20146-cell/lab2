package org.example;

import java.util.*;
import java.io.*;

public class Main {
    private static List<Artwork> gallery = new ArrayList<>();
    private static final String FILE_NAME = "gallery.dat";
    private static final String ADMIN_PASS = "123";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadData(); // Загружаем данные из файла при запуске

        while (true) {
            System.out.println("\n--- МЕНЮ ГАЛЕРЕИ ---");
            System.out.println("1. Добавить картину      6. Поиск по автору      11. Сорт. по автору");
            System.out.println("2. Добавить скульптуру   7. Фильтр по типу       12. Статистика");
            System.out.println("3. Добавить фото         8. Фильтр по году       13. Редактировать (Админ)");
            System.out.println("4. Просмотреть всё       9. Фильтр (диапазон)    14. Удалить (Админ)");
            System.out.println("5. Поиск по названию     10. Сорт. по названию   15. Сохранить и Выход");
            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Очистка после ввода числа

            switch (choice) {
                case 1: addWork("painting"); break;
                case 2: addWork("sculpture"); break;
                case 3: addWork("photo"); break;
                case 4: showGallery(); break;
                case 5: searchTitle(); break;
                case 6: searchAuthor(); break;
                case 7: filterType(); break;
                case 8: filterYear(); break;
                case 9: filterRange(); break;
                case 10: gallery.sort(Comparator.comparing(a -> a.title)); System.out.println("Отсортировано."); break;
                case 11: gallery.sort(Comparator.comparing(a -> a.author)); System.out.println("Отсортировано."); break;
                case 12: showStats(); break;
                case 13: editWork(); break;
                case 14: deleteWork(); break;
                case 15: saveData(); System.out.println("Пока!"); return;
                default: System.out.println("Нет такого пункта!");
            }
        }
    }

    // --- ЛОГИКА РАБОТЫ ---

    private static void addWork(String kind) {
        System.out.print("Название: "); String t = scanner.nextLine();
        System.out.print("Автор: "); String a = scanner.nextLine();
        System.out.print("Год: "); int y = scanner.nextInt();
        if (kind.equals("painting")) gallery.add(new Painting(t, a, y));
        else if (kind.equals("sculpture")) gallery.add(new Sculpture(t, a, y));
        else gallery.add(new Photograph(t, a, y));
        System.out.println("Добавлено успешно!");
    }

    private static void showGallery() {
        if (gallery.isEmpty()) System.out.println("Галерея пуста.");
        for (int i = 0; i < gallery.size(); i++) System.out.println(i + ". " + gallery.get(i));
    }

    private static void searchTitle() {
        System.out.print("Введите название: "); String t = scanner.nextLine();
        gallery.stream().filter(a -> a.title.toLowerCase().contains(t.toLowerCase())).forEach(System.out::println);
    }

    private static void searchAuthor() {
        System.out.print("Введите автора: ");
        String searchName = scanner.nextLine(); // Назвали переменную searchName, чтобы не путаться

        gallery.stream()
                .filter(work -> work.author.toLowerCase().contains(searchName.toLowerCase()))
                .forEach(System.out::println);
    }

    private static void filterType() {
        System.out.print("Введите тип (Картина/Скульптура/Фотография): "); String type = scanner.nextLine();
        gallery.stream().filter(a -> a.type.equalsIgnoreCase(type)).forEach(System.out::println);
    }

    private static void filterYear() {
        System.out.print("Введите год: "); int y = scanner.nextInt();
        gallery.stream().filter(a -> a.year == y).forEach(System.out::println);
    }

    private static void filterRange() {
        System.out.print("От года: "); int y1 = scanner.nextInt();
        System.out.print("До года: "); int y2 = scanner.nextInt();
        gallery.stream().filter(a -> a.year >= y1 && a.year <= y2).forEach(System.out::println);
    }

    private static void showStats() {
        long p = gallery.stream().filter(a -> a instanceof Painting).count();
        long s = gallery.stream().filter(a -> a instanceof Sculpture).count();
        System.out.println("Статистика: Картины: " + p + ", Скульптуры: " + s + ", Фото: " + (gallery.size() - p - s));
    }

    private static void editWork() {
        System.out.print("Пароль: ");
        if (scanner.nextLine().equals(ADMIN_PASS)) {
            showGallery();
            System.out.print("Введите индекс (ID): "); int id = scanner.nextInt(); scanner.nextLine();
            System.out.print("Новый автор: "); gallery.get(id).author = scanner.nextLine();
            System.out.print("Новый год: "); gallery.get(id).year = scanner.nextInt();
            System.out.println("Данные обновлены.");
        } else System.out.println("Неверный пароль!");
    }

    private static void deleteWork() {
        System.out.print("Пароль: ");
        if (scanner.nextLine().equals(ADMIN_PASS)) {
            showGallery();
            System.out.print("Введите индекс (ID) для удаления: ");
            int id = scanner.nextInt();
            gallery.remove(id);
            System.out.println("Запись удалена.");
        } else System.out.println("Неверный пароль!");
    }

    // --- ФАЙЛЫ ---
    private static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(gallery);
        } catch (IOException e) { System.out.println("Ошибка при сохранении."); }
    }

    private static void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            gallery = (List<Artwork>) ois.readObject();
        } catch (Exception e) { gallery = new ArrayList<>(); }
    }
}