/**
 * Main Angular.js application file
 */

"use strict";

var civsApp = angular.module('civsApp', [
	'ngRoute'
]);

civsApp.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/', {templateUrl: 'view/root.html', controller: 'RootCtrl'});
  $routeProvider.when('/registration', {templateUrl: 'view/registration.html', controller: 'RegistrationCtrl'});
  $routeProvider.otherwise({redirectTo: '/'});
}]);

