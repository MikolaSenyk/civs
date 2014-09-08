/**
 * Civil Society Application
 * Admin panel controller
 */

// FIXME move somewhere

civsApp.controller('AdminCtrl', function ($scope, $route, $location, $http, AuthFactory, UsersFactory, AgFactory, AssistanceFactory, LetterFactory) {
	$scope.title = "Адмін панель";
 	$scope.subTitle = "режим адміністратора";
 	$scope.action = $route.current.params.action || 'dashboard';
 	$scope.id = $route.current.params.id;

 	// add auth block
 	$scope.auth = AuthFactory.getAuthInfo('view/main/auth_panel.html');

 	if ( AuthFactory.isAdmin() ) {
 		console.log('action=' + $scope.action + ', id=' + $scope.id);
 		$scope['isActive_'+$scope.action] = true;

 		if ( $scope.action == "dashboard" ) {
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
 			};
 			$scope.changeRegCode = function() {
		 		$http.put(AuthFactory.apiUrl+"changeRegCode").success(function (json) {
					if ( json.success ) {
						$scope.regCode = json.regCode;
					} else {
						$scope.regCode = "Упс! " + json.messageText;
					}
				});
 			};
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
 			$scope.parentGroup = {};
 			$scope.hasParentGroup = false;
 			$scope.inAddForm = false;
 			$scope.priceDetails = {};
 			$scope.error = '';
 			//$scope.parentGroupId = $scope.id || -1; // default as null but numeric
 			// load groups
 			AgFactory.getList($scope.id || 0, function (json) {
 				if ( json.success ) {
 					$scope.groupList = json.items;
 					$scope.hasParentGroup = json.hasParent;
 					$scope.parentGroup = json.parent;
 				}
 			});
 			$scope.showAddGroup = function() {
 				$scope.currGroup = {
 					name: ''
 				};
 				$scope.inAddForm = true;
 				$scope.error = '';
 			};
 			$scope.hideAddGroup = function() {
 				$scope.inAddForm = false;
 				$scope.error = '';
 			};
 			$scope.createGroup = function() {
 				$scope.error = '';	
 				AgFactory.createGroup($scope.currGroup.name, $scope.id || 0, function (json) {
 					if ( json.success ) {
 						$scope.groupList.push(json);
 						$scope.inAddForm = false;
 					} else {
 						$scope.error = json.messageText;
 					}
 				});	
 			};
 			$scope.removeGroup = function(index) {
 				$scope.error = '';
 				AgFactory.removeGroup($scope.groupList[index].id, function (json) {
 					if ( json.success ) {
 						$scope.groupList.splice(index, 1);
 					} else {
 						$scope.error = json.messageText;
 					}
 				});
 			};
 			$scope.showGroupPrices = function(index) {
 				$scope.priceDetails.group = $scope.groupList[index];
 				$scope.priceDetails.show = true;
 				$scope.priceDetails.showAddPrice = function() {
 					$scope.priceDetails.inAddForm = true;
 					
 				};
 				$scope.priceDetails.hideAddPrice = function() {
 					$scope.priceDetails.inAddForm = false;
 				};
 			};
 		} else if ( $scope.action == "assistances" ) {
 			// assistances

 			var filterNames = { new: 'Нові', all: 'Всі' };
 			$scope.view = 'view/admin/assistances.html';
 			$scope.list = [];

 			$scope.filter = {
 				regime: null,
 				getName: function() {
 					return filterNames[$scope.filter.regime];
 				},
 				load: function(answer) {
 					if ( answer.success ) {
 						$scope.list = answer.items;
 					}
 				},
 				change: function(regime) {
 					$scope.filter.regime = regime;
 					// TODO load new list
 					if ( regime == 'all' ) AssistanceFactory.listAll($scope.filter.load);
 					else AssistanceFactory.listNew($scope.filter.load);
 				}
 			};
 			$scope.filter.change('new');

 			$scope.setApproved = function(id, state) {
 				if ( state ) {
 					// TODO set approved
 					AssistanceFactory.setApproved(id, function(answer) {
	 					if ( answer.success ) {
		 					for (var i in $scope.list) {
		 						var item = $scope.list[i];
		 						if ( item.id == id ) {
		 							$scope.list.splice(i, 1);
		 						}
		 					}
	 					}
 					});
 				}
 			};
 		} else if ( $scope.action == "userMessages" ) {
 			// messages from user
 			$scope.newMessages = [];
 			if ( $scope.newMessages.length == 0 ) {
 				LetterFactory.listNew(function(json) {
 					if ( json.success ) {
 						$scope.newMessages = json.items;
 					}
 				});
 			}
 			$scope.view = 'view/admin/userMessages.html';

 			$scope.readLetter = function(index) {
 				LetterFactory.markAsRead($scope.newMessages[index].id, function(json) {
 					if ( json.success ) {
 						$scope.newMessages[index].read = true;
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
