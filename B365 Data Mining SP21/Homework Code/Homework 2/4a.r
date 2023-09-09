n = 1000000 #num of reps

x = runif(n, 0, 1);
  A = (x < 0.5)
  B = ((2*x) %% 1) < 0.5

p_a = find_confidence_interval(mean(a), sd(a), n, 0.95)
p_b = find_confidence_interval(mean(b), sd(b), n, 0.95)
p_ab = find_confidence_interval(mean(a & b), sd(a & b), n, 0.95)

sprintf("P(A) = (%g,%g) where p_hat = %g", p_a[1], p_a[2], p_a[3])
sprintf("P(B) = (%g,%g) where p_hat = %g", p_b[1], p_b[2], p_b[3])
sprintf("P(A,B) = (%g,%g) where p_hat = %g", p_ab[1], p_ab[2], p_ab[3])

find_confidence_interval <- function(mean, st_dev, n, conf_lev) {
  error = qnorm(1 - ((1 - conf_lev) / 2)) * (st_dev / sqrt(n))
  low = mean - error
  high = mean + error
  ci = list(low, high, mean)
  return(ci)
}
