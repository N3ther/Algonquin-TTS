package net.oijon.algonquin.gui;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.oijon.algonquin.tts.IPA;
import net.oijon.algonquin.tts.trm.TRM;


public class GUI extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		//pre-startup
		VBox mainBox = new VBox();
		mainBox.setAlignment(Pos.CENTER);
		mainBox.setSpacing(5);
		
		ObservableList<String> options = 
		    FXCollections.observableArrayList(
		        "Classic",
		        "TRM (highly experimental)"
		    );
		final ComboBox synthType = new ComboBox(options);
		synthType.setValue("Classic");
		
		HBox pronounceBox = new HBox();
		pronounceBox.setAlignment(Pos.CENTER);
		GridPane pronounceGrid = new GridPane();
		pronounceGrid.setAlignment(Pos.CENTER);
		pronounceGrid.setHgap(10);
		pronounceGrid.setVgap(10);
		pronounceGrid.setPadding(new Insets(25, 25, 25, 25));
		
		Label consoleLabel = new Label("Console");
		TextArea console = new TextArea();
		
		Label insertIPA = new Label("Insert IPA:");
		TextArea insert = new TextArea("hɛloʊ ænd wɛlkəm tu ælgonkwɪn tɛkst tu spitʃ");
		
		Label fileNameLabel = new Label("File Name");
		TextField fileNameField = new TextField("output");
		VBox fileName = new VBox(fileNameLabel, fileNameField);
		Label packLabel = new Label("Sound Pack");
		
		String[] packnames;
		String packList = "Packs:\n";
		File packsDirFile = new File(System.getProperty("user.home") + "/AlgonquinTTS/packs/");
		packnames = packsDirFile.list();
		
		for (String pathname : packnames) {
            packList += pathname + "\n";
        }
		
		TextArea packListTextArea = new TextArea(packList);
		TextField packField = new TextField("newclassic");
		Button refreshPacksButton = new Button("Refresh");
		refreshPacksButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				String[] packnames;
				String packList = "Packs:\n";
				File packsDirFile = new File(System.getProperty("user.home") + "/AlgonquinTTS/packs/");
				packnames = packsDirFile.list();
				
				for (String pathname : packnames) {
		            packList += pathname + "\n";
		        }
				
				packListTextArea.setText(packList);
			}
			
		});
		VBox packVBox = new VBox(packLabel, packListTextArea, packField, refreshPacksButton);
		
		Button pronounceButton = new Button("Pronounce!");
		pronounceButton.setDefaultButton(true);
		
		pronounceButton.setOnAction(new EventHandler<ActionEvent>() {

	        @Override
	        public void handle(ActionEvent event) {
	        	if (synthType.getValue().equals("Classic")) {
	        		String message = IPA.createAudio(IPA.getFileNames(insert.getText()), fileNameField.getText(), packField.getText());
	        		console.setText(message);
	        		System.out.println(message);
	        	} else if (synthType.getValue().equals("TRM (highly experimental)")) {
	        		
	        		//String consoleResult = "Generated from a value of 1: " + Float.toString(tube.generate(1)) + "\n";
	        		//consoleResult += "Generated from a value of 2: " + Float.toString(tube.generate(2)) + "\n";
	        		//consoleResult += "Generated from a value of 1.5: " + Float.toString(tube.generate((float) 1.5)) + "\n";
	        		//consoleResult += "Generated from a value of 10: " + Float.toString(tube.generate(10)) + "\n";
	        		//consoleResult += "Generated from a value of 30: " + Float.toString(tube.generate(30)) + "\n";
	        		String message = "This is currently under development and is highly experimental!\n";
	        		message += "However, below you will find debug information to help more easially develop this!\n";
	        		message += "-----BEGIN TESTTUBE-----\n";
	        		message += TRM.createTestTube() + "\n";
	        		message += "-----END TESTTUBE-----\n";
	        		message += "-----BEGIN RAW OUTPUT-----\n";
	        		message += "Now, a .wav file will be created of a pure vocal output. This should be just a sine wave.\n";
	        		try {
						message += TRM.createTestWave();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						message += e.toString() + "\n";
					}
	        		message += "Now it will play!\n";
	        		try {
	    			    Clip clip = AudioSystem.getClip();
	    			    AudioInputStream ais = AudioSystem.getAudioInputStream(
	    			    		new File(System.getProperty("user.home") + "/AlgonquinTTS/testwave.wav").getAbsoluteFile()
	    			    		);
	    			    clip.open(ais);
	    			    clip.start();
	    			    long fileLength = clip.getMicrosecondLength();
	    			    while(clip.getMicrosecondLength() != clip.getMicrosecondPosition())
	    			    {
	    			    }
	    			  }
	    			  catch (UnsupportedAudioFileException e) {
	    				message += "Unsupported audio format: '" + System.getProperty("user.home") + "/AlgonquinTTS/testwave.wav" + "' - " + e.toString() + "\n";
	    			  }
	    			  catch (LineUnavailableException e) {
	    				message += "Could not play '" + System.getProperty("user.home") + "/AlgonquinTTS/testwave.wav" + "' - " + e.toString() + "\n";
	    			  }
	    			  catch (IOException e) {
	    				message += "Could not play '" + System.getProperty("user.home") + "/AlgonquinTTS/testwave.wav" + "' - " + e.toString() + "\n";
	    				e.printStackTrace();
	    			  }
	        		message += "If all went to plan, and you did not see any exceptions, you just heard the beep that will form the voicebox's raw output!\n";
	        		message += "Of course, this won't be what the end product sounds like, the end product will have to go through several tubes.\n";
	        		message += "These tubes represent parts of the mouth and nasal cavity, which should be able to approximate the sound of speaking!\n";
	        		message += "-----END RAW OUTPUT-----\n";
	        		message += "-----BEGIN 17cm TUBE OUTPUT-----\n";
	        		message += "A 17cm tube should allow a sound similar to a schwa!\n";
	        		try {
						message += TRM.createTest17cmWave();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						message += e.toString() + "\n";
					}
	        		try {
	    			    Clip clip = AudioSystem.getClip();
	    			    AudioInputStream ais = AudioSystem.getAudioInputStream(
	    			    		new File(System.getProperty("user.home") + "/AlgonquinTTS/test17cm.wav").getAbsoluteFile()
	    			    		);
	    			    clip.open(ais);
	    			    clip.start();
	    			    long fileLength = clip.getMicrosecondLength();
	    			    while(clip.getMicrosecondLength() != clip.getMicrosecondPosition())
	    			    {
	    			    }
	    			  }
	    			  catch (UnsupportedAudioFileException e) {
	    				message += "Unsupported audio format: '" + System.getProperty("user.home") + "/AlgonquinTTS/test17cm.wav" + "' - " + e.toString() + "\n";
	    			  }
	    			  catch (LineUnavailableException e) {
	    				message += "Could not play '" + System.getProperty("user.home") + "/AlgonquinTTS/test17cm.wav" + "' - " + e.toString() + "\n";
	    			  }
	    			  catch (IOException e) {
	    				message += "Could not play '" + System.getProperty("user.home") + "/AlgonquinTTS/test17cm.wav" + "' - " + e.toString() + "\n";
	    				e.printStackTrace();
	    			  }
	        		message += "Did the sound that just played sound like a schwa? If not, something isn't working...\n";
	        		console.setText(message);
	        		System.out.print(message);
	        		
	        	} else {
	        		console.setText("Unsupported synthesis type \'" + synthType.getValue() + "\'.");
	        	}
	        }
	    });
		/**
		GridPane ipaChart = new GridPane();
		ipaChart.setAlignment(Pos.CENTER);
		ipaChart.setHgap(5);
		ipaChart.setVgap(5);
		ipaChart.setPadding(new Insets(15, 15, 15, 15));
		
		Label plosive = new Label("Plosive");
		ipaChart.add(plosive, 0, 1);
		Label nasal = new Label("Nasal");
		ipaChart.add(nasal, 0, 2);
		Label trill = new Label("Trill");
		ipaChart.add(trill, 0, 3);
		Label tapOrFlap = new Label("Tap or Flap");
		ipaChart.add(tapOrFlap, 0, 4);
		Label fricative = new Label("Fricative");
		ipaChart.add(fricative, 0, 5);
		Label lateralFricative = new Label("Lateral fricative");
		ipaChart.add(lateralFricative, 0, 6);
		Label approximant = new Label("Approximant");
		ipaChart.add(approximant, 0, 7);
		Label lateralApproximant = new Label("Lateral Approximant");
		ipaChart.add(lateralApproximant, 0, 8);
		
		Label bilabial = new Label("Bilabial");
		ipaChart.add(bilabial, 1, 0);
		Button pButton = new Button("p");
		Button bButton = new Button("b");
		HBox bilabialPlosives = new HBox(pButton, bButton);
		ipaChart.add(bilabialPlosives, 1, 1);
		Button m̥Button = new Button("m̥");
		Button mButton = new Button("m");
		HBox bilabialNasals = new HBox(m̥Button, mButton);
		ipaChart.add(bilabialNasals, 1, 2);
		Button ʙ̥Button = new Button("ʙ̥");
		Button ʙButton = new Button("ʙ");
		HBox bilabialTrills = new HBox(ʙ̥Button, ʙButton);
		
		ipaChart.add(bilabialTrills, 1, 3);
		Button ⱱ̥̟Button = new Button("ⱱ̥̟");
		Button ⱱ̟Button = new Button("ⱱ̟");
		HBox bilabialTaps = new HBox(ⱱ̥̟Button, ⱱ̟Button);
		ipaChart.add(bilabialTaps, 1, 4);
		Button ɸButton = new Button("ɸ");
		Button βButton = new Button("β");
		HBox bilabialFricatives = new HBox(ɸButton, βButton);
		ipaChart.add(bilabialFricatives, 1, 5);
		Button impossible1 = new Button(" ");
		impossible1.setDisable(true);
		Button impossible2 = new Button(" ");
		impossible2.setDisable(true);
		HBox bilabialLateralFricatives = new HBox(impossible1, impossible2);
		ipaChart.add(bilabialLateralFricatives, 1, 6);
		Button ʋ̟̥Button = new Button("ʋ̟̥");
		Button ʋ̟Button = new Button("ʋ̟");
		HBox bilabialApproximants = new HBox(ʋ̟̥Button, ʋ̟Button);
		ipaChart.add(bilabialApproximants, 1, 7);
		Button impossible3 = new Button(" ");
		impossible3.setDisable(true);
		Button impossible4 = new Button(" ");
		impossible4.setDisable(true);
		HBox bilabialLateralApproximants = new HBox(impossible3, impossible4);
		ipaChart.add(bilabialLateralApproximants, 1, 8);
		
		
		
		Label labiodental = new Label("Labiodental");
		ipaChart.add(labiodental, 2, 0);
		Button p̪Button = new Button("p̪");
		Button b̪Button = new Button("b̪");
		HBox labiodentalPlosives = new HBox(p̪Button, b̪Button);
		ipaChart.add(labiodentalPlosives, 2, 1);
		Button ɱ̥Button = new Button("ɱ̥");
		Button ɱButton = new Button("ɱ");
		HBox labiodentalNasals = new HBox(ɱ̥Button, ɱButton);
		ipaChart.add(labiodentalNasals, 2, 2);
		Button voicelessLabiodentalTrillButton = new Button("̥ʙ̪̥");
		Button voicedLabiodentalTrillButton = new Button("̥ʙ");
		HBox labiodentalTrills = new HBox(voicelessLabiodentalTrillButton, voicedLabiodentalTrillButton);
		ipaChart.add(labiodentalTrills, 2, 3);
		Button ⱱ̥Button = new Button("ⱱ̥");
		Button ⱱButton = new Button("ⱱ");
		HBox labiodentalTaps = new HBox(ⱱ̥Button, ⱱButton);
		ipaChart.add(labiodentalTaps, 2, 4);
		Button fButton = new Button("f");
		Button vButton = new Button("v");
		HBox labiodentalFricatives = new HBox(fButton, vButton);
		ipaChart.add(labiodentalFricatives, 2, 5);
		Button impossible5 = new Button(" ");
		impossible5.setDisable(true);
		Button impossible6 = new Button(" ");
		impossible6.setDisable(true);
		HBox labiodentalLateralFricatives = new HBox(impossible5, impossible6);
		ipaChart.add(labiodentalLateralFricatives, 2, 6);
		Button ʋ̥Button = new Button("ʋ̥");
		Button ʋButton = new Button("ʋ");
		HBox labiodentalApproximants = new HBox(ʋ̥Button, ʋButton);
		ipaChart.add(labiodentalApproximants, 2, 7);
		Button impossible7 = new Button(" ");
		impossible7.setDisable(true);
		Button impossible8 = new Button(" ");
		impossible8.setDisable(true);
		HBox labiodentalLateralApproximants = new HBox(impossible7, impossible8);
		ipaChart.add(labiodentalLateralApproximants, 2, 8);
		
		Label dental = new Label("Dental");
		ipaChart.add(dental, 3, 0);
		Button t̪Button = new Button("t̪");
		Button d̪Button = new Button("d̪");
		HBox dentalPlosives = new HBox(t̪Button, d̪Button);
		ipaChart.add(dentalPlosives, 3, 1);
		Button n̥̪Button = new Button("n̥̪");
		Button n̪Button = new Button("n̪");
		HBox dentalNasals = new HBox(n̥̪Button, n̪Button);
		ipaChart.add(dentalNasals, 3, 2);
		Button r̥̪Button = new Button("r̥̪");
		Button r̪Button = new Button("r̪");
		HBox dentalTrills = new HBox(r̥̪Button, r̪Button);
		ipaChart.add(dentalTrills, 3, 3);
		Button ɾ̥̪Button = new Button("ɾ̥̪");
		Button ɾ̪Button = new Button("ɾ̪");
		HBox dentalTaps = new HBox(ɾ̥̪Button, ɾ̪Button);
		ipaChart.add(dentalTaps, 3, 4);
		Button θButton = new Button("θ");
		Button ðButton = new Button("ð");
		HBox dentalFricatives = new HBox(θButton, ðButton);
		ipaChart.add(dentalFricatives, 3, 5);
		Button ɬ̪Button = new Button("ɬ̪");
		Button ɮ̪Button = new Button("ɮ̪");
		HBox dentalLateralFricatives = new HBox(ɬ̪Button, ɮ̪Button);
		ipaChart.add(dentalLateralFricatives, 3, 6);
		Button ɹ̥̪Button = new Button("ɹ̥̪");
		Button ɹ̪Button = new Button("ɹ̪");
		HBox dentalApproximants = new HBox(ɹ̥̪Button, ɹ̪Button);
		ipaChart.add(dentalApproximants, 3, 7);
		Button l̥̪Button = new Button("l̥̪");
		Button l̪Button = new Button("l̪");
		HBox lateralApproximants = new HBox(l̥̪Button, l̪Button);
		ipaChart.add(lateralApproximants, 3, 8);
		
		Label alveolar = new Label("Alveolar");
		ipaChart.add(alveolar, 4, 0);
		Label postalveolar = new Label("Postalveolar");
		ipaChart.add(postalveolar, 5, 0);
		Label retroflex = new Label("Retroflex");
		ipaChart.add(retroflex, 6, 0);
		Label palatal = new Label("Palatal");
		ipaChart.add(palatal, 7, 0);
		Label velar = new Label("Velar");
		ipaChart.add(velar, 8, 0);
		Label uvular = new Label("Uvular");
		ipaChart.add(uvular, 9, 0);
		Label pharyngeal = new Label("Pharyngeal");
		ipaChart.add(pharyngeal, 10, 0);
		Label glottal = new Label("Glottal");
		ipaChart.add(glottal, 11, 0);
		**/
		
		VBox leftSide = new VBox(insertIPA, insert, pronounceButton);
		VBox rightSide = new VBox(synthType, fileName, packVBox);
		
		pronounceGrid.add(leftSide, 0, 1);
		pronounceGrid.add(rightSide, 1, 1);
		
		pronounceBox.getChildren().addAll(pronounceGrid);
		
		
		
		mainBox.getChildren().addAll(pronounceBox, consoleLabel, console);
		
		Scene scene = new Scene(mainBox, 750, 500);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Algonquin Text-to-Speach");
		primaryStage.show();
		
		
	}
	
	public static void main(String[] args) {
        launch(args);
    }

}
