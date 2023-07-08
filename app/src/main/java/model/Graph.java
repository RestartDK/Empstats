package model;

import java.awt.image.BufferedImage;    
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.security.AuthProvider;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.codec.binary.Base64;

import okhttp3.Request;

import com.azure.identity.AuthorizationCodeCredential;
import com.azure.identity.AuthorizationCodeCredentialBuilder;
import com.azure.identity.DeviceCodeCredential;
import com.azure.identity.DeviceCodeCredentialBuilder;
import com.azure.identity.DeviceCodeInfo;
import com.azure.identity.InteractiveBrowserCredential;
import com.azure.identity.InteractiveBrowserCredentialBuilder;
import com.azure.identity.UsernamePasswordCredential;
import com.azure.identity.UsernamePasswordCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.callrecords.models.CallRecord;
import com.microsoft.graph.callrecords.requests.SessionCollectionPage;
import com.microsoft.graph.logger.DefaultLogger;
import com.microsoft.graph.logger.LoggerLevel;
import com.microsoft.graph.models.Attendee;
import com.microsoft.graph.models.DateTimeTimeZone;
import com.microsoft.graph.models.EmailAddress;
import com.microsoft.graph.models.Event;
import com.microsoft.graph.models.ItemBody;
import com.microsoft.graph.models.MeetingParticipantInfo;
import com.microsoft.graph.models.MeetingParticipants;
import com.microsoft.graph.models.Message;
import com.microsoft.graph.models.OnlineMeeting;
import com.microsoft.graph.models.ProfilePhoto;
import com.microsoft.graph.models.User;
import com.microsoft.graph.models.AttendeeType;
import com.microsoft.graph.models.BodyType;
import com.microsoft.graph.options.HeaderOption;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.MessageCollectionPage;
import com.microsoft.graph.requests.MessageCollectionRequest;
import com.microsoft.graph.requests.OnlineMeetingCollectionPage;
import com.microsoft.graph.requests.ProfilePhotoRequest;
import com.microsoft.graph.requests.UserCollectionPage;
import com.microsoft.graph.requests.EventCollectionPage;
import com.microsoft.graph.requests.EventCollectionRequestBuilder;

public class Graph {

    private static GraphServiceClient<Request> graphClient = null;
    private static TokenCredentialAuthProvider authProvider = null;
    
    // Use username and password to authenticate user
    public static void initializeGraphAuth(String applicationId, List<String> scopes, String username, String password) {
    	// Create the auth provider		
    	final UsernamePasswordCredential usernamePasswordCredential = new UsernamePasswordCredentialBuilder()
    	        .clientId(applicationId)
    	        .username(username)
    	        .password(password)
    	        .build();
    	
    	authProvider = new TokenCredentialAuthProvider(scopes, usernamePasswordCredential);

        // Create default logger to only log errors
        DefaultLogger logger = new DefaultLogger();
        logger.setLoggingLevel(LoggerLevel.ERROR);

        // Build a Graph client
        graphClient = GraphServiceClient.builder()
            .authenticationProvider(authProvider)
            .logger(logger)
            .buildClient();
    }
    
    
    /*
     * API GET queries 
     */
    
    public static String getUserAccessToken()
    {
        try {
            URL meUrl = new URL("https://graph.microsoft.com/v1.0/me");
            return authProvider.getAuthorizationTokenAsync(meUrl).get();
        } catch(Exception ex) {
            return null;
        }
    }
    
    public static User getUser() {
        if (graphClient == null) throw new NullPointerException(
            "Graph client has not been initialized. Call initializeGraphAuth before calling this method");

        // GET /me to get authenticated user
        User me = graphClient
            .me()
            .buildRequest()
            .select("displayName,mailboxSettings,mail")
            .get();

        return me;
    }
    
    public static String[][] getUsersInfo() {
    	if (graphClient == null) throw new NullPointerException(
    			"Graph client has not been initialized. Call initializeGraphAuth before calling this method");
    	
    	// GET /users to get authenticated users
    	UserCollectionPage employees = graphClient
    			.users()
    			.buildRequest()
    			//.select("displayName,mail,id")
    			.get();
    	
    	// Little employees, so only paging one page (might change in future)
    	List<User> userpage = employees.getCurrentPage();
    	int usersize = userpage.size();
    	
    	String[][] tempusers = new String[usersize][5];
    	for (int i = 0; i < usersize; i++) {
    		User current = userpage.get(i);
    		
    		//tempusers[i][0] = current.givenName;
    		tempusers[i][0] = current.userPrincipalName;
    		tempusers[i][1] = current.mail;
    		tempusers[i][2] = current.id;
    		tempusers[i][3] = current.jobTitle;
    		tempusers[i][4] = current.displayName;
    	}
    	
    	return tempusers;
    }
    
    public static int[] getEmailRead(String userPrincipalName) {
    	if (graphClient == null) throw new NullPointerException(
    			"Graph client has not been initialized. Call initializeGraphAuth before calling this method");

    	// Try query, if 404 error then return null
    	try {
    		// GET /users/messages to get collection of messages from specified user
        	MessageCollectionPage messages = graphClient
        			.users(userPrincipalName)
        			.messages()
        			.buildRequest()
        			.get();
        	
        	int[] reademails = new int[7];
        	List<Message> messagelist = messages.getCurrentPage();
        	
        	// Iterating through each message
        	for (int i = 0; i < messagelist.size(); i++) {
        		Message current = messagelist.get(i);
        		
        		// Converting numerical date to a normal day of the weak
        		String date = current.receivedDateTime
        				.getDayOfWeek().toString();
        		
        		// If the email has been read on the x date add 1 to its index in reademails
        		if (current.isRead == true) 
        		{
        			Utils.chooseDay(date, reademails);
        		}
        	}
        	
        	return reademails;
    		
    	} catch (com.microsoft.graph.http.GraphServiceException messagesNotFound) {
    		System.out.print(messagesNotFound.getError());
    		return null;
    	}
  
    }
    
    public static int[] getEmailNotRead(String userPrincipalName) {
    	if (graphClient == null) throw new NullPointerException(
    			"Graph client has not been initialized. Call initializeGraphAuth before calling this method");
    	
    	// Try query, if 404 error then return null
    	try {
    		// GET /users/messages to get collection of messages from specified user
        	MessageCollectionPage messages = graphClient
        			.users(userPrincipalName)
        			.messages()
        			.buildRequest()
        			.get();
        	
        	int[] notreademails = new int[7];
        	List<Message> messagelist = messages.getCurrentPage();
        	
        	// Iterating through each message
        	for (int i = 0; i < messagelist.size(); i++) {
        		Message current = messagelist.get(i);
        		
        		// Converting numerical date to a normal day of the weak
        		String date = current.receivedDateTime
        				.getDayOfWeek().toString();
        		
        		// If the email has been read on the x date add 1 to its index in notemails
        		if (current.isRead == false) 
        		{
        			Utils.chooseDay(date, notreademails);
        		}
        	}
        	
        	return notreademails;
    		
    	} catch (com.microsoft.graph.http.GraphServiceException messagesNotFound) {
    		System.out.print(messagesNotFound.getError());
    		return null;
    	}
    }
    
    public static BufferedImage getPhoto(String userPrincipalName) { 
    	if (graphClient == null) throw new NullPointerException(
    			"Graph client has not been initialized. Call initializeGraphAuth before calling this method");
    	
    	// Try query, if 404 error then return null
    	try {
    		// GET /users/photo/$value getting the binary data of the image of chosen user
    		InputStream profilephoto = graphClient
        			.users(userPrincipalName)
        			.photo()
        			.content()
        			.buildRequest()
        			.get();
    		
    		BufferedImage photo = null;
        	
        	// Decoding binary data into base64 so a buffered image can be created
        	try {
    			String imageString = Base64.encodeBase64String(profilephoto.readAllBytes());
    			photo = Utils.decodeToImage(imageString);
    		} catch (IOException e) {
    			e.printStackTrace();
    		} 

        	System.out.println(photo);
        	return photo;
    	} catch (com.microsoft.graph.http.GraphServiceException imageNotFound) {
    		System.out.println(imageNotFound.getError());
    		return null;
    	} 
    	
    }
    
    public static int[] getPickedUpCalls(String userPrincipalName) {
    	if (graphClient == null) throw new NullPointerException(
    			"Graph client has not been initialized. Call initializeGraphAuth before calling this method");
    	
    	// Try query, if 404 error then return null
    	try {
    		OnlineMeetingCollectionPage onlinemeetings = graphClient
        			.users(userPrincipalName)
        			.onlineMeetings()
        			.buildRequest()
        			.get();
        	
        	int[] pickedupcalls = new int[7];
        	List <OnlineMeeting> meetinglist = onlinemeetings.getCurrentPage();
        	
        	// Iterating through each meeting 
        	for (int i = 0; i < meetinglist.size(); i++) {
        		 OnlineMeeting current = meetinglist.get(i);
        		 List<MeetingParticipantInfo> participants = current.participants.attendees;
        		 boolean presence = false;
        		 
        		 // If the current user is in the participant list, user did attend
        		 for (int j = 0; j < participants.size(); j++) {
        			 String attendee = participants.get(j).upn;
        			 
        			 if (userPrincipalName == attendee) {
        				 presence = true;
        			 }
        			 
        		 }
        		 
        		// Converting numerical date to a normal day of the weak
         		String date = current.startDateTime
         				.toString();
        		
        		// If the meeting was attended at x date add 1 to its index in pickedupcalls
        		if (presence == true) 
        		{
        			Utils.chooseDay(date, pickedupcalls);
        		}
        	}
        
        	return pickedupcalls;
    	} catch (com.microsoft.graph.http.GraphServiceException callnotFound) {
    		System.out.println(callnotFound.getError());
    		return null;
    	}
    }
    
    public static int[] getMissedCalls(String userPrincipalName) {
    	if (graphClient == null) throw new NullPointerException(
    			"Graph client has not been initialized. Call initializeGraphAuth before calling this method");
    	
    	// Try query, if 404 error then return null
    	try {
    		OnlineMeetingCollectionPage onlinemeetings = graphClient
        			.users(userPrincipalName)
        			.onlineMeetings()
        			.buildRequest()
        			.get();
        	
        	int[] missedcalls = new int[7];
        	List <OnlineMeeting> meetinglist = onlinemeetings.getCurrentPage();
        	
        	// Iterating through each meeting 
        	for (int i = 0; i < meetinglist.size(); i++) {
        		 OnlineMeeting current = meetinglist.get(i);
        		 List<MeetingParticipantInfo> participants = current.participants.attendees;
        		 boolean presence = false;
        		 
        		 // If the current user is not in the participant list, user did not attend
        		 for (int j = 0; j < participants.size(); j++) {
        			 String attendee = participants.get(j).upn;
        			 
        			 if (userPrincipalName == attendee) {
        				 presence = true;
        			 }
        			 
        		 }
        		 
        		// Converting numerical date to a normal day of the weak
         		String date = current.startDateTime
         				.toString();
        		
        		// If the meeting was not attended at x date add 1 to its index in missedcalls
        		if (presence == false) 
        		{
        			Utils.chooseDay(date, missedcalls);
        		}
        	}
        	
        	return missedcalls;
    		
    	} catch (com.microsoft.graph.http.GraphServiceException callnotFound) {
    		System.out.println();
    		return null;
    	}
    }
}
