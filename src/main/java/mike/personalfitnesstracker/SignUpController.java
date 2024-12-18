package mike.personalfitnesstracker;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class SignUpController
{
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
    @FXML
    private Button chooseImageBtn;

    private File pfpFile;

    private boolean darkmode;


    @javafx.fxml.FXML
    public void initialize() {

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

        if(!PatternChecker.isValidFeet(strFeet)) {
            if(strFeet.isEmpty()){
                invalid += "Feet cannot be empty!\n";
            }
            else{
                invalid += "Invalid metric for feet!\n";
            }
        }
        if(!PatternChecker.isValidInches(strInches)) {
            if(strInches.isEmpty()){
                invalid += "Inches cannot be empty!\n";
            }
            else{
                invalid += "Invalid metric for inches!\n";
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
        int heightFeet = 0;
        int heightInches = 0;

        if(!strFeet.isEmpty()){
            heightFeet = Integer.parseInt(strFeet);
        }

        if(!strInches.isEmpty()){
            heightInches = Integer.parseInt(strInches);
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
            AlertManager.showAlert(Alert.AlertType.WARNING, "Invalid input", invalid);
        }

        //otherwise, create account and add to Firebase
        else{

            //'Person' object that pertains to the user
            Person addPerson = new Person(firstName, lastName, age, curWeight, heightFeet, heightInches);

            String pfpString = uploadProfilePicture(pfpFile);

            System.out.println(pfpString);
            if(pfpString.isEmpty()){
                AlertManager.showAlert(Alert.AlertType.WARNING, "Error!", "Need to select a profile picture!");
            }

            //'Account' object created from Person object
            Account addAccount = new Account(username, password, email, firstName, lastName, age, curWeight, heightFeet, heightInches, targetWeight, pfpString);

            //Create a request to add valid person to Firebase
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setEmailVerified(false)
                    .setPassword(password)
                    .setDisplayName(username)
                    .setDisabled(false);

            //contains everything about the user record stored in the Authentication tab of Firebase
            UserRecord userRecord;
            try{
                userRecord = Main.fauth.createUser(request);

                //display info alert
                AlertManager.showAlert(Alert.AlertType.INFORMATION, "Success!", "Account created successfully!");


            }
            catch(FirebaseAuthException ex){
                System.out.println("Error creating a new user");
            }

            //creating a collection called 'Person'
            //UUID randomization
            DocumentReference docRef = Main.fstore.collection("Person").document(UUID.randomUUID().toString());

            //create a collection to store user data
            Map<String, Object> data = new HashMap<>();

            //Store user data into map
            data.put("First Name", addAccount.getFirstName());
            data.put("Last Name", addAccount.getLastName());
            data.put("Username", addAccount.getUsername());
            data.put("Password", addAccount.getPassword());
            data.put("Email", addAccount.getEmail());
            data.put("Age", addAccount.getAge());
            data.put("Current Ft", addAccount.getFeet());
            data.put("Current Inches", addAccount.getInches());
            data.put("Current Weight", addAccount.getWeight());
            data.put("Target Weight", addAccount.getTargetWeight());
            data.put("Profile Picture BlobInfo", addAccount.getPfpBlobInfo());

            //write data to 'Person' collection within Firebase
            ApiFuture<WriteResult> result = docRef.set(data);

            //take user back to Login-screen
            SceneManager.switchScene("login.fxml");
        }
    }

    @FXML
    public void cancel(ActionEvent actionEvent) throws IOException {
        SceneManager.switchScene("login.fxml");
    }



    private String uploadProfilePicture(File file){
        String destinationPath = "pfps/OK.jpg";


        try {
            // Initialize Firebase App
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/mike/personalfitnesstracker/key.json");
            StorageOptions options = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            Storage storage = options.getService();

            String bucketName = "personal-fitness-tracker-66576.firebasestorage.app"; // Replace with your bucket name
            destinationPath = "pfps/" + UUID.randomUUID() +  file.getName(); // Path in the bucket
            String filename = file.getName();
            String fileExtension = filename.substring(filename.lastIndexOf(".") + 1);
            // Read the file and upload to the bucket
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, destinationPath)
                    .setContentType("image/"+fileExtension) // Set the MIME type
                    .build();
            storage.create(blobInfo, fileBytes);

            System.out.println("File uploaded successfully to: " + destinationPath);

        } catch (Exception e) {
            e.printStackTrace();
        }





        return destinationPath;
    }


    @FXML
    public void chooseFileButtonClicked(ActionEvent actionEvent) {
        handleChooseFileButtonClicked();
    }

    private void handleChooseFileButtonClicked(){
        try {
            // Configure FileChooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select an Image to Upload");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );

            // Show FileChooser and get the selected file
            Stage currentStage = (Stage) chooseImageBtn.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(currentStage);
            if (selectedFile != null) {
                pfpFile = selectedFile;
            } else {
                System.out.println("No file selected.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}