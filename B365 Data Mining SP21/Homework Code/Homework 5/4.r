data = cars

X = data[,1] #speed
Y = data[,2] #distance

X = cbind(rep(1, nrow(data)), X)

solution = solve(t(X) %*% X, t(X) %*% Y)

dimnames(solution) = list(c("constant","X"), c("Coefficient"))

print(solution)

sprintf("For every unit increase in speed, a %g in distance is expected, based on the dataset",solution[2,1])