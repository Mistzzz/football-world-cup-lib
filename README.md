# Football World Cup Score Board

The boards support the following operations:
1. **Start a game**. When a game starts, it should capture (being initial score 0-0)\
   a. Home team\
   b. Away Team
2. **Finish a game**. It will remove a match from the scoreboard.
3. **Update score**. Receiving the pair score; home team score and away team score updates a game score.
4. **Get a summary of games by total score**. Those games with the same total score will be returned ordered by the most recently added to our system.

## As an example, being the current data in the system:
&nbsp;&nbsp;&nbsp;&nbsp;a. Mexico - Canada: 0 – 5\
&nbsp;&nbsp;&nbsp;&nbsp;b. Spain - Brazil: 10 – 2\
&nbsp;&nbsp;&nbsp;&nbsp;c. Germany - France: 2 – 2\
&nbsp;&nbsp;&nbsp;&nbsp;d. Uruguay - Italy: 6 – 6\
&nbsp;&nbsp;&nbsp;&nbsp;e. Argentina - Australia: 3 - 1

## The summary would provide with the following information:
1. Uruguay 6 - Italy 6
2. Spain 10 - Brazil 2
3. Mexico 0 - Canada 5
4. Argentina 3 - Australia 1
5. Germany 2 - France 2

## Assumptions
This library has exposed only ScoreBoard interface with proper implementation. With adding dependency to library you can create object of class [FootballWorldCupScoreBoard](src/main/java/pl/football/worldcup/FootballWorldCupScoreBoard.java) and then call proper methods described in the first paragraph.
1. **Start a game** with 2 options - with providing start time or without (then start time will be set to now()):
    ```java
        Long startMatch(String homeTeam, String awayTeam);
        
        Long startMatch(String homeTeam, String awayTeam, LocalDateTime startTime);
    ```
2. **Update score** for given match id:
    ```java
        boolean updateScore(Long id, MatchScore score);
    ```
3. **Finish a game** for given match id:
    ```java
        void finishMatch(Long id);
    ```
4. **Get a summary of games by total score** for currently started and not finished matches: 
    ```java
        List<FootballMatch> getSummaryMatchesByTotalScore();
    ```

## Output for provided test case
You can see the example usage in test class [FootballWorldCupScoreBoardTest.java](src/test/java/pl/football/worldcup/FootballWorldCupScoreBoardTest.java).\
All other releated classes also have implemented tests, which you can see in proper directory.
