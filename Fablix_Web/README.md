#the readme file explain the process_log.java

In the search servlet(KeywordSearchServlet.java), I set several time spots to record the ts and tj time. After get the ts and tj, they are written to the log.txt(/Fablix_Web/target/fablix/). 1 line per request.

process_log.java: read the data(ts and tj) from the log.txt and caculate the average of the value.