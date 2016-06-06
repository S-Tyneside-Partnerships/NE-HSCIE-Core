'use strict';

angular.module('rippleDemonstrator')
  .controller('InformationGovernanceModalCtrl', function ($scope, $modalInstance, consent, UserService, patient, modal) {

    $scope.currentUser = UserService.getCurrentUser();
    $scope.consent = consent;
    $scope.patient = patient;
    $scope.modal = modal;
    $scope.optChoices = [consent.optIn];

    if (modal.title === 'Create Information Governance') {
      $scope.consent.dateCreated = new Date().toISOString().slice(0, 10);
    } else {
      $scope.consent.dateCreated = new Date($scope.consent.dateCreated).toISOString().slice(0, 10);
    }

    $scope.consent.author = $scope.currentUser.firstName + ' ' + $scope.currentUser.surname;

    $scope.ok = function (infoGovForm, consent) {
      $scope.formSubmitted = true;

      if (infoGovForm.$valid) {
        $modalInstance.close(consent);
      }
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    $scope.openDatepicker = function ($event, name) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope[name] = true;
    };

  });