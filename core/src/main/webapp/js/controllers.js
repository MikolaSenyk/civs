var todoApp = angular.module('todoApp', []);


// Config
todoApp.config(function($routeProvider, $httpProvider) {
	// routes
	$routeProvider.
	when('/', {
		controller: "RootCtrl",
		resolve: {
			isLogged: function(CheckAuth) {
				return CheckAuth();
			}
		},
		templateUrl: 'view/root.html'
	}).
	when('/register', {
		controller: "RegisterCtrl",
		templateUrl: 'view/login.html'
	}).
	when('/list', {
		controller: "ListCtrl",
		resolve: {
			todoList: function(ListLoader) {
				return ListLoader();
			}
		},
		templateUrl: 'view/list.html'
	}).
	when('/add', {
		controller: "AddCtrl",
		templateUrl: 'view/edit.html'
	}).
	when('/edit/:taskId', {
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
	}).
	otherwise({
		redirectTo: "/"
	});
	//disable IE ajax request caching
	if ( !$httpProvider.defaults.headers.get ) {
        $httpProvider.defaults.headers.get = {};    
    }
	$httpProvider.defaults.headers.get['If-Modified-Since'] = '0';
});


// Services
todoApp.factory("ConfigFactory", function($http, $location) {
	var config = {
		apiUrl: "/todo/s/",
		isLogged: false
	};

	config.logOut = function() {
		config.isLogged = false;
		$http.get(this.apiUrl+"users/logout");
	};

	config.redirectNotAuth = function() {
		if ( !config.isLogged ) {
			$location.path("/");
		}
	}

	return config;
});

todoApp.factory("CheckAuth", function($q, $http, ConfigFactory) {
	return function() {
		var deffered = $q.defer(); // define promise
		$http.get(ConfigFactory.apiUrl+"users/islogged").success(function (json) {
			ConfigFactory.isLogged = json.success;
			deffered.resolve(json.success);	
		}).error(function (data) {
			ConfigFactory.isLogged = false;
			deffered.resolve(false);
		});
		return deffered.promise; // return promise
	};
});

todoApp.factory("ListLoader", function($q, $http, ConfigFactory) {
	return function() {
		var deffered = $q.defer();
		$http.get(ConfigFactory.apiUrl+"tasks/list").success(function (json) {
			deffered.resolve(json);	
		}).error(function (data) {
			deffered.resolve("Unable get ToDo list");
		});
		return deffered.promise;
	};
});

todoApp.factory("TaskLoader", function($q, $http, $route, ConfigFactory) {
	return function() {
		var deffered = $q.defer();
		$http.get(ConfigFactory.apiUrl+"tasks/get/" + $route.current.params.taskId).success(function (json) {
			deffered.resolve(json);	
		}).error(function (data) {
			deffered.resolve("Unable get ToDo task");
		});
		return deffered.promise;
	};
});


// Controllers
todoApp.controller('RootCtrl', function ($scope, $http, $location, ConfigFactory, isLogged) {
	$scope.title = "Home page";
	$scope.isLogged = ConfigFactory.isLogged;
	$scope.auth = {
		title: "Login form",
		login: "",
		passwd: "",
		isError: false,
		errorText: "",
		view: "view/login.html",
		isRegister: false,
		submit: function() {
			var p = {
				login: $scope.auth.login,
				passwd: $scope.auth.passwd
			}
			$http.get(ConfigFactory.apiUrl+"users/login", {params: p}).success(function(json) {
				if ( json.success ) {
					$location.path("/list");
				} else {
					$scope.auth.isError = true;
					$scope.auth.errorText = json.messageText;
				}
			});
			return false;
		}
	};
	
});

todoApp.controller('RegisterCtrl', function ($scope, $http, $location, ConfigFactory) {
	$scope.auth = {
		title: "Registration",
		login: "",
		passwd: "",
		passwd2: "",
		isError: false,
		errorText: "",
		isRegister: true,
		loginText: "New user login",
		passwdText: "User password",
		submitBtn: "Create new user",
		submit: function() {
			console.log("register submitted");
			if ( $scope.auth.passwd != $scope.auth.passwd2 ) {
				$scope.auth.isError = true;
				$scope.auth.errorText = "Passwords don't match";
				return;
			}
			// register
			var p = {
				login: $scope.auth.login,
				passwd: $scope.auth.passwd
			}
			$http.get(ConfigFactory.apiUrl+"users/register", {params: p}).success(function(json) {
				if ( json.success ) {
					$location.path('#/list');
				} else {
					$scope.auth.isError = true;
					$scope.auth.errorText = json.messageText;
				}
			});
			
		}
	};
});

todoApp.controller('LogoutCtrl', function ($scope, ConfigFactory) {
	$scope.title = "You've logged out";
	ConfigFactory.logOut();
});

todoApp.controller('ListCtrl', function ($scope, $http, ConfigFactory, todoList) {
	$scope.title = "ToDo list";
	$scope.todoList = todoList;

	$scope.removeItem = function(index) {
		// call API
		var todoId = todoList.items[index].id;
		$http.get(ConfigFactory.apiUrl+"tasks/remove/"+todoId);
		// remove item from list;
		$scope.todoList.items.splice(index,1);
	};
});

todoApp.controller('AddCtrl', function ($scope, $http, $location, ConfigFactory) {
	$scope.title = "Add new ToDo";
	$scope.addMode = true;
	$scope.statuses = ["NEW", "DONE"];
	$scope.priorities = [1,2,3,4,5,6,7,8,9,10];
	$scope.submitBtnText = "Add ToDo";
	$scope.errorText = "";
	$scope.submit = function() {
		$http.put(ConfigFactory.apiUrl+"tasks/add", $scope.todo).success(function(json) {
			if ( json.success ) {
				$scope.errorText = "saving...";
				$location.path("/list");
			} else {
				$scope.errorText = json.messageText;
			}
		}).error(function(err) {
			$scope.errorText = err;
		});
	};
	$scope.todo = {
		title: "",
		description: "",
		priority: 5,
		status: "NEW",		
	}
});

todoApp.controller('EditCtrl', function ($scope, $http, $location, ConfigFactory, todo) {
	$scope.todo = todo;
	$scope.title = "Edit ToDo task";
	$scope.addMode = false;
	$scope.statuses = ["NEW", "DONE"];
	$scope.priorities = [1,2,3,4,5,6,7,8,9,10];
	$scope.submitBtnText = "Save";
	$scope.errorText = "";
	$scope.submit = function() {
		$http.put(ConfigFactory.apiUrl+"tasks/modify/"+$scope.todo.id, $scope.todo).success(function(json) {
			if ( json.success ) {
				$scope.errorText = "saving...";
				$location.path("/list");
			} else {
				$scope.errorText = json.messageText;
			}
		}).error(function(err) {
			$scope.errorText = err;
		});
	};
});
