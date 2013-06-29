package multiplechoicetestpack;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

//Клас с един метод Create, кото връща макета за добавянето на нов въпрос
public class CreateQuestionBoard extends JPanel {

    private JLabel lblQuestionName;
    private JLabel lblRightAnswer;
    private JLabel lblAnswers;
    private JTextField txtQuestion;
    private JTextField txtFirstAnswer;
    private JTextField txtSecondAnswer;
    private JTextField txtThirdAnswer;
    private JTextField txtFourthAnswer;
    private JRadioButton rdbtnFirstCorrectAnswer;
    private JRadioButton rdbtnSecondCorrectAnswer;
    private JRadioButton rdbtnThirdCorrectAnswer;
    private JRadioButton rdbtnFourthCorrectAnswer;
    private JRadioButton rdbEmptyButton;
    //Създаване на празен бутон, който да бъде невидим и да взима селекцията за него
    //след като веднъж е бил маркиран някой от другите бутони, така истинските
    //бутони ще останат немаркирани ,а фиктивния невидим бтон ще е маркиран,но
    //няма да се вижда и няма да играе никаква роля
    private JPanel pnlQuestionPanelOuter;
    private JPanel pnlQuestionPanel;
    private JPanel pnlLabelPanel;
    private JPanel pnlFirstQuestionPanel;
    private JPanel pnlSecondQuestionPanel;
    private JPanel pnlThirdQuestionPanel;
    private JPanel pnlFourthQuestionPanel;

    public CreateQuestionBoard() {
        this.setLayout(new GridLayout(1, 1, 5, 5));
        lblQuestionName = new JLabel("Въпрос:", JLabel.CENTER);
        lblRightAnswer = new JLabel("Верен:", JLabel.CENTER);
        lblAnswers = new JLabel("Отговори:", JLabel.CENTER);


        txtQuestion = new JTextField();
        txtFirstAnswer = new JTextField();
        txtSecondAnswer = new JTextField();
        txtThirdAnswer = new JTextField();
        txtFourthAnswer = new JTextField();

        rdbtnFirstCorrectAnswer = new JRadioButton();
        rdbtnSecondCorrectAnswer = new JRadioButton();
        rdbtnThirdCorrectAnswer = new JRadioButton();
        rdbtnFourthCorrectAnswer = new JRadioButton();
        //Инициализираме фиктивния бутон и го правим невидим
        rdbEmptyButton = new JRadioButton();
        rdbEmptyButton.setVisible(false);


        pnlQuestionPanelOuter = new JPanel(new GridLayout(6, 2, 5, 5));
        pnlQuestionPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        pnlLabelPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        pnlFirstQuestionPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        pnlSecondQuestionPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        pnlThirdQuestionPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        pnlFourthQuestionPanel = new JPanel(new GridLayout(1, 2, 5, 5));



        this.Create();

    }

    public JRadioButton getRdbtnFirstCorrectAnswer() {
        return rdbtnFirstCorrectAnswer;
    }

    public void setRdbtnFirstCorrectAnswer(boolean isSelected) {
        this.rdbtnFirstCorrectAnswer.setSelected(isSelected);
    }

    public JRadioButton getRdbtnFourthCorrectAnswer() {
        return rdbtnFourthCorrectAnswer;
    }

    public void setRdbtnFourthCorrectAnswer(boolean isSelected) {
        this.rdbtnFourthCorrectAnswer.setSelected(isSelected);
    }

    public JRadioButton getRdbtnSecondCorrectAnswer() {
        return rdbtnSecondCorrectAnswer;
    }

    public void setRdbtnSecondCorrectAnswer(boolean isSelected) {
        this.rdbtnSecondCorrectAnswer.setSelected(isSelected);
    }

    public JRadioButton getRdbtnThirdCorrectAnswer() {
        return rdbtnThirdCorrectAnswer;
    }

    public void setRdbtnThirdCorrectAnswer(boolean isSelected) {
        this.rdbtnThirdCorrectAnswer.setSelected(isSelected);
    }

    public JTextField getTxtFirstAnswer() {
        return txtFirstAnswer;
    }

    public void setTxtFirstAnswer(String newText) {
        this.txtFirstAnswer.setText(newText);
    }

    public JTextField getTxtFourthAnswer() {
        return txtFourthAnswer;
    }

    public void setTxtFourthAnswer(String newText) {
        this.txtFourthAnswer.setText(newText);
    }

    public JTextField getTxtQuestion() {
        return txtQuestion;
    }

    public void setTxtQuestion(String newText) {
        this.txtQuestion.setText(newText);
    }

    public JTextField getTxtSecondAnswer() {
        return txtSecondAnswer;
    }

    public void setTxtSecondAnswer(String newText) {
        this.txtSecondAnswer.setText(newText);
    }

    public JTextField getTxtThirdAnswer() {
        return txtThirdAnswer;
    }

    public void setTxtThirdAnswer(String newText) {
        this.txtThirdAnswer.setText(newText);
    }

    public void setRdbEmptyButton(boolean isSelected) {
        this.rdbEmptyButton.setSelected(isSelected);
    }


    private void Create() {


        pnlQuestionPanel.add(lblQuestionName, BorderLayout.CENTER);
        pnlQuestionPanel.add(txtQuestion, BorderLayout.CENTER);



        pnlLabelPanel.add(lblRightAnswer, BorderLayout.CENTER);
        pnlLabelPanel.add(lblAnswers, BorderLayout.CENTER);

        rdbtnFirstCorrectAnswer.setHorizontalAlignment(SwingConstants.CENTER);
        rdbtnSecondCorrectAnswer.setHorizontalAlignment(SwingConstants.CENTER);
        rdbtnThirdCorrectAnswer.setHorizontalAlignment(SwingConstants.CENTER);
        rdbtnFourthCorrectAnswer.setHorizontalAlignment(SwingConstants.CENTER);


        pnlFirstQuestionPanel.add(rdbtnFirstCorrectAnswer, BorderLayout.CENTER);
        pnlFirstQuestionPanel.add(txtFirstAnswer, BorderLayout.CENTER);


        pnlSecondQuestionPanel.add(rdbtnSecondCorrectAnswer, BorderLayout.CENTER);
        pnlSecondQuestionPanel.add(txtSecondAnswer, BorderLayout.CENTER);

        pnlThirdQuestionPanel.add(rdbtnThirdCorrectAnswer, BorderLayout.CENTER);
        pnlThirdQuestionPanel.add(txtThirdAnswer, BorderLayout.CENTER);

        pnlFourthQuestionPanel.add(rdbtnFourthCorrectAnswer, BorderLayout.WEST);
        pnlFourthQuestionPanel.add(txtFourthAnswer, BorderLayout.EAST);

        pnlQuestionPanelOuter.add(pnlQuestionPanel);
        pnlQuestionPanelOuter.add(pnlLabelPanel);
        pnlQuestionPanelOuter.add(pnlFirstQuestionPanel);
        pnlQuestionPanelOuter.add(pnlSecondQuestionPanel);
        pnlQuestionPanelOuter.add(pnlThirdQuestionPanel);
        pnlQuestionPanelOuter.add(pnlFourthQuestionPanel);


        //Групиране на бутоните

        ButtonGroup group = new ButtonGroup();


        group.add(rdbtnFirstCorrectAnswer);
        group.add(rdbtnSecondCorrectAnswer);
        group.add(rdbtnThirdCorrectAnswer);
        group.add(rdbtnFourthCorrectAnswer);
        group.add(rdbEmptyButton);
        
        rdbEmptyButton.setSelected(true);






        //Връщане на построения панел
        this.add(pnlQuestionPanelOuter, BorderLayout.CENTER);

    }
}
