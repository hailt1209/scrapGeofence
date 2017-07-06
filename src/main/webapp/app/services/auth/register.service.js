(function () {
    'use strict';

    angular
        .module('scrapGeofenceApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
