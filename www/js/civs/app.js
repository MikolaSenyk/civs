
/**
 * Civil Society Application
 */

var civsApp = angular.module('civsApp', []);

civsApp.config(function($routeProvider, $httpProvider) {
	var v = 2;
	// routes
	$routeProvider.
	when('/auth/:action', {
		controller: "AuthCtrl",
		templateUrl: 'view/auth.html'
	}).
	when('/user/:action', {
		controller: "UserCtrl",
		templateUrl: 'view/user.html?v='+v,
		resolve: {
			chechAuth: function(CheckAuth) {
				return CheckAuth();
			}
		}
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
	when('/admin/:action/:id', {
		controller: "AdminCtrl",
		templateUrl: 'view/admin.html',
		resolve: {
			chechAuth: function(CheckAuth) {
				return CheckAuth();
			}
		}
	}).
	when('/:action', {
		controller: "MainCtrl",
		templateUrl: 'view/main.html',
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

civsApp.factory("UsersFactory", function($http) {
 	console.log("UsersFactory init");
 	var users = {};
 	users.config = {
 		apiUrl: "/core/s/users/"
 	};
 	users.getList = function(callback) {
 		$http.get(this.config.apiUrl + 'list').success(callback);
 	};
 	users.blockUser = function(userId) {
 		$http.put(this.config.apiUrl + userId + '/block');
 	};
 	users.unblockUser = function(userId) {
 		$http.put(this.config.apiUrl + userId + '/unblock');
 	};
 	users.removeUser = function(userId) {
 		$http.delete(this.config.apiUrl + userId);
 	};
 	users.getInfo = function(callback) {
 		$http.get(this.config.apiUrl + 'get').success(callback);
 	};
 	users.updateOptions = function(options, callback) {
 		$http.put(this.config.apiUrl + 'update', options).success(callback);
 	};

 	return users;
});

civsApp.factory("AgFactory", function($http) {
	var ag = {};
	ag.config = {
		apiUrl: "/core/s/ags/"
	};
	
	ag.getList = function(parentId, callback) {
 		$http.get(this.config.apiUrl + 'list/' + parentId).success(callback);
 	};
 	ag.createGroup = function(groupName, parentId, callback) {
 		var p = { name: groupName, parentId: parentId };
 		$http.put(this.config.apiUrl + 'create', p).success(callback);
 	};
 	ag.removeGroup = function(groupId, callback) {
 		$http.put(this.config.apiUrl + groupId + '/remove').success(callback);
 	};

	return ag;
});

civsApp.factory("PriceFactory", function($http) {
	var pf = {};
	pf.config = {
		apiUrl: "/core/s/prices/"
	};
	
	pf.getList = function(groupId, callback) {
 		$http.get(this.config.apiUrl + 'list/' + groupId).success(callback);
 	};
 	pf.createPrice = function(item, groupId, callback) {
 		var p = { groupId: groupId, name: item.name, measure: item.measure, gradeOne: item.gradeOne, gradeTwo: item.gradeTwo, outOfSeason: item.outOfSeason };
 		$http.post(this.config.apiUrl + 'create', p).success(callback);
 	};
 	pf.updatePrice = function(item, callback) {
 		var p = { id: item.id, groupId: item.groupId, name: item.name, measure: item.measure, gradeOne: item.gradeOne, gradeTwo: item.gradeTwo, outOfSeason: item.outOfSeason };
 		$http.put(this.config.apiUrl + item.id + '/update', p).success(callback);
 	};
 	pf.removePrice = function(priceId, callback) {
 		$http.delete(this.config.apiUrl + priceId + '/remove').success(callback);
 	};

	return pf;
});

civsApp.factory("AssistanceFactory", function ($http) {
	var assistance = {};

	assistance.config = {
		apiUrl: "/core/s/assistances/"
	};

	assistance.listByUser = function(callback) {
		$http.get(this.config.apiUrl + 'listByUser').success(callback);
	};

	assistance.listAll = function(callback) {
		$http.get(this.config.apiUrl + 'listAll').success(callback);
	};

	assistance.listLast = function(callback) {
		$http.get(this.config.apiUrl + 'listLast').success(callback);
	};

	assistance.listNew = function(callback) {
		$http.get(this.config.apiUrl + 'listNew').success(callback);
	};

	assistance.create = function(params, callback) {
		$http.post(this.config.apiUrl + 'create', params).success(callback);
	};
	assistance.remove = function(assistanceId, callback) {
 		$http.delete(this.config.apiUrl + assistanceId).success(callback);
 	};
 	assistance.setApproved = function(assistanceId, callback) {
 		$http.put(this.config.apiUrl + assistanceId + '/approve').success(callback);
 	};
 	assistance.enableAll = function(cb) {
 		$http.post(this.config.apiUrl + 'enable/all').success(cb);
 	};
 	assistance.disableAll = function(cb) {
 		$http.post(this.config.apiUrl + 'disable/all').success(cb);
 	};

	return assistance;
});

civsApp.factory("LetterFactory", function ($http) {
	var letter = {};

	letter.config = {
		apiUrl: "/core/s/letter/"
	};

	letter.sendToAdmin = function(msg, callback) {
		$http.post(this.config.apiUrl + 'sendToAdmin', {"message": msg}).success(callback);
	};

	letter.listNew = function(callback) {
		$http.post(this.config.apiUrl + 'listNew').success(callback);
	};

	letter.markAsRead = function(letterId, callback) {
		$http.get(this.config.apiUrl + letterId +'/markAsRead').success(callback);
	};

	return letter;
});

civsApp.factory("ImageFactory", function ($http) {
	var images = {};

	images.config = {
		apiUrl: "/core/s/images/"
	};

	images.upload = function(imgBody, callback) {
		$http.post(this.config.apiUrl + 'upload', {"imgBody": imgBody, "imgExt": "jpg"}).success(callback);
	};

	images.uploadAvatar = function(imgBody, callback) {
		$http.post(this.config.apiUrl + 'uploadAvatar', {"imgBody": imgBody, "imgExt": "jpg"}).success(callback);
	};

	return images;
});

var jsTools = {
	emptyFields: function(fieldNames, obj) {
		var arrFn = fieldNames.split(',');
		if ( obj === undefined ) obj = {};
		for (var i in arrFn) {
			obj[arrFn[i]] = '';
		}
		return obj;
	},
	copyFields: function(srcObj, dstObj) {
		for (var k in srcObj) dstObj[k]=srcObj[k];
	}
};