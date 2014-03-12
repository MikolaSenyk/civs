/**
 * Civil Society Application
 * User panel controller
 */


civsApp.controller('UserCtrl', function ($scope, $route, $location, $http, AuthFactory, UsersFactory, AssistanceFactory) {
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
 			$scope.view = 'view/user/cabinet.html';
 			$scope.user = {
 				login: '',
 				createTime: '',
 				options: {
 					firstName: '',
 					middleName: '',
 					lastName: ''
 				}
 			};
 			$scope.visibility = {
 				viewProfile: true,
 				editProfile: false,
 				viewChangePass: false
 			};

 			// load user info
 			UsersFactory.getInfo(function(json) {
 				if ( json.success ) {
 					$scope.user = json.info;
 				}
 			});

 			$scope.editProfile = false;
 			$scope.editUserInfo = function(state) {
 				if ( state ) {
 					$scope.options = {
 						firstName: $scope.user.options.firstName,
 						lastName: $scope.user.options.lastName,
 						middleName: $scope.user.options.middleName
 					}
 				}
 				$scope.visibility.editProfile = state;
 				$scope.visibility.viewProfile = !state;
 				console.log('edit=' + state);
 			};

 			$scope.submitProfile = function() {
 				$scope.editUserInfo(false);
 				$scope.user.options = $scope.options;
 				// save on server
 				UsersFactory.updateOptions($scope.options, function(json) {
 					if ( !json.success ) window.alert("Ooops!");
 				});
 				
 			};

 			$scope.changePass = false;
 			$scope.changePassword = function() {
 				$scope.visibility.viewProfile = false;
 				$scope.visibility.viewChangePass = true;
 			};

 			
 		} else if ( $scope.action == "assistances" ) {
 			$scope.title = "Мій внесок";
 			$scope.view = 'view/user/assistances.html';
 			$scope.assistanceList = [];
 			AssistanceFactory.listByUser(function (json) {
 				if ( json.success ) {
 					$scope.assistanceList = json.items;
 				}
 			});


 		} else {
 			$scope.view = 'view/403.html';	
 		}
 	} else {
 		$scope.view = 'view/403.html';
 	}
 	
});
