package multiplechoicetestpack;

public class Report {

    public String reportOfExam(double resultInPercentage, String nameOfStudent, QuestionsRecord[] questions) {
        StringBuilder result = new StringBuilder();
        result.append("Име: ").append(nameOfStudent).append("\n");


        //Добавяне на въпросите, отговорите , кой е верният въпрос и кой е врният отговор
        for (int i = 0; i < questions.length; i++) {
            result.append("\nВъпрос № ").append((i + 1)).append("\n");
            result.append("      ").append(questions[i].getQuestionName().trim()).append("\n");
            result.append("             1) ").append(questions[i].getAnswersOfTheQuestion()[0].trim());
            result.append("\n             2) ").append(questions[i].getAnswersOfTheQuestion()[1].trim());
            result.append("\n             3) ").append(questions[i].getAnswersOfTheQuestion()[2].trim());
            result.append("\n             4) ").append(questions[i].getAnswersOfTheQuestion()[3].trim());
            result.append("\n");

            boolean isClientMarkSomething = false;
            for (int j = 0; j < 4; j++) {
                if (questions[i].getUserChoise()[j] == true) {
                    isClientMarkSomething = true;
                    result.append("Потребителя е отбелязал: ").append(questions[i].getAnswersOfTheQuestion()[j].trim());
                }
            }
            //Ако няма нищо маркирано
            if (isClientMarkSomething == false) {
                result.append("Потребителя не е отбелязал никакъв отговор. ");
            }
            for (int j = 0; j < 4; j++) {
                if (questions[i].getRightAnswer()[j] == true) {
                    result.append("\nВерният отговор на този въпрос е: ").append(questions[i].getAnswersOfTheQuestion()[j].trim());
                    result.append("\n");
                }
            }


        }//end of for въпросите

        //Сега изчисляваме оценката на студента
        if (resultInPercentage < 55) {
            result.append("Крайната оценка на студента е:  Слаб 2\n");
            return result.toString();
        } else if (resultInPercentage < 65) {
            result.append("Крайната оценка на студента е:  Среден 3\n");
            return result.toString();
        } else if (resultInPercentage < 75) {
            result.append("Крайната оценка на студента е:  Добър 4\n");
            return result.toString();
        } else if (resultInPercentage < 85) {
            result.append("Крайната оценка на студента е:  Много Добър 5\n");
            return result.toString();
        } else if (resultInPercentage <= 100) {
            result.append("Крайната оценка на студента е:  Отличен 6\n");
            return result.toString();
        }



        //2	From  0     to  54 percentage of the correct answers
        //3	From  55  to 64 percentage of the correct answers
        //4	From  65  to 74 percentage of the correct answers
        //5	From  75  to 84 percentage of the correct answers
        //6	From  85  to 100 percentage of the correct answers



        return result.toString();
    }
}
