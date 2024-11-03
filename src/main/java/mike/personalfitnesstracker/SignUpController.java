package mike.personalfitnesstracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class SignUpController
{
    @FXML
    private RadioButton newcomerRB;
    @FXML
    private RadioButton proRB;
    @FXML
    private CheckBox RippedCB;
    @FXML
    private RadioButton amateurRB;
    @FXML
    private CheckBox loseWeightCB;
    @FXML
    private CheckBox gainMuscleCB;
    @FXML
    private TextField enterPasswordTF;
    @FXML
    private TextField enterEmailTF;
    @FXML
    private TextField enterUserTF;
    @FXML
    private TextField enterAgeTF;
    @FXML
    private TextField enterCurWeightTF;
    @FXML
    private TextField enterTargetWeightTF;
    @FXML
    private TextField enterLastNameTF;
    @FXML
    private TextField enterFirstNameTF;
    @FXML
    private TextField enterFeetTF;
    @FXML
    private TextField enterInchesTF;

    @javafx.fxml.FXML
    public void initialize() {

    }

    @FXML
    public void getLevel(ActionEvent actionEvent) {

    }

    @FXML
    public void getGoals(ActionEvent actionEvent) {

    }

    @FXML
    public void createAccount (ActionEvent event) throws IOException {

        //get user inputted data from text fields (convert to appropriate type as needed)
        String username = enterUserTF.getText();
        String password = enterPasswordTF.getText();
        String email = enterEmailTF.getText();
        String firstName = enterFirstNameTF.getText();
        String lastName = enterLastNameTF.getText();
        String strAge = enterAgeTF.getText();
        String strFeet = enterFeetTF.getText();
        String strInches = enterInchesTF.getText();
        String strWeight = enterCurWeightTF.getText();
        String strTargetWeight = enterTargetWeightTF.getText();

        //local variable used to output error message to user if incorrect data is inputted
        String invalid = "";

        if(!PatternChecker.isValidUsername(username)) {
            if(username.isEmpty()){
                invalid += "Username cannot be empty!\n";
            }
            else{
                invalid += "Invalid username!\n";
            }
        }

        if(!PatternChecker.isValidPassword(password)) {
            if(password.isEmpty()){
                invalid += "Password cannot be empty!\n";
            }
            else{
                invalid += "Invalid password!\n";
            }
        }

        if(!PatternChecker.isCorrectEmail(email)) {
            if(email.isEmpty()){
                invalid += "Email cannot be empty!\n";
            }
            else{
                invalid += "Invalid email!\n";
            }
        }

        if(!PatternChecker.isCorrectFirstName(firstName)) {
            if(firstName.isEmpty()){
                invalid += "First name cannot be empty!\n";
            }
            else{
                invalid += "Invalid first name!\n";
            }
        }

        if(!PatternChecker.isCorrectLastName(lastName)) {
            if(lastName.isEmpty()){
                invalid += "Last name cannot be empty!\n";
            }
            else{
                invalid += "Invalid last name!\n";
            }
        }

        if(!PatternChecker.isValidAge(strAge)) {
            if(strAge.isEmpty()){
                invalid += "Age cannot be empty!\n";
            }
            else{
                invalid += "Invalid age!\n";
            }
        }



        if(!PatternChecker.isCorrectWeight(strWeight)) {
            if(strWeight.isEmpty()){
                invalid += "Current weight cannot be empty!\n";
            }
            else{
                invalid += "Invalid current weight!\n";
            }
        }

        if(!PatternChecker.isCorrectWeight(strTargetWeight)) {
            if(strTargetWeight.isEmpty()){
                invalid += "Target weight cannot be empty!\n";
            }
            else{
                invalid += "Invalid target weight!\n";
            }
        }

        //check if user's input is within the program's specified limits
        //checking age
        int age = 0;
        if(!strAge.isEmpty()){
            age = Integer.parseInt(strAge);
        }


        //checking height
        double heightFeet = 0.0;
        double heightInches = 0.0;

        if(!strFeet.isEmpty()){
            heightFeet = Double.parseDouble(strFeet);
        }

        if(!strInches.isEmpty()){
            heightInches = Double.parseDouble(strInches);
        }

        //local variable used to determine height of the user. Will combine feet and inches properly
        double height;

        //if the user's inches are between 0-9, then properly format height variable
        if(heightInches >= 0 && heightInches < 10){
            height = heightFeet + (heightInches / 10);
        }
        //if the user's inches are either 10 or 11, then properly format as so
        else{
            height = heightFeet + (heightInches / 100);
        }



        //checking weight
        double curWeight = 0.0;
        double targetWeight = 0.0;

        if(!strWeight.isEmpty()){
            curWeight = Double.parseDouble(strWeight);
        }
        if(!strTargetWeight.isEmpty()){
            targetWeight = Double.parseDouble(strTargetWeight);
        }



        //if the user inputted any incorrect information when creating an account, alert the user as so
        if(!invalid.isEmpty()){
            Alert a = new Alert(Alert.AlertType.WARNING);

            a.setHeight(300);
            a.setWidth(250);

            a.setHeaderText("Invalid input");

            a.setContentText(invalid);

            a.show();
        }
        //otherwise, create account and add to Firebase
        else{

            //'Person' object that pertains to the user
            Person addPerson = new Person(firstName, lastName, age, curWeight, height);

            //'Account' object created from Person object
            Account addAccount = new Account(username, password, email, addPerson);

            Parent loginParent = FXMLLoader.load(getClass().getResource("login.fxml"));
            Scene loginScene = new Scene(loginParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(loginScene);
            window.centerOnScreen();
            window.show();
        }
    }
}