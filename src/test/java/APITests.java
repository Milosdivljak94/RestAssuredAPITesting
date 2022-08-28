import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.testng.Assert;

public class APITests {
	
	Scanner myObj = new Scanner(System.in); 
	String authorization = "Bearer ff24dd069563db8d69994c9f4aa872e8d13054fb137577f68faff23e766d170c";
	
	//Positive test cases START
	@Test
void createNewUser() {
 
	String id;
	 

//requires tester to enter name and email for user body
	 
	 System.out.println("Enter the name for new user:");
	 String name = myObj.nextLine();
	 System.out.println("\r\nEnter the email for new user:");
	 String email = myObj.nextLine();	 
     String body = "{\"name\":\""+ name +"\",\"gender\":\"male\",\"email\":\""+ email +"\",\"status\":\"active\"}";	
     
     
//creates new user using POST method
     
	 RequestSpecification Request = RestAssured.given().header("Authorization", authorization)
			                                           .header("Content-Type", "application/json").body(body);
	 Response response = Request.request(Method.POST, "https://gorest.co.in/public/v2/users");
	 
	 String serverResponse = response.getBody().asString();
	 System.out.println("\r\nServer response content is:" + serverResponse);
	 
	 int statusCode = response.getStatusCode(); 
	 System.out.println("\r\nServer response Code is:" + statusCode);
	 Assert.assertEquals(statusCode, 201);
	 
	 
//extracts users ID from server response	
	 
	 String[] list = serverResponse.split("\"");
	 id = list[2].substring(1,list[2].length()-1);
	 System.out.println("\r\nUser is created with ID :" + id);

		 
	}
	
	@Test
void updateUser() {
		
//requires tester to enter valid user ID, name and email	
		
	 System.out.println("\r\nEnter id of the user you wish to update:\r\n");
	 String id = myObj.nextLine();
	 System.out.println("\r\nEnter the new name for user:");
	 String name = myObj.nextLine();
	 System.out.println("\r\nEnter the new email for user:");
	 String email = myObj.nextLine();
		
     String body = "{\"name\":\""+ name +"\",\"email\":\""+ email +"\",\"status\":\"active\"}";
     
//updates user details using PUT method     
   
	 RequestSpecification Request = RestAssured.given().header("Authorization", authorization)
			                                           .header("Content-Type", "application/json").body(body);
			                                           
	 Response response = Request.request(Method.PUT, "https://gorest.co.in/public/v2/users/" + id );
	 
	 System.out.println("\r\nUpdated user results:\r\n");
	 String serverResponse = response.getBody().asString();
	 System.out.println("\r\nServer response content is:" + serverResponse);

//verifies the correct status code is displayed	 
	 
	 int statusCode = response.getStatusCode(); 
	 System.out.println("\r\nServer response Code is:" + statusCode);
	 Assert.assertEquals(statusCode, 200);
		 
	 
	}
	
	@Test
void deleteUser() {

//requires tester to enter valid user ID		
		
	 Scanner myObj = new Scanner(System.in); 
	 System.out.println("\r\nEnter id of the user you wish to delete:");

	 String id = myObj.nextLine(); 
	 
//deletes user using DELETE method
   
	 RequestSpecification Request = RestAssured.given().header("Authorization", authorization);	                                         
	 Response response = Request.request(Method.DELETE, "https://gorest.co.in/public/v2/users/" + id );
	 
	 System.out.println("\r\nResults:\r\n");
	 String serverResponse = response.getBody().asString();
	 System.out.println("Server response content is:" + serverResponse);
	 
//verifies the correct status code is displayed	 
	 
	 int statusCode = response.getStatusCode(); 
	 System.out.println("\r\nServer response Code is:" + statusCode);
	 Assert.assertEquals(statusCode, 204);
	 System.out.println("\r\nUser with id: " + id + " is deleted");

	 //Positive test cases END
	 
	}
     //Negative test cases START
	@Test
void createNewUserWithInvalidAuth() {
		
	 String body = "{\"name\":\"zoki\",\"gender\":\"male\",\"email\":\"zole9989@gmail.com\",\"status\":\"active\"}";	 
		   
	 RequestSpecification Request = RestAssured.given().header("Authorization", "/")
				              			.header("Content-Type", "application/json").body(body);
	 Response response = Request.request(Method.POST, "https://gorest.co.in/public/v2/users");
		 
	 String serverResponse = response.getBody().asString();
	 System.out.println("\r\nServer response content is:" + serverResponse);

//verifies the new user creation has failed
	 
	 int statusCode = response.getStatusCode(); 
	 System.out.println("\r\nServer response Code is:" + statusCode);
	 Assert.assertEquals(statusCode, 401);
		
	}
	
	@Test 
void updateUserWithInvalidId() {

//returns current time in milliseconds so the email parameter is never the same	value

	 long random = System.currentTimeMillis();

	 String body = "{\"name\":\"zoranko\",\"gender\":\"male\",\"email\":\""+ random +"@gmail.com\",\"status\":\"active\"}";
	 String body1 = "{\"name\":\"zoran1\",\"gender\":\"male\",\"email\":\"zole919@gmail.com\",\"status\":\"active\"}";
	 
//creates a new user	 
	 
	 RequestSpecification Request = RestAssured.given().header("Authorization", authorization)
                 .header("Content-Type", "application/json").body(body);
	 Response response = Request.request(Method.POST, "https://gorest.co.in/public/v2/users");

//attempts to update user details	 
	 
	 Request = RestAssured.given().header("Authorization", authorization)
				                                           .header("Content-Type", "application/json").body(body1);
	 response = Request.request(Method.PUT, "https://gorest.co.in/public/v2/users/"+random);
		 
	 String serverResponse = response.getBody().asString();
	 System.out.println("\r\nServer response content is:" + serverResponse);

//verifies the user update has failed due to invalid ID	 
	 
	 int statusCode = response.getStatusCode(); 
	 System.out.println("\r\nServer response Code is:" + statusCode);
	 Assert.assertEquals(statusCode, 404);
	
 	}
	
	@Test
void deletingSameUserTwice() {
		
	 String id;		
	 String body = "{\"name\":\"zorance\",\"gender\":\"male\",\"email\":\"zole9329@gmail.com\",\"status\":\"active\"}";

//creates a new user
	 
	 RequestSpecification Request = RestAssured.given().header("Authorization", authorization)
                 .header("Content-Type", "application/json").body(body);
	 Response response = Request.request(Method.POST, "https://gorest.co.in/public/v2/users");

//grabs users ID
	 
	 String serverResponse = response.getBody().asString();
	 String[] list = serverResponse.split("\"");
	 id = list[2].substring(1,  list[2].length()-1);
	 
//deletes user	 
		   
	 Request = RestAssured.given().header("Authorization", authorization);				                                          
	 response = Request.request(Method.DELETE, "https://gorest.co.in/public/v2/users/" + id );
	 
//attempts to delete the same user again	 
		 
	 Request = RestAssured.given().header("Authorization", authorization);				                                          
	 response = Request.request(Method.DELETE, "https://gorest.co.in/public/v2/users/" + id );
	 
//verifies the correct status code is displayed	 
		 		 
	 int statusCode = response.getStatusCode(); 
	 System.out.println("\r\nServer response Code is:" + statusCode);
	 Assert.assertEquals(statusCode, 404);
		
	}
	
	@Test
	
void createUserWithoutBody() {
		
//attempts to create a user without a given body		
		   
	 RequestSpecification Request = RestAssured.given().header("Authorization", authorization)
				                                           .header("Content-Type", "application/json");
	 Response response = Request.request(Method.POST, "https://gorest.co.in/public/v2/users");
		 
	 String serverResponse = response.getBody().asString();
	 System.out.println("\r\nServer response content is:" + serverResponse);
	 
//verifies the correct status code is displayed
	 
	 int statusCode = response.getStatusCode(); 
	 System.out.println("\r\nServer response Code is:" + statusCode);
	 Assert.assertEquals(statusCode, 422);
		
	}
	

}
