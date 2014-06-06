/**
 * Civil Society Application
 * User panel controller
 */
civsApp.controller('UserCtrl', function ($scope, $route, $location, $http, AuthFactory, UsersFactory, AssistanceFactory, AgFactory) {
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
 			userCabinet.viewAssistances($scope, AssistanceFactory, AgFactory);
 			
 		} else {
 			$scope.title = "Розділ не готовий";
 			$scope.subTitle = "не дійшли руки";
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
			options: jsTools.emptyFields("firstName,middleName,lastName,phone,address")
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
			$scope.error = '';
			if ( state ) {
				$scope.options = {
					firstName: $scope.user.options.firstName,
					lastName: $scope.user.options.lastName,
					middleName: $scope.user.options.middleName,
					phone: $scope.user.options.phone,
					address: $scope.user.options.address
				}
				userCabinet.mode('editProfile', $scope);
			} else userCabinet.mode('viewProfile', $scope);
		};

		$scope.submitProfile = function() {
			
			// save on server
			UsersFactory.updateOptions($scope.options, function(json) {
				if ( json.success ) {
					$scope.editUserInfo(false);
					$scope.user.options = $scope.options;
				} else {
					$scope.error = json.messageText;
				}
			});
			
		};
	},
	/**
	 * Change password stuff
	 */
	changePassword: function($scope, AuthFactory) {
		$scope.changePassword = function(state) {
			$scope.error = '';
			$scope.pass = jsTools.emptyFields("old,new,newAgain");
			var modeName = ( state ) ? 'viewChangePass' : 'viewProfile';
			userCabinet.mode(modeName, $scope);
		};
		$scope.submitPassword = function(pass) {
			// validate
			$scope.error = '';
			if ( ! $scope.pass.old ) {
				$scope.error = 'Empty old password';
				return;
			}
			if ( $scope.pass.new != $scope.pass.newAgain ) {
				$scope.error = 'Password mismatch';
				return;	
			}
			// save
			AuthFactory.changePassword({newPass: $scope.pass.new, oldPass: $scope.pass.old}, function(json) {
				if ( json.success ) {
					$scope.changePassword(false);
				} else {
					$scope.error = json.messageText;
				}
			})
		};
	},
	/**
	 * Show list of user's assistances
	 */
	viewAssistances: function($scope, AssistanceFactory, AgFactory) {
		$scope.assistanceList = [];
		$scope.showAddAssistanceForm = false;
		$scope.assistanceGroups = [];
		AgFactory.getList(function(json) {
			if ( json.success ) {
				$scope.assistanceGroups = json.items;
			}
		});
		$scope.assistance = jsTools.emptyFields("description,groupId");
		AssistanceFactory.listByUser(function (json) {
			if ( json.success ) {
				$scope.assistanceList = json.items;
			}
		});
		$scope.addAssistance = function(state) {
			$scope.error = '';
			$scope.showAddAssistanceForm = state;

		};
		$scope.submitAssistance = function(assistance) {
			$scope.error = '';
			// validate
			if ( !assistance.groupId ) {
				$scope.error = "Будь ласка, вкажіть групу допомоги";
				return;
			}
			var p = {
				description: assistance.description,
				groupId: assistance.groupId
			};
			AssistanceFactory.create(p, function(json) {
				if ( json.success ) {
					$scope.assistanceList.unshift(json.item);
					$scope.addAssistance(false);

				} else $scope.error = json.messageText;
				//console.dir(json);
			});
			console.log('TODO submit assistance: ' + assistance.description + ', groupId=' + assistance.groupId);
		};
		$scope.removeAssistances = function(id) {
			if ( window.confirm("Ви впевнені, що бажаєте видалити своє оголошення про допомогу?") ) {
				AssistanceFactory.remove(id, function(json) {
					if ( json.success ) {
						for (var i=0; i<$scope.assistanceList.length; i++) {
							var currItem = $scope.assistanceList[i];
							if ( currItem.id == id ) {
								$scope.assistanceList.splice(i, 1);
								break;
							}
						}
					}
				});
			}
		};
	}
}