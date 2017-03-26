package com.zensar.zenlabs.zeva.voiceid.mscs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.microsoft.cognitive.speakerrecognition.SpeakerIdentificationClient;
import com.microsoft.cognitive.speakerrecognition.SpeakerIdentificationRestClient;
import com.microsoft.cognitive.speakerrecognition.contract.CreateProfileException;
import com.microsoft.cognitive.speakerrecognition.contract.EnrollmentException;
import com.microsoft.cognitive.speakerrecognition.contract.GetProfileException;
import com.microsoft.cognitive.speakerrecognition.contract.identification.IdentificationException;
import com.microsoft.cognitive.speakerrecognition.contract.identification.IdentificationOperation;
import com.microsoft.cognitive.speakerrecognition.contract.identification.OperationLocation;
import com.microsoft.cognitive.speakerrecognition.contract.identification.Status;

public class SpeakerIdentificationService {
	private OperationLocation lastOperationLocation;
	private IdentificationOperation lastIdentificationOperation;
	private SpeakerIdentificationClient client;
	public SpeakerIdentificationService(final String subscriptionKey){
		this.client =new SpeakerIdentificationRestClient(subscriptionKey);;
	}
	
	
	public  void enroll(UUID profileID,String filePath) throws EnrollmentException, IOException {

		client.enroll(new FileInputStream(new File(filePath)) , profileID);
	}
	
	public  String checkProfileStatus(UUID profileID) throws GetProfileException, IOException{
		return client.getProfile(profileID).enrollmentStatus.name() ;
		
		
	}
	public  UUID CreateProfile() throws CreateProfileException, IOException{
		return this.client.createProfile("en-us").identificationProfileId;
		
	}
	
	public  void submitIdentify(String filePath,List<UUID> searchIDs) throws FileNotFoundException, IdentificationException, IOException {
		lastOperationLocation= this.client.identify(new FileInputStream(new File(filePath)), searchIDs, true);
		
	}
	public boolean  verifyIdentiy() throws IdentificationException, IOException  {
		 lastIdentificationOperation= this.client.checkIdentificationStatus(lastOperationLocation);
		 System.out.println(lastIdentificationOperation.status);
		 return lastIdentificationOperation.status== Status.SUCCEEDED;
	}
	public UUID getLastId(){
		return lastIdentificationOperation.processingResult.identifiedProfileId;
	}
	
}
