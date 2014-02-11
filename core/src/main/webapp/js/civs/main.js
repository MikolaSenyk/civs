/**
 * Civil Society Application
 * Main controller for none logged users
 */

civsApp.controller('MainCtrl', function ($scope, $http, $location, AuthFactory) {
	$scope.title = "Назва сайту";
 	$scope.subTitle = "допомога - це спільна справа";
 	
 	// add auth block
 	$scope.auth = AuthFactory.getAuthInfo('view/main/auth_panel.html');
 	
 	
});
