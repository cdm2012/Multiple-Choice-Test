package multiplechoicetestpack;

//Добавя въпрос към банката с въпросите
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.JOptionPane;

public class AddQuestion {

    public void addQuestion(CreateQuestionBoard questionBoard, String filePath) {
        if (questionBoard.getTxtQuestion().getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Не сте въпрос !!!", "Непопълнено поле", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (questionBoard.getTxtFirstAnswer().getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Отговор 1 е непопълнен!!!", "Непопълнено поле", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (questionBoard.getTxtSecondAnswer().getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Отговор 2 е непопълнен!!!", "Непопълнено поле", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (questionBoard.getTxtThirdAnswer().getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Отговор 3 е непопълнен!!!", "Непопълнено поле", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (questionBoard.getTxtFourthAnswer().getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Отговор 4 е непопълнен!!!", "Непопълнено поле", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!questionBoard.getRdbtnFirstCorrectAnswer().isSelected()
                && !questionBoard.getRdbtnSecondCorrectAnswer().isSelected()
                && !questionBoard.getRdbtnThirdCorrectAnswer().isSelected()
                && !questionBoard.getRdbtnFourthCorrectAnswer().isSelected()) {
            JOptionPane.showMessageDialog(null, "Не сте избрали кой е правилния отговор!!!", "Грешка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Добавяне на въпроса във файла
        RandomAccessFile raf = null;
        QuestionsRecord question = new QuestionsRecord();

        try {

            raf = new RandomAccessFile(filePath, "rw");
            raf.seek(raf.length());

            //Добавяне на въпроса
            question.setQuestionName(questionBoard.getTxtQuestion().getText().trim());

            //Добавяне на отноворите на въпроса
            question.setAnswersOfTheQuestion(new String[]{
                        questionBoard.getTxtFirstAnswer().getText().trim(),
                        questionBoard.getTxtSecondAnswer().getText().trim(),
                        questionBoard.getTxtThirdAnswer().getText().trim(),
                        questionBoard.getTxtFourthAnswer().getText().trim()
                    });
            //Добавяне кой е правилни отговор на въпроса
            question.setRightAnswer(new boolean[]{
                        questionBoard.getRdbtnFirstCorrectAnswer().isSelected(),
                        questionBoard.getRdbtnSecondCorrectAnswer().isSelected(),
                        questionBoard.getRdbtnThirdCorrectAnswer().isSelected(),
                        questionBoard.getRdbtnFourthCorrectAnswer().isSelected()
                    });

            //Добавяме масив с бъдещия избор на потребителя
            question.setUserChoise(new boolean[]{false, false, false, false});

            //Записване на обекта във файла
            question.write(raf);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        questionBoard.setTxtQuestion("");
        questionBoard.setTxtFirstAnswer("");
        questionBoard.setTxtSecondAnswer("");
        questionBoard.setTxtThirdAnswer("");
        questionBoard.setTxtFourthAnswer("");
        questionBoard.setRdbtnFirstCorrectAnswer(false);
        questionBoard.setRdbtnSecondCorrectAnswer(false);
        questionBoard.setRdbtnThirdCorrectAnswer(false);
        questionBoard.setRdbtnFourthCorrectAnswer(false);




    }//end of addQuestion
}
