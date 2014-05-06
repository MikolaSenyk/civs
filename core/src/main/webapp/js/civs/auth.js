/**
 * Civil Society Application
 * Auth service and registry controller
 */

var ADMIN_ROLE_NAME = "ADMIN";
var USER_ROLE_NAME = "USER";
var ANONYMOUS_ROLE_NAME = "ANONYMOUS";

civsApp.factory("AuthFactory", function($http, $location) {
	console.log("AuthFactory init");
	var auth = {
		apiUrl: "/core/s/auth/",
		login: '',
		role: ANONYMOUS_ROLE_NAME,
		isLogged: false
	};

	auth.doLogout = function() {
		auth.isLogged = false;
		$http.get(this.apiUrl+"logout");
	};

	auth.doLogin = function(p, loginFailedFn) {
		$http.get(this.apiUrl+"login", {params: p}).success(function(json) {
			if ( json.success ) {
				auth.setAuthInfo(json);
				$location.path("/");
			} else {
				loginFailedFn();
			}
		});
	};

	auth.changePassword = function(p, cbFunc) {
		$http.post(this.apiUrl+"changePass", p).success(function (json) {
			cbFunc(json);
		});
	};

	auth.setAuthInfo = function(json) {
		auth.isLogged = json.success;
		auth.login = angular.uppercase(json.login);
		auth.role = json.role;
	};

	auth.getAuthInfo = function(view) {
		console.log("isLogged: " + this.isLogged);
		var res = {
			isLogged: this.isLogged,
			login: this.login,
			view: view,
			isAdmin: this.isAdmin(),
			isUser: this.isUser()
		};
		return res;
	};

	auth.isUser = function() {
		return ( this.isLogged && this.role == USER_ROLE_NAME );
	};

	auth.isAdmin = function() {
		return ( this.isLogged && this.role == ADMIN_ROLE_NAME );
	};	

	return auth;
});

civsApp.factory("CheckAuth", function($q, $http, AuthFactory) {
	return function() {
		console.log("CheckAuth");
		var deffered = $q.defer(); // define promise
		$http.get(AuthFactory.apiUrl+"islogged").success(function (json) {
			AuthFactory.setAuthInfo(json);
			deffered.resolve(json.success);	
		}).error(function (data) {
			AuthFactory.isLogged = false;
			deffered.resolve(false);
		});
		return deffered.promise; // return promise
	};
});

civsApp.controller('AuthCtrl', function ($scope, $route, $http, $location, AuthFactory) {
	$scope.action = $route.current.params.action;
	
	if ( $scope.action == 'login' ) {
		$scope.view = "view/auth/login.html";
		$scope.title = "Вхід у систему";
	 	$scope.subTitle = "аутентифікація";
	 	$scope.error = null;
	 	$scope.login = '';
	 	$scope.passwd = '';
	 	$scope.submit = function() {
	 		AuthFactory.doLogin(
	 			{login: this.login, passwd: this.passwd},
	 			function() {
	 				$scope.error = "Неправильно вказаний логін чи пароль";
	 			}
	 		);
	 	};
	} else if ( $scope.action == 'logout' ) {
		AuthFactory.doLogout();
		$scope.view = "view/auth/logout.html";
		$scope.title = "Ви вийшли";
	 	$scope.subTitle = "з системи";
	} else if ( $scope.action == 'register' ) {
		console.log("register ctrl");
		$scope.view = "view/auth/register.html";
		$scope.title = "Реєстрація";
	 	$scope.subTitle = "нового користувача у систему";
	 	$scope.error = null;
	 	$scope.login = '';
	 	$scope.passwd = '';
	 	$scope.passwdCheck = '';
	 	$scope.code = '';
	 	$scope.submit = function() {
	 		var p = {
	 			code: this.code,
	 			login: this.login,
	 			passwd: this.passwd,
	 			passwdCheck: this.passwdCheck
	 		};
	 		$http.put(AuthFactory.apiUrl+"register", p).success(function (json) {
				if ( json.success ) {
					$location.path("/");
					AuthFactory.setAuthInfo(json);	
				} else {
					$scope.error = "Помилка реєстрації: " + json.messageText;
				}
			});
	 	};
	}
	

});
