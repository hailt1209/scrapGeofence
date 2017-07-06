(function() {
    'use strict';

    angular
        .module('scrapGeofenceApp')
        .factory('AceHardwareScrapperService', AceHardwareScrapperService);

    AceHardwareScrapperService.$inject = ['$resource'];

    function AceHardwareScrapperService ($resource) {
        var service = $resource('api/aceHardware/scrap', {}, {
            'scrap': { method: 'GET', params: {}, isArray: false,
                interceptor: {
                    response: function(response) {
                        // expose response
                        return response;
                    }
                }
            }
        });

        return service;
    }
})();
