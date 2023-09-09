n = 1000# num of repetitions

x = runif(n, 0, 1)
y = runif(n, 0, 1)

#A = rep(1:n)
#B = rep(1:n)

a = as.logical(c(x + y < 1))
b = as.logical(c(x - y < 0))

#plot(X, Y, type="p", pch=2)
plot(x, y,
     pch = ifelse(a, ifelse(b, 0, 1), ifelse(b, 2, 5)),
     col = ifelse(a, ifelse(b, "red", "green"), ifelse(b, "blue", "black")))

#P(A)
p_a = find_confidence_interval(mean(a), sd(a), n, 0.95)

sprintf("P(A) = (%g,%g) where p_hat = %g", p_a[1], p_a[2], p_a[3])

#P(A|B) = P(A, B) / P(B)

p_ab = find_confidence_interval(mean(a & b) / mean(b), sd(a & b) / sd(b), n, 0.95)


sprintf("P(A|B) = (%g,%g) where p_hat = %g", p_ab[1], p_ab[2], p_ab[3])

find_confidence_interval <- function(mean, st_dev, n, conf_lev) {
  error = qnorm(1 - ((1 - conf_lev) / 2)) * (st_dev / sqrt(n))
  low = mean - error
  high = mean + error
  ci = list(low, high, mean)
  return(ci)
}