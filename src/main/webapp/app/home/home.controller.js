(function() {
    'use strict';

    angular
        .module('scrapGeofenceApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'AceHardwareScrapperService', 'AlertService'];

    function HomeController ($scope, Principal, LoginService, $state, AceHardwareScrapperService, AlertService) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.scrapAceHardware = scrapAceHardware;
        
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }
        
        function scrapAceHardware () {
            AceHardwareScrapperService.scrap({}, scrapSuccess, error);
            function scrapSuccess(data) {
                console.log(data);
            };
        }
        
        function error(error) {
            console.log(error);
        };
    }
})();
