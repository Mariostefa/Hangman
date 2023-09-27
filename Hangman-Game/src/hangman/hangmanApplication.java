
package hangman;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.input.KeyCode;
import java.text.Normalizer;
import javafx.scene.effect.DropShadow;
  

public class hangmanApplication extends Application {
    
    public static void main(String[] args) {
       
        launch(args);
    }
    
    //Stage - Scene - Panes Create
    private Stage stage;
    private Stage popupStage;
    private Scene sceneGame;
    private Scene sceneStart;
    private BorderPane paneGame;
    private BorderPane paneStart;
    
    //Button Creates
    private Button btnStart;
    private Button btnQuit;
    private Button guessButton;
            
    //Hboxes - Labels and Textfieds
    private HBox fieldsContainer;
    private Label incorrectLettersLabel;
    private Label incorrectGuessesLabel;
    private TextField guessTheLetter;
    private Label emptyLabel;
    
    // Create the ImageView
    private Image image;
    private Image image2;
    ImageView imageView;
    Image icon = new Image("images/icon.png");
    
    //Craete RadioButtons and Group
    private ToggleGroup radioGroup = new ToggleGroup();
    private RadioButton rBtnEasy = new RadioButton("Easy Words");
    private RadioButton rBtnNormal = new RadioButton("Normal Words");
    private RadioButton rBtnHard = new RadioButton("Hard Words");
    private Label rButtons = new Label("DIFFICULTY");
    
    //Creates values for the point system
    private int points_Counter = 0;
    private Label points = new Label("POINTS : " + points_Counter );
    
    // creates the fail value
    private int fails = 0;

    //creates values for the wordSetUp
    private String randomWord;
    private int wordLength;
    private int letterCounter;
    
    //creates String for the guessHandler
    private String guessedLetter;

    //remove tonos
    private static String normalizedWord;
    private static String wordWithoutTonos;
    
    
    //Lists with words
    List<String> wordEasy = new ArrayList<>(Arrays.asList(
            "ΑγάπΕ", "ΘάλασσΑ", "ΟυρανΟ", "ΦίλΟ", "ΦωΣ",
            "ΠόρτΑ", "ΆνοιξΗ", "ΚαλΟ", "ΨυχΉ", "ΠαιχνίδΙ",
            "ΧαρΆ", "ΠόλΗ", "ΚήποΣ", "ΑεροπλάνΟ", "ΠιάτΟ"
    ));
    List<String> wordNormal = new ArrayList<>(Arrays.asList(
            "ΑνακάλυψΗ", "ΠεριπέτειΑ", "ΜελωδίΑ", "ΚρυφτΟ", "ΧαρούμενοΣ",
            "ΣυναίσθημΑ", "ΠολιτισμοΣ", "ΑρχαιολογίΑ", "ΕπιστήμΗ", "ΜαγείΑ", 
            "ΚαθρέφτηΣ", "ΑνατολΗ", "ΠαράδεισοΣ", "ΣκοτάδΙ", "ΑπόκρυφοΣ"
    ));
    List<String> wordHard = new ArrayList<>(Arrays.asList(
            "ΑποθέωσΗ", "ΑβύσσοΣ", "ΕυλογίΑ", "ΑστρονομίΑ", "ΑμφισβήτησΗ",
            "ΚοσμογονίΑ", "ΠαράδοξΟ", "ΣυμβολισμοΣ", "ΜεταμόρφωσΗ", "ΥπερβολΉ",
            "ΚαταστροφΉ", "ΥποκείμενΟ", "ΣαλόνικΑ", "ΑσυνείδητΟ" , "ΑπορρόφησΗ"
    ));

    //lists with used Words
    List<String> usedEasyWords = new ArrayList<>();
    List<String> usedNormalWords = new ArrayList<>();
    List<String> usedHardWords = new ArrayList<>();

    

    
    
    @Override
    public void start(Stage primaryStage) {
        
        stage = primaryStage;
        
        //sets up the RadioButtons
        rBtnEasy.setSelected(true);
        rBtnEasy.setToggleGroup(radioGroup);
        rBtnNormal.setToggleGroup(radioGroup);
        rBtnHard.setToggleGroup(radioGroup);
        rButtons.setStyle( "-fx-font-weight: bold;");
        rBtnEasy.setStyle( "-fx-font-weight: bold;");
        rBtnNormal.setStyle( "-fx-font-weight: bold;");
        rBtnHard.setStyle( "-fx-font-weight: bold;");
        
        //Style dropShadow
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(javafx.scene.paint.Color.GRAY);
        dropShadow.setRadius(5);
        dropShadow.setSpread(0.5);
        dropShadow.setOffsetX(3);
        dropShadow.setOffsetY(3);
        
        //methods and actions of the button Start and Quit;
        btnStart = new Button ("START THE GAME");
        btnStart.setOnAction(e -> btnStart_click());
        btnStart.setStyle(" -fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: blue; -fx-background-color: #f5f5cb; -fx-background-radius: 30 ;");
        btnStart.setEffect(dropShadow);
        
        btnQuit = new Button ("EXIT");
        btnQuit.setOnAction(e -> btnQuit_click());
        btnQuit.setStyle(" -fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: red; -fx-background-color: #f5f5cb; -fx-background-radius: 30 ;");
        btnQuit.setEffect(dropShadow);
        
        
        // Load and sets the image
        imageView = new ImageView();
        image = new Image("/images/0.png");
        imageView.setImage(image);
        imageView.setFitWidth(300); // Set desired width
        imageView.setFitHeight(350); // Set desired height
        imageView.setPreserveRatio(true);
        
        //vbox for RadioButtons
        VBox radioBox = new VBox(10);
        radioBox.setPadding(new Insets(10));
        radioBox.getChildren().addAll(rButtons, rBtnEasy, rBtnNormal, rBtnHard);
        radioBox.setAlignment(Pos.CENTER);
        
        
        //hbox for the each label of random word
        fieldsContainer = new HBox(10); // HBox to contain the fields
        fieldsContainer.setPadding(new Insets(15));
        fieldsContainer.setAlignment(Pos.CENTER);
        
        
        // labels to display incorrect guesses
        incorrectLettersLabel = new Label();
        incorrectLettersLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #167ec4; -fx-font-weight: bold;");
        incorrectGuessesLabel = new Label("WRONG LETTER : ");
        incorrectGuessesLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        //hbox for the incorrect labels
        HBox incorrect = new HBox();
        incorrect.setSpacing(15);
        incorrect.getChildren().addAll(incorrectGuessesLabel , incorrectLettersLabel);
        incorrect.setAlignment(Pos.CENTER);
        
        
        
        // Create a textfield where the player is gonna type random letters
        guessTheLetter = new TextField();
        guessTheLetter.setOnKeyPressed(e ->{ 
            if (e.getCode() == KeyCode.ENTER) //gets key code from keyboard and runs the guesshandler action only when ENTER is pressed
                { guessHandler();
            }
        });
        /*TextFormatter<String> textFormatter = new TextFormatter<>(change -> { //this allows only greek letters to be typed on the textfield
            String newText = change.getControlNewText();
            if (newText.matches("[Α-Ωα-ω]*")) {
                return change;
            }
            return null;
        });
        guessTheLetter.setTextFormatter(textFormatter);*/
        
        //vbox for textfield guess
        VBox guessletter = new VBox (guessTheLetter);
        guessletter.setMaxWidth(30);
        guessletter.setAlignment(Pos.CENTER);

        //sets up the button you are gonna press to pass the value you typed on the textfield
        guessButton = new Button("GUESS");
        guessButton.setOnAction(e -> guessHandler());
        guessButton.setStyle("-fx-background-color: #f5f5cb; -fx-background-radius: 30 ;");
        guessButton.setEffect(dropShadow);
        
        //sets vbox for textfield and button (we create the previous vbox so the button does not change its width)
        VBox guess = new VBox (5);
        guess.getChildren().addAll(guessletter, guessButton);
        guess.setMaxWidth(100);
        guess.setAlignment(Pos.CENTER);
        
        //vbox for all the elements
        VBox guessElements = new VBox (10);
        guessElements.getChildren().addAll(imageView, fieldsContainer  , guess , incorrect);
        guessElements.setAlignment(Pos.CENTER);
        
        // points label
        StackPane pointPane = new StackPane(points); 
        points.setStyle("-fx-font-size: 18px; -fx-text-fill: #167ec4; -fx-font-weight: bold;");
        
        //set up pane For The Main Game
        paneGame = new BorderPane();
        paneGame.setPadding(new Insets(0,50,0,0));
        paneGame.setCenter(guessElements );
        paneGame.setRight(pointPane);
        
    
        //vbox for button start and quit
        VBox start = new VBox(10);
        start.getChildren().addAll(btnStart , btnQuit );
        start.setAlignment(Pos.CENTER);
        

        //hbox for the starting screen with all elements
        HBox startBox = new HBox(10);
        startBox.getChildren().addAll(start,radioBox);
        startBox.setAlignment(Pos.CENTER);
        
        //set up pane for the Starting Screen
        paneStart = new BorderPane();
        paneStart.setPadding(new Insets(0,0,0,0));
        paneStart.setCenter(startBox);
        
        
        //Set up scenes for game and start
        sceneStart = new Scene(paneStart , 350 , 250 );
        paneStart.setStyle("-fx-background-color: FDFD96; -fx-border-color: #808046; -fx-border-width: 3px;");
        
        sceneGame = new Scene(paneGame , 600 , 500 );        
        paneGame.setStyle("-fx-background-color: #FDFD96; -fx-border-color: #808046; -fx-border-width: 5px;");
        
        //primary stage
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(sceneStart);
        primaryStage.setTitle("LOBBY");
        primaryStage.show();
    }


    //button start that starts the game
    private void btnStart_click(){
        int radio = 0;
        if (rBtnEasy.isSelected()){
            radio = 0;            
        }else if(rBtnNormal.isSelected()){
            radio = 1;
        }else{
            radio = 2;
        }
        listWord(radio);
        
        stage.setScene(sceneGame);
        stage.setTitle("Hangman");
    }
        
    
    //button that shutsdown the game
    private void btnQuit_click(){  
        stage.close();  
    }
    
    
    //Checks if the usedlist and the list of the selected diffuclty are the same 
    //if there are the same that means that all words have been used
    private boolean statusList (int radio){
        boolean full = false;
        if (radio == 0){
            if(usedEasyWords.size() == wordEasy.size()){
                full = true;
            }
        }else if (radio == 1){
            if(usedNormalWords.size() == wordNormal.size()){
                full = true;
            }
        }else{
            if(usedHardWords.size() == wordHard.size()){
                full = true;
            }
        }
        return full;
    }
    
    
    //gets a random word , checks if this random word it presents on the used words 
    //if it is not runs removeTonos and then runs the wordSetUp
    private void listWord(int radio){
        String word = null;
       
        if (radio == 0){
            while (word == null ) {
                Random random = new Random();
                int randomIndex = random.nextInt(wordEasy.size());
                word = wordEasy.get(randomIndex);
                if(!(usedEasyWords.contains(word))){                //checks if the random word has been already used ones 
                    randomWord = word;
                }
            }   
            usedEasyWords.add(randomWord);
        }else if (radio == 1){
            while (word == null ) {
                Random random = new Random();
                int randomIndex = random.nextInt(wordNormal.size());
                word = wordNormal.get(randomIndex);
                if(!(usedNormalWords.contains(word))){                  //checks if the random word has been already used ones 
                    randomWord = word;
                }
            }   
            usedNormalWords.add(randomWord);
        }else{
            while (word == null ) {
                Random random = new Random();
                int randomIndex = random.nextInt(wordHard.size());
                word = wordHard.get(randomIndex);
                if(!(usedHardWords.contains(word))){                    //checks if the random word has been already used ones 
                    randomWord = word;
                }
            }   
            usedHardWords.add(randomWord);
        }
        randomWord = removeTonos(randomWord);
        wordSetUp(randomWord);
    }
    
    
    //gets the string and removes tonos use it only if you have words that have tonos in it
    public static String removeTonos(String word) {
        normalizedWord = Normalizer.normalize(word, Normalizer.Form.NFD);
        wordWithoutTonos = normalizedWord.replaceAll("\\p{M}", "");
    
        return wordWithoutTonos;
    }
    
    
    //gets the word and creates the Lables for eachLetter  
    public void wordSetUp(String word){

        randomWord = word;
        wordLength = randomWord.length(); //Checks the length of the randomWord
        
        char firstLetter = randomWord.charAt(0);
        char lastLetter  = randomWord.charAt(wordLength-1);
        
        //create labels for each letter
        for (int i = 0; i < wordLength; i++) {
            if(i==0){
               emptyLabel = new Label(Character.toString(firstLetter));
            }else if ( i < wordLength-1) {
               emptyLabel = new Label("_");  
            }else {
               emptyLabel = new Label(Character.toString(lastLetter));
            }
            
            emptyLabel.setStyle("-fx-font-size: 30px;"); 
            fieldsContainer.getChildren().add(emptyLabel); //adds it on a HBox
        }

    }
    

    //gets the input and checks if this letter is presented on the word 
    //checks if the input is a signle letter
    //checks if the letter has already been given
    private void guessHandler() {
        guessedLetter = guessTheLetter.getText().trim().toLowerCase(); //gets the input from TextField
         
        if (!guessedLetter.isEmpty()) {                                 // checks if its empty
            char letter = guessedLetter.charAt(0);                  //gets the first character 
            

            
            boolean isCorrectGuess = false;

            // Check if the letter is present in the word
            for (int i = 1; i < randomWord.length()-1; i++) {
                if (randomWord.charAt(i) == letter  ) {
                    // Correct guess, update the corresonding label
                   emptyLabel = (Label) fieldsContainer.getChildren().get(i);
                   isCorrectGuess = true;
                   String label = emptyLabel.getText();
                   if (!(label.charAt(0) == letter)){                     //checks if the letter that is correct has been already used
                       emptyLabel.setText(Character.toString(letter));
                       letterCounter++;
                   }
                }
            }
            
            
            if (letterCounter+2 == randomWord.length()){                    //add points every time they find the word correctly
                points_Counter++;                   
                points.setText("Points : "+ points_Counter);
                popUp();
            }
            
            if (!isCorrectGuess) {                                           
                // Incorrect guess, update the incorrect guesses label
                String incorrectGuesses = incorrectLettersLabel.getText();      
                if (!(incorrectGuesses.contains(Character.toString(letter)))){ //checks if the letter is already typed on the wrong letters
                    
                    incorrectGuesses += letter + " ";
                    incorrectLettersLabel.setText(incorrectGuesses); //adds it on the label

                    fails++;
                    
                    //changes the color of the background and the button μαντεψε
                    
                    if (fails == 1) {
                        colorChange("-fx-background-color: #ffc642; -fx-border-color: #d1a236; -fx-border-width: 5px;" , "-fx-background-color: #f2d696; -fx-background-radius: 30 ;");
                    } else if (fails == 2) {    
                        colorChange("-fx-background-color: #FFAE42; -fx-border-color: #c98830; -fx-border-width: 5px;" , "-fx-background-color: #f7bf72; -fx-background-radius: 30 ;");
                    } else if (fails == 3) {    
                        colorChange("-fx-background-color: #d6810f; -fx-border-color: #ad680a; -fx-border-width: 5px;" , "-fx-background-color: #c2832f; -fx-background-radius: 30 ;");
                    } else if (fails == 4) {    
                        colorChange("-fx-background-color: #d65b0f; -fx-border-color: #963f09; -fx-border-width: 5px;" , "-fx-background-color: #b85d25; -fx-background-radius: 30 ;");
                    } else if (fails == 5) {
                        colorChange("-fx-background-color: #eb5731; -fx-border-color: #a33e24; -fx-border-width: 5px;" , "-fx-background-color: #db7358; -fx-background-radius: 30 ;");
                    } else if (fails == 6) {
                        colorChange("-fx-background-color: #942719; -fx-border-color: #78190d; -fx-border-width: 5px;" , "-fx-background-color: #913a2f; -fx-background-radius: 30 ;");
                    } else if (fails == 7) {
                        colorChange("-fx-background-color: #541311; -fx-border-color: #3b0503; -fx-border-width: 5px;" , "-fx-background-color: #5e201d; -fx-background-radius: 30 ;");
                    } else {
                        colorChange("-fx-background-color: #3b0d0c; -fx-border-color: #2e0403; -fx-border-width: 5px;" , "-fx-background-color: #4f1e1c; -fx-background-radius: 30 ;");
                    }
                    
                    //changes the image every time the guess is wrong
                    
                    if (fails == 7 ){
                        image2 = new Image("images/7.png");
                        imageView.setImage(image2);
                        guessTheLetter.setDisable(!guessTheLetter.isDisabled());
                        popUp();
                    }else if (fails < 7 ){
                        image2 = new Image("images/"+ fails +".png");
                        imageView.setImage(image2);
                    }
                }
            }

            guessTheLetter.clear();
        }
    }
    
  
    /*runs when they guessed the letter or when they lost.
    gives two choises
    choise:
    1: PlayAgain / Keep Playing
    2: Quit the Game
    3: Difficulty of the word
    */
    private void popUp(){
        popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UTILITY);
        
        Label question;
        Label winLose;
        Label word;
        
        if ( fails == 7){
            //ετικέτες
            winLose = new Label("YOU LOST");
            question = new Label ( "PLAY AGAIN ?");
            word = new Label("YOUR WORD WAS : " + randomWord);
            popupStage.setTitle("");

        }else{
             //ετικέτες
            winLose = new Label("CONGRATULATIONS YOU WON");
            question = new Label ( "DO YOU WANT KEEP PLAYING ?");
            word = new Label("");
            popupStage.setTitle("CONTINUE");
            guessTheLetter.setDisable(!guessTheLetter.isDisabled());
        }
        
        // Γκρουπ ετικετών
        VBox chooseReplay = new VBox (5);
        chooseReplay.getChildren().addAll(winLose , question ,word );
        chooseReplay.setAlignment(Pos.CENTER);
        
        //κουμπία
        Button btnYes = new Button("YES");
        btnYes.setOnAction(e -> btnYes_Click());
            
        Button btnNo = new Button("NO");
        btnNo.setOnAction(e -> btnNo_Click());
            
        //Γρκούπ Κουμπίων
        HBox answerBtn = new HBox(10);
        answerBtn.getChildren().addAll(btnYes,btnNo);
        answerBtn.setAlignment(Pos.CENTER);
            
        //Alignment chooseReplay and answerBtn
        VBox lablesButtons = new VBox(8);
        lablesButtons.getChildren().addAll(chooseReplay,answerBtn);
        lablesButtons.setAlignment(Pos.CENTER);
        
        VBox radioBox = new VBox(10);
        radioBox.setPadding(new Insets(10));
        radioBox.getChildren().addAll(rButtons, rBtnEasy, rBtnNormal, rBtnHard);
        radioBox.setAlignment(Pos.CENTER);
        
        HBox placements = new HBox(10);
        placements.getChildren().addAll(lablesButtons,radioBox);
        placements.setAlignment(Pos.CENTER);
        
        
        StackPane pop = new StackPane();
        pop.getChildren().addAll(placements);
        pop.setPrefSize(500,300);
        
        

        word.setStyle("-fx-font-size: 15px; -fx-font-weight: bold");
        winLose.setStyle("-fx-font-size: 18px; -fx-font-weight: bold");
        question.setStyle("-fx-font-size: 18px; -fx-font-weight: bold");
        pop.setStyle("-fx-background-color: #FDFD96; -fx-border-color: #808046; -fx-border-width: 3px;");
        btnYes.setStyle("-fx-background-color: lightgreen; -fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 30");
        btnNo.setStyle("-fx-background-color: red; -fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 30");
        
        Scene popUpScene = new Scene(pop);
        popupStage.setScene(popUpScene);
        popupStage.showAndWait();
            
    }

    
    //checks if there is any available words on the selected difficulty and resets the values
    private void btnYes_Click() {
        int radio ;
        boolean full ;
        if (rBtnEasy.isSelected()){
            radio = 0;
            full = statusList(radio);  
        }else if(rBtnNormal.isSelected()){
            radio = 1;
            full = statusList(radio);  
        }else{
            radio = 2;
            full = statusList(radio);  
        }
        
        if (full){
            Error();
        }else{
            fails = 0;
            image2 = new Image("images/0.png");
            imageView.setImage(image2);
            popupStage.close();
            fieldsContainer.getChildren().clear();
            incorrectLettersLabel.setText("");
            guessTheLetter.setDisable(!guessTheLetter.isDisabled());
            letterCounter = 0;
            listWord(radio);
            colorChange("-fx-background-color: #FDFD96; -fx-border-color: #808046; -fx-border-width: 5px;" , "-fx-background-color: #f5f5cb; -fx-background-radius: 30 ;");
        }
    }

    
    //ShutsDown the game
    private void btnNo_Click() {
        popupStage.close();
        btnQuit_click();
    }
    
    
    //throws an error when the words of the selected difficulty have been used!
    private void Error(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        
        alert.setTitle("Error alert");
        alert.setHeaderText("WORD LIST ERROR");
        alert.setContentText("THERE ARE NO MORE WORDS AVAILABLE FOR YOU, PLEASE RESTART THE GAME ");
        alert.showAndWait();
        
        
    }
     
  
    //changes the backgroud color of the game and the guess button color
    private void colorChange(String colorBack , String colorBtn){
        paneGame.setStyle(colorBack);
        guessButton.setStyle(colorBtn);
    }
    

}










