/**
 * Registration form controller
 */

"use strict";

civsApp.controller('RegistrationCtrl', function($scope) {
	$scope.title = "Registration Form";
	// fields
	$scope.fields = {
		code: "",
		login: "",
		passwd: "",
		passwdCheck: ""
	}
	
	$scope.submit = function() {
		console.log('form was submitted');
	}

 	return $scope;
});
