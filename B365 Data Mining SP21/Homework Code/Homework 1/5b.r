m = 1000 #number of experiments
n = 1000 #number of repetitions
p = 0.3 #probability of P(A)
results = rep(0, m)

for (i in 1:m) {
  ex <- run_experiment(p, n)
  results[i] = p <= ex[2] && p >= ex[1]
}
sprintf("Probability fell within the calculated confidence interval %g times or %g%%", sum(results), sum(results)/m)

run_experiment <- function(probability, repetitions) {
  results = rep(0, repetitions) #results vector
  
  for (i in 1:repetitions) {
    results[i] = runif(1) < probability
  }
  
  sum = sum(results)
  p_hat = sum / repetitions
  lower_confidence_interval = p_hat - 1.96 * sqrt((p_hat * (1 - p_hat)) / repetitions) #95% Confidence interval
  upper_confidence_interval = p_hat + 1.96 * sqrt((p_hat * (1 - p_hat)) / repetitions)
  
  ci <- list(lower_confidence_interval, upper_confidence_interval)
  return(ci)
}