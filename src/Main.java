import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        GameProgress tetris = new GameProgress(80, 30, 2, 2.5);
        GameProgress mario = new GameProgress(40, 120, 7, 52.3);
        GameProgress batman = new GameProgress(100, 100, 10, 70.7);

        String fail = "D://Games//savegames//";

        String tetrisFile = "D://Games//savegames//tetrisSave.dat";
        String marioFile = "D://Games//savegames//marioSave.dat";
        String batmanFile = "D://Games//savegames//batmanSave.dat";

        saveGame(tetris, tetrisFile); // Создания файлов сохранения прогресса
        saveGame(mario, marioFile);
        saveGame(batman, batmanFile);

        String zipGames = tetrisFile + " " + marioFile + " " + batmanFile;
        String zipRoute = "D://Games//savegames//savegames.zip";

        zipFiles(zipRoute, zipGames); // Архивация файлов

        File game1 = new File(tetrisFile);
        File game2 = new File(marioFile);
        File game3 = new File(batmanFile);

        game1.delete(); // Удаление файлов
        game2.delete();
        game3.delete();

        openZip(fail, zipRoute); // Разархивирование файлов

        tetris = openProgress(tetrisFile); // Десериализация файлов обратно в объект GameProgress и вывод в консоль
        mario = openProgress(marioFile);
        batman = openProgress(batmanFile);

    }

    public static void saveGame(GameProgress game, String route) {
        try {
            FileOutputStream fos = new FileOutputStream(route);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(game);
            oos.close();

        } catch (Exception e) {
            System.out.println(e.getMessage() + "ERROR");
        }
    }

    public static void zipFiles(String zipRoute, String game) {

        String[] routs = game.split(" ");

        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipRoute));
             FileInputStream fis1 = new FileInputStream(routs[0]);
             FileInputStream fis2 = new FileInputStream(routs[1]);
             FileInputStream fis3 = new FileInputStream(routs[2])) {

            String[] tokens1 = routs[0].split("//");
            String[] tokens2 = routs[1].split("//");
            String[] tokens3 = routs[2].split("//");

            ZipEntry tetris = new ZipEntry(tokens1[tokens1.length - 1]);
            ZipEntry mario = new ZipEntry(tokens2[tokens2.length - 1]);
            ZipEntry batman = new ZipEntry(tokens3[tokens3.length - 1]);

            zout.putNextEntry(tetris);
            byte[] buffer1 = new byte[fis1.available()];
            fis1.read(buffer1);
            zout.write(buffer1);
            zout.closeEntry();

            zout.putNextEntry(mario);
            byte[] buffer2 = new byte[fis2.available()];
            fis2.read(buffer2);
            zout.write(buffer2);
            zout.closeEntry();

            zout.putNextEntry(batman);
            byte[] buffer3 = new byte[fis3.available()];
            fis3.read(buffer3);
            zout.write(buffer3);
            zout.closeEntry();

        } catch (Exception e) {
            System.out.println(e.getMessage() + "ERROR");
        }
    }

    public static void openZip(String fail, String zip) {

        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zip))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(fail + name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static GameProgress openProgress(String gameRout) {

        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(gameRout);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(gameProgress);

        return gameProgress;
    }
}
