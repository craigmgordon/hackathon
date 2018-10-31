package com.example.hackathon;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;

import com.google.protobuf.ByteString;

@RestController
public class Speech2Text {

	@GetMapping("/speech2text")
	public String speech2Text() {
		System.out.println("In Speech to text.....");
		String text = "";

		try (SpeechClient speechClient = SpeechClient.create()) {

			// The path to the audio file to transcribe
			//String fileName = "/Users/craig/Documents/output.mp3";
			//String fileName = "/Users/craig/Documents/026NTWY_U8_CL.mp3";
			//String fileName = "/Users/craig/Documents/anotheraudiotest.mp3";
			//String fileName = "/Users/craig/Documents/Recording 2.flac";
			String fileName = "audio/output2.flac";
			

			// Reads the audio file into memory
			Path path = Paths.get(getClass().getClassLoader()
                    .getResource(fileName)
                    .toURI());
			byte[] data = Files.readAllBytes(path);
			ByteString audioBytes = ByteString.copyFrom(data);

			// Builds the sync recognize request
			RecognitionConfig config = RecognitionConfig.newBuilder().setEncoding(AudioEncoding.FLAC)
					.setSampleRateHertz(48000).setLanguageCode("en-US").setLanguageCode("yue-Hant-HK").build();
			RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();

			// Performs speech recognition on the audio file
			RecognizeResponse response = speechClient.recognize(config, audio);
			List<SpeechRecognitionResult> results = response.getResultsList();

			for (SpeechRecognitionResult result : results) {
				// There can be several alternative transcripts for a given chunk of speech.
				// Just use the
				// first (most likely) one here.
				SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
				text = alternative.getTranscript();
				System.out.printf("Transcription: %s%n", text);
			}
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		System.out.println("Exiting.....");
		return text;
	}
}
