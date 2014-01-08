/**
 * Registration form controller
 */

"use strict";

function addField(fields, name, value) {
	return fields.name = { "name": name, "value": value, "isError": false };
}

civsApp.controller('RegistrationCtrl', function($scope) {
	$scope.title = "Registration Form";
	// fields
	$scope.fields = {};
	addField($scope.fields, "code", "");
	addField($scope.fields, "login", "");
	addField($scope.fields, "passwd", "");
	addField($scope.fields, "passwdCheck", "");
	
	
	$scope.submit = function() {
		if ( regForm.code.$error ) {
			console.log('code error');
			$scope.fields.code.isError = true;
		}
		console.log('form was submitted');
	}

 	return $scope;
});
