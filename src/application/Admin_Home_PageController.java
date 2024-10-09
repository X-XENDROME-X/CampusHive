	package application;
	
	import java.io.IOException;
	import javafx.fxml.FXML;
	import javafx.fxml.FXMLLoader;
	import javafx.scene.Node;
	import javafx.scene.Parent;
	import javafx.scene.Scene;
	import javafx.scene.control.Button;
	import javafx.stage.Stage;
	import javafx.event.ActionEvent;
	
	public class Admin_Home_PageController {
	
	    @FXML
	    private Button deleteUser;
	
	    @FXML
	    private Button editRoles;
	
	    @FXML
	    private Button inviteUser;
	
	    @FXML
	    private Button listUsers;
	
	    @FXML
	    private Button logOut;
	
	    @FXML
	    private Button resetUser;
	
	    // Add this method to handle the logout button action
	    @FXML
	    private void handleLogoutAction(ActionEvent event) {
	        try {
	            // Load the Create Account FXML file
	            Parent createAccountPage = FXMLLoader.load(getClass().getResource("Create Account Page.fxml"));
	            
	            // Set up the new scene
	            Scene createAccountScene = new Scene(createAccountPage);
	            
	            // Get the current stage from the event source
	            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	            
	            // Set the new scene on the stage
	            currentStage.setScene(createAccountScene);
	            currentStage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    @FXML
	    private void handleResetUserAction(ActionEvent event) {
	        try {
	            // Load the Reset User Account FXML file
	            Parent resetUserAccountPage = FXMLLoader.load(getClass().getResource("ResetPassOTP.fxml"));
	            
	            // Set up the new scene
	            Scene resetUserAccountScene = new Scene(resetUserAccountPage);
	            
	            // Get the current stage from the event source
	            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	            
	            // Set the new scene on the stage
	            currentStage.setScene(resetUserAccountScene);
	            currentStage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    @FXML
	    private void handleInviteUserAction(ActionEvent event) {
	        try {
	            // Load the Invite User FXML file
	            Parent inviteUserPage = FXMLLoader.load(getClass().getResource("Invite_User_Page.fxml"));
	            
	            // Set up the new scene
	            Scene inviteUserScene = new Scene(inviteUserPage);
	            
	            // Get the current stage from the event source
	            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	            
	            // Set the new scene on the stage
	            currentStage.setScene(inviteUserScene);
	            currentStage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    @FXML
	    private void handleListUsersAction(ActionEvent event) {
	        try {
	            // Load the User List FXML file
	            Parent userListPage = FXMLLoader.load(getClass().getResource("ListofUsers.fxml"));
	            
	            // Set up the new scene
	            Scene userListScene = new Scene(userListPage);
	            
	            // Get the current stage from the event source
	            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	            
	            // Set the new scene on the stage
	            currentStage.setScene(userListScene);
	            currentStage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    @FXML
	    private void handleDeleteUserAction(ActionEvent event) {
	        try {
	            // Load the Delete User FXML file
	            Parent deleteUserPage = FXMLLoader.load(getClass().getResource("Delete User Account.fxml"));
	            
	            // Set up the new scene
	            Scene deleteUserScene = new Scene(deleteUserPage);
	            
	            // Get the current stage from the event source
	            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	            
	            // Set the new scene on the stage
	            currentStage.setScene(deleteUserScene);
	            currentStage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    @FXML
	    private void handleEditRoleAction(ActionEvent event) {
	        try {
	            // Load the Delete User FXML file
	            Parent editRolePage = FXMLLoader.load(getClass().getResource("Add_Remove_UserRole.fxml"));
	            
	            // Set up the new scene
	            Scene editRoleScene = new Scene(editRolePage);
	            
	            // Get the current stage from the event source
	            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	            
	            // Set the new scene on the stage
	            currentStage.setScene(editRoleScene);
	            currentStage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
