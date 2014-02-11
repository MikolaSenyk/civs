/**
 * Civil Society Application
 * Admin panel controller
 */

civsApp.controller('AdminCtrl', function ($scope, $route, $http, $location, AuthFactory) {
	$scope.title = "Адмін панель";
 	$scope.subTitle = "режим адміністратора";
 	$scope.action = $route.current.params.action;
 	

 	// add auth block
 	$scope.auth = AuthFactory.getAuthInfo('view/main/auth_panel.html');

 	if ( AuthFactory.isAdmin() ) {
 		$scope['isActive_'+$scope.action] = true;
 		
 		if ( $scope.action == "" ) {
 			// Dashboard
 			$scope.view = 'view/admin/dashboard.html';
 			$scope.regCode = null;
 			$scope.showRegCode = function() {
		 		$http.get(AuthFactory.apiUrl+"getRegCode").success(function (json) {
					if ( json.success ) {
						$scope.regCode = json.regCode;
					} else {
						$scope.regCode = "Упс! " + json.messageText;
					}
				});
 			}
 		} else if ( $scope.action == "users" ) {
 			// users
 			$scope.view = 'view/admin/users.html';
 			$scope.isLoading = true;
 			$scope.userList = [];
 			// TODO do load users
 			//$scope.isLoading = false;


 		} else {
 			$scope.view = 'view/403.html';	
 		}
 	} else {
 		$scope.view = 'view/403.html';
 	}
 	
});
