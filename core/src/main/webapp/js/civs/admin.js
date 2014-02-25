/**
 * Civil Society Application
 * Admin panel controller
 */

// FIXME move somewhere

civsApp.controller('AdminCtrl', function ($scope, $route, $location, $http, AuthFactory, UsersFactory, AgFactory) {
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
 		} else if ( $scope.action == "groups" ) {
 			// assistance groups
 			$scope.view = 'view/admin/groups.html';
 			$scope.groupList = [];
 			$scope.inAddForm = false;
 			// load groups
 			AgFactory.getList(function (json) {
 				if ( json.success ) {
 					$scope.groupList = json.items;
 				}
 			});
 			$scope.showAddGroup = function() {
 				$scope.currGroup = {
 					name: ''
 				};
 				$scope.inAddForm = true;
 			};
 			$scope.hideAddGroup = function() {
 				$scope.inAddForm = false;
 			};
 			$scope.createGroup = function() {
 				$scope.inAddForm = false;
 				AgFactory.createGroup($scope.currGroup.name, function (json) {
 					if ( json.success ) {
 						$scope.groupList.push(json);
 					}
 				});	
 			};
 			$scope.removeGroup = function(index) {
 				AgFactory.removeGroup($scope.groupList[index].id, function (json) {
 					if ( json.success ) {
 						$scope.groupList.splice(index, 1);
 					}
 				});
 			};
 		} else {
 			$scope.view = 'view/403.html';	
 		}
 	} else {
 		$scope.view = 'view/403.html';
 	}
 	
});
