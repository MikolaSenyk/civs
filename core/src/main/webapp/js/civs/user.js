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
 			userCabinet.createDefaults($scope, UsersFactory);
 			userCabinet.editProfile($scope, UsersFactory);
 			userCabinet.changePassword($scope, AuthFactory);

 			
 			
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

var userCabinet = {
	/**
	 * Set "mode" visible and clear others
	 */
	mode: function(mode, $scope) {
		for (var i in $scope.visibility) {
			$scope.visibility[i] = false;
		}
		$scope.visibility[mode] = true;
	},
	/**
	 * Main page of user cabinet
	 */
	createDefaults: function($scope, UsersFactory) {
		$scope.user = {
			login: '',
			createTime: '',
			options: {
				firstName: '',
				middleName: '',
				lastName: ''
			}
		};
		$scope.visibility = {};
		userCabinet.mode('viewProfile', $scope);

		// load user info
		UsersFactory.getInfo(function(json) {
			if ( json.success ) {
				$scope.user = json.info;
			}
		});
	},
	/**
	 * Build edit profile stuff
	 */
	editProfile: function($scope, UsersFactory) {
		$scope.editUserInfo = function(state) {
			if ( state ) {
				$scope.options = {
					firstName: $scope.user.options.firstName,
					lastName: $scope.user.options.lastName,
					middleName: $scope.user.options.middleName
				}
				userCabinet.mode('editProfile', $scope);
			} else userCabinet.mode('viewProfile', $scope);
		};

		$scope.submitProfile = function() {
			$scope.editUserInfo(false);
			$scope.user.options = $scope.options;
			// save on server
			UsersFactory.updateOptions($scope.options, function(json) {
				if ( !json.success ) window.alert("Ooops!");
			});
			
		};
	},
	/**
	 * Change password stuff
	 */
	changePassword: function($scope, AuthFactory) {

		$scope.changePassword = function(state) {
			var modeName = 'viewProfile';
			if ( state ) {
				modeName = 'viewChangePass';
				$scope.options = {
					oldPass: '', newPass: '', newPassAgain: ''
				}
			}
			userCabinet.mode(modeName, $scope);
		};
		$scope.submitPassword = function() {
			// TODO validate
			
			$scope.error = '';
			AuthFactory.changePassword({newPass: $scope.options.newPass, oldPass: $scope.options.oldPass}, function(json) {
				if ( json.success ) {
					$scope.changePassword(false);
				} else {
					$scope.error = json.messageText;
				}
			})
		};
	}
}