/**
 * Civil Society Application
 * User panel controller
 */


civsApp.controller('UserCtrl', function ($scope, $route, $location, $http, AuthFactory, UsersFactory) {
	$scope.title = "Неавторизований";
 	$scope.subTitle = "режим користувача";
 	$scope.action = $route.current.params.action;
 	if ( $scope.action == '' ) $scope.action = "cabinet";
 	

 	// add auth block
 	$scope.auth = AuthFactory.getAuthInfo('view/main/auth_panel.html');

 	if ( AuthFactory.isUser() ) {
 		$scope['isActive_'+$scope.action] = true;
 		
 		if ( $scope.action == "cabinet" ) {
 			// private cabinet
 			$scope.title = "Особистий кабінет";
 			$scope.view = 'view/user/cabinet.html?v=3';
 			$scope.user = {
 				login: $scope.auth.login,
 				createTime: '',
 				firstName: '',
 				middleName: '',
 				lastName: ''
 			};
 			

 			// load user info
 			UsersFactory.getInfo(function(json) {
 				if ( json.success ) {
 					$scope.user = json.info;
 				}
 			});

 			$scope.editProfile = false;
 			$scope.editUserInfo = function(state) {
 				$scope.editProfile = state;
 				console.log('edit=' + state);
 			};

 			
 		} else if ( $scope.action == "assistances" ) {
 			$scope.title = "Мій внесок";

 		} else {
 			$scope.view = 'view/403.html';	
 		}
 	} else {
 		$scope.view = 'view/403.html';
 	}
 	
});
