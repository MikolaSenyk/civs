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
 	users.blockUser = function(userId) {
 		$http.put(this.config.apiUrl + userId + '/block');
 	}
 	users.unblockUser = function(userId) {
 		$http.put(this.config.apiUrl + userId + '/unblock');
 	}
 	users.removeUser = function(userId) {
 		$http.delete(this.config.apiUrl + userId);
 	}

 	return users;
});

civsApp.controller('AdminCtrl', function ($scope, $route, $location, $http, AuthFactory, UsersFactory) {
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
 			$scope.userId2Index = {};
 			$scope.blockUser = function(userId) {
 				$scope.userList[$scope.userId2Index[userId]].enabled = false;
 				UsersFactory.blockUser(userId);
 			};
 			$scope.unblockUser = function(userId) {
 				$scope.userList[$scope.userId2Index[userId]].enabled = true;
 				UsersFactory.unblockUser(userId);
 			};
 			$scope.updateUserIndexes = function(items) {
 				var index = 0;
 				for (var i in items) {
 					$scope.userId2Index[items[i].id] = index++;
 				}
 			};
 			$scope.removeUser = function(userId) {
 				$scope.userList.splice($scope.userId2Index[userId], 1);
 				$scope.updateUserIndexes($scope.userList);
 				UsersFactory.removeUser(userId);
 			};

 			// load users
 			UsersFactory.getList(function(json) {
 				$scope.isLoading = false;
 				if ( json.success ) {
 					$scope.updateUserIndexes(json.items);
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
