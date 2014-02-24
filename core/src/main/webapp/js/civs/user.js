/**
 * Civil Society Application
 * User panel controller
 */


civsApp.controller('UserCtrl', function ($scope, $route, $location, $http, AuthFactory, UsersFactory) {
	$scope.title = "Неавторизований";
 	$scope.subTitle = "режим користувача";
 	$scope.action = $route.current.params.action;
 	

 	// add auth block
 	$scope.auth = AuthFactory.getAuthInfo('view/main/auth_panel.html');

 	if ( AuthFactory.isUser() ) {
 		$scope['isActive_'+$scope.action] = true;
 		
 		if ( $scope.action == "" ) {
 			// TODO Dashboard
 			
 		} else if ( $scope.action == "cabinet" ) {
 			// private cabinet
 			$scope.title = "Особистий кабінет";
 			$scope.view = 'view/user/cabinet.html';
 			$scope.user = {
 				login: $scope.auth.login,
 				regDate: "123"
 			};
 			

 			// TODO load user info
 			
 		} else if ( $scope.action == "assistances" ) {
 			$scope.title = "Мій внесок";

 		} else {
 			$scope.view = 'view/403.html';	
 		}
 	} else {
 		$scope.view = 'view/403.html';
 	}
 	
});
