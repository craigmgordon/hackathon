package com.example.hackathon;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.texttospeech.v1beta1.AudioConfig;
import com.google.cloud.texttospeech.v1beta1.AudioEncoding;
import com.google.cloud.texttospeech.v1beta1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1beta1.SynthesisInput;
import com.google.cloud.texttospeech.v1beta1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1beta1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1beta1.VoiceSelectionParams;
import com.google.protobuf.ByteString;

@RestController
public class Text2Speech {

	@GetMapping("text2speech")
	public void text2Speech() {
		String text = "Hello World! How are you doing today? This is Google Cloud Text-to-Speech Demo!";
        //String outputAudioFilePath = "/Users/craig/Documents/output.mp3";
		String outputAudioFilePath = getClass().getClassLoader().getResource("audio/output.mp3").getFile();
 
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            // Set the text input to be synthesized
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();
 
            // Build the voice request; languageCode = "en_us"
            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder().setLanguageCode("en-US")
                    .setSsmlGender(SsmlVoiceGender.FEMALE)
                    .build();
 
            // Select the type of audio file you want returned
            AudioConfig audioConfig = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.LINEAR16) // MP3 audio.
                    .build();
 
            // Perform the text-to-speech request
            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
 
            // Get the audio contents from the response
            ByteString audioContents = response.getAudioContent();
 
            // Write the response to the output file.
            try (OutputStream out = new FileOutputStream(outputAudioFilePath)) {
                out.write(audioContents.toByteArray());
                System.out.println("Audio content written to file \"output.wav\"");
            }
        }
        catch (Exception ex){
        	System.out.println(ex.getMessage());
        }
	}
	
}
