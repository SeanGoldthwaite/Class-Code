data(nottem)
y = nottem
n = length(y)
x = 1:n

X = cbind(rep(1, n), x)
Y = as.matrix(y)
colnames(X) = c("Intercept", "X")

#2b
solution  = solve(t(X) %*% X, t(X) %*% Y)
error = y - (X %*% solution)
sse = t(error) %*% error

#2c
X_periodic = cbind(rep(1, n), cos(x * pi / 6), sin(x * pi / 6))
colnames(X_periodic) = c("Intercept", "cos", "sin")

solution_periodic = solve(t(X_periodic) %*% X_periodic, t(X_periodic) %*% Y)
error_periodic = y - (X_periodic %*% solution_periodic)
sse_periodic = t(error_periodic) %*% error_periodic

#2d
X_linearperiodic = cbind(rep(1, n), x, cos(x * pi / 6), sin(x * pi / 6))
colnames(X_linearperiodic) = c("Intercept", "X", "cos", "sin")

solution_linearperiodic = solve(t(X_linearperiodic) %*% X_linearperiodic, t(X_linearperiodic) %*% Y)
error_linearperiodic = Y - (X_linearperiodic %*% solution_linearperiodic)
sse_linearperiodic = t(error_linearperiodic) %*% error_linearperiodic

#2a
plot(x,y,type="b", col="black", ylim=c(30,70), xlim=c(0,245), asp=1)
#2b
lines(x, X %*% solution, col="blue")
#2c
lines(x, X_periodic %*% solution_periodic, col="red")
#2d
lines(x, X_linearperiodic %*% solution_linearperiodic, col="green")

legend("topleft", c("Data",
                    "y = ax + b",
                    "y = a * cos(x * pi/6) + b * sin(x * pi/6) + c",
                    "y = a * cos(x * pi/6) + b * sin(x * pi/6) + c + dx"),
      fill=c("black", "blue", "red", "green"))