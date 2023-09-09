n = 3000 # number of repetitions
player_a_results = rep(0, n) # vector of results of player a
player_b_results = rep(0, n) # vector of results of player b
player_c_results = rep(0, n) # vector of results of player c

player_a_wins = rep(0, n)

for (i in 1:n) {
  sum = player_a_results[i] + player_b_results[i] + player_c_results[i]
  
  while (sum == 3 || sum == 0) {
    player_a_results[i] = runif(1) < 0.5
    player_b_results[i] = runif(1) < 0.5
    player_c_results[i] = runif(1) < 0.5
    
    
    sum = player_a_results[i] + player_b_results[i] + player_c_results[i]
  }
  
  if (sum == 1)
    player_a_wins[i] = player_a_results[i]
  if (sum == 2)
    
    player_a_wins[i] = 1 - player_a_results[i]
}
cat("Player A won ", sum(player_a_wins), " times or ", sum(player_a_wins)/n, "%\n")

