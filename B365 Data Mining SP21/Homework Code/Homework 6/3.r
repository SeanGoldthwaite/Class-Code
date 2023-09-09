y = as.matrix(AirPassengers)
logy = as.matrix(log10(AirPassengers))
n = length(logy)
x = rep(1:n)


X = cbind(rep(1, n), x)
colnames(X) = c("Intercept", "X")

#3b
solution = solve(t(X) %*% X, t(X) %*% logy)

#3a
plot(x,logy, main="log10(AirPassengers)")
lines(x, X %*% solution, col="blue")

#3c
plot(x, y, main="AirPassengers")
lines(x, 10^(X %*% solution), col="blue")