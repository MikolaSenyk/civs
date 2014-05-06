/**
 * Civil Society Application
 * Main controller for none logged users
 */

civsApp.controller('MainCtrl', function ($scope, $route, $http, $location, AuthFactory) {
	$scope.title = "СОЛІДАРНА ГРОМАДА";
 	$scope.subTitle = " - це спільна справа";
 	$scope.action = $route.current.params.action;
 	if ( ! $scope.action ) $scope.action = 'base';
 	$scope['isActive_'+$scope.action] = true;
 	
 	// add auth block
 	$scope.auth = AuthFactory.getAuthInfo('view/main/auth_panel.html');
 	if ( $scope.action == 'base' ) {
 		$scope.view = 'view/main/base.html';
 	} else if ( $scope.action == 'mision' ) {
 		$scope.view = 'view/main/mision.html';
 	} else if ( $scope.action == 'help' ) {
 		$scope.view = 'view/main/help.html';
 	} else if ( $scope.action == 'contacts' ) {
 		$scope.view = 'view/main/contacts.html';
 	} else {
		$scope.view = 'view/403.html';
	}
 	
 	
});
