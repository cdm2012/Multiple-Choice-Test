package multiplechoicetestpack;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;

//Клас за конструиране на запис на въпросите във Radnom Access File
public class QuestionsRecord implements Serializable{

    private boolean[] userChoise;//масив от булеви стойности за отговора
    //посочен от потребителя
    private boolean[] rightAnswer;//масив от булеви стойности за отговора на въпроса
    private String questionName;//Стринг за името на студента
    private String[] answersOfTheQuestion;//Масив от стрингове за отговорите на въпросите

    public QuestionsRecord() {
        userChoise = new boolean[4];
        rightAnswer = new boolean[4];
        questionName = new String();
        answersOfTheQuestion = new String[4];
        for (int i = 0; i < userChoise.length; i++) {
            userChoise[i] = new Boolean(false);
        }

        for (int i = 0; i < rightAnswer.length; i++) {
            rightAnswer[i] = new Boolean(false);
        }

        questionName = new String();

        for (int i = 0; i < answersOfTheQuestion.length; i++) {
            answersOfTheQuestion[i] = new String();
        }

    }

    public void read(RandomAccessFile raf) throws IOException {
        //Чете въпроса
        char[] temp = new char[400];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = raf.readChar();
        }
        questionName = new String(temp);

        //чете отговорите на въпросите
        for (int j = 0; j < answersOfTheQuestion.length; j++) {
            temp = new char[200];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = raf.readChar();
            }//end of inner for
            answersOfTheQuestion[j] = new String(temp);
        }//end of outer for

        //чете масива от булеви стойности съдържащ кой отговор е правилният
        //Големината на масива е 4*1=4 байта
        for (int i = 0; i < rightAnswer.length; i++) {
            rightAnswer[i] = raf.readBoolean();
        }
        //чете масива от булеви стойности съдържащ кой отговор е маркирал потребителя
        //Големината на масива е 4*1=4 байта
        for (int i = 0; i < userChoise.length; i++) {
            userChoise[i] = raf.readBoolean();
        }

    }

    public void write(RandomAccessFile raf) throws IOException {
        //записва въпроса
        StringBuffer sb;
        if (questionName != null) {
            sb = new StringBuffer(questionName);
        } else {
            sb = new StringBuffer();
        }
        sb.setLength(400);
        raf.writeChars(sb.toString());

        //записва отговорите на въпросите
        for (int i = 0; i < answersOfTheQuestion.length; i++) {
            if (answersOfTheQuestion[i] != null) {
                sb = new StringBuffer(answersOfTheQuestion[i]);
            } else {
                sb = new StringBuffer();
            }
            sb.setLength(200);
            raf.writeChars(sb.toString());
        }

        //Записва масива от булеви стойности за това кой е превилния отговор
        for (int i = 0; i < rightAnswer.length; i++) {
            raf.writeBoolean(rightAnswer[i]);
        }

        //Записва масива от булеви стойности за това кой е маркирания
        //от потребителя отговор на въпрос
        for (int i = 0; i < userChoise.length; i++) {
            raf.writeBoolean(userChoise[i]);
        }
    }

    public String[] getAnswersOfTheQuestion() {
        return answersOfTheQuestion;
    }

    public void setAnswersOfTheQuestion(String[] answersOfTheQuestion) {
        for (int i = 0; i < answersOfTheQuestion.length; i++) {
            if (answersOfTheQuestion[i] != null) {
                this.answersOfTheQuestion[i] = answersOfTheQuestion[i];
            } else {
                this.answersOfTheQuestion[i] = new String();
            }
        }
    }//end of  setAnswerOfQuestions

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = (questionName != null) ? questionName : new String();
    }

    public boolean[] getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(boolean[] rightAnswer) {
        for (int i = 0; i < rightAnswer.length; i++) {
            this.rightAnswer[i] = rightAnswer[i];
        }
    }

    public boolean[] getUserChoise() {
        return userChoise;
    }

    public void setUserChoise(boolean[] userChoise) {
        for (int i = 0; i < userChoise.length; i++) {
            this.userChoise[i] = userChoise[i];
        }
    }

    //връща големината на един запис
    int size() {
        //400 байта за името на въпроса ,4*200 за отговорите 2*4*1 за двата
        //булеви масива и 1-те 2-ва ги умножаваме по 2-ве заради празните интервали
        return 2 * (400 + 800) + 8;
    }
}
