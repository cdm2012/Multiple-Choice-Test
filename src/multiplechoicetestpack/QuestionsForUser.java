package multiplechoicetestpack;

//Клас, който попълва въпросите в панел за въпроси на клиента
public class QuestionsForUser {

    private String[] questionAsnwers = new String[4];//Масив от стрингове за
    //отговорите на въпросите които да бъдат попълнени
    private String[] questionAsnwersOfUser = new String[4];//Масив от стрингове за
    //отговорите на въпросите, които да бъдат прочетени
    private boolean[] boolRightQuestionsOfUser = new boolean[4];//Масив от булеви стойности
    //за верните отговори
    private boolean[] boolUserQuestionsOfUser = new boolean[4];//Масив от булеви стойности
    //за отговорите на потребителя
    private boolean[] boolRadioButtonsState = new boolean[4];//Създаване на булев масив
    //от стойности за Радио Бутоните, които трябва да се изведат на потребителя
    //в него се съдържа стойността на маркираното от потребителя преди ако имат
    //такова разбира се, ако няма няма отметка на никой от Радио Бутоните

    public void fill(CreateQuestionBoard whereToWrite, QuestionsRecord whatToWrite) {
        whereToWrite.setTxtQuestion(whatToWrite.getQuestionName().trim());

        for (int i = 0; i < 4; i++) {
            questionAsnwers[i] = whatToWrite.getAnswersOfTheQuestion()[i];
            boolRadioButtonsState[i] = whatToWrite.getUserChoise()[i];
            whereToWrite.setRdbEmptyButton(true);
            

        }
        whereToWrite.setTxtFirstAnswer(questionAsnwers[0].trim());
        whereToWrite.setTxtSecondAnswer(questionAsnwers[1].trim());
        whereToWrite.setTxtThirdAnswer(questionAsnwers[2].trim());
        whereToWrite.setTxtFourthAnswer(questionAsnwers[3].trim());

        whereToWrite.setRdbtnFirstCorrectAnswer(boolRadioButtonsState[0]);
        whereToWrite.setRdbtnSecondCorrectAnswer(boolRadioButtonsState[1]);
        whereToWrite.setRdbtnThirdCorrectAnswer(boolRadioButtonsState[2]);
        whereToWrite.setRdbtnFourthCorrectAnswer(boolRadioButtonsState[3]);
    }//edn of fill

    //Записва отговора на потребителя
    QuestionsRecord readUserChoise(CreateQuestionBoard fromWhereToRead, QuestionsRecord whatToRead) {
        QuestionsRecord questionsRecordToReturn = new QuestionsRecord();

        //Взима името на въпроса
        questionsRecordToReturn.setQuestionName(whatToRead.getQuestionName());

        //Прави 3-ри масива за отговорите на въпросите, за верните отговори и
        //за отговорите на потребителя
        for (int i = 0; i < 4; i++) {
            questionAsnwersOfUser[i] = whatToRead.getAnswersOfTheQuestion()[i];
            boolRightQuestionsOfUser[i] = whatToRead.getRightAnswer()[i];
            boolUserQuestionsOfUser[i] = whatToRead.getUserChoise()[i];
        }
        //Слага конструираните масиви в questionsrecordToReturn
        questionsRecordToReturn.setAnswersOfTheQuestion(questionAsnwersOfUser);
        questionsRecordToReturn.setRightAnswer(boolRightQuestionsOfUser);
        questionsRecordToReturn.setUserChoise(boolUserQuestionsOfUser);

        //Връща обект от QuestionsRecord съдържащ селектирания отговор на потребителя
        //както и върпоса с верния отговор
        return questionsRecordToReturn;

    }
}
