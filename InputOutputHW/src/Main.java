import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

/**
 * Created by agcheb on 24.07.17.
 */
public class Main {

    public static void main(String[] args) throws IOException {

         // Первое задание
        File file = new File("newfile.txt");
        //file.createNewFile();
        FileInputStream in = new FileInputStream(file);
        byte[] arr = new byte[in.available()];
        int k = in.read(arr);
        in.close();
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
        System.out.println("Количество байтов в файле   "+ k);


        System.out.println("Конец проверки первого задания");
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();        System.out.println();
        


        // Второе задание

        File file1 = new File("file1.txt");
        File file2 = new File("file2.txt");
        File file3 = new File("file3.txt");
        File file4 = new File("file4.txt");
        File file5 = new File("file5.txt");

//        file1.createNewFile();
//        file2.createNewFile();
//        file3.createNewFile();
//        file4.createNewFile();
//        file5.createNewFile();



        FileInputStream in1 = new FileInputStream(file1);
        FileInputStream in2 = new FileInputStream(file2);
        FileInputStream in3 = new FileInputStream(file3);
        FileInputStream in4 = new FileInputStream(file4);
        FileInputStream in5 = new FileInputStream(file5);

        ArrayList<FileInputStream> arrayList = new ArrayList<>();

        arrayList.add(in1);
        arrayList.add(in2);
        arrayList.add(in3);
        arrayList.add(in4);
        arrayList.add(in5);

        Enumeration<FileInputStream> e = Collections.enumeration(arrayList);

        FileOutputStream out = new FileOutputStream(new File("genfile.txt"));

        while (e.hasMoreElements()){
            in =e.nextElement();
            byte[] arrbyte = new byte[in.available()];
            k = in.read(arrbyte);
            out.write(arrbyte);
            in.close();
        }
        out.close();


        // Третье задание - консольное приложение

            //File kniga = new File("kniga.txt");
            //kniga.createNewFile();

        final int pagesize = 1800;
        String bookname = "kniga.txt";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Добрый день! Сегодня мы будем читать страницы из книги:" +bookname);
        System.out.println(" Команда для выхода: /end");

        while (true) {
            System.out.println("Выберите страницу...");
            String s = reader.readLine();
            if (s.equals("/end")) break;
            int pagenum = Integer.parseInt(s);

            byte[] bytespage = readCharsFromFile(bookname, pagenum, pagesize);
            if(bytespage==null){
                System.out.println("попробуйте еще раз");
                continue;
            }
            String page = new String(bytespage);
            page = page.trim();
            System.out.println(page);

        }
        reader.close();



    }
    public static byte[] readCharsFromFile(String filePath, int pagenum, int pagesize) throws IOException {
        // открываем файл только для чтения
        RandomAccessFile file = new RandomAccessFile(filePath, "r");
        if(file.length()<(pagenum-1)*pagesize){
            System.out.println("Книга не такая большая, такой страницы нет. В этой книге не более " +((file.length()/pagesize)+1) + " страниц.");
            return null;
        }
        file.seek((pagenum-1)*pagesize);
        byte[] bytes = new byte[pagesize];
        file.read(bytes);
        file.close();
        return bytes;
    }
}
