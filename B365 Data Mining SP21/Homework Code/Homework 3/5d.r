pd = c(0.5, 0.25, 0.25)

prob_matrix = matrix(nrow = 3, ncol = 6)
prob_matrix[1,1] = 1/6
prob_matrix[1,2] = 1/6
prob_matrix[1,3] = 1/6
prob_matrix[1,4] = 1/6
prob_matrix[1,5] = 1/6
prob_matrix[1,6] = 1/6

prob_matrix[2,1] = 1/9
prob_matrix[2,2] = 2/9
prob_matrix[2,3] = 1/9
prob_matrix[2,4] = 2/9
prob_matrix[2,5] = 1/9
prob_matrix[2,6] = 2/9

prob_matrix[3,1] = 1/9
prob_matrix[3,2] = 1/9
prob_matrix[3,3] = 1/9
prob_matrix[3,4] = 2/9
prob_matrix[3,5] = 2/9
prob_matrix[3,6] = 2/9
  
print(prob_matrix)


n = 10000 #number of trials

success = 0
for (i in 1:n) {
  dice = choose_dice(1)
  results = switch(dice, "A" = dice_a(3), "B" = dice_b(3), "C" = dice_c(3))
  
  # print(dice)
  # print(results)
  
  dice_probabilities = rep(0,3)

  #only calculates numerator
  for (d in 1:3) { #loops through the dice
    result = pd[d]
    for (r in 1:3) { #loops through results
      result = result * prob_matrix[d, results[r]]
    }
    dice_probabilities[d] = result
  }
  
  #calculates denominator
  denom = 0
  for (res in results) { #loops through results gotten
    result = 1
    for (d in 1:3) { #loops through dice possibilities)
      result = result * prob_matrix[d, res]
    }
    denom = denom + result
  }
  dice_probabilities = dice_probabilities/denom
  prediction = switch(which.max(dice_probabilities), "A", "B", "C")
  if (prediction == dice)
    success = success + 1
}
sprintf("%g correct guesses in %g trails. %g%% accuracy!", success, n, (success/n)*100)

dice_a <- function(num_rolls) {
  return(sample(1:6, size = num_rolls, replace = TRUE))
} 
dice_b <- function(num_rolls) {
  return(sample(1:6, size = num_rolls, replace = TRUE, prob = c(1/9, 2/9, 1/9, 2/9, 1/9, 2/9)))
} 
dice_c <- function(num_rolls) {
  return(sample(1:6, size = num_rolls, replace = TRUE, prob = c(1/9, 1/9, 1/9, 2/9, 2/9, 2/9)))
}
choose_dice <- function(num_dice) {
  return(sample(c("A", "B", "C"), size = num_dice, replace = TRUE, prob = c(pd[1], pd[2], pd[3])))
}