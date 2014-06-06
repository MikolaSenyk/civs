/**
 * Civil Society Application
 * Main controller for none logged users
 */

civsApp.controller('MainCtrl', function ($scope, $route, $http, $location, AuthFactory, AssistanceFactory) {
	$scope.title = "Громадська організація \"Солідарна громада\"";
 	$scope.subTitle = "";
 	$scope.action = $route.current.params.action;
 	if ( ! $scope.action ) $scope.action = 'base';
 	$scope['isActive_'+$scope.action] = true;
 	
 	// add auth block
 	$scope.auth = AuthFactory.getAuthInfo('view/main/auth_panel.html');
 	if ( $scope.action == 'base' ) {
 		$scope.view = 'view/main/base.html';
 		$scope.lastAssistances = [];
 		AssistanceFactory.listLast(function(json) {
 			if ( json.success ) {
 				$scope.lastAssistances = json.items;
 			}
 		});
 	} else if ( $scope.action == 'mission' ) {
 		$scope.view = 'view/main/mission.html';
 	} else if ( $scope.action == 'activity' ) {
 		$scope.view = 'view/main/activity.html';
 	} else if ( $scope.action == 'projects' ) {
 		$scope.view = 'view/main/projects.html';
 	} else if ( $scope.action == 'news' ) {
 		$scope.view = 'view/main/news.html';
 	} else if ( $scope.action == 'contacts' ) {
 		$scope.view = 'view/main/contacts.html';
 	} else {
		$scope.view = 'view/403.html';
	}
 	
 	
});
