/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multiplechoicetestpack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author User
 */
public class JFileChoosers {

    JFileChooser openFile;
    JFileChooser createNewFile;

    public String openFile() {

        File directory = new File(".");
        //Взма текущата директория и я подава на JFileChooser-a
        try {
            openFile = new JFileChooser(new File(directory.getCanonicalPath()));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //Позволява избирането само файлове
        openFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = openFile.showOpenDialog(null);
        if (result == JFileChooser.CANCEL_OPTION) {
            return new String();
        }
        File name = openFile.getSelectedFile();

        if (!openFile.getSelectedFile().exists()) {
            JOptionPane.showMessageDialog(null, "Посочение от вас файл не съществува !!!\n", "Грешка", JOptionPane.ERROR_MESSAGE);
            return new String();

        }
        if (!openFile.getSelectedFile().toString().substring(
                openFile.getSelectedFile().toString().length() - 4).equals(".txt")
                && !openFile.getSelectedFile().toString().substring(
                openFile.getSelectedFile().toString().length() - 4).equals(".doc")) {


            JOptionPane.showMessageDialog(null, "Не сте избрали файл с правилно разширене  !!!"
                    + "\nФайлът трябва да бъде с разширение:\n► .txt\n► .doc", "Грешка", JOptionPane.ERROR_MESSAGE);
            return new String();

        }
        return name.toString();
    }//end of Open File

//Save new File
    public String createNewBankOfQuestions() {
        File directory = new File(".");


        try {
            createNewFile = new JFileChooser(directory.getCanonicalPath());



        } catch (IOException ex) {
            ex.printStackTrace();


        }


        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                ".txt and .doc files", "txt", "doc");
        createNewFile.setFileFilter(filter);
        createNewFile.setAcceptAllFileFilterUsed(false);



        int result = createNewFile.showSaveDialog(null);


        if (result == JFileChooser.CANCEL_OPTION) {
            return new String();


        } //Ако посочения от потребителя файл съществува, то се извежда confirmDialog,
        //в който се пита за следващи действия. Дали да се презапише файла и цялата
        //предишна информация да се изтрие или да се прекрати операцията при на-
        //тискането на Cancel бутона
        if (!createNewFile.getSelectedFile().toString().substring(
                createNewFile.getSelectedFile().toString().length() - 4).equals(".txt")
                && !createNewFile.getSelectedFile().toString().substring(
                createNewFile.getSelectedFile().toString().length() - 4).equals(".doc")) {


            JOptionPane.showMessageDialog(null, "Не сте въвели коректно разширени на файла !!!"
                    + "\nФайлът трябва да бъде с разширение:\n► .txt\n► .doc", "Грешка", JOptionPane.ERROR_MESSAGE);


            return new String();



        }
        if (createNewFile.getSelectedFile().exists()) {
            int personChoise = JOptionPane.showConfirmDialog(null, "Файлът вече съществува !\n Искате ли го презапишете като цялата предишна информация ще бъде изтрита ?", "Save", JOptionPane.OK_CANCEL_OPTION);


            if (personChoise == JOptionPane.OK_OPTION) {
                try {
                    ///!!! Тук да се сложи randomAcsses File да прави
                    Formatter write = new Formatter(createNewFile.getSelectedFile().toString());


                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();


                }
                System.out.println("Noviq fajl:" + createNewFile.getSelectedFile().toString());



            } //ако е натиснат Cancel то отново сеотваря нов JChooser ,за да може
            //потребителя да изперер нов файл
            if (personChoise == JOptionPane.CANCEL_OPTION) {
                return new String();


            }

        } else {
            try {
                //Създава нов файл със посоченото име
                Formatter write = new Formatter(createNewFile.getSelectedFile().toString());


            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Не сте въвели правилно име", "Грешка", JOptionPane.ERROR_MESSAGE);
                createNewBankOfQuestions();
                System.out.println("Грешно име ");



            }
        }

        return createNewFile.getSelectedFile().toString();

    }
}
