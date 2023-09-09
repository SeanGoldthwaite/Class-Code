n = 1000 # number of trails
p = 0.3 #probability of P(A)
results = rep(0, n) #results vector

for (i in 1:n) {
  results[i] = runif(1) < p
}

sum = sum(results)
p_hat = sum / n
lower_confidence_interval = p_hat - 1.96 * sqrt((p_hat * (1 - p_hat)) / n) #95% Confidence interval
upper_confidence_interval = p_hat + 1.96 * sqrt((p_hat * (1 - p_hat)) / n)

sprintf("Confidence interval: (%f, %f)", lower_confidence_interval, upper_confidence_interval)