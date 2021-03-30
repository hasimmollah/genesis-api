Feature: Get Telephone Details (Integration Tests)

     @Dev  
  Scenario Outline: Validate Health Check of Genesis Api
    When I make a request for health check to Genesis Api
    Then I should get the response with StatusCode as <StatusCode> and health check message as <healthcheckmessage>
    Examples:
      | User               | StatusCode | healthcheckmessage    |
      | HasimValidUser     | 200        | Genesis api is up |
