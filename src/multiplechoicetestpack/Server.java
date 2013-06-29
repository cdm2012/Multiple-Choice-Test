package multiplechoicetestpack;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class Server extends JFrame {

    private JPanel pnlParametersOfTheTestOuter = new JPanel();
    private JPanel pnlQuestionsForTheTestOuter = new JPanel();
    private JTabbedPane tabPane;//Tabbed Pane за сървара, в която да показва
    //2-ва таба- "Параметри на теста" и "Въпроси"
    private final JPanel pnlParametersOfTheTest =
            new JPanel();//Панел за 1-я таб "Параметри на теста"
    private JLabel lblTimeForTest = new JLabel(" Време за теста в минути");
    private JLabel lblNumberOfQuestions = new JLabel(" Брой на въпросите в теста");
    private JSpinner spnMinutes = new JSpinner();//spinner определящ колко минути
    //да е продължителността на теста
    private JSpinner spnNumberOfQuestions = new JSpinner();//spinner определящ
    //броя на въпросите
    private JPanel pnlForQuestionButtons = new JPanel();//панел за бутоните за
    //отваряне на стара банка с въпроси или за създаване на нова
    private JPanel pnlForQuestionButtonsOuter = new JPanel();//външен панел за
    // бутоните за отваряне на стара банка с въпроси или за създаване на нова
    private JPanel pnlForNewQuestion = new JPanel();//панел за добавяне на нов
    //върпос към вече отворена банка с въпроси
    private JPanel pnlForStartSendingQuestions = new JPanel(new GridLayout(1, 2, 5, 5));
    //панел за добавяне на бутон, след чието натискане вече може да се пратят
    //тестовете на свързалите се клиенти
    private JPanel pnlForStartSendingQuestionsOuter = new JPanel();//външен панел
    //за добавяне на бутон, след чието натискане вече може да се пратят тестовете
    // на свързалите се клиенти
    private JLabel lblInfromationForQuestion = new JLabel("");//дава информация за
    //грешки при въвеждането на нов въпрос
    private JLabel lblInfromationForRunSending = new JLabel();//дава информация за
    //грешки при опит за изпращане на тестови въпроси към клиентите
    private JButton btnOpenBankOfQuestions = new JButton("Open File");
    private JButton btnCreateBankOfQuestions = new JButton("Create new File");
    private JButton btnAddNewQuestion = new JButton("Add Question");
    private JButton btnRunSendingQuestion = new JButton("Run Sending");
    /*************************************************************************/
    private ServerSocket server; // server socket
    private Socket connection; // connection to client
    private int counter = 1; // counter of number of connections
    //private ExecutorService pool = Executors.newCachedThreadPool();
    /*************************************************************************/
    private String strBankOfQuestionsName;//Съхранява името абсолютния път на файла
    private boolean boolCanAddQuestions;//Съхранява true ако имаме отворен файл или
    // съответно създаден нов файл и false ако не е избрана или създадена банка
    //с въпроси
    private int intNumberOfQuestionsInTheBank = 0;//Съхранява колко въпроса има в
    //посочената банка с въпроси
    private int intNumberOfMinutesForTheTest = 0;//Съхранява за колко минути трябва
    //да се изпълни теста
    private int intNumberOfQuestionsForTheTest = 0;//Съхранява колко въпроса ще има
    //теста
    private QuestionsRecord questionsForSending[];//въпросите, които
    //се изпращат на всеки свързан потребител
    private boolean isServerIsReadyToSend = false;//променлива за това дали сървара
    //е готов да праща информация към клиента

    public Server() {
        super("Multiple Choice Test Generator");//Слагане на заглавие на сървара

        tabPane = new JTabbedPane();
        //Външните панели за Parameters
        pnlParametersOfTheTestOuter.setBorder(new TitledBorder(new LineBorder(Color.BLUE, 2), " Параметри на теста "));
        pnlQuestionsForTheTestOuter.setBorder(new TitledBorder(new LineBorder(Color.RED, 2), " Въпроси "));
        pnlParametersOfTheTestOuter.setLayout(new GridLayout(5, 1, 10, 10));
        pnlQuestionsForTheTestOuter.setLayout(new GridLayout(3, 1, 10, 10));


        //Вътрешните панели за Parameters

        pnlParametersOfTheTest.setLayout(new GridLayout(2, 2, 10, 10));
        pnlForQuestionButtons.setLayout(new GridLayout(2, 2, 10, 10));
        pnlForNewQuestion.setLayout(new GridLayout(1, 1, 10, 10));


        //Определяне модела на Spinner-ите
        SpinnerNumberModel minutes = new SpinnerNumberModel(0, 0, 300, 1);
        SpinnerNumberModel questions = new SpinnerNumberModel(0, 0, 350, 1);

        //Създаване на панела за добавяне на нов въпрос
        final CreateQuestionBoard createQuestionBoard = new CreateQuestionBoard();

        //Правене на бутона Add New Question неактивен
        btnAddNewQuestion.setEnabled(false);
        btnRunSendingQuestion.setEnabled(false);

        //Добавяне на моделите към спинарите
        spnMinutes.setModel(minutes);
        spnNumberOfQuestions.setModel(questions);

        //Добавяме Listener, който при смяна на фокуса от spnMinutes да проверява за коректност на данните
        ((JSpinner.DefaultEditor) spnMinutes.getEditor()).getTextField().addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent e) {
                int valueOfMinutesSpinner = 0;
                String spnFieldText = new String();
                spnFieldText = String.valueOf(((JSpinner.DefaultEditor) spnMinutes.getEditor()).getTextField().getValue());

                try {
                    valueOfMinutesSpinner = Integer.parseInt(spnFieldText);
                    if (valueOfMinutesSpinner < 0 || valueOfMinutesSpinner > 300) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException numberFormatException) {
                    spnMinutes.setValue("0");

                }
            }
        });

        //Open File Chooser за отваряне на вече съществуващ файл с въпроси
        btnOpenBankOfQuestions.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                strBankOfQuestionsName = new JFileChoosers().openFile();
                if (!strBankOfQuestionsName.trim().equals("")) {
                    boolCanAddQuestions = true;
                    btnAddNewQuestion.setEnabled(true);
                    btnRunSendingQuestion.setEnabled(true);
                }

            }
        });

        //Save File Chooser за създаване на нова банка с въпроси
        btnCreateBankOfQuestions.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                strBankOfQuestionsName = new JFileChoosers().createNewBankOfQuestions();
                if (!strBankOfQuestionsName.trim().equals("")) {
                   
                    boolCanAddQuestions = true;
                    btnAddNewQuestion.setEnabled(true);

                    btnRunSendingQuestion.setEnabled(true);

                }
            }
        });


        //Бутона btnAddNewQuestion
        btnAddNewQuestion.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new AddQuestion().addQuestion(createQuestionBoard, strBankOfQuestionsName);


            }
        });

        //Бутона RunSending за изпращане на въпросите към свързалите се потребители
        btnRunSendingQuestion.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                QuestionsRecord question = new QuestionsRecord();


                intNumberOfQuestionsForTheTest = Integer.parseInt(spnNumberOfQuestions.getValue().toString());
                RandomAccessFile raf = null;
                if (new File(strBankOfQuestionsName).exists()) {
                    try {
                        raf = new RandomAccessFile(strBankOfQuestionsName, "rw");
                        intNumberOfQuestionsInTheBank = (int) raf.length() / question.size();
                        intNumberOfMinutesForTheTest = Integer.parseInt(spnMinutes.getValue().toString());
                        //Проверки за валидност на броя на въпросите за теста
                        //Ако има 0-ла въпроса или повече въпроси от колкото има
                        //в банката с въпроси тогава събщава за грешка
                        if (Integer.parseInt(spnNumberOfQuestions.getValue().toString()) == 0) {
                            JOptionPane.showMessageDialog(null, "Посочили сте теста да бъде от 0( нула) въпроса, което е невалидна стойност !!!", "Невалиден Брой Въпроси За Теста", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (Integer.parseInt(spnNumberOfQuestions.getValue().toString()) > intNumberOfQuestionsInTheBank) {
                            JOptionPane.showMessageDialog(null, "Броя на въпросите на теста, които сте задали са повече\n от въпросите, съдържащи се в банката с въпросите !", "Невалиден Брой Въпроси За Теста", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (intNumberOfMinutesForTheTest == 0) {
                            JOptionPane.showMessageDialog(null, "Броя на минутите на теста, които сте задали са равни на 0-ла !", "Невалиден Брой Минути За Теста", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        //изчисляване колко минути средно се пада за един въпрос
                        //Ако са по-малко от 0,5 съобщава за прекалено малко време
                        //и задава опция за корекция или за продължение

                        double doubleTimeAverageForQuestion;//Променлива, която
                        //съхранява времето за един въпрос

                        doubleTimeAverageForQuestion = ((double) intNumberOfMinutesForTheTest / Double.parseDouble(spnNumberOfQuestions.getValue().toString()));
                        if (doubleTimeAverageForQuestion < 0.5) {
                            int choise = JOptionPane.showConfirmDialog(null, String.format("Времето за един въпрос е: %.2f мин., което е под\n 0.5 мин(30 секунди) на въпрос. Сигурни ли сте,\n че искате да разрешите изпълняването на теста\n за %d минути със средно %.2f мин за въпрос?",
                                    doubleTimeAverageForQuestion, intNumberOfMinutesForTheTest, doubleTimeAverageForQuestion), "Времето За Отговор На Един Въпросите Е Прекалено Малко", JOptionPane.OK_CANCEL_OPTION);
                            if (choise == JOptionPane.CANCEL_OPTION) {
                                return;
                            }
                        }

                        //Конструира масива с върпосите, които трябва да изпрати

                        questionsForSending = new QuestionsRecord[intNumberOfQuestionsForTheTest];
                        for (int i = 0; i < questionsForSending.length; i++) {
                            questionsForSending[i] = new QuestionsRecord();

                        }
                        //Конструиране на масив с булеви стойности за всичи елементи от банката
                        //с въпроси и ако въпроса е избран съответния му номвер в
                        //масива с булеви стойности става true и не може да се избира 2-ри път
                        boolean isChosen[] = new boolean[intNumberOfQuestionsInTheBank];

                        //Произволно число, което избира произволен въпрос от банката
                        //И проверява дали той е зает или не е ако не е го добавя
                        //към масива questionsForSending ,който ще прати на потребителите
                        Random randomQuestionPosition = new Random();

                        for (int i = 0; i < questionsForSending.length; i++) {

                            do {
                                int next = randomQuestionPosition.nextInt(intNumberOfQuestionsInTheBank);

                                if (isChosen[next] == true) {

                                    continue;
                                } else {
                                    isChosen[next] = true;
                                    raf.seek(question.size() * next);
                                    questionsForSending[i].read(raf);

                                    break;
                                }
                            } while (true);
                            

                        }//end of for




                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    isServerIsReadyToSend = true;
                }//end of if strBankOfQuestionsName.exists()
            }
        });

        //Добавя бутона  етикета и speener към pnParametersOfTheTest панела
        pnlParametersOfTheTest.add(lblTimeForTest);
        pnlParametersOfTheTest.add(spnMinutes);
        pnlParametersOfTheTest.add(lblNumberOfQuestions);
        pnlParametersOfTheTest.add(spnNumberOfQuestions);

        //Добавя бутона  етикета и speener към  pnlForQuestionButtons панела
        pnlForQuestionButtons.add(btnOpenBankOfQuestions);
        pnlForQuestionButtons.add(btnCreateBankOfQuestions);
        pnlForQuestionButtons.add(btnAddNewQuestion);
        pnlForQuestionButtons.add(lblInfromationForQuestion);



        pnlForStartSendingQuestions.add(btnRunSendingQuestion);



        pnlForQuestionButtonsOuter.add(pnlForQuestionButtons, BorderLayout.SOUTH);
        pnlForStartSendingQuestionsOuter.add(pnlForStartSendingQuestions);
        pnlParametersOfTheTestOuter.add(pnlParametersOfTheTest, BorderLayout.CENTER);
        pnlQuestionsForTheTestOuter.add(pnlForQuestionButtonsOuter, BorderLayout.NORTH);
        pnlQuestionsForTheTestOuter.add(createQuestionBoard, BorderLayout.CENTER);
        pnlQuestionsForTheTestOuter.add(pnlForStartSendingQuestionsOuter);

        tabPane.addTab("Параметри на теста ", null, pnlParametersOfTheTestOuter, "Задайте параметрите на теста");
        tabPane.addTab("Въпроси ", null, pnlQuestionsForTheTestOuter, "Задайте банката с въпроси и добавете нови");
        add(tabPane, BorderLayout.CENTER);
        setSize(375, 550); // set size of window
        setVisible(true); // show window
    } // end Server constructor

    // set up and run server
    public void runServer() {
        int count = 0;
        try // set up server to receive connections; process connections
        {
            server = new ServerSocket(12345, 100); // create ServerSocket

            while (true) {
                try {
                    Socket s = waitForConnection(); // wait for a connection
                    Thread thread = new Thread(new InnerSocket(s));
                    thread.setName(String.valueOf(count++));
                    thread.start();
                } // end try
                catch (IOException iOException) {
                    displayMessage("\nServer terminated connection");
                } // end catch
                finally {
                    // closeConnection(); //  затваря връзката

                    counter++;
                } // end finally
            } // end while
        } // end try
        catch (IOException ioException) {
            ioException.printStackTrace();
        } // end catch
    } // end method runServer

//чака да се свърже клиент със сървара и ако се свърже му праща теста с времето
    //ако теста е готов
    private Socket waitForConnection() throws IOException {

        connection = server.accept(); //позволява на сървара да
        //приеме връзката

        return connection;
    } // end of waitForConnection

    //Затваря връзката склиентите
    private void closeConnection() {

        try {
            connection.close(); // затваря сокета
        } // end try
        catch (IOException ioException) {
            ioException.printStackTrace();
        } // end catch
    } // end of closeConnection

    private class InnerSocket implements Runnable {

        private Socket socket;
        private ObjectOutputStream output; // изходен поток към клиента
        private ObjectInputStream input; // входящ поток към клиента

        //Конструктор пиемащ подаден сокет
        public InnerSocket(Socket s) {
            socket = s;
        }

        public void run() {
            runClient(socket);
        }//end of run

        public void runClient(Socket s) {
            try {
                getStreams(s); // взима потоците
                processConnection(s); // изпълнява връзката
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                closeConnection();
            }
        }//end of runClient

        // взима потоците за изпращане и получаване на информацията
        private void getStreams(Socket s) throws IOException {
            // взима изходящия поток
            output = new ObjectOutputStream(s.getOutputStream());
            output.flush(); // изчиства изходящия буфер

            // взима входящия поток
            input = new ObjectInputStream(s.getInputStream());

        } // end of getStreams

        // process connection with client
        private void processConnection(Socket s) throws IOException {

            boolean toSendOrNot = false;//клиентът като се кънектне праща на
            //сървара заявка,че е готов и чака да му се прати теста
            boolean booleanTheTestEnds = false;//Променлива, в която става true
            //когато клиента е свършил с теста

            try {
                //!!! Прави равнината на клиента за въпросите активна ако прочете ,че
                //пратената му булева стойност е true
                toSendOrNot = Boolean.parseBoolean(input.readObject().toString()); // прочита съобщението от
                //клиента и сравнява дали е true
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }

            do {
                // try {
                if (isServerIsReadyToSend == true && toSendOrNot == true) {
                    //Изпраща времето за теста
                    sendData(intNumberOfMinutesForTheTest);
                    //Изпраща масива с обектите от тим QuestionsRecord
                    sendData(questionsForSending);

                    //Чака потвръждение,че е свършил теста при true
                    do {
                        try {

                            Thread.sleep(1000);
                            booleanTheTestEnds = Boolean.parseBoolean(input.readObject().toString());
                            File directory = new File(".");
                            File file = new File(directory.getCanonicalPath() + "\\" + input.readObject().toString() + ".txt");

                          
                            Writer outPut = new BufferedWriter(new FileWriter(file));



                            String report = input.readObject().toString();
                            
                            outPut.write(report);

                            outPut.close();


                            break;
                        } catch (IOException ex) {
                            //ex.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        } catch (InterruptedException ex) {
                        }
                    } while (true);//end of do / while

                } else {
                    try {
                        //Нишката спи 1  сек ,за да не се получи забиване на системата
                        //поради проверката на isServerIsReadyToSend == true && toSendOrNot == true
                        //Тъй като isServerIsReadyToSend се променя асинхронно
                        Thread.sleep(1000);


                    } catch (InterruptedException ex) {
                        //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }

//                    }
//                } catch (ClassNotFoundException classNotFoundException) {
//                    classNotFoundException.printStackTrace();
                }

            } while (booleanTheTestEnds == false);
        } // end of processConnection

        // close streams and socket
        private void closeConnection() {

            try {
                output.close(); // затваря изходящия поток
                input.close(); // затваря входящия поток
                socket.close(); // затваря сокета
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
            }
        } // end of closeConnection

        // изпраща информация към клиента
        private void sendData(Object informationToSend) {
            try //изпраща информация на клиента
            {
                output.writeObject(informationToSend);
                output.flush(); // flush output to client


            } catch (IOException ioException) {
                ioException.printStackTrace();

            } // end catch
        } // end of sendData

        //SendData за масива с въпроси
        private void sendData(Object[] informationToSend) {

            try //изпраща информация на клиента
            {
                //Праща дължината на масива
                output.writeObject(informationToSend.length);
                output.flush();
                //изпраща всичките обекти от масива един по един
                for (int i = 0; i < informationToSend.length; i++) {
                    output.writeObject((Object) informationToSend[i]);
                    output.flush(); // flush output to client
                }

            } catch (IOException ioException) {
                ioException.printStackTrace();

            } // end catch
        } // end of sendData
    }//end of InnerSocket

    // manipulates displayArea in the event-dispatch thread
    private void displayMessage(final String messageToDisplay) {

        SwingUtilities.invokeLater(
                new Runnable() {

                    public void run() // updates displayArea
                    {
                    } // end method run
                } // end anonymous inner class
                ); // end call to SwingUtilities.invokeLater
    } // end method displayMessage

    // manipulates enterField in the event-dispatch thread
    private void setTextFieldEditable(final boolean editable) {
        SwingUtilities.invokeLater(
                new Runnable() {

                    public void run() // sets enterField's editability
                    {
                        //!!!                     enterField.setEditable(editable);
                    } // end method run
                } // end inner class
                ); // end call to SwingUtilities.invokeLater
    } // end method setTextFieldEditable
} // end class Server

