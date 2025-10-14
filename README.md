# githubScoringService
This service is the implements the githubScoringApi and searches the official github-API for
repositories using specific searchcriteria such as language and creation date.

The search result will be sorted by a popularity score based on number of stars, forks and
recency of updates.

## Troubleshooting and usage of the github-API
The github-API has specific limitations that need to be considered when using this service.

When a valid authorization token is provided github allows up to 30 search requests against the
repository search endpoint
And in total a maximum of 100 results per page.
The Maximum number of results the github-API can return is 1000

When no valid authorization token is provided github allows only 10 search requests per minute.

So the total Maximum of results the ScoringService can return is 1000 results.

The Github-API can only perform a repository search, when there is at least 
one search criteria given. 

In our case we need at least the language or the creation date.


## Scoring Algorithm
This service applies a scoring score to each repository based on 
- recency of updates 
- number of stars
- number of forks

The higher the score, the higher the position in the result list

In the current implementation the stars have a factor of 1000 and the fors a factor of 100
In addition to that we count the minutes from now since the last update.

That leads to the popularity score


## Future improvements
- more detailed error handling
- implementing an own type for the github-api-response instead of string
- adding spring security to the project
- more testing of corner cases and error handling
- adding more logging to the service using log4j or similar
- more detailed documentation on how to use the service
- maybe adding a possibility to determine all available languages to use a enum instead of plain text 
- maybe some more springboot3 refactoring
