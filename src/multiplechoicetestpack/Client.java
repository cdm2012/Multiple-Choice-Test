package multiplechoicetestpack;

// Fig. 24.7: Client.java
// Client that reads and displays information sent from a Server.
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class Client extends JFrame {

    private JPanel pnlTimePannel = new JPanel();//Панел за оставащото време и колко
    //от въпросите са непопълнени
    private JPanel pnlTimePannelOutter = new JPanel();//Панел за панела за
    //оставащото време
    private JPanel pnlQuestionPannel = new JPanel(new GridLayout(1, 1, 5, 5));//Панел за въпросите и
    //отговорите както и кой отговор да маркира
    private JPanel pnlOuterPanel = new JPanel();//Външен панел за клиента
    private JPanel pnlForReport = new JPanel(new GridLayout(1, 1));//Панел за резултата от теста
    private JPanel pnlForSeconds = new JPanel();//Панел съдържащ лейбъл с
    //оставащото време за изпълнението на теста
    private JPanel pnlForLabelSeconds = new JPanel();//Панел съдържащ лейбъл
    //с мерната единица на оставащото време
    private JPanel pnlNumberOfCurrentQuestion = new JPanel();//Панел показващ
    //лейбъл съдържащ поредния номер на въпроса, който се показва
    private JPanel pnlForSendButton = new JPanel();//Панел, който съдържа бутона
    //за изпращане на теста
    private JPanel pnlPreviousNextQuestion = new JPanel(new BorderLayout(10, 10));
    //Панел съдържащ бутоните за предишен и следващ въпрос
    private JPanel pnlForPreviousQuestion = new JPanel();//Панел за бутона Previous
    private JPanel pnlForNextQuestion = new JPanel();//Панел за бутона Next
    private JPanel pnlForFirstName = new JPanel();//Панел за 1-то име на студента
    private JPanel pnlForSecondName = new JPanel();//Панел за 2-то име на студента
    private JLabel lblForTime = new JLabel();//Лейбъл показващ оставащото време
    //до края на теста
    private JTextField txtNameOfTheStudent = new JTextField();//текстово поле името на студента
    private JLabel lblname = new JLabel("Име:", JLabel.CENTER);//Лейбъл със съдържание име
    private JLabel lblForMeasureUnit = new JLabel(" Секунди");//Лейбъл показващ
    //мерната единица на показващото се в lblForTime поле
    private JLabel lblNumberOfCurrentQuestion = new JLabel();//Лейбъл показващ
    //поредния номер на показвания въпрос
    private JButton btnSend = new JButton(" Изпрати теста");
    private CreateQuestionBoard pnlBoardForQuestions = new CreateQuestionBoard();
    //Създаване на панелза за въпросите
    private JButton btnPreviousQuestion = new JButton("Previous");//Бутон за зареждане на
    //предишния въпрос
    private JButton btnNextQuestion = new JButton("Next");//Бутон за зареждане на
    //следващия въпрос
    private JTextArea txtAreaForResult = new JTextArea();//Текстова равнина за резултата от
    //теста
    //************************************************************************
    private ObjectOutputStream output; // output stream to server
    private ObjectInputStream input; // input stream from server
    private String chatServer; // host server-a
    private Socket client; // сокет за комуникация със сървара
    //************************************************************************
    private boolean isReadyToSendTheFilledExam = false;//Дали е може да се прати 
    //изпита с попълнените отговори към сървата
    private int intNumbersOfQuestion = 0;//Броя на въпросите за теста
    private QuestionsRecord[] arrayWithQuestions;//Масив с въпросите, които 
    //получава клиента
    private int intCurrentNumberOfQuestion = 0;//Кой е текущия номер на върпоса,
    //който се показва на потребителя
    private int intNumberOfTrueQuestions = 0;//Броя на верните въпроси
    private String stringReportFromTest = new String();//репорт за протичането на теста

    //Конструктор
    public Client(String host) {
        super("Client");

        chatServer = host; // сървар, към който този клиент се свързва
        pnlOuterPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 2), " Тест "));
        pnlOuterPanel.setLayout(new GridLayout(3, 1, 10, 10));
        pnlTimePannel.setLayout(new GridLayout(3, 2, 10, 10));

        //Прави бутоните неактивни
        btnSend.setEnabled(false);
        btnPreviousQuestion.setEnabled(false);
        btnNextQuestion.setEnabled(false);

        //***Action Listeners

        //Action Listener за бутона Next
        btnNextQuestion.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //Увеличава с едно поредния номер на въпроса при натискане на бутона
                //NextQuestion
                //Създава обешкт от тип QuestionsForUser за попълване на панела
                //със съответния въпрос
                QuestionsForUser questionsForUser = new QuestionsForUser();
                intCurrentNumberOfQuestion = (intCurrentNumberOfQuestion + 1) % intNumbersOfQuestion;
                lblNumberOfCurrentQuestion.setText(String.format("%d of %d", intCurrentNumberOfQuestion + 1,
                        intNumbersOfQuestion));
                questionsForUser.fill(pnlBoardForQuestions, arrayWithQuestions[intCurrentNumberOfQuestion]);


            }
        });//end of Listener

        //Action Listener за бутона Previous
        btnPreviousQuestion.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //Намалява с едно поредния номер на въпроса при натискане на бутона
                //PreiousQuestion
                //Създава обешкт от тип QuestionsForUser за попълване на панела
                //със съответния въпрос
                QuestionsForUser questionsForUser = new QuestionsForUser();
                intCurrentNumberOfQuestion =
                        ((intCurrentNumberOfQuestion - 1) < 0) ? (intNumbersOfQuestion - 1) : (intCurrentNumberOfQuestion - 1);
                lblNumberOfCurrentQuestion.setText(String.format("%d of %d", intCurrentNumberOfQuestion + 1,
                        intNumbersOfQuestion));
                questionsForUser.fill(pnlBoardForQuestions, arrayWithQuestions[intCurrentNumberOfQuestion]);
            }
        });//end of Action Listener

        //Добавяне на Listener за първия Radio Button
        pnlBoardForQuestions.getRdbtnFirstCorrectAnswer().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //Създава булев масив, който set-ва към съответния потребителски отговор
                boolean[] setUserChoiseArray = new boolean[4];
                boolean isWasWrittenBefore = false;
                //Провеява дали потребителя е марирвал нещо преди
                for (int i = 0; i < 4; i++) {
                    //Ако някой от отговорите преди това е маркиран
                    if (arrayWithQuestions[intCurrentNumberOfQuestion].getUserChoise()[i] == true) {
                        isWasWrittenBefore = true;
                        //Ако предишния маркиран отговор е бил правилен  и е различен от отговор 0
                        //намали броча на верните отговори, защото потребителя се отказва от правилно
                        //селектирания отговор
                        if (arrayWithQuestions[intCurrentNumberOfQuestion].getRightAnswer()[i] == true && (i != 0)) {
                            intNumberOfTrueQuestions--;
                        }
                        //Ако е маркиран от потребителя грешен отговор преди, но сега той посочва 0-я отговор
                        //и той е верен
                        if (arrayWithQuestions[intCurrentNumberOfQuestion].getRightAnswer()[i] == false && (arrayWithQuestions[intCurrentNumberOfQuestion].getRightAnswer()[0] == true)) {
                            intNumberOfTrueQuestions++;
                        }
                    }
                }//end of for
                //Ако не е маркирано преди това и верния отговоре 0-вия увеличи броя на
                //верните отговори с единица

                if ((isWasWrittenBefore == false) && (arrayWithQuestions[intCurrentNumberOfQuestion].getRightAnswer()[0] == true)) {
                    intNumberOfTrueQuestions++;
                }

                setUserChoiseArray[0] = true;
                arrayWithQuestions[intCurrentNumberOfQuestion].setUserChoise(setUserChoiseArray);



            }
        });//end of Listener

        //Добавяне на Listener за втория Radio Button
        pnlBoardForQuestions.getRdbtnSecondCorrectAnswer().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //Създава булев масив, който set-ва към съответния потребителски отговор
                boolean[] setUserChoiseArray = new boolean[4];
                boolean isWasWrittenBefore = false;
                //Провеява дали потребителя е марирвал нещо преди
                for (int i = 0; i < 4; i++) {
                    //Ако някой от отговорите преди това е маркиран
                    if (arrayWithQuestions[intCurrentNumberOfQuestion].getUserChoise()[i] == true) {
                        isWasWrittenBefore = true;
                        //Ако предишния маркиран отговор е бил правилен  и е различен от отговор 1
                        //намали броча на верните отговори, защото потребителя се отказва от правилно
                        //селектирания отговор
                        if (arrayWithQuestions[intCurrentNumberOfQuestion].getRightAnswer()[i] == true && (i != 1)) {
                            intNumberOfTrueQuestions--;
                        }
                        //Ако е маркиран от потребителя грешен отговор преди, но сега той посочва 1-я отговор
                        //и той е верен
                        if (arrayWithQuestions[intCurrentNumberOfQuestion].getRightAnswer()[i] == false && (arrayWithQuestions[intCurrentNumberOfQuestion].getRightAnswer()[1] == true)) {
                            intNumberOfTrueQuestions++;
                        }
                    }
                }//end of for
                //Ако не е маркирано преди това и верния отговоре 1-вия увеличи броя на
                //верните отговори с единица

                if ((isWasWrittenBefore == false) && (arrayWithQuestions[intCurrentNumberOfQuestion].getRightAnswer()[1] == true)) {
                    intNumberOfTrueQuestions++;
                }
                setUserChoiseArray[1] = true;
                arrayWithQuestions[intCurrentNumberOfQuestion].setUserChoise(setUserChoiseArray);




            }
        });//end of Listener

        //Добавяне на Listener за третия Radio Button
        pnlBoardForQuestions.getRdbtnThirdCorrectAnswer().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //Създава булев масив, който set-ва към съответния потребителски отговор
                boolean[] setUserChoiseArray = new boolean[4];
                boolean isWasWrittenBefore = false;
                //Провеява дали потребителя е марирвал нещо преди
                for (int i = 0; i < 4; i++) {
                    //Ако някой от отговорите преди това е маркиран
                    if (arrayWithQuestions[intCurrentNumberOfQuestion].getUserChoise()[i] == true) {
                        isWasWrittenBefore = true;
                        //Ако предишния маркиран отговор е бил правилен  и е различен от отговор 2
                        //намали броча на верните отговори, защото потребителя се отказва от правилно
                        //селектирания отговор
                        if (arrayWithQuestions[intCurrentNumberOfQuestion].getRightAnswer()[i] == true && (i != 2)) {
                            intNumberOfTrueQuestions--;
                        }
                        //Ако е маркиран от потребителя грешен отговор преди, но сега той посочва 2-я отговор
                        //и той е верен
                        if (arrayWithQuestions[intCurrentNumberOfQuestion].getRightAnswer()[i] == false && (arrayWithQuestions[intCurrentNumberOfQuestion].getRightAnswer()[2] == true)) {
                            intNumberOfTrueQuestions++;
                        }
                    }
                }//end of for
                //Ако не е маркирано преди това и верния отговоре 2-ия увеличи броя на
                //верните отговори с единица
                if ((isWasWrittenBefore == false) && (arrayWithQuestions[intCurrentNumberOfQuestion].getRightAnswer()[2] == true)) {
                    intNumberOfTrueQuestions++;
                }
                setUserChoiseArray[2] = true;
                arrayWithQuestions[intCurrentNumberOfQuestion].setUserChoise(setUserChoiseArray);



            }
        });//end of Listener

        //Добавяне на Listener за четвъртия Radio Button
        pnlBoardForQuestions.getRdbtnFourthCorrectAnswer().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //Създава булев масив, който set-ва към съответния потребителски отговор
                boolean[] setUserChoiseArray = new boolean[4];
                boolean isWasWrittenBefore = false;
                //Провеява дали потребителя е марирвал нещо преди
                for (int i = 0; i < 4; i++) {
                    //Ако някой от отговорите преди това е маркиран
                    if (arrayWithQuestions[intCurrentNumberOfQuestion].getUserChoise()[i] == true) {
                        isWasWrittenBefore = true;
                        //Ако предишния маркиран отговор е бил правилен  и е различен от отговор 3
                        //намали броча на верните отговори, защото потребителя се отказва от правилно
                        //селектирания отговор
                        if (arrayWithQuestions[intCurrentNumberOfQuestion].getRightAnswer()[i] == true && (i != 3)) {
                            intNumberOfTrueQuestions--;
                        }
                        //Ако е маркиран от потребителя грешен отговор преди, но сега той посочва 3-я отговор
                        //и той е верен
                        if (arrayWithQuestions[intCurrentNumberOfQuestion].getRightAnswer()[i] == false && (arrayWithQuestions[intCurrentNumberOfQuestion].getRightAnswer()[3] == true)) {
                            intNumberOfTrueQuestions++;
                        }
                    }
                }//end of for
                //Ако не е маркирано преди това и верния отговоре 3-вия увеличи броя на
                //верните отговори с единица
                if ((isWasWrittenBefore == false) && (arrayWithQuestions[intCurrentNumberOfQuestion].getRightAnswer()[3] == true)) {
                    intNumberOfTrueQuestions++;
                }
                setUserChoiseArray[3] = true;
                arrayWithQuestions[intCurrentNumberOfQuestion].setUserChoise(setUserChoiseArray);



            }
        });//end of Listener

        //Добавяне на ActionListener за бутона Send
        btnSend.addActionListener(new ActionListener() {

            double doublePercentageOfTrueQuestions = 0;//Процентът на верни отговори
            //на теста, които е дал потребителя

            public void actionPerformed(ActionEvent e) {

                if (txtNameOfTheStudent.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(null, "Не сте въвели име", "Няма Име", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                //Пресмятане на процента верни отгооври
                doublePercentageOfTrueQuestions = ((double) intNumberOfTrueQuestions / (double) intNumbersOfQuestion) * 100;


                //Изпраща към клиента репорта за теста му
                pnlOuterPanel.removeAll();
                pnlOuterPanel.setVisible(false);

                Report reportOfExam = new Report();

                stringReportFromTest = reportOfExam.reportOfExam(doublePercentageOfTrueQuestions, txtNameOfTheStudent.getText(), arrayWithQuestions);

                txtAreaForResult.append(stringReportFromTest);
                txtAreaForResult.setVisible(true);
                pnlForReport.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 2), " Резултат "));
                pnlForReport.add(new JScrollPane(txtAreaForResult));
                add(pnlForReport);
                //pnlForReport.repaint();
                System.out.println(stringReportFromTest);



                isReadyToSendTheFilledExam = true;



            }
        });


        ///!!!Изтрий
        lblForTime.setText("576");
        lblForMeasureUnit.setText(" секунди");
        lblNumberOfCurrentQuestion.setText("0 of 0");
        ///
        lblForTime.setHorizontalAlignment(SwingConstants.CENTER);
        lblNumberOfCurrentQuestion.setHorizontalAlignment(SwingConstants.CENTER);
        pnlTimePannel.add(lblForTime);
        pnlTimePannel.add(lblForMeasureUnit);
        pnlTimePannel.add(lblNumberOfCurrentQuestion);
        pnlTimePannel.add(btnSend);
        pnlTimePannel.add(lblname);
        pnlTimePannel.add(txtNameOfTheStudent);


        //Добавяне на панел за въпросите
        pnlQuestionPannel.add(pnlBoardForQuestions, BorderLayout.CENTER);

        //Добавяне на Бутоните Next Question и Previous Question

        pnlForPreviousQuestion.add(btnPreviousQuestion, new BorderLayout().CENTER);
        pnlForNextQuestion.add(btnNextQuestion, new BorderLayout().CENTER);
        pnlPreviousNextQuestion.add(pnlForPreviousQuestion, new BorderLayout().CENTER);
        pnlPreviousNextQuestion.add(pnlForNextQuestion, new BorderLayout().LINE_END);

        //Добавяне на панела към прозореца
        pnlTimePannelOutter.add(pnlTimePannel, BorderLayout.CENTER);
        pnlOuterPanel.add(pnlTimePannelOutter);
        pnlOuterPanel.add(pnlQuestionPannel, BorderLayout.CENTER);
        pnlOuterPanel.add(pnlPreviousNextQuestion, new BorderLayout().CENTER);
        add(pnlOuterPanel, BorderLayout.CENTER);


        setSize(375, 550); // задава размер на прозореца за клиента
        setVisible(true); // показва прозореца
    } // край на конструктора за клиента

    // Свързване към сървара и размяна на информация
    public void runClient() {
        try {
            connectToServer(); // създава сокет, за да направи връзка
            getStreams(); // взима входящите и изходящите потоци
            processConnection(); // изпълнява връзката
        } catch (EOFException eofException) {
            //!!!
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            closeConnection(); // затваря връзката
        }
    } // end of runClient

    // Връзка със сървара
    private void connectToServer() throws IOException {

        //Прави сокет, за да направи връзкасъс сървара
        client = new Socket(InetAddress.getByName(chatServer), 12345);

    } // end of connectToServer

    // Взима потоците за изпращане и приемане на информация
    private void getStreams() throws IOException {
        //Създава изходящ поток за обекти
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush(); // flush output buffer to send header information

        //Създава входящ поток за обекти
        input = new ObjectInputStream(client.getInputStream());

    } // end of getStreams

    // Изпълняване на връзката със сървара
    private void processConnection() throws IOException {

        do {
            try {
                //Изпраща заявка към сървара, че може да му изпрати информацията
                boolean sendtrue = true;
                output.writeObject(sendtrue);
                output.flush();
                //Приема за колко минути трябва да се направи теста
                int minutes = Integer.parseInt(input.readObject().toString());
                //Приема колко въпроса ще е теста
                intNumbersOfQuestion = Integer.parseInt(input.readObject().toString());
                //Създаване на масив с големина равна на броя на въпросите подадени
                //му от сървара
                arrayWithQuestions = new QuestionsRecord[intNumbersOfQuestion];

                //Попълване на масива с въпросите
                for (int i = 0; i < intNumbersOfQuestion; i++) {
                    arrayWithQuestions[i] = (QuestionsRecord) input.readObject();
                }




                //Прави бутоните активни
                btnSend.setEnabled(true);
                btnPreviousQuestion.setEnabled(true);
                btnNextQuestion.setEnabled(true);
                lblNumberOfCurrentQuestion.setText(String.format("%d of %d", intCurrentNumberOfQuestion + 1,
                        intNumbersOfQuestion));
                //!!!PRoba
                QuestionsForUser questionsForUser = new QuestionsForUser();
                questionsForUser.fill(pnlBoardForQuestions, arrayWithQuestions[intCurrentNumberOfQuestion]);


                //Да изпрати теста на Сървара и тогава затваря


                //Проверявай дали е натиснат буона за изпращане или времето е изтекло
                do {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                    }
                    if (isReadyToSendTheFilledExam == true) {

                        sendData((Object) (true));
                        //Праща името на студента,за да може сървъра да запише
                        //репорта за неговия изпит на файл със същото име
                        sendData((Object) (txtNameOfTheStudent.getText().trim()));
                        //Изпращане на репорт, който сървара трябва да запише
                        sendData((Object) (stringReportFromTest));
                        break;
                    }
                } while (true);



                //!!! Да се направи на true означава да изпрати информацията за
                //теста на сървара и да изведе резултата


            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            } // end catch

        } while (isReadyToSendTheFilledExam == false);
    } // end of processConnection

    // close streams and socket
    private void closeConnection() {
        try {
            output.close(); // затваряне на изходящия поток
            input.close(); // затваряне на входящия поток
            client.close(); // затваряне на сокета
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    } // end of closeConnection

    // Изпраща информация до сървара
    private void sendData(Object informationToSend) {
        try {
            output.writeObject(informationToSend);
            output.flush(); // изчиства информацията за изход
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } // end catch
    } // end method sendData
    // manipulates displayArea in the event-dispatch thread
    /*private void displayMessage(final String messageToDisplay) {
    SwingUtilities.invokeLater(
    new Runnable() {

    public void run() {
    displayArea.append(messageToDisplay);
    }
    } // end of inner class
    );
    } // end of displayMessage*/
} // end of class Client

