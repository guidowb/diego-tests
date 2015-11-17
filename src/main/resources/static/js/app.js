angular.module(
	'diego-tests',
	[ 'ngRoute', 'ngResource', 'ngAnimate', 'angular.filter' ]
).config(
	function($routeProvider) {
		$routeProvider.otherwise('/tasks');
		$routeProvider.when('/tasks', {
			templateUrl : 'views/tasks.html',
			controller : 'tasks'
		});
	}
).controller(
	'tasks',
	function($rootScope, $scope, $http, $timeout, $animate) {
		$http.get('/tasks').success(function(data) {
			$scope.tasks = data;
		});
	}
);