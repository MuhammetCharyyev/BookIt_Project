@smoke
Feature: User Verification

  @wip
  Scenario: verify information about logged user
    Given I logged Bookit api using "fscoughx@msu.edu" and "feodorascough"
    When I get the current user information from api
    Then status code should be 200

