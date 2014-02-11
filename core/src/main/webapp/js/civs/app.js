/**
 * Civil Society Application
 */

var civsApp = angular.module('civsApp', []);

civsApp.config(function($routeProvider, $httpProvider) {
	// routes
	$routeProvider.
	when('/', {
		controller: "MainCtrl",
		templateUrl: 'view/main.html',
		resolve: {
			chechAuth: function(CheckAuth) {
				return CheckAuth();
			}
		}
	}).
	when('/auth/:action', {
		controller: "AuthCtrl",
		templateUrl: 'view/auth.html'
	}).
	when('/user', {
		controller: "UserCtrl",
		templateUrl: 'view/user.html'
	}).
	when('/admin/:action', {
		controller: "AdminCtrl",
		templateUrl: 'view/admin.html',
		resolve: {
			chechAuth: function(CheckAuth) {
				return CheckAuth();
			}
		}
	}).
	/*when('/edit/:taskId', {
		controller: "EditCtrl",
		resolve: {
			todo: function(TaskLoader) {
				return TaskLoader();
			}
		},
		templateUrl: 'view/edit.html'
	}).
	when('/logout', {
		controller: "LogoutCtrl",
		templateUrl: 'view/logout.html'
	}).*/
	otherwise({
		redirectTo: "/"
	});
	
	//disable IE ajax request caching
	if ( !$httpProvider.defaults.headers.get ) {
        $httpProvider.defaults.headers.get = {};    
    }
	$httpProvider.defaults.headers.get['If-Modified-Since'] = '0';
});
