/**
 * Civil Society Application
 * Admin panel controller
 */

// FIXME move somewhere
civsApp.factory("UsersFactory", function($http) {
 	console.log("UsersFactory init");
 	var users = {};
 	users.config = {
 		apiUrl: "/core/s/users/"
 	};
 	users.getList = function(callback) {
 		$http.get(this.config.apiUrl + 'list').success(callback);
 	}
 	return users;
});

civsApp.controller('AdminCtrl', function ($scope, $route, $location, AuthFactory, UsersFactory) {
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
 			$scope.isError = false;
 			$scope.search = '';
 			$scope.userList = [];
 			// TODO do load users

 			UsersFactory.getList(function(json) {
 				$scope.isLoading = false;
 				if ( json.success ) {
 					$scope.userList  = json.items;
 				} else {
 					$scope.isError = true;
 				}
 			});
 		} else {
 			$scope.view = 'view/403.html';	
 		}
 	} else {
 		$scope.view = 'view/403.html';
 	}
 	
});
